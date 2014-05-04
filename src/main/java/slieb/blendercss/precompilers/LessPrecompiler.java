package slieb.blendercss.precompilers;

import com.github.sommeri.less4j.Less4jException;
import com.github.sommeri.less4j.LessCompiler;
import com.google.common.io.Files;
import com.google.inject.Inject;
import slieb.blendercss.CssPrecompiler;
import slieb.blendercss.FileGenerator;

import java.io.File;
import java.io.IOException;

public class LessPrecompiler implements CssPrecompiler {

    private final FileGenerator fileGenerator;

    private final LessCompiler lessCompiler;

    @Inject
    public LessPrecompiler(FileGenerator fileGenerator, LessCompiler lessCompiler) {
        this.fileGenerator = fileGenerator;
        this.lessCompiler = lessCompiler;
    }

    @Override
    public Boolean canCompile(File inputFile) {
        return inputFile.getName().endsWith(".less");
    }

    @Override
    public File compile(File inputFile) throws IOException {
        try {
            File outputFile = fileGenerator.getOutputFileFor(inputFile, "css");
            String cssString = lessCompiler.compile(inputFile).getCss();
            outputFile.getParentFile().mkdirs();
            Files.write(cssString.getBytes(), outputFile);
            return outputFile;
        } catch (Less4jException e) {
            throw new RuntimeException(e);
        }
    }
}
