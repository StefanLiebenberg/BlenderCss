package slieb.blendercss.configuration;


import com.google.inject.AbstractModule;
import slieb.blendercss.precompilers.internal.CssPrecompiler;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

public class PreCompilerModule extends AbstractModule {

    private final Class<? extends CssPrecompiler> precompilerClassObject;

    public PreCompilerModule(Class<? extends CssPrecompiler> precompilerClassObject) {
        this.precompilerClassObject = precompilerClassObject;
    }

    @Override
    protected void configure() {
        newSetBinder(binder(), CssPrecompiler.class).addBinding().to(precompilerClassObject);
    }
}
