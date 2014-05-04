package slieb.features;


import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import slieb.blendercss.CompileOptions;
import slieb.blendercss.Compiler;
import slieb.blendercss.Loader;

import java.io.File;
import java.util.List;

public class MixedCompileTest extends AbstractFeatureTest {
    private Injector injector = Loader.getInjector();

    private slieb.blendercss.Compiler compiler;

    @Before
    public void setup() {
        compiler = injector.getInstance(Compiler.class);
    }

    @Test
    public void testMixedCompile() throws Throwable {

        List<File> inputFiles = new ImmutableList.Builder<File>()
                .add(getResourceFile("stylesheets/basic.css"))
                .add(getResourceFile("stylesheets/basic.sass"))
                .build();

        File outputFile = getOutputFile("style.css");
        File renameMap = getOutputFile("rename.js");

        CompileOptions options = new CompileOptions.Builder()
                .setOutputCssRenameMap(renameMap)
                .build();

        compiler.compile(inputFiles, outputFile, options);
    }
}
