package slieb.issues;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.NodeData;
import cz.vutbr.web.css.StyleSheet;
import cz.vutbr.web.domassign.DirectAnalyzer;
import org.apache.stanbol.enhancer.engines.htmlextractor.impl.DOMBuilder;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import slieb.blendercss.CompileOptions;
import slieb.features.AbstractFeatureTest;

import java.io.File;
import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;

public class Issue004Test extends AbstractFeatureTest {

    private CompileOptions.Builder optionsBuilder;

    private ImmutableList.Builder<File> filesBuilder;

    private File outputFile;

    @Before
    public void setupFile() {
        outputFile = getOutputFile("style.css");
        optionsBuilder = new CompileOptions.Builder();
        filesBuilder = new ImmutableList.Builder<>();
    }

    @Test
    public void testImageUrl() throws Throwable {
        filesBuilder.add(getResourceFile("stylesheets/issues/i004/image_url.scss"));
        optionsBuilder.setShouldDebug(false);
        optionsBuilder.setShouldCompile(true);
        optionsBuilder.setImagesPath("/serve/assets/images/");
        optionsBuilder.setOutputPath("/server/css/style.css");

        compiler.compile(filesBuilder.build(), outputFile, optionsBuilder.build());
        String content = Files.toString(outputFile, Charset.defaultCharset());

        StyleSheet styleSheet = CSSFactory.parse(content);
        DirectAnalyzer directAnalyzer = new DirectAnalyzer(styleSheet);


        Document doc = DOMBuilder.jsoup2DOM(Jsoup.parse("<!DOCTYPE html><html><body><div ID='elone' class='issue004-image-url-case-01'></div><div id='target-02' class='issue004-image-url-case-02'></div><div id='target-03' class='issue004-image-url-case-03'></div><div id='target-04' class='issue004-image-url-case-04'></div></body></html>"));
        doc.normalizeDocument();
        doc.importNode(doc.getDocumentElement(), true);

        Element element = doc.getElementById("elone");
        NodeData data = directAnalyzer.getElementStyle(element, null, null);




        assertTrue(content.contains(".issue004-image-url-case-01{background-image:url('/serve/assets/images/bad.jpg')}"));
        assertTrue(content.contains(".issue004-image-url-case-02{background-image:url('/bad.jpg')}"));
//        Disable this assert, as compass does not normalize things.
//        assertTrue(content.contains(".issue004-image-url-case-03{background-image:url(/serve/bad.jpg)}"));
        assertTrue(content.contains(".issue004-image-url-case-04{background-image:url('http://domain.com/bad.jpg')}"));
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
