package slieb.blendercss.configuration;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import org.jruby.Ruby;

import static org.jruby.javasupport.JavaEmbedUtils.initialize;


public class JRubyRuntimeModule extends AbstractModule {

    private static final ImmutableList<String> LOAD_PATHS =
            new ImmutableList.Builder<String>()
                    .add("classpath:gems/sass-3.2.7/lib")
                    .add("classpath:gems/compass-0.12.2/lib")
                    .add("classpath:lib")
                    .build();

    @Override
    protected void configure() {
        bind(Ruby.class).toInstance(initialize(LOAD_PATHS));
    }
}
