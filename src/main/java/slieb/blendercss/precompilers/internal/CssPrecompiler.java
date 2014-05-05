package slieb.blendercss.precompilers.internal;


import slieb.blendercss.CompileOptions;

import java.io.File;
import java.io.IOException;

public interface CssPrecompiler {

    /**
     * @param inputFile
     * @return
     */
    public Boolean canCompile(File inputFile);

    /**
     * @param inputFile The input file.
     * @return The output file.
     * @throws IOException
     */
    public File compile(File inputFile, CompileOptions options) throws IOException;
}
