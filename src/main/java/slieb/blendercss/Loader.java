package slieb.blendercss;


import com.github.sommeri.less4j.LessCompiler;
import com.github.sommeri.less4j.core.DefaultLessCompiler;
import com.google.inject.Injector;
import slieb.blendercss.configuration.DirectoryModule;
import slieb.blendercss.configuration.InterfaceModule;
import slieb.blendercss.configuration.JRubyRuntimeModule;
import slieb.blendercss.configuration.PreCompilerModule;
import slieb.blendercss.precompilers.functions.ImageUrlPrecompiler;
import slieb.blendercss.precompilers.languages.CompassPrecompiler;
import slieb.blendercss.precompilers.languages.LessPrecompiler;

import java.io.File;

import static com.google.inject.Guice.createInjector;

public class Loader {
    public static Injector getInjector(File workingDirectory) {
        return createInjector(
                new DirectoryModule("workingDirectory", workingDirectory),
                new JRubyRuntimeModule(),
                new InterfaceModule<>(LessCompiler.class, DefaultLessCompiler.class),
                new PreCompilerModule(ImageUrlPrecompiler.class),
                new PreCompilerModule(LessPrecompiler.class),
                new PreCompilerModule(CompassPrecompiler.class));
    }
}
