package slieb.blendercss.utilities;


import javax.annotation.Nonnull;
import java.util.HashMap;

public class RenamingMap extends HashMap<String, String> {

    private static final String DELIM = "-";

    @Nonnull
    public String getCssName(@Nonnull String cssClass) {
        if (containsKey(cssClass)) {
            return get(cssClass);
        }

        String d = "";
        final StringBuilder result = new StringBuilder();
        for (String key : cssClass.split(DELIM)) {
            result.append(d).append(containsKey(key) ? get(key) : key);
            d = DELIM;
        }
        return result.toString();
    }
}
