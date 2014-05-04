package slieb.blendercss;


import com.google.inject.Inject;
import slieb.blendercss.api.GssCompilerApi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Compiler {

    private final Set<CssPrecompiler> preCompilers;

    private final GssCompilerApi gssCompilerApi;

    @Inject
    public Compiler(Set<CssPrecompiler> precompilers, GssCompilerApi gssCompilerApi) {
        this.preCompilers = precompilers;
        this.gssCompilerApi = gssCompilerApi;
    }

    private File precompileFilePass(File inputFile) throws IOException {
        for (CssPrecompiler precompiler : preCompilers) {
            if (precompiler.canCompile(inputFile)) {
                return precompiler.compile(inputFile);
            }
        }
        return null;
    }

    private File precompileFile(File inputFile) throws IOException {
        File outputFile = precompileFilePass(inputFile);
        if (outputFile != null) {
            return precompileFile(outputFile);
        } else {
            return inputFile;
        }
    }

    private List<File> precompile(List<File> inputFiles) throws IOException {
        List<File> result = new ArrayList<>();
        for (File inputFile : inputFiles) {
            result.add(precompileFile(inputFile));
        }
        return result;
    }


    private void compileFiles(List<File> inputFiles, File outputFile, CompileOptions options) throws IOException {
        for (File inputFile : inputFiles) {
            String name = inputFile.getName();
            if (!(name.endsWith(".css") || name.endsWith(".gss"))) {
                System.err.println(String.format("Warning: File '%s' does not have css or gss extension, assuming the file compatible with gss compiler.", inputFile.getPath()));
            }
        }
        gssCompilerApi.compile(inputFiles, outputFile, options);
    }

    public void compile(List<File> inputFiles, File outputFile, CompileOptions options) throws IOException {
        compileFiles(precompile(inputFiles), outputFile, options);
    }
}

