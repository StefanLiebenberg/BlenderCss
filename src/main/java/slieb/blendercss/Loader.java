package slieb.blendercss;


import com.google.inject.Injector;
import slieb.blendercss.configuration.JRubyRuntimeModule;

import static com.google.inject.Guice.createInjector;

public class Loader {
    public static Injector getInjector() {
        return createInjector(new JRubyRuntimeModule());
    }
}
