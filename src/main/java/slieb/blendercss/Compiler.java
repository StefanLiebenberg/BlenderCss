package slieb.blendercss;


import com.google.common.io.Files;
import com.google.inject.Inject;
import org.hamcrest.Matcher;
import slieb.blendercss.api.CompassEngineApi;
import slieb.blendercss.api.GssCompilerApi;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;

public class Compiler {

    private final MessageDigest md;

    private static final Matcher<String> cssMatcher = endsWith(".css");
    private static final Matcher<String> sassMatcher = endsWith(".sass");
    private static final Matcher<String> scssMatcher = endsWith(".scss");

    private static final Matcher<File> cssFileMatcher = having(on(File.class).getPath(), cssMatcher);

    private static final Matcher<File> compassFileMatcher = having(on(File.class).getPath(), anyOf(sassMatcher, scssMatcher));

    private final CompassEngineApi compassApi;

    private final GssCompilerApi gssCompilerApi;

    @Inject
    public Compiler(CompassEngineApi compassApi, GssCompilerApi gssCompilerApi) {
        this.compassApi = compassApi;
        this.gssCompilerApi = gssCompilerApi;
        try {
            this.md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File workingDirectory;

    public File getWorkingDirectory() {
        if (workingDirectory == null) {
            workingDirectory = Files.createTempDir();
            workingDirectory.deleteOnExit();
        }
        return workingDirectory;
    }

    private void compileCompassFile(File inputFile, File outputFile, CompileOptions options) throws IOException {
        compassApi.setImporter(inputFile.getParentFile());
        compassApi.setOption("filename", inputFile.getPath());
        compassApi.setOption("css_filename", outputFile.getPath());
        compassApi.compile(inputFile, outputFile, options);
    }

    private void compileFiles(List<File> inputFiles, File outputFile, CompileOptions options) throws IOException {
        gssCompilerApi.compile(inputFiles, outputFile, options);
    }

    public void compile(List<File> inputFiles, File outputFile, CompileOptions options) throws IOException {
        List<File> compassFiles = filter(compassFileMatcher, inputFiles);
        List<File> otherFiles = filter(not(compassFileMatcher), inputFiles);
        List<File> cssFiles = filter(cssFileMatcher, otherFiles);
        List<File> extraFiles = filter(not(cssFileMatcher), otherFiles);

        if (!extraFiles.isEmpty()) {
            for (File file : extraFiles) {
                System.err.println(file);
            }
            throw new RuntimeException("Unknown files in list to CssCompiler");
        }

        for (File compassFile : compassFiles) {
            File cssOutputFile = new File(getWorkingDirectory(), getMD5(compassFile.getAbsolutePath()));
            compileCompassFile(compassFile, cssOutputFile, options);
            cssFiles.add(cssOutputFile);
        }

        compileFiles(cssFiles, outputFile, options);

    }

    private String getMD5(String message) throws IOException {
        String digest = null;
        try {
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return digest;
    }
}

