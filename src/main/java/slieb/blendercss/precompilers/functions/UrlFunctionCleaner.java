package slieb.blendercss.precompilers.functions;

import com.google.inject.Inject;
import slieb.blendercss.CompileOptions;
import slieb.blendercss.internal.FileGenerator;
import slieb.blendercss.precompilers.internal.AbstractFunctionPrecompiler;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;


public class UrlFunctionCleaner extends AbstractFunctionPrecompiler {

    private static final Pattern PATTERN = Pattern.compile("url\\(\"([^\\)]+)\"\\)");

    private static final String URL_FORMAT = "url('%s')";

    @Inject
    public UrlFunctionCleaner(@Nonnull FileGenerator fileGenerator) {
        super(PATTERN, fileGenerator, new String[]{".css"}, null);
    }

    @Override
    protected String parseFunction(CompileOptions options, String... args) {
        checkArgument(args.length == 2);
        final String url = args[1];
        checkNotNull(url);
        return format(URL_FORMAT, url.replaceAll("\"", "").replaceAll("'", ""));
    }
}
