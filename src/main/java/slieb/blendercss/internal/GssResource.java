package slieb.blendercss.internal;

import com.google.common.css.SourceCode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public interface GssResource {


    File getOriginalSourceFile();

    String getFileName();

    String getContents();

    default SourceCode asSourceCode() {
        return new SourceCode(getFileName(), getContents());
    }

    class StringGssResource implements GssResource {

        private final String content;

        private final String fileName;

        private final File originalFile;

        public StringGssResource(final String fileName, final String content, final File originalFile) {
            this.content = content;
            this.fileName = fileName;
            this.originalFile = originalFile;
        }

        @Override
        public File getOriginalSourceFile() {
            return originalFile;
        }

        @Override
        public String getFileName() {
            return fileName;
        }

        @Override
        public String getContents() {
            return content;
        }
    }

    class FileGssResource implements GssResource {

        private final File file;

        private final File orginalFile;

        public FileGssResource(File file, File orginalFile) {
            this.file = file;
            this.orginalFile = orginalFile;
        }

        public FileGssResource(File file) {
            this.file = file;
            this.orginalFile = file;
        }

        @Override
        public File getOriginalSourceFile() {
            return orginalFile;
        }

        @Override
        public String getFileName() {
            return file.getName();
        }

        @Override
        public String getContents() {
            try {
                return FileUtils.readFileToString(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
