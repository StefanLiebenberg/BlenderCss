package slieb.blendercss.precompilers.internal;

import slieb.blendercss.BlendOptions;
import slieb.blendercss.internal.FileGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkState;

public abstract class AbstractPrecompiler implements CssPrecompiler {

    protected final FileGenerator fileGenerator;
    protected final String[] inputExtensions;
    protected final String outputExtension;

    /**
     * @param fileGenerator
     * @param inputExtensions IF null, then all inputs are allowed. ( important
     *                        to use other checks then to ensure that no
     *                        circular compiling happens )
     * @param outputExtension IF null, then original file's input
     *                        extension will be used.
     */
    protected AbstractPrecompiler(@Nonnull FileGenerator fileGenerator, @Nullable String[] inputExtensions, @Nullable String outputExtension) {
        this.fileGenerator = fileGenerator;
        this.inputExtensions = inputExtensions;
        this.outputExtension = outputExtension;
    }

    @Override
    public Boolean canCompile(File inputFile) {
        if (inputExtensions != null) {
            String name = inputFile.getName();
            for (String ext : inputExtensions) {
                if (name.endsWith(ext)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public File compile(File inputFile, BlendOptions options) throws IOException {
        final File outputFile = fileGenerator.getOutputFileFor(inputFile, outputExtension);
        outputFile.getParentFile().mkdirs();
        compile(inputFile, outputFile, options);
        checkState(outputFile.exists(), "Should have created output file.");
        return outputFile;
    }

    public abstract void compile(File inputFile, File outputFile, BlendOptions options) throws IOException;
}
