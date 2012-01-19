package org.iplantc.workflow.marshaller;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.iplantc.files.types.ReferenceGenomeHandler;
import org.iplantc.persistence.dto.step.TransformationStep;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.dao.NotificationSetDao;
import org.iplantc.workflow.data.DataObject;
import org.iplantc.workflow.integration.util.HeterogeneousRegistry;
import org.iplantc.workflow.integration.util.NullHeterogeneousRegistry;
import org.iplantc.workflow.model.Property;
import org.iplantc.workflow.model.PropertyGroup;
import org.iplantc.workflow.model.Rule;
import org.iplantc.workflow.model.Template;
import org.iplantc.workflow.model.Validator;
import org.iplantc.workflow.template.notifications.NotificationSet;
import org.iplantc.persistence.dto.transformation.Transformation;
import org.iplantc.workflow.util.ListUtils;
import org.iplantc.workflow.util.Predicate;
import org.json.JSONObject;

/**
 * Marshalls analyses for the Discovery Environment UI. This class was extracted and slightly refactored from
 * org.iplantc.workflow.HibernateTemplateFetcher.
 */
public class UiAnalysisMarshaller {

    private DaoFactory daoFactory;

    private ReferenceGenomeHandler referenceGenomeHandler;

    private HeterogeneousRegistry registry = new NullHeterogeneousRegistry();

    private boolean marshallNotificationSet = false;

    public void setRegistry(HeterogeneousRegistry registry) {
        this.registry = registry == null ? new NullHeterogeneousRegistry() : registry;
    }

    public void includeNotificationSet() {
        marshallNotificationSet = true;
    }

    public void excludeNotificationSet() {
        marshallNotificationSet = false;
    }

    public UiAnalysisMarshaller(DaoFactory daoFactory, ReferenceGenomeHandler referenceGenomeHandler) {
        this.daoFactory = daoFactory;
        this.referenceGenomeHandler = referenceGenomeHandler;
    }

    public JSONObject marshall(TransformationActivity analysis) throws Exception {
        Template template = templateFromAnalysis(analysis);
        TemplateMarshaller marshaller = new TemplateMarshaller();
        template.accept(marshaller);
        JSONObject json = marshaller.getCumulativeJson();
        if (marshallNotificationSet) {
            addNotificationSet(json, analysis);
        }
        return json;
    }

    public Template templateFromAnalysis(TransformationActivity analysis) throws Exception {
        Template analysisTemplate = new Template();
        analysisTemplate.setId(analysis.getId());
        analysisTemplate.setName(analysis.getName());
        analysisTemplate.setLabel(analysis.getName());
        analysisTemplate.setType(analysis.getType());
        analysisTemplate.setDescription(analysis.getDescription());
        copyPropertyGroups(analysisTemplate, analysis);
        return analysisTemplate;
    }

    private void addNotificationSet(JSONObject json, TransformationActivity analysis) throws Exception {
        NotificationSet notificationSet = loadNotificationSet(analysis);
        if (notificationSet != null) {
            json.put("wizardNotifications", marshallNotificationSet(notificationSet));
        }
    }

    private JSONObject marshallNotificationSet(NotificationSet notificationSet) throws Exception {
        NotificationMarshaller marshaller = new NotificationMarshaller();
        notificationSet.accept(marshaller);
        return marshaller.getCumulativeJson();
    }

    private NotificationSet loadNotificationSet(TransformationActivity analysis) {
        NotificationSet notificationSet = loadNotificationSetFromRegistry(analysis.getId());
        if (notificationSet == null) {
            notificationSet = loadNotificationSetFromDatabase(analysis);
        }
        return notificationSet;
    }

    private NotificationSet loadNotificationSetFromDatabase(TransformationActivity analysis) {
        NotificationSetDao notificationSetDao = daoFactory.getNotificationSetDao();
        List<NotificationSet> results = notificationSetDao.findNotificationSetsForAnalysis(analysis);
        return CollectionUtils.isEmpty(results) ? null : results.get(0);
    }

    private NotificationSet loadNotificationSetFromRegistry(String analysisId) {
        NotificationSet retval = null;
        for (NotificationSet notificationSet : registry.getRegisteredObjects(NotificationSet.class)) {
            if (StringUtils.equals(notificationSet.getTemplate_id(), analysisId)) {
                retval = notificationSet;
                break;
            }
        }
        return retval;
    }

