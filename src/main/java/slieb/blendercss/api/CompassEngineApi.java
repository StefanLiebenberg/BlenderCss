package slieb.blendercss.api;

import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.jruby.Ruby;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.jruby.javasupport.JavaUtil.convertJavaToRuby;

public class CompassEngineApi {

    private final Ruby runtime;

    private final IRubyObject compassApi;

    @Inject
    public CompassEngineApi(Ruby runtime) {
        this.runtime = runtime;
        this.runtime.getLoadService().require("SassCompilerApi");
        this.compassApi = runtime.evalScriptlet("SassCompilerApi.new");
    }

    public void compile(File inputFile, File outputFile) throws IOException {
        final String outputString = compile(inputFile);
        final File parentFile = outputFile.getParentFile();
        if (parentFile.exists() || parentFile.mkdirs()) {
            FileUtils.write(outputFile, outputString, Charset.defaultCharset());
        }
    }

    public String compile(File inputFile) {
        try {
            final IRubyObject rPathName = getRubyString(inputFile.getAbsolutePath());
            final IRubyObject rResult = callMethodRaw("compile", rPathName);
            return (String) rResult.toJava(String.class);
        } catch (Throwable t) {
            throw new RuntimeException("Error while trying to invoke compile on CompassApi. " + t.getMessage(), t);
        }
    }

    public void unshiftLoadpath(String loadpath) {
        callMethodRaw("unshiftLoadpath", getRubyString(loadpath));
    }

    public void setHttpImagesPath(String path) {
        callMethodRaw("setHttpImagesPath", getRubyString(path));
    }

    public void setRelativeAssets(Boolean value) {
        callMethodRaw("setRelativeAssets", convertJavaToRuby(runtime, value));
    }

    public void setOption(String key, Object value) {
        setOptionRaw(convertJavaToRuby(runtime, key), convertJavaToRuby(runtime, value));
    }

    public IRubyObject getRubySym(String value) {
        return getRubyString(value)
                .callMethod(context(), "to_sym");
    }

    private ThreadContext context() {
        return runtime.getCurrentContext();
    }

    private IRubyObject getRubyString(String value) {
        return convertJavaToRuby(runtime, value);
    }

    private IRubyObject callMethodRaw(String methodName, IRubyObject... arguments) {
        return compassApi.callMethod(context(), methodName, arguments);
    }

    private void setOptionRaw(IRubyObject key, IRubyObject value) {
        callMethodRaw("set", key, value);
    }

    public void setImporter(File directory) {
        callMethodRaw("setImporterDirectory", convertJavaToRuby(runtime, directory.getAbsolutePath()));
    }
}
