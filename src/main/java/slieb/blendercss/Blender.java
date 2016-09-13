package slieb.blendercss;

import com.google.common.css.SourceCode;
import com.google.common.css.compiler.ast.GssParserException;
import com.google.inject.Inject;
import slieb.blendercss.api.GssCompilerApi;
import slieb.blendercss.exceptions.BlenderException;
import slieb.blendercss.internal.GssResource;
import slieb.blendercss.precompilers.internal.CssProcessor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class Blender {

    private final Set<CssProcessor> preCompilers;

    private final GssCompilerApi gssCompilerApi;

    @Inject
    public Blender(final Set<CssProcessor> processors, final GssCompilerApi gssCompilerApi) {
        this.preCompilers = processors;
        this.gssCompilerApi = gssCompilerApi;
    }

    public void compile(final List<GssResource> resources, final File outputFile, final BlendOptions options) {
        try {
            doCompile(preProcessResources(resources, options), outputFile, options);
        } catch (BlenderException blenderException) {
            throw blenderException;
        } catch (Throwable throwable) {
            throw new BlenderException("There was some error when trying to do blender compile", throwable);
        }
    }

    private List<GssResource> preProcessResources(List<GssResource> resources, final BlendOptions options) {
        for (CssProcessor.Phase phase : CssProcessor.PRIORITISED) {
            resources = preProcessResourcesForPhase(resources, options, phase);
        }
        return resources;
    }

    private List<GssResource> preProcessResourcesForPhase(final List<GssResource> resources, final BlendOptions options,
                                                          final CssProcessor.Phase phase) {
        return resources.stream()
                        .map(input -> preProcessResource(phase, input, options))
                        .collect(toList());
    }

    private GssResource preProcessResource(final CssProcessor.Phase phase, final GssResource resource, final BlendOptions options) {
        for (CssProcessor processors : preCompilers) {
            if (processors.canProcess(phase, resource)) {
                return preProcessResource(phase, processors.process(resource, options), options);
            }
        }
        return resource;
    }

    private void doCompile(final List<GssResource> resources, final File outputFile, final BlendOptions options) throws IOException, GssParserException {
        gssCompilerApi.compile(toSourceCode(resources), outputFile, options);
    }

    private List<SourceCode> toSourceCode(final List<GssResource> resources) {
        return resources.stream().map(GssResource::asSourceCode).collect(toList());
    }
}

