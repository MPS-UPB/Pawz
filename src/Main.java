import javax.xml.parsers.ParserConfigurationException;
//import java.io.IOException;

public class Main {

	/**
	 * @param args
	 * @throws ParserConfigurationException 
	 */
	public static void main(String[] args) throws ParserConfigurationException {
		/*File out = null;
		try {
			TransferFiles.download("http://localhost:8080/Paws", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (out != null ) {
			System.out.println("Succes!!!");
			//proceseaza fisier
		}*/
		
		Utils.readFiles();
		Utils.getXmlStringHash();
	}

}