    private void copyPropertyGroups(Template analysisTemplate, final TransformationActivity analysis) throws Exception {
        for (TransformationStep step : analysis.getSteps()) {
            final String stepName = step.getName();
            final Transformation transformation = step.getTransformation();
            final String groupNamePrefix = getGroupNamePrefixForStep(analysis, step);
            Template template = loadTemplate(transformation.getTemplate_id());

            // copy the list of template inputs to track which inputs were not found in properties
            // for old style templates
            List<DataObject> orphanInputList = new LinkedList<DataObject>(template.getInputs());

            for (PropertyGroup oldgroup : template.getPropertyGroups()) {
                PropertyGroup group = new PropertyGroup();

                group.setName(groupNamePrefix + oldgroup.getName());
                group.setId(oldgroup.getId());
                group.setLabel(groupNamePrefix + oldgroup.getLabel());
                group.setGroupType(oldgroup.getGroupType());
                group.setDescription(oldgroup.getDescription());
                group.setVisible(oldgroup.isVisible());

                for (Property property : oldgroup.getProperties()) {

                    if (transformation.containsProperty(property.getId()) || !property.getIsVisible()) {
                        continue;
                    }

                    String propertyType = property.getPropertyTypeName();
                    if (propertyType.equalsIgnoreCase("output")) {
                        continue;
                    } else if (propertyType.equalsIgnoreCase("input")) {
                        DataObject input = property.getDataObject();

                        if (transformation.containsProperty(input.getId())
                                || analysis.isTargetInMapping(stepName, input.getId())) {
                            continue;
                        }

                        // Remove this input from the orphan list.
                        // This remove call will work since templates build their input list from the
                        // same input objects found in these properties.
                        orphanInputList.remove(input);

                        property = getFinalProperty(input);
                    }

                    if (!analysis.getType().equals("TNRS")) {
                        property.setId(stepName + "_" + property.getId());
                    }

                    Property new_property = fixStepNamesForProperty(property, stepName);
                    group.addProperty(new_property);
                }

                if (group.getProperties().isEmpty()) {
                    continue;
                }

                analysisTemplate.addPropertyGroup(group);
            }

            // Filter out any orphaned inputs that are not specified by the user.
            orphanInputList = ListUtils.filter(new Predicate<DataObject>() {
                @Override
                public Boolean call(DataObject arg) {
                    return !transformation.containsProperty(arg.getId())
                            && !analysis.isTargetInMapping(stepName, arg.getId());
                }
            }, orphanInputList);

            if (!orphanInputList.isEmpty()) {
                PropertyGroup input_group = new PropertyGroup();

                input_group.setId("idPanelData1");
                input_group.setName(groupNamePrefix + "Select data:");
                input_group.setLabel(groupNamePrefix + "Select input data");
                input_group.setGroupType("step");

                for (DataObject obj : orphanInputList) {
                    Property input = getFinalProperty(obj);

                    if (!analysis.getType().equalsIgnoreCase("TNRS")) {
                        input.setId(stepName + "_" + obj.getId());
                    }

                    input_group.addProperty(input);
                }

                analysisTemplate.addPropertyGroup(0, input_group);

                if (input_group.getProperties().size() == 1) {
                    input_group.setLabel(input_group.getProperties().get(0).getLabel().replaceAll(": *$", ""));
                }
            }
        }
    }

    private Template loadTemplate(String id) throws Exception {
        Template template = loadTemplateFromRegistry(id);
        if (template == null) {
            template = loadTemplateFromDatabase(id);
        }
        if (template == null) {
            throw new Exception("The requested template " + id + " does not exist");
        }
        return template;
    }

    private Template loadTemplateFromRegistry(String id) {
        return registry.get(Template.class, id);
    }

    private Template loadTemplateFromDatabase(String id) throws Exception {
        Template template = daoFactory.getTemplateDao().findById(id);
        if (template == null) {
            throw new Exception("The requested template " + id + " does not exist");
        }
        return template;
    }

    private Property fixStepNamesForProperty(Property property, String step_name) {

        if (property.getValidator() == null)
            return property;

        Validator validator = property.getValidator();

        List<Rule> rules = validator.getRules();

        for (Rule rule : rules) {

            if (rule.getName().contains("IntAboveField") || rule.getName().contains("IntBelowField")) {
                List<String> arguments = rule.getArguments();

                if (!StringUtils.isNumeric(arguments.get(0))) {
                    List<String> new_arguments = new LinkedList<String>();
                    new_arguments.add(step_name + "_" + arguments.get(0));
                    rule.setArguments(new_arguments);
                }

            }
        }

        return property;
    }

    private Property getFinalProperty(DataObject dataobject) {
        UiInputPropertyGeneratorFactory factory = new UiInputPropertyGeneratorFactory(referenceGenomeHandler,
            daoFactory);
        UiInputPropertyGenerator generator = factory.getUiInputPropertyGenerator(dataobject.getInfoTypeName());
        return generator.generateProperty(dataobject);
    }

    private String getGroupNamePrefixForStep(TransformationActivity analysis, TransformationStep step) {
        String prefix = "";
        if (analysis.isMultistep()) {
            Template template = daoFactory.getTemplateDao().findById(step.getTemplateId());
            if (template != null) {
                prefix = template.getName() + " - ";
            }
        }
        return prefix;
    }
}
