package slieb.features;


import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import slieb.blendercss.BlendOptions;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class CompassCompileTest extends AbstractFeatureTest {

    private File outputFile, renameMap;

    @Before
    public void setupFiles() {
        outputFile = getOutputFile("style.css");
        renameMap = getOutputFile("rename.js");
    }


    @Test
    public void testCompileCompassSCSSFile() throws Throwable {
        blender.compile(
                new ImmutableList.Builder<File>()
                        .add(getResourceFile("stylesheets/features/compass_compile/reset.scss"))
                        .build(),
                outputFile,
                new BlendOptions.Builder()
                        .setOutputCssRenameMap(renameMap)
                        .build());

        assertTrue(outputFile.exists());
        assertTrue(renameMap.exists());
    }

    @Test
    public void testCompileCompassSASSFile() throws Throwable {
        blender.compile(
                new ImmutableList.Builder<File>()
                        .add(getResourceFile("stylesheets/features/compass_compile/boxes.sass"))
                        .build(),
                outputFile,
                new BlendOptions.Builder()
                        .setOutputCssRenameMap(renameMap)
                        .build());

        assertTrue(outputFile.exists());
        assertTrue(renameMap.exists());
    }

    @Test
    public void testCompileCompassLESSFile() throws Throwable {
        blender.compile(
                new ImmutableList.Builder<File>()
                        .add(getResourceFile("stylesheets/features/compass_compile/simple.less"))
                        .build(),
                outputFile,
                new BlendOptions.Builder()
                        .setOutputCssRenameMap(renameMap)
                        .build());
        assertTrue(outputFile.exists());
        assertTrue(renameMap.exists());
    }
}
