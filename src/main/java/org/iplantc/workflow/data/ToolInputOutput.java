package org.iplantc.workflow.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.marshaler.BaseTemplateMarshaller;
import org.iplantc.workflow.model.WorkflowElement;
import org.springframework.util.ObjectUtils;

/**
 * Represents an input to or an output from a tool.
 *
 * @author Dennis Roberts
 */
public class ToolInputOutput extends WorkflowElement {

    /**
     * Indicates whether the input or output refers to one or multiple files.
     */
    private String multiplicity;

    /**
     * Indicates the relative position of the argument in the argument list.
     */
    private int argumentListPosition;

    /**
     * The command-line option used to specify the input or output.
     */
    private String commandLineOption;

    /**
     * The list of supported data objects.
     */
    private List<DataObject> supportedDataObjects = new ArrayList<DataObject>();

    /**
     * Creates an empty input/output place holder.
     */
    public ToolInputOutput() {
        super();
    }

    /**
     * Creates an input/output place holder with the given id, name, label and description
     *
     * @param id the place holder identifier.
     * @param name the place holder name.
     * @param label the place holder label.
     * @param description the place holder description.
     */
    public ToolInputOutput(String id, String name, String label, String description) {
        super(id, name, label, description);
    }

    /**
     * @return the current multiplicity setting.
     */
    public String getMultiplicity() {
        return multiplicity;
    }

    /**
     * @param multiplicity the new multiplicity setting.
     */
    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    /**
     * @return the current argument list position.
     */
    public int getArgumentListPosition() {
        return argumentListPosition;
    }

    /**
     * @param argumentListPosition the new argument list position.
     */
    public void setArgumentListPosition(int argumentListPosition) {
        this.argumentListPosition = argumentListPosition;
    }

    /**
     * @return the current command-line option.
     */
    public String getCommandLineOption() {
        return commandLineOption;
    }

    /**
     * @param commandLineOption the new command-line option.
     */
    public void setCommandLineOption(String commandLineOption) {
        this.commandLineOption = commandLineOption;
    }

    /**
     * @return an unmodifiable copy of the current list of supported data objects.
     */
    public List<DataObject> getSupportedDataObjects() {
        return supportedDataObjects;
    }

    /**
     * @param supportedDataObjects the new list of supported data objects.
     */
    public void setSupportedDataObjects(List<DataObject> supportedDataObjects) {
        this.supportedDataObjects.clear();
        this.supportedDataObjects.addAll(supportedDataObjects);
    }

    /**
     * Adds a new data object to the list of supported data objects.
     *
     * @param dataObject
     */
    public void addSupportedDataObject(DataObject dataObject) {
        supportedDataObjects.add(dataObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(BaseTemplateMarshaller marshaller) throws WorkflowException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        boolean equals = false;
        if (o instanceof ToolInputOutput) {
            ToolInputOutput other = (ToolInputOutput) o;
            equals = super.equals(other);
            if (equals && !StringUtils.equals(multiplicity, other.getMultiplicity())) {
                equals = false;
            }
            if (equals && argumentListPosition != other.getArgumentListPosition()) {
                equals = false;
            }
            if (equals && !StringUtils.equals(commandLineOption, other.getCommandLineOption())) {
                equals = false;
            }
            if (equals && !ObjectUtils.nullSafeEquals(supportedDataObjects, other.getSupportedDataObjects())) {
                equals = false;
            }
        }
        return equals;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += ObjectUtils.nullSafeHashCode(multiplicity);
        hashCode += ObjectUtils.hashCode(argumentListPosition);
        hashCode += ObjectUtils.nullSafeHashCode(commandLineOption);
        hashCode += ObjectUtils.nullSafeHashCode(supportedDataObjects);
        return hashCode;
    }
}
