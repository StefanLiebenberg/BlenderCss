package slieb.blendercss;


import com.google.inject.Inject;
import slieb.blendercss.api.GssCompilerApi;
import slieb.blendercss.precompilers.internal.CssPrecompiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Blender {

    private final Set<CssPrecompiler> preCompilers;

    private final GssCompilerApi gssCompilerApi;

    @Inject
    public Blender(Set<CssPrecompiler> precompilers, GssCompilerApi gssCompilerApi) {
        this.preCompilers = precompilers;
        this.gssCompilerApi = gssCompilerApi;
    }

    private File precompileFilePass(File inputFile, BlendOptions options) throws IOException {
        for (CssPrecompiler precompiler : preCompilers) {
            if (precompiler.canCompile(inputFile)) {
                return precompiler.compile(inputFile, options);
            }
        }
        return null;
    }

    private File precompileFile(File inputFile, BlendOptions options) throws IOException {
        File outputFile = precompileFilePass(inputFile, options);
        if (outputFile != null) {
            return precompileFile(outputFile, options);
        } else {
            return inputFile;
        }
    }

    private List<File> precompile(List<File> inputFiles, BlendOptions options) throws IOException {
        List<File> result = new ArrayList<>();
        if (inputFiles != null && !inputFiles.isEmpty()) {
            for (File inputFile : inputFiles) {
                result.add(precompileFile(inputFile, options));
            }
        }
        return result;
    }


    private void compileFiles(List<File> inputFiles, File outputFile, BlendOptions options) throws IOException {
        for (File inputFile : inputFiles) {
            String name = inputFile.getName();
            if (!(name.endsWith(".css") || name.endsWith(".gss"))) {
                System.err.println(String.format("Warning: File '%s' does not have css or gss extension, assuming the file compatible with gss compiler.", inputFile.getPath()));
            }
        }
        gssCompilerApi.compile(inputFiles, outputFile, options);
    }

    public void compile(List<File> inputFiles, File outputFile, BlendOptions options) throws IOException {
        compileFiles(precompile(inputFiles, options), outputFile, options);
    }
}

