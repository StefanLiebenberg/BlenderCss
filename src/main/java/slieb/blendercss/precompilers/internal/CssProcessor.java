package slieb.blendercss.precompilers.internal;

import slieb.blendercss.BlendOptions;
import slieb.blendercss.internal.GssResource;

import java.io.IOException;

public interface CssProcessor {

    enum Phase {
        FUNCTIONS,
        LANGUAGES
    }

    Phase[] PRIORITISED = new Phase[]{Phase.FUNCTIONS, Phase.LANGUAGES};

    /**
     * @param phase The phase in which this processor is active.
     * @param input The input source code resource
     * @return A boolean to determine whether or not this resource can be processed.
     */
    Boolean canProcess(Phase phase, GssResource input);

    /**
     * @param input   The input source code resource
     * @param options The blender options.
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
