package slieb.blendercss.precompilers;

import com.google.inject.Inject;
import slieb.blendercss.CssPrecompiler;
import slieb.blendercss.FileGenerator;
import slieb.blendercss.api.CompassEngineApi;

import java.io.File;
import java.io.IOException;

public class CompassPrecompiler implements CssPrecompiler {

    private final CompassEngineApi compassApi;

    private final FileGenerator fileGenerator;

    @Inject
    public CompassPrecompiler(CompassEngineApi compassApi, FileGenerator fileGenerator) {
        this.compassApi = compassApi;
        this.fileGenerator = fileGenerator;
    }

    @Override
    public Boolean canCompile(File inputFile) {
        String name = inputFile.getName();
        return name.endsWith(".sass") || name.endsWith(".scss");
    }

    @Override
    public File compile(File inputFile) throws IOException {
        File outputFile = fileGenerator.getOutputFileFor(inputFile, "css");
        compassApi.setImporter(inputFile.getParentFile());
        compassApi.setOption("filename", inputFile.getPath());
        compassApi.setOption("css_filename", outputFile.getPath());
        compassApi.setOption("cache_location", fileGenerator.getCacheDirectory().getPath());
        compassApi.compile(inputFile, outputFile);
        return outputFile;
    }
}
