package slieb.issues;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import slieb.features.AbstractFeatureTest;

import java.io.File;

public class Issue010Test extends AbstractFeatureTest {

    private File outputFile;

    @Before
    public void setupOutputFile() throws Exception {
        outputFile = getOutputFile("output.css");
    }

    @Test
    public void testImportGeneral() throws Throwable {
        generalCompile(outputFile, getResourceFile("stylesheets/issues/i010/component.scss"));
        System.out.println(FileUtils.readFileToString(outputFile));
    }

    @Test
    public void testImportComponent() throws Throwable {
        generalCompile(outputFile,
                       getResourceFile("stylesheets/issues/i010/component.scss"),
                       getResourceFile("stylesheets/issues/i010/page.scss"));
        System.out.println(FileUtils.readFileToString(outputFile));
    }
}
