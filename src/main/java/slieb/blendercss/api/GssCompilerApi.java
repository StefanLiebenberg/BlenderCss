package slieb.blendercss.api;


import com.google.common.collect.ImmutableList;
import com.google.common.css.JobDescription;
import com.google.common.css.Vendor;
import com.google.common.css.compiler.commandline.ClosureCommandLineCompiler;
import com.google.inject.Singleton;
import slieb.blendercss.BlendOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static ch.lambdaj.Lambda.collect;
import static ch.lambdaj.Lambda.on;
import static com.google.common.base.Preconditions.checkArgument;

@Singleton
public class GssCompilerApi {

    public String[] getArguments(List<File> inputFiles, File outputFile, BlendOptions options) throws IOException {

        final ImmutableList.Builder<String> arguments = new ImmutableList.Builder<>();

        arguments.add("--allow-unrecognized-functions");
        arguments.add("--allow-unrecognized-properties");

        File cssRenamingMap = options.getOutputCssRenameMap();
        if (cssRenamingMap != null) {
            // TODO create directory for rename map.
            arguments.add("--output-renaming-map");
            arguments.add(cssRenamingMap.getAbsolutePath());

            if (options.getShouldCompile()) {
                arguments.add("--output-renaming-map-format");
                arguments.add("CLOSURE_COMPILED");
                if (options.getShouldDebug()) {
                    arguments.add("--rename");
                    arguments.add("DEBUG");
                } else {
                    arguments.add("--rename");
                    arguments.add("CLOSURE");
                }
            } else {
                arguments.add("--output-renaming-map-format");
                arguments.add("CLOSURE_UNCOMPILED");
                arguments.add("--rename");
                arguments.add("NONE");
            }
        }

        if (options.getShouldDebug() || !options.getShouldCompile()) {
            arguments.add("--pretty-print");
        }

        arguments.add("--output-file");
        arguments.add(outputFile.getAbsolutePath());

        JobDescription.InputOrientation orientation = options.getOrientation();
        if (orientation != null) {
            arguments.add("--input-orientation");
            arguments.add(orientation.name());
        }

        Vendor vendor = options.getVendor();
        if (vendor != null) {
            arguments.add("--vendor");
            arguments.add(vendor.name());
        }

        arguments.addAll(collect(inputFiles, on(File.class).getAbsolutePath()));

        List<String> argumentsList = arguments.build();
        return argumentsList.toArray(new String[argumentsList.size()]);
    }

    public void compile(List<File> inputFiles, File outputFile, BlendOptions options) throws IOException {
        checkArgument(inputFiles != null && !inputFiles.isEmpty());
        checkArgument(outputFile != null);
        checkArgument(options != null);
        outputFile.getParentFile().mkdirs();
        ClosureCommandLineCompiler.main(getArguments(inputFiles, outputFile, options));
    }
}
