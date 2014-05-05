package slieb.issues;

import com.google.common.collect.ImmutableList;
import cz.vutbr.web.css.*;
import cz.vutbr.web.csskit.TermURIImpl;
import cz.vutbr.web.domassign.Analyzer;
import cz.vutbr.web.domassign.DirectAnalyzer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import slieb.blendercss.CompileOptions;
import slieb.features.AbstractFeatureTest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class Issue004Test extends AbstractFeatureTest {

    private CompileOptions.Builder optionsBuilder;

    private ImmutableList.Builder<File> filesBuilder;

    private File outputFile;

    private Document document;

    @Before
    public void setupFile() throws Throwable {
        outputFile = getOutputFile("style.css");
        optionsBuilder = new CompileOptions.Builder();
        filesBuilder = new ImmutableList.Builder<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
    }

    @Test
    public void testImageUrl() throws Throwable {
        filesBuilder.add(getResourceFile("stylesheets/issues/i004/image_url.scss"));
        optionsBuilder.setShouldDebug(false);
        optionsBuilder.setShouldCompile(true);
        optionsBuilder.setImagesPath("/serve/assets/images/");
        optionsBuilder.setOutputPath("/server/css/style.css");

        compiler.compile(filesBuilder.build(), outputFile, optionsBuilder.build());
        StyleSheet styleSheet = CSSFactory.parse(outputFile.getPath(), Charset.defaultCharset().name());
        DirectAnalyzer directAnalyzer = new DirectAnalyzer(styleSheet);
        assertElementBackgroundImageUrlEquals("/serve/assets/images/bad.jpg", "issue004-image-url-case-01", directAnalyzer);
        assertElementBackgroundImageUrlEquals("/bad.jpg", "issue004-image-url-case-02", directAnalyzer);
        assertElementBackgroundImageUrlEquals("http://domain.com/bad.jpg", "issue004-image-url-case-04", directAnalyzer);
    }

    public void assertElementBackgroundImageUrlEquals(String expected, String cssClass, DirectAnalyzer analyzer) throws Throwable {
        Element case1Element = document.createElement("div");
        case1Element.setAttribute("class", cssClass);
        NodeData data = analyzer.getElementStyle(case1Element, null, null);
        CSSProperty.BackgroundImage backgroundImage = data.getProperty("background-image");
        Term<String> object = data.getValue(TermURIImpl.class, "background-image");
        assertEquals(expected, object.getValue());
    }

    //    @Test
    public void testStylesheetUrl() throws Throwable {

    }

    //    @Test
    public void testFontUrl() throws Throwable {

    }

    //    @Test
    public void testGeneratedImageUrl() throws Throwable {

    }
}
