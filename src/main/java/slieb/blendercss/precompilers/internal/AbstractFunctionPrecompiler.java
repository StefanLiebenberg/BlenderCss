package slieb.blendercss.precompilers.internal;


import slieb.blendercss.BlendOptions;
import slieb.blendercss.internal.FileGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractFunctionPrecompiler extends AbstractPrecompiler {

    private final Pattern pattern;

    /**
     *
     * @param pattern
     * @param fileGenerator
     * @param inputExtensions IF null, then all inputs are allowed. ( important
     *                        to use other checks then to ensure that no
     *                        circular compiling happens )
     * @param outputExtension IF null, then original file's input
     *                        extension will be used.
     */
    protected AbstractFunctionPrecompiler(@Nonnull Pattern pattern,
                                          @Nonnull FileGenerator fileGenerator,
                                          @Nullable String[] inputExtensions,
                                          @Nullable String outputExtension) {
        super(fileGenerator, inputExtensions, outputExtension);
        this.pattern = pattern;
    }

    @Override
    public Boolean canCompile(File inputFile) {
        if (super.canCompile(inputFile)) {
            try (FileReader fileReader = new FileReader(inputFile);) {
                final BufferedReader reader = new BufferedReader(fileReader);
                String string;
                Boolean found = false;
                while ((string = reader.readLine()) != null && !found) {
                    if (pattern.matcher(string).find()) {
                        found = true;
                    }
                }
                reader.close();
                fileReader.close();
                return found;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    @Override
    public void compile(File inputFile, File outputFile, BlendOptions options) throws IOException {
        try (FileReader fileReader = new FileReader(inputFile)) {
            BufferedReader reader = new BufferedReader(fileReader);
            outputFile.getParentFile().mkdirs();
            try (FileWriter fileWriter = new FileWriter(outputFile)) {
                BufferedWriter writer = new BufferedWriter(fileWriter);
                String string;
                while ((string = reader.readLine()) != null) {
                    writer.write(parseLine(string, options));
                    writer.newLine();
                }
                writer.close();
            }
            reader.close();
        }
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
