package slieb.features;


import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import slieb.blendercss.CompileOptions;

import java.io.File;
import java.util.List;

public class MixedCompileTest extends AbstractFeatureTest {

    private File outputFile, renameMap;

    @Before
    public void setupFiles() {
        outputFile = getOutputFile("style.css");
        renameMap = getOutputFile("rename.js");
    }

    @Test
    public void testMixedCompile() throws Throwable {

        List<File> inputFiles = new ImmutableList.Builder<File>()
                .add(getResourceFile("stylesheets/features/compass_compile/reset.scss"))
                .add(getResourceFile("stylesheets/features/compass_compile/boxes.sass"))
                .add(getResourceFile("stylesheets/features/compass_compile/simple.less"))
                .build();

        CompileOptions options = new CompileOptions.Builder()
                .setOutputCssRenameMap(renameMap)
                .build();

        compiler.compile(inputFiles, outputFile, options);
    }
}
