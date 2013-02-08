import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;



public class Utils {

	public static ArrayList<String> xsdFiles;
	public static ArrayList<String> exeFiles;
	public static ArrayList<String> imgFiles = new ArrayList<>();
	public static ArrayList<String> imgFilesRelative = new ArrayList<>();
	
	public static HashMap<String, ArrayList<String>> hash;
	public static ArrayList<String> execsWithMultipleInputFiles;
	public static String sessionID;
	
	public static String PathExe = "C:/Documents and Settings/Administrator/workspace/_Pawz_/WebContent/WEB-INF/execs/";
	//public static String PathXMLSchemas = "C:/Documents and Settings/Administrator/workspace/_Pawz_/WebContent/WEB-INF/execs/xml_schemas";
	public static String PathImagesRelative = "";

	
	public enum ExecType{Preprocessing, Binarization, Layout, Paging, OCR, Hierarchy, PDF, Support, Crop, Rotate, Other};
	public enum FileType{Xml, Image, Other}
	
	public static String removeExtension (String file) {
		StringBuilder sb = new StringBuilder(file);
		sb.replace(file.length()-4, file.length(), "");
		return sb.toString();
	}
	
	public static ExecType GetExecTypeFromString(Option executable)
	{
		String execName = executable.name;
		for (ExecType exec : ExecType.values())
		{
			if(execName.toLowerCase().contains(exec.toString().toLowerCase()))
			{
				return exec;
			}
		}
		return ExecType.Other;
	}
	
	public static FileType InputType(ExecType exec)
	{
		switch(exec)
		{
		case Preprocessing:
			return FileType.Image;
		case Binarization:
			return FileType.Image;
		case Layout:
			return FileType.Image;
		case Paging:
			return FileType.Xml;
		case OCR:
			return FileType.Xml;
		case Hierarchy:
			return FileType.Xml;
		case PDF:
			return FileType.Xml;
		case Other:
			return FileType.Other;
		case Support:
			return FileType.Image;
		case Rotate:
			return FileType.Image;
		case Crop:
			return FileType.Image;
		default:
			return FileType.Other;
		}
	}
	
