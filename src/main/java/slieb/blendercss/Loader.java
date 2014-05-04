package slieb.blendercss;


import com.google.inject.Injector;
import slieb.blendercss.configuration.DirectoryModule;
import slieb.blendercss.configuration.JRubyRuntimeModule;
import slieb.blendercss.configuration.PreCompilerModule;
import slieb.blendercss.precompilers.CompassPrecompiler;

import java.io.File;

import static com.google.inject.Guice.createInjector;

public class Loader {
    public static Injector getInjector(File workingDirectory) {
        return createInjector(
                new DirectoryModule("workingDirectory", workingDirectory),
                new JRubyRuntimeModule(),
                new PreCompilerModule(CompassPrecompiler.class));
    }
}
