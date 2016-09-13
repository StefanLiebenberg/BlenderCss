package slieb.blendercss.precompilers.functions;

import com.google.inject.Inject;
import slieb.blendercss.BlendOptions;
import slieb.blendercss.internal.FileGenerator;
import slieb.blendercss.precompilers.internal.AbstractFunctionProcessor;

import javax.annotation.Nonnull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

public class ImageUrlProcessor extends AbstractFunctionProcessor {

    private static final String URL_FORMAT = "url('%s')";

    private static final Pattern PATTERN = Pattern.compile("image-url\\(([^\\)]+)\\)");

    private static final String[] INPUT_EXTENSIONS = new String[]{".css", ".gss"};

    @Inject
    public ImageUrlProcessor(@Nonnull FileGenerator fileGenerator) {
        super(PATTERN, fileGenerator, Phase.FUNCTIONS, INPUT_EXTENSIONS, null);
    }

    @Override
    protected String parseFunction(BlendOptions options, String... args) {
        checkArgument(args.length == 2);
        final String urlContent = args[1];
        final String path = urlContent.replaceAll("\"", "").replaceAll("'", "");
        return format(URL_FORMAT, resolvePath(path, options));
    }

    private String resolvePath(String path, BlendOptions options) {
        try {
            URI pathURI = new URI(path);
            if (!pathURI.isAbsolute()) {
                String imagesPath = options.getImagesPath();
                if (imagesPath != null) {
                    pathURI = new URI(imagesPath).resolve(path);
                }
            }
            return pathURI.toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
