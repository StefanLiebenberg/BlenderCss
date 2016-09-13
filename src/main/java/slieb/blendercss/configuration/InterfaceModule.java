package slieb.blendercss.configuration;

import com.google.inject.AbstractModule;

public class InterfaceModule<A> extends AbstractModule {

    private final Class<A> interfaceClass;

    private final Class<? extends A> implementationClass;

    public InterfaceModule(Class<A> interfaceClass, Class<? extends A> implementationClass) {
        this.interfaceClass = interfaceClass;
        this.implementationClass = implementationClass;
    }

    @Override
    protected void configure() {
        bind(interfaceClass).to(implementationClass);
    }
}
