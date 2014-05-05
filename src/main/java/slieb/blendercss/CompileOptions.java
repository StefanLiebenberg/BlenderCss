package slieb.blendercss;


import com.google.common.css.JobDescription;
import com.google.common.css.Vendor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;

@Immutable
public class CompileOptions {

    private final Boolean shouldCompile, shouldDebug;
    private final String imagesPath, outputPath;
    private final File outputCssRenameMap;
    private final JobDescription.InputOrientation orientation;
    private final Vendor vendor;

    public CompileOptions(@Nonnull Boolean shouldCompile,
                          @Nonnull Boolean shouldDebug,
                          @Nullable String imagesPath,
                          @Nullable String outputPath,
                          @Nullable File outputCssRenameMap,
                          @Nullable JobDescription.InputOrientation orientation,
                          @Nullable Vendor vendor) {
        this.shouldCompile = shouldCompile;
        this.shouldDebug = shouldDebug;
        this.imagesPath = imagesPath;
        this.outputPath = outputPath;
        this.outputCssRenameMap = outputCssRenameMap;
        this.orientation = orientation;
        this.vendor = vendor;
    }

    @Nonnull
    public Boolean getShouldCompile() {
        return shouldCompile;
    }

    @Nonnull
    public Boolean getShouldDebug() {
        return shouldDebug;
    }

    @Nullable
    public String getOutputPath() {
        return outputPath;
    }

    @Nullable
    public String getImagesPath() {
        return imagesPath;
    }

    @Nullable
    public File getOutputCssRenameMap() {
        return outputCssRenameMap;
    }

    @Nullable
    public JobDescription.InputOrientation getOrientation() {
        return orientation;
    }

    @Nullable
    public Vendor getVendor() {
        return vendor;
    }

    public static class Builder {
        private Boolean shouldCompile = false, shouldDebug = false;
        private String imagesPath, outputPath;
        private File outputCssRenameMap;
        private JobDescription.InputOrientation orientation;
        private Vendor vendor;

        public Builder setShouldCompile(@Nonnull Boolean shouldCompile) {
            this.shouldCompile = shouldCompile;
            return this;
        }

        public Builder setShouldDebug(@Nonnull Boolean shouldDebug) {
            this.shouldDebug = shouldDebug;
            return this;
        }

        public Builder setImagesPath(@Nullable String imagesPath) {
            this.imagesPath = imagesPath;
            return this;
        }

        public Builder setOutputPath(@Nullable String outputPath) {
            this.outputPath = outputPath;
            return this;
        }

        public Builder setOutputCssRenameMap(@Nullable File outputCssRenameMap) {
            this.outputCssRenameMap = outputCssRenameMap;
            return this;
        }

        public void setOrientation(@Nullable JobDescription.InputOrientation orientation) {
            this.orientation = orientation;
        }

        public void setVendor(@Nullable Vendor vendor) {
            this.vendor = vendor;
        }

        @Nonnull
        public CompileOptions build() {
            return new CompileOptions(shouldCompile, shouldDebug, imagesPath, outputPath, outputCssRenameMap, orientation, vendor);
        }
    }

}
