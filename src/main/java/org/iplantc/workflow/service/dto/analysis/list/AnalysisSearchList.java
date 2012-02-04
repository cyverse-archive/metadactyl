package org.iplantc.workflow.service.dto.analysis.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Session;
import org.iplantc.persistence.dto.listing.AnalysisGroup;
import org.iplantc.persistence.dto.listing.AnalysisListing;
import org.iplantc.workflow.service.dto.AbstractDto;
import org.iplantc.workflow.service.dto.JsonField;
import org.iplantc.workflow.util.Lambda;
import org.iplantc.workflow.util.ListUtils;

/**
 * A data transfer object representing a list of filtered analyses.
 * 
 * @author psarando
 */
public class AnalysisSearchList extends AbstractDto {

    /**
     * The list of filtered analyses.
     */
    @JsonField(name = "templates")
    private List<AnalysisSummary> templates;

    /**
     * @return the list of analyses.
     */
    public List<AnalysisSummary> getAnalyses() {
        return templates;
    }

    /**
     * @param session the Hibernate session that will filter the templates in analysisGroups.
     * @param searchTerm the string used by session to filter the templates in analysisGroups.
     * @param analysisGroups the list of groups containing the templates being marshaled.
     */
    public AnalysisSearchList(final Session session, final String searchTerm, List<AnalysisGroup> analysisGroups) {
        this(session, searchTerm, analysisGroups, new HashSet<AnalysisListing>(), new HashMap<Long, Integer>());
    }

    /**
     * @param session the Hibernate session that will filter the templates in analysisGroups.
     * @param searchTerm the string used by session to filter the templates in analysisGroups.
     * @param analysisGroups the list of groups containing the templates being marshaled.
     * @param favorites the template group containing the user's favorites.
     * @param userRatings the user's template ratings.
     */
    public AnalysisSearchList(final Session session, final String searchTerm, List<AnalysisGroup> analysisGroups, final Set<AnalysisListing> favorites,
            final Map<Long, Integer> userRatings) {
        templates = new ArrayList<AnalysisSummary>();

        extractTemplates(session, searchTerm, analysisGroups, favorites, userRatings);
    }

    /**
     * Extracts analyses from the list of template groups and their subgroups,
     * filtered by the given search term, and adds them to the templates list.
     *
     * @param session the Hibernate session that will filter the templates in analysisGroups.
     * @param searchTerm the string used by session to filter the templates in analysisGroups.
     * @param analysisGroups the list of groups containing the templates being marshaled.
     * @param favorites the template group containing the user's favorites.
     * @param userRatings the user's analysis ratings.
     * @return the list of analysis data transfer objects.
     */
    private void extractTemplates(final Session session, final String searchTerm, List<AnalysisGroup> analysisGroups, final Set<AnalysisListing> favorites,
            final Map<Long, Integer> userRatings) {
        for (final AnalysisGroup group : analysisGroups) {
            templates.addAll(ListUtils.map(new Lambda<AnalysisListing, AnalysisSummary>() {
                @Override
                public AnalysisSummary call(AnalysisListing arg) {
                    AnalysisSummary analysis = new AnalysisSummary(arg, favorites, userRatings);

                    analysis.setGroupId(group.getId());
                    analysis.setGroupName(group.getName());

                    return analysis;
                }
            }, group.filterAnalysesByNameOrDesc(session, searchTerm)));

            extractTemplates(session, searchTerm, group.getSubgroups(), favorites, userRatings);
        }
    }
}
