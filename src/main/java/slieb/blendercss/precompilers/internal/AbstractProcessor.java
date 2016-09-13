package slieb.blendercss.precompilers.internal;

import slieb.blendercss.internal.FileGenerator;
import slieb.blendercss.internal.GssResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractProcessor implements CssProcessor {

    protected final FileGenerator fileGenerator;

    private final Phase phase;

    private final String[] inputExtensions;

    private final String outputExtension;

    /**
     * @param fileGenerator   utility class
     * @param phase
     * @param inputExtensions IF null, then all inputs are allowed. ( important
     *                        to use other checks then to ensure that no
     *                        circular compiling happens )
     * @param outputExtension IF null, then original file's input
     */
    protected AbstractProcessor(@Nonnull FileGenerator fileGenerator, final Phase phase, @Nullable String[] inputExtensions,
                                @Nullable String outputExtension) {
        this.fileGenerator = fileGenerator;
        this.phase = phase;
        this.inputExtensions = inputExtensions;
        this.outputExtension = outputExtension;
    }

    @Override
    public Boolean canProcess(Phase phase, GssResource input) {
        if (phase != this.phase) {
            return false;
        }

        if (inputExtensions != null) {
            String name = input.getFileName();
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

    protected String renameExtension(String filename) {
        if (inputExtensions != null && outputExtension != null) {
            for (String ext : inputExtensions) {
                if (filename.endsWith(ext)) {
                    int index = filename.indexOf(ext);
                    return new StringBuilder(filename).replace(index, index + ext.length(), outputExtension).toString();
                }
            }
        }
        return filename;
    }
}
