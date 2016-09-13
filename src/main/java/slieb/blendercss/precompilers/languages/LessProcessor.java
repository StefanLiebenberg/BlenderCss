package slieb.blendercss.precompilers.languages;

import com.github.sommeri.less4j.Less4jException;
import com.github.sommeri.less4j.LessCompiler;
import com.github.sommeri.less4j.LessSource;
import com.google.inject.Inject;
import slieb.blendercss.BlendOptions;
import slieb.blendercss.internal.FileGenerator;
import slieb.blendercss.internal.GssResource;
import slieb.blendercss.precompilers.internal.AbstractProcessor;

import java.io.IOException;

public class LessProcessor extends AbstractProcessor {

    private static final String[] INPUT_EXTENSIONS = new String[]{".less"};

    private static final String OUTPUT_EXTENSION = "less.css";

    private final LessCompiler lessCompiler;

    @Inject
    public LessProcessor(FileGenerator fileGenerator, LessCompiler lessCompiler) {
        super(fileGenerator, Phase.LANGUAGES, INPUT_EXTENSIONS, OUTPUT_EXTENSION);
        this.lessCompiler = lessCompiler;
    }

    // why save as file anyway.
    @Override
    public GssResource processWithIO(GssResource code, BlendOptions options) throws IOException {
        try {
            final String fileName = renameExtension(code.getFileName());
            final LessSource.StringSource source = new LessSource.StringSource(code.getContents(), fileName);
            final LessCompiler.CompilationResult result = lessCompiler.compile(source);
            final String css = result.getCss();
            return new GssResource.StringGssResource(fileName, css, code.getOriginalSourceFile());
        } catch (Less4jException e) {
            throw new RuntimeException(e);
        }
    }
}
