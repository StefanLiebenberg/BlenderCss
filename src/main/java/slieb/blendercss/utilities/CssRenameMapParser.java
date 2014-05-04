package slieb.blendercss.utilities;


import com.google.common.io.Files;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.codehaus.jackson.map.ObjectMapper;

import javax.annotation.Nonnull;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.Charset.defaultCharset;

@Singleton
public class CssRenameMapParser {

    public final ObjectMapper objectMapper;

    @Inject
    public CssRenameMapParser(@Nonnull ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private String getJsonContent(String content) {
        return content.substring(content.indexOf('{'), content.lastIndexOf('}') + 1);
    }

    public void parse(String content, Map<String, String> map) throws IOException {

        map.putAll((Map<String, String>) objectMapper.readValue(getJsonContent(content), HashMap.class));
    }

    public RenamingMap parse(String content) throws IOException {
        RenamingMap map = new RenamingMap();
        parse(content, map);
        return map;
    }

    public RenamingMap parse(File file) throws IOException {
        return parse(Files.toString(file, defaultCharset()));
    }
}
