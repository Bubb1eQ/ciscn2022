package ctf.utils;

import ctf.model.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class XmlUtil {

    private static final String[] blackList = {
            "file", "ftp", "http", "data", "class", "bash", "log", "dtd",
            "url", "conf", "etc", "proc", "history", "tomcat", "flag"
    };

    public static boolean check(String xmlString) {
        for (String banWord : blackList) {
            if (xmlString.toLowerCase().contains(banWord)) {
                return true;
            }
        }
        return false;
    }

    public static String validate(String xmlString) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema;

        try {
            schema = schemaFactory.newSchema(new File("/opt/tomcat/webapps/service.xsd"));
            Validator validator = schema.newValidator();
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            validator.validate(new StreamSource(new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8))));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            return toXML(e);
        }
        return null;
    }

    public static Object parse(String xmlString) {
        Service service = null;
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

            service = new Service();
            service.setName(doc.getElementsByTagName("serviceName").item(0).getTextContent());
            service.setOperation(doc.getElementsByTagName("operateName").item(0).getTextContent());
            service.setParams(doc.getElementsByTagName("params").item(0).getTextContent());

            return service;
        } catch (Exception e) {
            e.printStackTrace();
            return toXML(e);
        }
    }

    public static String toXML(String xmlParam) {
        if (xmlParam == null) {
            xmlParam = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><input></input></root>";
        }
        xmlParam = xmlParam.replaceAll("\\<\\?xml(.+?)\\?\\>",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        return xmlParam.trim();
    }

    public static String toXML(Throwable e) {
        return "<exception className=\"" + e.getClass().getName() + "\">" + "<message><![CDATA[" + e.getMessage() + "]]></message>" + "</exception>";
    }


}
