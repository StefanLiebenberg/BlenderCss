package slieb.blendercss.internal;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SourceCodeHelper {

    public static GssResource sourceCodeFromFile(File file) throws IOException {
        return new GssResource.FileGssResource(file);
    }

    public static void saveSourceCodeInFile(GssResource code, File outputFile) throws IOException {
        FileUtils.write(outputFile, code.getContents());
    }

}
