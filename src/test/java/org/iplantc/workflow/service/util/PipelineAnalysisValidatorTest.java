package org.iplantc.workflow.service.util;

import org.iplantc.persistence.dto.step.TransformationStep;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.dao.mock.MockDaoFactory;
import org.iplantc.workflow.util.UnitTestUtils;
import org.junit.Test;

/**
 * Unit tests for org.iplantc.workflow.service.util.PipelineAnalysisValidator.
 * 
 * @author Dennis Roberts
 */
public class PipelineAnalysisValidatorTest {
    
    /**
     * Verifies that we get an exception for an analysis with no steps.
     */
    @Test(expected = WorkflowException.class)
    public void testEmptyAnalysis() {
        TransformationActivity analysis = UnitTestUtils.createAnalysis("analysis");
        PipelineAnalysisValidator.validateAnalysis(analysis);
    }

    /**
     * Verifies that we get an exception for a multi-step analysis.
     */
    @Test(expected = WorkflowException.class)
    public void testMultistepAnalysis() {
        TransformationActivity analysis = UnitTestUtils.createAnalysis("analysis");
        analysis.addStep(new TransformationStep());
        analysis.addStep(new TransformationStep());
        PipelineAnalysisValidator.validateAnalysis(analysis);
    }

    /**
     * Verifies that a single step analysis passes validation.
     */
    @Test
    public void testSingleStepAnalysis() {
        TransformationActivity analysis = UnitTestUtils.createAnalysis("analysis");
        analysis.addStep(new TransformationStep());
        PipelineAnalysisValidator.validateAnalysis(analysis);
    }

    /**
     * Verifies that the validator can validate an analysis using its ID and a data access object factory.
     */
    @Test
    public void testValidationById() {
        MockDaoFactory daoFactory = new MockDaoFactory();
        TransformationActivity analysis = UnitTestUtils.createAnalysis("analysis");
        analysis.addStep(new TransformationStep());
        daoFactory.getTransformationActivityDao().save(analysis);
        PipelineAnalysisValidator.validateAnalysis("analysisid", daoFactory);
    }

    /**
     * Verifies that the validator throws an exception for an unknown analysis ID.
     */
    @Test(expected = WorkflowException.class)
    public void testUnknownAnalysisId() {
        PipelineAnalysisValidator.validateAnalysis("unknownid", new MockDaoFactory());
    }
}
