package slieb.blendercss.precompilers.internal;

import slieb.blendercss.BlendOptions;
import slieb.blendercss.internal.FileGenerator;
import slieb.blendercss.internal.GssResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractFunctionProcessor extends AbstractProcessor {

    private final Pattern pattern;

    /**
     * @param pattern         A pattern to match against the file contents.
     * @param fileGenerator   A instance of the file generator helper class.
     * @param phase           The phase in which this processor is active.
     * @param inputExtensions IF null, then all inputs are allowed. ( important
     *                        to use other checks then to ensure that no
     *                        circular compiling happens )
     * @param outputExtension IF null, then original file's input
     *                        extension will be used.
     */
    protected AbstractFunctionProcessor(@Nonnull Pattern pattern,
                                        @Nonnull FileGenerator fileGenerator,
                                        @Nonnull Phase phase,
                                        @Nullable String[] inputExtensions,
                                        @Nullable String outputExtension) {
        super(fileGenerator, phase, inputExtensions, outputExtension);
        this.pattern = pattern;
    }

    @Override
    public Boolean canProcess(Phase phase, GssResource input) {
        if (super.canProcess(phase, input)) {
            if (pattern.matcher(input.getContents()).find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public GssResource processWithIO(GssResource sourceCode, final BlendOptions options) throws IOException {
        final String fileName = sourceCode.getFileName();
        final File originalSourceFile = sourceCode.getOriginalSourceFile();
        final String content = new BufferedReader(new StringReader(sourceCode.getContents()))
                .lines()
                .map(line -> parseLine(line, options))
                .collect(Collectors.joining("\n"));
        return new GssResource.StringGssResource(fileName, content, originalSourceFile);
    }

    private String[] getArgs(final Matcher matcher) {
        final int length = matcher.groupCount();
        final String[] arguments = new String[length + 1];
        for (int i = 0; i <= length; i++) {
            arguments[i] = matcher.group(i);
        }
        return arguments;
    }

    private String parseLine(String line, BlendOptions options) {
        final Matcher matcher = pattern.matcher(line);
        final StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, parseFunction(options, getArgs(matcher)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    protected abstract String parseFunction(BlendOptions options, String... args);
}
