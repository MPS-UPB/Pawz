import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class Utils {

	public static ArrayList<String> xsdFiles;
	public static ArrayList<String> exeFiles;
	public static HashMap<String, ArrayList<String>> hash;
	
	public static void readFiles() {
		xsdFiles = new ArrayList<String>();
		String path = "C:/Documents and Settings/Administrator/workspace/Paws/WebContent/WEB-INF/execs/xml_schemas";
		File dir = new File(path);
		File listDir[] = dir.listFiles();
		for (int i = 0; i < listDir.length; i++) {
			xsdFiles.add(path+"/"+listDir[i].getName());
		}
		
		exeFiles = new ArrayList<String>();
		String exePath = "C:/Documents and Settings/Administrator/workspace/Paws/WebContent/WEB-INF/execs/";
		File exeDir = new File(exePath);
		File listDirExe[] = exeDir.listFiles();
		for (int i = 0; i<listDirExe.length; i++) {
			if (listDirExe[i].isFile() && listDirExe[i].getName().contains(".exe")){
				exeFiles.add(exePath+"/"+listDirExe[i].getName());
				System.out.println(listDirExe[i].getName());
			}
		}
	}
		
	public static void createConfigFile () throws ParserConfigurationException {
		if (xsdFiles == null) 
			readFiles();
		
		XMLGenerator gen = new XMLGenerator();
		String xmlString = gen.getXMLString(xsdFiles);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  

        try  
        {  
            builder = factory.newDocumentBuilder();  
  
            Document document = builder.parse(new InputSource(new StringReader( xmlString ) ) );  
  
            TransformerFactory tranFactory = TransformerFactory.newInstance();  
            Transformer trans = tranFactory.newTransformer();  
            
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            
            Source src = new DOMSource(document);  
            Result dest = new StreamResult(new File("configFile.xml"));  
            trans.transform( src, dest );
        } catch (Exception e)  
        {  
            e.printStackTrace();  
        } 
	}
	
	public static void parseXML(String fileName){
		List<Option> options = new ArrayList<Option>();

		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new File(fileName));
			
			// normalize text representation
			doc.getDocumentElement ().normalize ();

			NodeList listOfOptions = doc.getElementsByTagName("option");

			for (int i = 0; i < listOfOptions.getLength() ; i++){

				String name=null;
				HashMap<String,String> param = new HashMap<String, String>();
				ArrayList<String> paramList = new ArrayList<String>();
					
				Node firstOptionNode = listOfOptions.item(i);
				if(firstOptionNode.getNodeType() == Node.ELEMENT_NODE){
					Element firstOptionElement = (Element)firstOptionNode;
					NodeList nameList = firstOptionElement.getElementsByTagName("name");
					Element nameElement = (Element)nameList.item(0);
	
					NodeList textNameList = nameElement.getChildNodes();
					name = ((Node)textNameList.item(0)).getNodeValue().trim();
					
					NodeList listOfParam = firstOptionElement.getElementsByTagName("parameter");
					int totalParam = listOfParam.getLength();                 
	
					for (int k=0; k<totalParam; k++ ){
						String key=null, value=null;
						Element paramElement = (Element)listOfParam.item(k);
						
						NodeList textParameterList = paramElement.getChildNodes();
						String aux[]=((Node)textParameterList.item(0)).getNodeValue().trim().split(",");
						key = aux[0].trim();
						value = aux[1].trim();
						param.put(key, value);
						paramList.add(key);
					}
				}//end if
				Option op = new Option(name, param, paramList);
				options.add(op);
			}//end for
		} catch (SAXParseException err) {
			System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
			System.out.println(" " + err.getMessage ());
		} catch (SAXException e) {
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();
		} catch (Throwable t) {
			t.printStackTrace ();
		}
	}
}
