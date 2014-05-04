package slieb.features;


import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import slieb.blendercss.CompileOptions;
import slieb.blendercss.Compiler;
import slieb.blendercss.Loader;
import slieb.blendercss.utilities.CssRenameMapParser;
import slieb.blendercss.utilities.RenamingMap;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CssRenamingMapTest extends AbstractFeatureTest {

    private Injector injector = Loader.getInjector();

    private CssRenameMapParser renameMapParser;

    private Compiler compiler;

    @Before
    public void setup() {
        renameMapParser = injector.getInstance(CssRenameMapParser.class);
        compiler = injector.getInstance(Compiler.class);
    }

    @Test
    public void testRenamingMapForBasicCss() throws Exception {

        List<File> inputFiles = new ImmutableList.Builder<File>()
                .add(getResourceFile("stylesheets/basic.css"))
                .build();

        File outputFile = getOutputFile("style.css");
        File renameMap = getOutputFile("rename.js");

        CompileOptions options = new CompileOptions.Builder()
                .setOutputCssRenameMap(renameMap)
                .build();

        compiler.compile(inputFiles, outputFile, options);

        assertTrue(renameMap.exists());
        RenamingMap renamingMap = renameMapParser.parse(renameMap);
        assertTrue(renamingMap.containsKey("basic-css"));
    }

    @Test
    public void testRenamingMapForSassFiles() throws Exception {

        List<File> inputFiles = new ImmutableList.Builder<File>()
                .add(getResourceFile("stylesheets/basic.sass"))
                .build();

        File outputFile = getOutputFile("style.css");
        File renameMap = getOutputFile("rename.js");

        CompileOptions options = new CompileOptions.Builder()
                .setOutputCssRenameMap(renameMap)
                .build();

        compiler.compile(inputFiles, outputFile, options);

        assertTrue(renameMap.exists());
        RenamingMap renamingMap = renameMapParser.parse(renameMap);
        assertTrue(renamingMap.containsKey("home-page"));
    }
}
