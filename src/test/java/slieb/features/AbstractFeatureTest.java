package slieb.features;


import com.google.common.io.Files;
import com.google.inject.Injector;
import org.apache.tools.ant.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import slieb.blendercss.Loader;

import java.io.File;
import java.io.IOException;


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

    protected File workingDirectory;

    protected Injector injector;

    protected slieb.blendercss.Compiler compiler;

    @Before
    public void setup() {
        workingDirectory = getOutputDirectory();
        injector = Loader.getInjector(workingDirectory);
        compiler = injector.getInstance(slieb.blendercss.Compiler.class);
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.delete(workingDirectory);
    }
}
