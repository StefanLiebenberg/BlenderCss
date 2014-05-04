package slieb.blendercss;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

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

    public File getOutputFileFor(File inputFile, String outputExtention) throws IOException {
        String fullPath = inputFile.getAbsolutePath();
        String md5String = getMD5(fullPath);
        return new File(workingDirectory, md5String + "." + outputExtention);
    }
}
