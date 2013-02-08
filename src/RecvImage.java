

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RecvImage
 */
@WebServlet("/RecvImage")
public class RecvImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ServletContext sc = null;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecvImage() {
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
		
		String fileName = request.getHeader("X-File-Name");
		String sessionID = ""+fileName.hashCode();
		File dir = new File (sc.getRealPath("/images/") + "/");
		dir.mkdir();
		dir = new File (sc.getRealPath("/images/") + "/" + sessionID);
		dir.mkdir();
		dir = new File (sc.getRealPath("/images/") + "/" + sessionID + "/");
		dir.mkdir();
		File image = new File (dir.getAbsolutePath() + "/" + fileName);
		
		BufferedReader reader = request.getReader();
		FileWriter fw = new FileWriter(image.getAbsoluteFile());
		
		String line = null;
		String content="";
		while ((line = reader.readLine()) != null)
		   content += line + "\n";
		
		Utils.sessionID = sessionID;
		Utils.imgFiles.add(image.getAbsolutePath());
		Utils.imgFilesRelative.add(fileName);
		System.out.println(image.getAbsolutePath());
		
		byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(content);   
		FileOutputStream osf = new FileOutputStream(image);  
		osf.write(btDataFile);  
		osf.flush(); 
		
		
		PrintWriter done = response.getWriter();
		done.write("WE ARE DONE!!!!!!");
		
		
	}

}
