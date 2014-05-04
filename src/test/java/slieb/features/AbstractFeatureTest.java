package slieb.features;


import com.google.common.io.Files;

import java.io.File;


public abstract class AbstractFeatureTest {

    private File resourcesDirectory = new File("src/test/resources");

    private File outputDirectory;

    public File getOutputDirectory() {
        if (outputDirectory == null) {
            outputDirectory = Files.createTempDir();
        }
        return outputDirectory;
    }

    public File getOutputFile(String path) {
        return new File(getOutputDirectory(), path);
    }

    public File getResourcesDirectory() {
        return resourcesDirectory;
    }

    public File getResourceFile(String path) {
        return new File(resourcesDirectory, path);
    }

    public File getStylesheetsFile(String path) {
        return new File(getResourceFile("stylesheets"), path);
    }
}
