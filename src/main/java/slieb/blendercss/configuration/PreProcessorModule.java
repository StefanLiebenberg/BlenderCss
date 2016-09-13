package slieb.blendercss.configuration;

import com.google.inject.AbstractModule;
import slieb.blendercss.precompilers.internal.CssProcessor;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

public class PreProcessorModule extends AbstractModule {

    private final Class<? extends CssProcessor> preProcessorClass;

    public PreProcessorModule(Class<? extends CssProcessor> preProcessorClass) {
        this.preProcessorClass = preProcessorClass;
    }

    @Override
    protected void configure() {
        newSetBinder(binder(), CssProcessor.class).addBinding().to(preProcessorClass);
    }
}