	public static FileType OutputType(ExecType exec)
	{
		switch(exec)
		{
		case Preprocessing:
			return FileType.Image;
		case Binarization:
			return FileType.Image;
		case Layout:
			return FileType.Xml;
		case Paging:
			return FileType.Xml;
		case OCR:
			return FileType.Xml;
		case Hierarchy:
			return FileType.Xml;
		case PDF:
			return FileType.Other;
		case Support:
			return FileType.Image;
		case Rotate:
			return FileType.Image;
		case Crop:
			return FileType.Image;
		case Other:
			return FileType.Other;
		default:
			return FileType.Other;
		}
	
	}
	
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
		
/*
	public static String createConfigFile () {
		if (xsdFiles == null) 
			readFiles();

		XMLGenerator gen = null;
		try {
			gen = new XMLGenerator();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
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
			Result dest = new StreamResult(new File("WebContent/configFile.xml"));  
			trans.transform( src, dest );
		} catch (Exception e)  
		{  
			e.printStackTrace();  
		} 
		execsWithMultipleInputFiles = new ArrayList<String>(gen.execsWithMultipleInputFiles);
		
		return xmlString;
	}
*/	
	public static List<Option> parseXML(String fileName){
		List<Option> options = new ArrayList<Option>();

		String ID = sessionID;

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

					if (firstOptionElement.getElementsByTagName("parameter")==null){
						String key=null, value=null;

						key = "0";
						value = "0";

						param.put(key, value);
						paramList.add(key);
					}
					else {
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
					}
				}//end if
				Option op = new Option(name, param, paramList, ID);
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
		return options;
	}
	
	public static HashMap<String,String> generateXmlFiles(List<Option> options) throws IOException{
        
    //    List<Option> options = Utils.parseXML("test.xml");
        HashMap<String,String> xmluri = new HashMap<>();
        System.out.println(options.size());
        
        int indexOfExecMultipleInput = -1;

        for (int contor = 0; contor < options.size(); contor ++){
        	Option aux = options.get(contor);
        	if (Utils.execsWithMultipleInputFiles != null)
        	if (Utils.execsWithMultipleInputFiles.contains(aux.name)){
        		indexOfExecMultipleInput = contor;
                break;
        	}
        }
        if(indexOfExecMultipleInput == -1)
        {
        	for (int contor = 0; contor < options.size(); contor ++){
        		
        		Option aux = options.get(contor);
                    
                for (int img = 0; img < Utils.imgFilesRelative.size(); img ++){
                	
                	String xmlInputFilePath = Utils.PathExe + Utils.sessionID + "/" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) +".xml";
                	File dir = new File (Utils.PathExe + Utils.sessionID);
            		dir.mkdir();
                	File newFile = new File(xmlInputFilePath);

                    FileWriter fw = null;
                    try {
                    	fw = new FileWriter(newFile.getAbsoluteFile());
                    } catch (IOException ex) {
                    	//Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("<task>\n");
                        
                        // establish type of input/output files for current exec
                        FileType inputType = InputType(GetExecTypeFromString(aux));
                        FileType outputType = InputType(GetExecTypeFromString(aux));
                        File dir2 = new File (Utils.PathImagesRelative + aux.sessionID);
                        dir2.mkdir();
                        
                        if (contor == 0){
                        	if(inputType == FileType.Image){
                        		bw.write("<inputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.imgFilesRelative.get(img) + "\"" + "/>\n");
                        	}
                        	if(options.size() >1){
	                        	if(outputType == FileType.Image)
								{
									bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".png" + "\"" + "/>\n" );
								}
								else if(outputType == FileType.Xml)
								{
									bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n" );
								}
                        	}
                        	else
                        	{
                        		if(outputType == FileType.Image)
    							{
    								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + "output_" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".png" + "\"" + "/>\n" );
    							}
    							else if(outputType == FileType.Xml)
    							{
    								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + "output_" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n" );
    							}
                        	}
                        		
                        }
                        else if (contor == options.size() - 1){
                        	
							if(inputType == FileType.Image)
							{
								bw.write("<inputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(options.get(contor-1).name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".png" + "\"" + "/>\n");
                            }
							else if(inputType == FileType.Xml)
							{
								bw.write("<inputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(options.get(contor-1).name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n");
							}
							
                            if(outputType == FileType.Image)
							{
								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + "output_" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".png" + "\"" + "/>\n" );
							}
							else if(outputType == FileType.Xml)
							{
								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + "output_" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n" );
							}
                        }
                        else{
							if(inputType == FileType.Image)
							{
								bw.write("<inputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(options.get(contor-1).name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".png" + "\"" + "/>\n");
                            }
							else if(inputType == FileType.Xml)
							{
								bw.write("<inputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(options.get(contor-1).name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n");
							}
							
							if(outputType == FileType.Image)
							{
								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".png" + "\"" + "/>\n" );
							}
							else if(outputType == FileType.Xml)
							{
								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n" );
							}
                        }
                        for (int i = 0; i < aux.paramsList.size(); i ++){
                            if (aux.paramsList.get(i).equals("0"))
                                continue;
                            bw.write("<" + aux.paramsList.get(i) + ">");
                            bw.write(aux.params.get(aux.paramsList.get(i)));
                            bw.append("</"+ aux.paramsList.get(i) + ">\n");
                        }
                        bw.write("</task>");
                        xmluri.put(aux.name, newFile.getAbsolutePath());
                        bw.close();
                        fw.close();
                    }
                }
        }
		//caz in care avem un executabil cu mai multe input image
        else
		{
			for (int contor = 0; contor < indexOfExecMultipleInput; contor ++){
        		
        		Option aux = options.get(contor);
                    
                for (int img = 0; img < Utils.imgFilesRelative.size(); img ++){
                	
                	String xmlInputFilePath = Utils.PathExe + Utils.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) +".xml";
                	File newFile = new File(xmlInputFilePath);

                    FileWriter fw = null;
                    try {
                    	fw = new FileWriter(newFile.getAbsoluteFile());
                    } catch (IOException ex) {
                    	//Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("<task>\n");
                        
                        // establish type of input/output files for current exec
                        FileType inputType = InputType(GetExecTypeFromString(aux));
                        FileType outputType = InputType(GetExecTypeFromString(aux));
                        
                        
                        if (contor == 0){
                        	if(inputType == FileType.Image){
                        		bw.write("<inputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.imgFilesRelative.get(img) + "\"" + "/>\n");
                        	}
                        	
                        	if(outputType == FileType.Image)
							{
								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".png" + "\"" + "/>\n" );
							}
							else if(outputType == FileType.Xml)
							{
								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n" );
							}
                        		
                        }
                        else if (contor == options.size() - 1){
							if(inputType == FileType.Image)
							{
								bw.write("<inputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(options.get(contor-1).name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".png" + "\"" + "/>\n");
                            }
							else if(inputType == FileType.Xml)
							{
								bw.write("<inputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(options.get(contor-1).name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n");
							}
							
                            if(outputType == FileType.Image)
							{
								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + "output_" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".png" + "\"" + "/>\n" );
							}
							else if(outputType == FileType.Xml)
							{
								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + "output_" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n" );
							}
                        }
                        else{
							if(inputType == FileType.Image)
							{
								bw.write("<inputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(options.get(contor-1).name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".png" + "\"" + "/>\n");
                            }
							else if(inputType == FileType.Xml)
							{
								bw.write("<inputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(options.get(contor-1).name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n");
							}
							
							if(outputType == FileType.Image)
							{
								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".png" + "\"" + "/>\n" );
							}
							else if(outputType == FileType.Xml)
							{
								bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n" );
							}
                        }
                        for (int i = 0; i < aux.paramsList.size(); i ++){
                            if (aux.paramsList.get(i).equals("0"))
                                continue;
                            bw.write("<" + aux.paramsList.get(i) + ">");
                            bw.write(aux.params.get(aux.paramsList.get(i)));
                            bw.append("</"+ aux.paramsList.get(i) + ">\n");
                        }
                        bw.write("</task>");
                        xmluri.put(aux.name, newFile.getAbsolutePath());
                        bw.close();
                        fw.close();
                    
                }  
            }
			
			Option aux = options.get(indexOfExecMultipleInput);
			String xmlInputFilePath = Utils.PathExe + Utils.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + "img" +".xml";
            File newFile = new File(xmlInputFilePath);

            FileWriter fw = null;
            try {
            	fw = new FileWriter(newFile.getAbsoluteFile());
            } catch (IOException ex) {
               	//Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("<task>\n");
                        
            // establish type of input/output files for current exec
            FileType inputType = InputType(GetExecTypeFromString(aux));
            FileType outputType = InputType(GetExecTypeFromString(aux));
			
			for (int img = 0; img < Utils.imgFilesRelative.size(); img ++){
				bw.write("<inputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(options.get(indexOfExecMultipleInput-1).name) + "_" + Utils.removeExtension(Utils.imgFilesRelative.get(img)) + ".xml" + "\"" + "/>\n");
			}
			if(indexOfExecMultipleInput == options.size() -1)
			{
				if(outputType == FileType.Image)
				{
					bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + "output_" + Utils.removeExtension(aux.name) + "_" + "img" + ".png" + "\"" + "/>\n" );
				}
				else if(outputType == FileType.Xml)
				{
					bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + "output_" + Utils.removeExtension(aux.name) + "_" + "img" + ".xml" + "\"" + "/>\n" );
				}
			}
			else
			{
				if(outputType == FileType.Image)
				{
					bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + "img" + ".png" + "\"" + "/>\n" );
				}
				else if(outputType == FileType.Xml)
				{
					bw.write("<outputFile name=\"" + Utils.PathImagesRelative + aux.sessionID + "\\" + Utils.removeExtension(aux.name) + "_" + "img" + ".xml" + "\"" + "/>\n" );
				}
			}
			
			for (int i = 0; i < aux.paramsList.size(); i ++){
				if (aux.paramsList.get(i).equals("0"))
					continue;
				bw.write("<" + aux.paramsList.get(i) + ">");
                bw.write(aux.params.get(aux.paramsList.get(i)));
                bw.append("</"+ aux.paramsList.get(i) + ">\n");
            }
            bw.write("</task>");
            xmluri.put(aux.name, newFile.getAbsolutePath());
            bw.close();
            fw.close();	
		}                       
        return xmluri;
    
	}
}
