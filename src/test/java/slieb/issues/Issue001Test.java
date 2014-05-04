package slieb.issues;


import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import slieb.blendercss.CompileOptions;
import slieb.features.AbstractFeatureTest;

import java.io.File;

import static org.junit.Assert.assertFalse;

public class Issue001Test extends AbstractFeatureTest {

    private File pwdCacheDirectory, outputFile;

    @Before
    public void setupFile() {
        pwdCacheDirectory = new File(".sass-cache");
        outputFile = getOutputFile("style.css");
    }

    @Before
    public void ensureNoCache() {
        assertFalse(pwdCacheDirectory.exists());
    }

    @Test
    public void testNoCache() throws Throwable {
        assertFalse(pwdCacheDirectory.exists());
        compiler.compile(
                new ImmutableList.Builder<File>()
                .add(getResourceFile("stylesheets/features/compass_compile/boxes.sass"))
                .build(), outputFile,
                new CompileOptions.Builder().build());
        assertFalse(pwdCacheDirectory.exists());
    }
}
