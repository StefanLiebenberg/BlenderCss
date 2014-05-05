package slieb.blendercss.precompilers.internal;

import com.google.common.base.Preconditions;
import slieb.blendercss.CompileOptions;
import slieb.blendercss.internal.FileGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;


public abstract class AbstractPrecompiler implements CssPrecompiler {

    protected final FileGenerator fileGenerator;
    protected final String[] inputExtensions;
    protected final String outputExtension;

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
    public File compile(File inputFile, CompileOptions options) throws IOException {
        File outputFile = fileGenerator.getOutputFileFor(inputFile, outputExtension);
        outputFile.getParentFile().mkdirs();
        compile(inputFile, outputFile, options);
        Preconditions.checkState(outputFile.exists(), "Should have created output file.");
        return outputFile;
    }

    public abstract void compile(File inputFile, File outputFile, CompileOptions options) throws IOException;
}
