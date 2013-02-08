import java.io.*;
import java.util.ArrayList;
import java.util.zip.*;

public class ZipIt {
	public static void doTheZip (ArrayList<String> filesToZip, String zipFileName) throws IOException{

		ZipOutputStream out = new ZipOutputStream
				(new FileOutputStream(zipFileName));
		out.setLevel(Deflater.DEFAULT_COMPRESSION);

		for (String file : filesToZip) {
			byte[] buffer = new byte[18024];
			try{

				FileInputStream in = new FileInputStream(file);
				File fileAux = new File(file);
				out.putNextEntry(new ZipEntry(fileAux.getName()));
				int len;
				while ((len = in.read(buffer)) > 0){
					out.write(buffer, 0, len);
				}
				in.close();
			}
			catch (IllegalArgumentException iae){
				iae.printStackTrace();
				System.exit(0);
			}
			catch (FileNotFoundException fnfe){
				fnfe.printStackTrace();
				System.exit(0);
			}
			catch (IOException ioe){
				ioe.printStackTrace();
				System.exit(0);
			}
		}

		out.closeEntry();
		out.close();
	}
}