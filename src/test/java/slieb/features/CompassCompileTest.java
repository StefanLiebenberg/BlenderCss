package slieb.features;

import org.junit.Before;
import org.junit.Test;
import slieb.blendercss.BlendOptions;
import slieb.blendercss.internal.GssResource;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class CompassCompileTest extends AbstractFeatureTest {

    private File outputFile, renameMap;

    @Before
    public void setupFiles() {
        outputFile = getOutputFile("style.css");
        renameMap = getOutputFile("rename.js");
    }

    private void compileWithRename(GssResource... items) {
        blender.compile(resources(items), outputFile, new BlendOptions.Builder()
                .setOutputCssRenameMap(renameMap)
                .build());
    }

    @Test
    public void testCompileCompassSCSSFile() throws Throwable {
        compileWithRename(getResourceFile("stylesheets/features/compass_compile/reset.scss"));
        assertTrue(outputFile.exists());
        assertTrue(renameMap.exists());
    }

    @Test
    public void testCompileCompassSASSFile() throws Throwable {
        compileWithRename(getResourceFile("stylesheets/features/compass_compile/boxes.sass"));
        assertTrue(outputFile.exists());
        assertTrue(renameMap.exists());
    }

    @Test
    public void testCompileCompassLESSFile() throws Throwable {
        compileWithRename(getResourceFile("stylesheets/features/compass_compile/simple.less"));
        assertTrue(outputFile.exists());
        assertTrue(renameMap.exists());
    }
}
