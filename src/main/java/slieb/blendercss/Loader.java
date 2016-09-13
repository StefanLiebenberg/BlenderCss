package slieb.blendercss;

import com.github.sommeri.less4j.LessCompiler;
import com.github.sommeri.less4j.core.DefaultLessCompiler;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import slieb.blendercss.configuration.DirectoryModule;
import slieb.blendercss.configuration.InterfaceModule;
import slieb.blendercss.configuration.JRubyRuntimeModule;
import slieb.blendercss.configuration.PreProcessorModule;
import slieb.blendercss.precompilers.functions.ImageUrlProcessor;
import slieb.blendercss.precompilers.languages.CompassProcessor;
import slieb.blendercss.precompilers.languages.LessProcessor;

import java.io.File;
import java.io.IOException;

import static com.google.inject.Guice.createInjector;

@SuppressWarnings("WeakerAccess")
public class Loader {

    public static AbstractModule getDefaultBlenderModule(final File workingDirectory) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                install(new DirectoryModule("workingDirectory", workingDirectory));
                install(new JRubyRuntimeModule());
                install(new InterfaceModule<>(LessCompiler.class, DefaultLessCompiler.class));
                install(new PreProcessorModule(ImageUrlProcessor.class));
                install(new PreProcessorModule(LessProcessor.class));
                install(new PreProcessorModule(CompassProcessor.class));
            }
        };
    }

    public static AbstractModule getCompassOnlyModule(final File workingDirectory) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                install(new DirectoryModule("workingDirectory", workingDirectory));
                install(new JRubyRuntimeModule());
                install(new PreProcessorModule(CompassProcessor.class));
            }
        };
    }

    public static Blender getBlenderFromModule(Module module) {
        return createInjector(module).getInstance(Blender.class);
    }

    public static Blender getDefaultBlender() throws IOException {
        return getBlenderFromModule(getDefaultBlenderModule(File.createTempFile("blender", "default")));
    }

    public static Blender getCompassOnlyBlender() throws IOException {
        return getBlenderFromModule(getCompassOnlyModule(File.createTempFile("blender", "compass")));
    }

    public static Blender getDefaultBlender(final File workingDirectory) {
        return getBlenderFromModule(getDefaultBlenderModule(workingDirectory));
    }
}
