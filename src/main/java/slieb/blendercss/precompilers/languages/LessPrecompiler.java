package slieb.blendercss.precompilers.languages;

import com.github.sommeri.less4j.Less4jException;
import com.github.sommeri.less4j.LessCompiler;
import com.google.common.io.Files;
import com.google.inject.Inject;
import slieb.blendercss.CompileOptions;
import slieb.blendercss.internal.FileGenerator;
import slieb.blendercss.precompilers.internal.AbstractPrecompiler;

import java.io.File;
import java.io.IOException;

public class LessPrecompiler extends AbstractPrecompiler {

    private static final String[] INPUT_EXTENSIONS = new String[]{".less"};

    private static final String OUTPUT_EXTENSION = "less.css";

    private final LessCompiler lessCompiler;

    @Inject
    public LessPrecompiler(FileGenerator fileGenerator, LessCompiler lessCompiler) {
        super(fileGenerator, INPUT_EXTENSIONS, OUTPUT_EXTENSION);
        this.lessCompiler = lessCompiler;
    }

    @Override
    public void compile(File inputFile, File outputFile, CompileOptions options) throws IOException {
        try {
            LessCompiler.CompilationResult result = lessCompiler.compile(inputFile);
            String cssString = result.getCss();
            Files.write(cssString.getBytes(), outputFile);
        } catch (Less4jException e) {
            throw new RuntimeException(e);
        }
    }
}
