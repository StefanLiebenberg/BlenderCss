package slieb.features;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import slieb.blendercss.BlendOptions;
import slieb.blendercss.internal.GssResource;
import slieb.blendercss.utilities.CssRenameMapParser;
import slieb.blendercss.utilities.RenamingMap;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CssRenamingMapTest extends AbstractFeatureTest {

    private CssRenameMapParser renameMapParser;

    @Before
    public void setupParser() {
        renameMapParser = injector.getInstance(CssRenameMapParser.class);
    }

    @Test
    public void testRenamingMapForBasicCss() throws Exception {
        
        List<GssResource> inputFiles = new ImmutableList.Builder<GssResource>()
                .add(getResourceFile("stylesheets/features/css_renaming_map/basic.css"))
                .build();

        File outputFile = getOutputFile("style.css");
        File renameMap = getOutputFile("rename.js");

        BlendOptions options = new BlendOptions.Builder()
                .setOutputCssRenameMap(renameMap)
                .build();

        blender.compile(inputFiles, outputFile, options);

        assertTrue(renameMap.exists());
        RenamingMap renamingMap = renameMapParser.parse(renameMap);
        assertTrue(renamingMap.containsKey("basic-css"));
    }

    @Test
    public void testRenamingMapForSassFiles() throws Exception {

        List<GssResource> inputFiles = new ImmutableList.Builder<GssResource>()
                .add(getResourceFile("stylesheets/features/css_renaming_map/basic.sass"))
                .build();

        File outputFile = getOutputFile("style.css");
        File renameMap = getOutputFile("rename.js");

        BlendOptions options = new BlendOptions.Builder()
                .setOutputCssRenameMap(renameMap)
                .build();

        blender.compile(inputFiles, outputFile, options);

        assertTrue(renameMap.exists());
        RenamingMap renamingMap = renameMapParser.parse(renameMap);
        assertTrue(renamingMap.containsKey("home-page"));
    }
}
