package slieb.blendercss.precompilers.languages;

import com.google.inject.Inject;
import slieb.blendercss.BlendOptions;
import slieb.blendercss.api.CompassEngineApi;
import slieb.blendercss.internal.FileGenerator;
import slieb.blendercss.internal.GssResource;
import slieb.blendercss.internal.SourceCodeHelper;
import slieb.blendercss.precompilers.internal.AbstractProcessor;

import java.io.File;
import java.io.IOException;

public class CompassProcessor extends AbstractProcessor {

    private static final String[] INPUT_EXTENSIONS = new String[]{".sass", ".scss"};

    private static final String OUTPUT_EXTENSIONS = "compass.css";

    private final CompassEngineApi compassApi;

    private final File compassDirectory;

    @Inject
    public CompassProcessor(FileGenerator fileGenerator, CompassEngineApi compassApi) {
        super(fileGenerator, Phase.LANGUAGES, INPUT_EXTENSIONS, OUTPUT_EXTENSIONS);
        this.compassApi = compassApi;
        this.compassDirectory = fileGenerator.getOutputDirectory("compass");
    }

    @Override
    public GssResource processWithIO(GssResource input, BlendOptions options) throws IOException {
        File inFile = new File(compassDirectory, input.getFileName());
        SourceCodeHelper.saveSourceCodeInFile(input, inFile);
        File outFile = fileGenerator.getOutputFileFor(input, ".css");
        //        compassApi.setImporter(input.getOriginalSourceFile());
        compassApi.setOption("filename", inFile.getPath());
        compassApi.unshiftLoadpath(input.getOriginalSourceFile()
                                        .getParentFile()
                                        .getAbsolutePath());

        String cssFilename = options.getOutputPath();
        if (cssFilename != null) {
            compassApi.setOption("css_filename", cssFilename);
        } else {
            compassApi.setOption("css_filename", null);
        }
        compassApi.setOption("cache_location", fileGenerator.getCacheDirectory().getPath());
        compassApi.setHttpImagesPath(options.getImagesPath());
        compassApi.setRelativeAssets(false);
        compassApi.compile(inFile, outFile);
        return SourceCodeHelper.sourceCodeFromFile(outFile);
    }
}
