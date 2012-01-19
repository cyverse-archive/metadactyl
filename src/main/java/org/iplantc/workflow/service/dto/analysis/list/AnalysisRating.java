package org.iplantc.workflow.service.dto.analysis.list;

import java.util.Map;
import net.sf.json.JSONObject;
import org.iplantc.persistence.dto.listing.AnalysisListing;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.service.dto.AbstractDto;
import org.iplantc.workflow.service.dto.JsonField;

/**
 * A data transfer object representing an analysis rating.
 * 
 * @author Dennis Roberts
 */
public class AnalysisRating extends AbstractDto {

    /**
     * The average rating for the analysis.
     */
    @JsonField(name = "average")
    private double average;

    /**
     * The rating assigned to the analysis by the current user.
     */
    @JsonField(name = "user", optional = true)
    private Integer user;

    /**
     * @return the average rating.
     */
    public double getAverage() {
        return average;
    }

    /**
     * @return the rating assigned to the analysis by the user or null if the user hasn't rated the analysis.
     */
    public Integer getUser() {
        return user;
    }

    /**
     * A package-private constructor for use with unit tests.
     * 
     * @param average the average rating.
     * @param user the user.
     */
    AnalysisRating(double average, Integer user) {
        this.average = average;
        this.user = user;
    }

    /**
     * @param json a JSON object representing the analysis rating.
     */
    public AnalysisRating(JSONObject json) {
        fromJson(json);
    }

    /**
     * @param str a JSON string representing the analysis rating.
     */
    public AnalysisRating(String str) {
        fromString(str);
    }

    public AnalysisRating(AnalysisListing analysis) {
        this.average = analysis.getAverageRating();
        this.user = null;
    }

    /**
     * @param analysis the analysis that these rating values apply to.
     * @param userRatings the user's analysis ratings.
     */
    public AnalysisRating(AnalysisListing analysis, Map<Long, Integer> userRatings) {
        this.average = analysis.getAverageRating();
        this.user = userRatings.get(analysis.getHid());
    }
}
