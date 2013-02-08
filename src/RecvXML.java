

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RecvXML
 */
@WebServlet("/RecvXML")
public class RecvXML extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ServletContext sc = null;     
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RecvXML() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		Utils.readFiles();
		sc = config.getServletContext();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BufferedReader reader = request.getReader();
		//File xml = new File (sc.getRealPath("file_option.xml"));
		//File xml = new File ("file_option.xml");
		//PrintWriter xmlWR = new PrintWriter(xml);

		String line = null;
		String content = "";
		while ((line = reader.readLine()) != null)
			//xmlWR.write(line + "\n");
			content += line;



		//		byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(content);   
		//		FileOutputStream osf = new FileOutputStream(xml);  
		//		osf.write(btDataFile);  
		//		osf.flush(); 
		//		


		File xml = new File(sc.getRealPath("file_option.xml"));
		FileWriter fstream = new FileWriter(xml.getAbsoluteFile());
		System.out.println(sc.getRealPath("file_option.xml"));
		System.out.println(xml.getAbsoluteFile());
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(content);
		out.close();
		//fstream.close();
		
		List<Option> l = Utils.parseXML(xml.getAbsolutePath());
		HashMap<String, String> hm = Utils.generateXmlFiles(l);
		ExecUtils.RunExecutables(hm);
		
		
	}

}
