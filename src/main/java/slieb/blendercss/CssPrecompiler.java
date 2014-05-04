package slieb.blendercss;


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
    public File compile(File inputFile) throws IOException;
}
