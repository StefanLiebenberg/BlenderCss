package slieb.features;

import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.tools.ant.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import slieb.blendercss.BlendOptions;
import slieb.blendercss.Blender;
import slieb.blendercss.Loader;
import slieb.blendercss.internal.GssResource;
import slieb.blendercss.internal.SourceCodeHelper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class AbstractFeatureTest {

    private File resourcesDirectory = new File("src/test/resources");

    private File outputDirectory;

    protected List<GssResource> resources(GssResource... resources) {
        return Arrays.stream(resources).collect(toList());
    }

    protected void generalCompile(File outputFile, final GssResource... items) {
        blender.compile(resources(items), outputFile, new BlendOptions.Builder()
                .setShouldCompile(true)
                .setShouldDebug(true)
                .build());
    }

    private File getOutputDirectory() {
        if (outputDirectory == null) {
            outputDirectory = Files.createTempDir();
        }
        return outputDirectory;
    }

    protected File getOutputFile(String path) {
        return new File(getOutputDirectory(), path);
    }

    protected GssResource getResourceFile(String path) throws IOException {
        return SourceCodeHelper.sourceCodeFromFile(new File(resourcesDirectory, path));
    }

    private File workingDirectory;

    protected Blender blender;

    protected Injector injector;

    @Before
    public void setup() {
        workingDirectory = getOutputDirectory();
        injector = Guice.createInjector(Loader.getDefaultBlenderModule(workingDirectory));
        blender = injector.getInstance(Blender.class);
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.delete(workingDirectory);
    }
}
