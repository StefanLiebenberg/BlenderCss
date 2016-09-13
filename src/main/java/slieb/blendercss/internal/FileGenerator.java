package slieb.blendercss.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

import static java.lang.String.format;
import static org.apache.commons.io.FilenameUtils.getExtension;

@Singleton
public class FileGenerator {

    private final MessageDigest md;

    private final File workingDirectory;

    @Inject
    public FileGenerator(@Named("workingDirectory") File workingDirectory) {
        this.workingDirectory = workingDirectory;
        try {
            this.md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File getOutputDirectory(String dirName) {
        return new File(workingDirectory, dirName);
    }

    private File getTempDirectory() {
        return getOutputDirectory("preprocessed");
    }

    public File getCacheDirectory() {
        return getOutputDirectory("cache");
    }

    private String getMD5(String message) throws IOException {
        String digest;
        try {
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return digest;
    }

    public File getOutputFileFor(GssResource input, String outputExt) throws IOException {
        String md5String = getMD5(input.getFileName());
        String extension = outputExt != null ? outputExt : getExtension(input.getFileName());
        String name = format("%s.%s", md5String, extension);
        return new File(getTempDirectory(), name);
    }
}
