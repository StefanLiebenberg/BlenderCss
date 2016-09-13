package slieb.blendercss.precompilers.internal;

import slieb.blendercss.BlendOptions;
import slieb.blendercss.internal.GssResource;

import java.io.IOException;

public interface CssProcessor {

    enum Phase {
        BEFORE,
        FUNCTIONS,
        LANGUAGES,
        AFTER
    }

    Phase[] PRIORITISED = new Phase[]{Phase.BEFORE, Phase.FUNCTIONS, Phase.LANGUAGES, Phase.AFTER};

    /**
     * @param input The input source code resource
     * @return A boolean to determine whether or not this resource can be processed.
     */
    Boolean canProcess(Phase phase, GssResource input);

    /**
     * @param input The input source code resource
     * @return The output source code resource.
     */
    default GssResource process(GssResource input, BlendOptions options) {
        try {
            return processWithIO(input, options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    GssResource processWithIO(GssResource input, BlendOptions options) throws IOException;
}
