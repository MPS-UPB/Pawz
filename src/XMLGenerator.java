import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.*;

import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSTerm;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class XMLGenerator {

    DocumentBuilderFactory dbfac;
    DocumentBuilder docBuilder;
    Document doc;
    
    public XMLGenerator() throws ParserConfigurationException {
    	dbfac = DocumentBuilderFactory.newInstance();
    	docBuilder = dbfac.newDocumentBuilder();
    	doc = docBuilder.newDocument();
    }
        
    public String getXMLString(XSDParser parser, String file, XSParticle[] particles) {
		String xmlString = "";
		
        try {
            //create the root element and add it to the document
            Element root = doc.createElement("config");
            doc.appendChild(root);

            //create child element, add an attributes, and add to root
            Element child = doc.createElement("execName");
            String value = parser.getExecName();
            child.setAttribute("name", value);
            root.appendChild(child);
            String propertyName = null;
    		String propertyType = null;

    		for (XSParticle p : particles) {
    			XSTerm pTerm = p.getTerm();
    			propertyName = pTerm.asElementDecl().getName();
    			if (pTerm.asElementDecl().getType().getName() != null) {
    				propertyType = pTerm.asElementDecl().getType().getName();
    			}
    			else 
    				propertyType = pTerm.asElementDecl().getType().getBaseType().getName();
    			
    			if (propertyName.compareTo("execInfo") != 0 
    				&& propertyName.compareTo("inputFile") != 0
    				&& propertyName.compareTo("outputFile") != 0) {
    			
	    			Element innerChild = doc.createElement("parameter");
	                innerChild.setAttribute("name", propertyName);
	                innerChild.setAttribute("type", propertyType);
	                child.appendChild(innerChild);
    			}
    		}
    		
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            xmlString = sw.toString();

        } catch (Exception e) {
            System.out.println(e);
        }
        return xmlString;
    }
}