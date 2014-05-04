package slieb.blendercss.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.io.File;


public class DirectoryModule extends AbstractModule {

    private final String name;

    private final File workingDirectory;

    public DirectoryModule(String name, File workingDirectory) {
        this.name = name;
        this.workingDirectory = workingDirectory;
    }

    @Override
    protected void configure() {
        bind(File.class)
                .annotatedWith(Names.named(name))
                .toInstance(workingDirectory);
    }
}
