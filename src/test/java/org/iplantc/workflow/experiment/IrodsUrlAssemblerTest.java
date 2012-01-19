package org.iplantc.workflow.experiment;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for org.iplantc.workflow.experiment.IrodsUrlAssembler.
 * 
 * @author Dennis Roberts
 */
public class IrodsUrlAssemblerTest {

    /**
     * The URL assembler to use in each test.
     */
    private IrodsUrlAssembler assembler;

    /**
     * Initializes each of the unit tests.
     */
    @Before
    public void initialize() {
        assembler = new IrodsUrlAssembler();
        assembler.setHost("somehost");
        assembler.setPort("9999");
        assembler.setUser("user");
        assembler.setPassword("pass");
        assembler.setZone("iplant");
    }

    /**
     * Verifies that we can assemble a plain URL.
     */
    @Test
    public void shouldAssembleUrl() {
        String actual = assembler.assembleUrl("/foo/bar/baz");
        String expected = "irods://user:pass@somehost:9999/foo/bar/baz";
        assertEquals(expected, actual);
    }

    /**
     * Verifies that a path containing spaces is decoded appropriately.
     */
    @Test
    public void shouldEncodePathsWithSpaces() {
        String actual = assembler.assembleUrl("/foo/bar baz");
        String expected = "irods://user:pass@somehost:9999/foo/bar+baz";
        assertEquals(expected, actual);
    }
}
