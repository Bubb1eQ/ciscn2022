package ctf.utils;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

class XmlUtilTest {

    private static final String[] blackList = {
            "file", "ftp", "http", "data", "class", "bash", "log",
            "url", "conf", "etc", "proc", "history", "tomcat", "flag"
    };

    @Test
    void build() {
        String xmlString = "<root>\n" +
                "    <input>\n" +
                "        <serviceCall>\n" +
                "            <serviceName>byccycc</serviceName>\n" +
                "            <operateName>a</operateName>\n" +
                "            <params>b</params><return></return><exception></exception></serviceCall></input></root>";
        for (String banWord : blackList) {
            if (xmlString.toLowerCase().contains(banWord)) {
                System.out.println(banWord);
            }
        }
        System.out.println("Passed");
    }


    @Test
    void parse() {
        String xmlString = "<root>\n" +
                "    <data>\n" +
                "        <serviceCall>\n" +
                "            <serviceName>byc</serviceName>\n" +
                "            <operateName>write</operateName>\n" +
                "            <params>\n" +
                "                filename\n" +
                "            </params>\n" +
                "            <return></return>\n" +
                "            <exception></exception>\n" +
                "        </serviceCall>\n" +
                "    </data>\n" +
                "</root>";
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = null;
        try {
            schema = schemaFactory.newSchema(new File("service.xsd"));
            Validator validator = schema.newValidator();
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            validator.validate(new StreamSource(new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            StringBuffer result = new StringBuffer();
            result.append("<exception className=\"").append(e.getClass().getName()).append("\">").append("<message><![CDATA[").append(e.getMessage()).append("]]></message>").append("</exception>");
            System.out.println(result);
            //System.out.println(e.getMessage());
            //e.printStackTrace();
        }

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setXIncludeAware(false);
            documentBuilderFactory.setExpandEntityReferences(false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            documentBuilderFactory.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8))));
            doc.getDocumentElement().normalize();

            System.out.println(doc.getElementsByTagName("serviceName").item(0).getTextContent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void toXML() {
        Exception e = new Exception("Bad words detected");
        System.out.println("<exception className=\"" + e.getClass().getName() + "\">" + "<message><![CDATA[" + e.getMessage() + "]]></message>" + "</exception>");
    }

    @Test
    void check() {
        String xmlString = "fIle:///";
        for (String banWord : blackList) {
            if (xmlString.toLowerCase().contains(banWord)) {
                System.out.println("no ");
            }
        }
    }
}