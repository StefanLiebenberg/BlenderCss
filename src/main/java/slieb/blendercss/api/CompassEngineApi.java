package slieb.blendercss.api;


import com.google.common.io.Files;
import com.google.inject.Inject;
import org.jruby.Ruby;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

import java.io.File;
import java.io.IOException;

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

    private ThreadContext context() {
        return runtime.getCurrentContext();
    }

    public void setOption(String key, Object value) {
        setOptionRaw(convertJavaToRuby(runtime, key), convertJavaToRuby(runtime, value));
    }

    public IRubyObject getRubySym(String value) {
        return getRubyString(value)
                .callMethod(context(), "to_sym");
    }

    public IRubyObject getRubyString(String value) {
        return convertJavaToRuby(runtime, value);
    }

    public IRubyObject callMethodRaw(String methodName, IRubyObject... arguments) {
        return compassApi.callMethod(context(), methodName, arguments);
    }

    public void setOptionRaw(IRubyObject key, IRubyObject value) {
        callMethodRaw("set", key, value);
    }

    public void setImporter(File directory) {
        callMethodRaw("setImporterDirectory", convertJavaToRuby(runtime, directory.getAbsolutePath()));
    }

    public String compile(File inputFile) {
        return (String) callMethodRaw("compile",
                getRubyString(inputFile.getAbsolutePath()))
                .toJava(String.class);
    }

    public void unshiftLoadpath(String loadpath) {
        callMethodRaw("unshiftLoadpath", getRubyString(loadpath));
    }

    public void compile(File inputFile, File outputFile) throws IOException {
        String outputString = compile(inputFile);
        Files.write(outputString.getBytes(), outputFile);
    }
}
