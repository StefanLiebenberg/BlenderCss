package slieb.blendercss.precompilers.languages;

import com.google.inject.Inject;
import slieb.blendercss.CompileOptions;
import slieb.blendercss.internal.FileGenerator;
import slieb.blendercss.api.CompassEngineApi;
import slieb.blendercss.precompilers.internal.AbstractPrecompiler;

import java.io.File;
import java.io.IOException;

public class CompassPrecompiler extends AbstractPrecompiler {

    private static final String[] INPUT_EXTENSIONS = new String[]{".sass", ".scss"};

    private static final String OUTPUT_EXTENSIONS = "compass.css";

    private final CompassEngineApi compassApi;

    @Inject
    public CompassPrecompiler(FileGenerator fileGenerator, CompassEngineApi compassApi) {
        super(fileGenerator, INPUT_EXTENSIONS, OUTPUT_EXTENSIONS);
        this.compassApi = compassApi;
    }


    @Override
    public void compile(File inputFile, File outputFile, CompileOptions options) throws IOException {
        compassApi.setImporter(inputFile.getParentFile());
        compassApi.setOption("filename", inputFile.getPath());
        String cssFilename = options.getOutputPath();
        if (cssFilename != null) {
            compassApi.setOption("css_filename", cssFilename);
        } else {
            compassApi.setOption("css_filename", null);
        }
        compassApi.setOption("cache_location", fileGenerator.getCacheDirectory().getPath());
        compassApi.setHttpImagesPath(options.getImagesPath());
        compassApi.setRelativeAssets(false);
        compassApi.compile(inputFile, outputFile);
    }

}
