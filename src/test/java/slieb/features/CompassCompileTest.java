package slieb.features;


import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import org.apache.tools.ant.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import slieb.blendercss.CompileOptions;
import slieb.blendercss.Loader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CompassCompileTest extends AbstractFeatureTest {

    private File workingDirectory;

    private Injector injector;

    private slieb.blendercss.Compiler compiler;

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

    @Test
    public void testCompileCompassSCSSFile() throws Throwable {

        List<File> inputFiles = new ImmutableList.Builder<File>()
                .add(getResourceFile("stylesheets/features/compass_compile/reset.scss"))
                .build();

        File outputFile = getOutputFile("style.css");
        File renameMap = getOutputFile("rename.js");

        CompileOptions options = new CompileOptions.Builder()
                .setOutputCssRenameMap(renameMap)
                .build();

        compiler.compile(inputFiles, outputFile, options);

        assertTrue(outputFile.exists());
        assertTrue(renameMap.exists());
    }

    @Test
    public void testCompileCompassSASSFile() throws Throwable {

        List<File> inputFiles = new ImmutableList.Builder<File>()
                .add(getResourceFile("stylesheets/features/compass_compile/boxes.sass"))
                .build();

        File outputFile = getOutputFile("style.css");
        File renameMap = getOutputFile("rename.js");

        CompileOptions options = new CompileOptions.Builder()
                .setOutputCssRenameMap(renameMap)
                .build();

        compiler.compile(inputFiles, outputFile, options);

        assertTrue(outputFile.exists());
        assertTrue(renameMap.exists());
    }

    @Test
    public void testCompileCompassLESSFile() throws Throwable {
        List<File> inputFiles = new ImmutableList.Builder<File>()
                .add(getResourceFile("stylesheets/features/compass_compile/simple.less"))
                .build();

        File outputFile = getOutputFile("style.css");
        File renameMap = getOutputFile("rename.js");

        CompileOptions options = new CompileOptions.Builder()
                .setOutputCssRenameMap(renameMap)
                .build();

        compiler.compile(inputFiles, outputFile, options);

        assertTrue(outputFile.exists());
        assertTrue(renameMap.exists());
    }
}
