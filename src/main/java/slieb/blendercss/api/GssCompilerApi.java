package slieb.blendercss.api;

import com.google.common.css.*;
import com.google.common.css.compiler.ast.BasicErrorManager;
import com.google.common.css.compiler.ast.ErrorManager;
import com.google.common.css.compiler.ast.GssError;
import com.google.common.css.compiler.ast.GssParserException;
import com.google.common.css.compiler.commandline.DefaultCommandLineCompiler;
import com.google.common.io.Files;
import com.google.inject.Singleton;
import slieb.blendercss.BlendOptions;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.charset.StandardCharsets.UTF_8;

@Singleton
public class GssCompilerApi {

    private JobDescription createJobDescription(List<SourceCode> inputs, BlendOptions options) {
        JobDescriptionBuilder builder = new JobDescriptionBuilder();
        builder.setInputs(inputs);
        builder.allowUnrecognizedFunctions();
        builder.allowUnrecognizedProperties();

        if (options.getOutputCssRenameMap() != null) {
            if (options.getShouldCompile()) {
                builder.setOutputRenamingMapFormat(OutputRenamingMapFormat.CLOSURE_COMPILED);
                builder.setCssSubstitutionMapProvider(options.getShouldDebug() ?
                                                              () -> new SplittingSubstitutionMap(new SimpleSubstitutionMap()) :
                                                              () -> new SplittingSubstitutionMap(new MinimalSubstitutionMap()));
            } else {
                builder.setCssSubstitutionMapProvider(IdentitySubstitutionMap::new);
                builder.setOutputRenamingMapFormat(OutputRenamingMapFormat.CLOSURE_UNCOMPILED);
            }
        }

        if (options.getShouldDebug() || !options.getShouldCompile()) {
            builder.preserveComments(); // pretty print.
            builder.setOutputFormat(JobDescription.OutputFormat.PRETTY_PRINTED);
        } else {
            builder.setOutputFormat(JobDescription.OutputFormat.COMPRESSED);
        }

        builder.setAllowDefPropagation(true);
        builder.setAllowKeyframes(true);
        builder.setAllowWebkitKeyframes(true);
        builder.setAllowMozDocument(true);
        builder.setProcessDependencies(true);

        if (options.getShouldCompile()) {
            builder.setSimplifyCss(true);
            builder.setEliminateDeadStyles(true);
            builder.setSourceMapLevel(JobDescription.SourceMapDetailLevel.ALL);
            builder.setOptimizeStrategy(JobDescription.OptimizeStrategy.MAXIMUM);
        }

        JobDescription.InputOrientation orientation = options.getOrientation();
        if (orientation != null) {
            builder.setInputOrientation(orientation);
        }

        Vendor vendor = options.getVendor();
        if (vendor != null) {
            builder.setVendor(vendor);
        }

        return builder.getJobDescription();
    }

    public void compile(List<SourceCode> inputs, File outputFile, BlendOptions options) throws IOException, GssParserException {
        checkArgument(inputs != null && !inputs.isEmpty());
        checkArgument(outputFile != null);
        checkArgument(options != null);
        final File parentFile = outputFile.getParentFile();
        if (parentFile.exists() || parentFile.mkdirs()) {
            final JobDescription jobDescription = createJobDescription(inputs, options);
            final ExitCodeReporter exitCodeReporter = new ExitCodeReporter();
            final OutputInfo outputInfo = new OutputInfo(outputFile, options.getOutputCssRenameMap(), null);
            GssCompiler.executeJob(jobDescription, exitCodeReporter, outputInfo);

            if (exitCodeReporter.hasErrors) {
                throw new RuntimeException("gss failure");
            }
        }
    }

    private static class ExitCodeReporter implements ExitCodeHandler {

        private boolean hasErrors = false;

        /**
         * Process the request to exit with the specified exit code.
         * Implementations of this method <em>must never</em> return normally.
         *
         * @param exitCode
         */
        @Override
        public void processExitCode(final int exitCode) {
            hasErrors = exitCode != AbstractCommandLineCompiler.SUCCESS_EXIT_CODE;
        }
    }

    private static class OutputInfo {

        @Nullable
        public final File outputFile;

        @Nullable
        final File renameFile;

        @Nullable
        final File sourceMapFile;

        private OutputInfo(File outputFile, File renameFile, File sourceMapFile) {
            this.outputFile = outputFile;
            this.renameFile = renameFile;
            this.sourceMapFile = sourceMapFile;
        }
    }

    private static class GssErrorManager extends BasicErrorManager {

        private boolean warningsAsErrors = false;

        @Override
        public void print(String msg) {
            System.err.println(msg);
        }

        @Override
        public void reportWarning(GssError warning) {
            if (warningsAsErrors) {
                report(warning);
            } else {
                super.reportWarning(warning);
            }
        }

        public void setWarningsAsErrors(boolean state) {
            warningsAsErrors = state;
        }
    }

    private static class GssCompiler extends DefaultCommandLineCompiler {

        private GssCompiler(final JobDescription job, final ExitCodeHandler exitCodeHandler,
                            final ErrorManager errorManager) {
            super(job, exitCodeHandler, errorManager);
        }

        static void executeJob(JobDescription job, ExitCodeHandler exitCodeHandler, OutputInfo outputInfo) {
            GssErrorManager errorManager = new GssErrorManager();
            GssCompiler compiler =
                    new GssCompiler(job, exitCodeHandler, errorManager);

            String compilerOutput = compiler.execute(outputInfo.renameFile, outputInfo.sourceMapFile);

            if (outputInfo.outputFile == null) {
                System.out.print(compilerOutput);
            } else {
                try {
                    Files.write(compilerOutput, outputInfo.outputFile, UTF_8);
                } catch (IOException e) {
                    AbstractCommandLineCompiler.exitOnUnhandledException(e, exitCodeHandler);
                }
            }
        }
    }
}

