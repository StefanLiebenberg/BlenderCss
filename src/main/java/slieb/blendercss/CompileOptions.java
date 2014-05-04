package slieb.blendercss;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public class CompileOptions {
    private final File imagesDirectory, outputCssRenameMap;
    private final Boolean shouldCompile, shouldDebug;

    public CompileOptions(@Nullable File imagesDirectory, @Nullable File outputCssRenameMap, @Nonnull Boolean shouldCompile, @Nonnull Boolean shouldDebug) {
        this.imagesDirectory = imagesDirectory;
        this.outputCssRenameMap = outputCssRenameMap;
        this.shouldCompile = shouldCompile;
        this.shouldDebug = shouldDebug;
    }

    public File getImagesDirectory() {
        return imagesDirectory;
    }

    public File getOutputCssRenameMap() {
        return outputCssRenameMap;
    }

    @Nonnull
    public Boolean getShouldCompile() {
        return shouldCompile;
    }

    @Nonnull
    public Boolean getShouldDebug() {
        return shouldDebug;
    }

    public static class Builder {
        private File imagesDirectory, outputCssRenameMap;
        private Boolean shouldCompile = false, shouldDebug = false;

        public Builder setImagesDirectory(File imagesDirectory) {
            this.imagesDirectory = imagesDirectory;
            return this;
        }

        public Builder setOutputCssRenameMap(File outputCssRenameMap) {
            this.outputCssRenameMap = outputCssRenameMap;
            return this;
        }

        public Builder setShouldCompile(Boolean shouldCompile) {
            this.shouldCompile = shouldCompile;
            return this;
        }

        public Builder setShouldDebug(Boolean shouldDebug) {
            this.shouldDebug = shouldDebug;
            return this;
        }

        public CompileOptions build() {
            return new CompileOptions(imagesDirectory, outputCssRenameMap, shouldCompile, shouldDebug);
        }
    }

}
