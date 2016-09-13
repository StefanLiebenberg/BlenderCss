package slieb.issues;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;
import slieb.blendercss.BlendOptions;
import slieb.blendercss.internal.GssResource;
import slieb.features.AbstractFeatureTest;

import java.io.File;

import static java.nio.charset.Charset.defaultCharset;
import static org.junit.Assert.assertTrue;

public class Issue002Test extends AbstractFeatureTest {

    public File outputFile, outputRenameMap;

    @Before
    public void setupFiles() {
        outputFile = getOutputFile("style.css");
        outputRenameMap = getOutputFile("renameMap.js");
    }

    private void compileOptions(Boolean shouldCompile, Boolean shouldDebug) throws Throwable {
        blender.compile(
                new ImmutableList.Builder<GssResource>()
                        .add(getResourceFile("stylesheets/issues/i002/user.less"))
                        .build(),
                outputFile,
                new BlendOptions.Builder()
                        .setShouldCompile(shouldCompile)
                        .setShouldDebug(shouldDebug)
                        .setOutputCssRenameMap(outputRenameMap)
                        .build());
    }

    @Test
    public void testRenameMapForDevelopment() throws Throwable {
        compileOptions(false, false);
        assertTrue(outputRenameMap.exists());
        assertTrue(outputFile.exists());
        String content = Files.toString(outputRenameMap, defaultCharset());
        assertTrue(content.startsWith("CLOSURE_CSS_NAME_MAPPING ="));
    }

    @Test
    public void testRenameMapForProduction() throws Throwable {
        compileOptions(true, false);
        assertTrue(outputRenameMap.exists());
        assertTrue(outputFile.exists());
        String content = Files.toString(outputRenameMap, defaultCharset());
        assertTrue(content.startsWith("goog.setCssNameMapping("));
    }
}
