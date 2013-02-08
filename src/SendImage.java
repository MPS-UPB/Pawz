

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SendImage
 */
@WebServlet("/SendImage")
public class SendImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SendImage() {
		super();
		// TODO Auto-generated constructor stub
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
		PrintWriter out = response.getWriter();
		String output = "";
		ArrayList<String> filesToZip = new ArrayList<String>();
		String zipFileName = Utils.PathImagesRelative + Utils.sessionID + "\\archive.zip";
		
		File f = new File (Utils.PathImagesRelative + Utils.sessionID);	
		File[] file = f.listFiles();
		for (int i =0; i<file.length ; i++) {
			if (file[i].getName().startsWith("output_"))
				filesToZip.add(file[i].getAbsolutePath());
		}
		ZipIt.doTheZip(filesToZip, zipFileName);
		//http://localhost/_Pawz_/SendImage
		output = "http://localhost/_Pawz_/images/" + Utils.sessionID + "/archive.zip";
		out.write(output);
	}

}
