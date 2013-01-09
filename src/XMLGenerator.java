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
        
    public String getXMLString(ArrayList<String> files) {
		String xmlString = "";
		HashMap <String, ArrayList<XSDParser>> parserHash = new HashMap<String, ArrayList<XSDParser>>();
		HashMap <String, ArrayList<XSParticle[]>> particleHash = new HashMap<String, ArrayList<XSParticle[]>>();
		ArrayList<String> execTypes = new ArrayList<String>();
		
		for (int i = 0; i < files.size(); i++) {
			XSDParser parser = new XSDParser();
			XSParticle[] particles = parser.parseXSD(new File(files.get(i)));
			
			String[] execTypeVal = parser.getExecType().split(",");
			for (int j = 0; j < execTypeVal.length; j++) {
				if (parserHash.containsKey(execTypeVal[j]) && 
						particleHash.containsKey(execTypeVal[j])) {
					parserHash.get(execTypeVal[j]).add(parser);
					particleHash.get(execTypeVal[j]).add(particles);
				}
				else {
					execTypes.add(execTypeVal[j]);
					
					ArrayList<XSDParser> parserArr = new ArrayList<XSDParser>();
					ArrayList<XSParticle[]> particleArr = new ArrayList<XSParticle[]>();
					parserArr.add(parser);
					particleArr.add(particles);
					parserHash.put(execTypeVal[j], parserArr);
					particleHash.put(execTypeVal[j], particleArr);
				}
			}
			
		}

		try {
            //create the root element and add it to the document
            Element root = doc.createElement("config");
            doc.appendChild(root);

            for (int i = 0; i < execTypes.size(); i++) {
            	Element execTypeElem = doc.createElement("execType");
            	execTypeElem.setAttribute ("name", execTypes.get(i));
            	
            	root.appendChild(execTypeElem);
            	
            	ArrayList<XSDParser> parsers = parserHash.get(execTypes.get(i));
            	ArrayList<XSParticle[]> particles = particleHash.get(execTypes.get(i));
            	
            	for (int j = 0; j < parsers.size(); j++) {
                    Element execNameElem = doc.createElement("execName");
                    String value = parsers.get(j).getExecName();
                    execNameElem.setAttribute("name", value);
                    execTypeElem.appendChild(execNameElem);
                    String propertyName = null;
            		String propertyType = null;
            		
            		for (XSParticle p : particles.get(j)) {
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
        	                execNameElem.appendChild(innerChild);
            			}
            		}   		
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