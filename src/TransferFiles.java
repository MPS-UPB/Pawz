import java.io.*;
import java.net.*;


public class TransferFiles {

	public static void download (String m_url, File outFile) throws IOException {
		URL url = new URL(m_url);
	    HttpURLConnection con = (HttpURLConnection)url.openConnection();
	    try {
	        InputStream in = con.getInputStream();
	        try {
	            OutputStream out = new FileOutputStream(outFile);
	            try {
	                byte buf[] = new byte[4096];
	                for (int n = in.read(buf); n > 0; n = in.read(buf)) {
	                    out.write(buf, 0, n);
	                }
	            } finally {
	                out.close();
	            }
	        } finally {
	            in.close();
	        }
	    } finally {
	        con.disconnect();
	    }
	}
	
	public static void upload (String m_url, File inFile) throws IOException {
		URL url = new URL(m_url);
	    HttpURLConnection con = (HttpURLConnection)url.openConnection();
	    try {
	        con.setDoOutput(true);
	        con.setRequestMethod("POST");
	        OutputStream out = con.getOutputStream();
	        try {
	            InputStream in = new FileInputStream(inFile);
	            try {
	                byte buffer[] = new byte[4096];
	                for (int n = in.read(buffer); n > 0; n = in.read(buffer)) {
	                    out.write(buffer, 0, n);
	                }
	            } finally {
	                in.close();
	            }
	        } finally {
	            out.close();
	        }
	        int code = con.getResponseCode();
	        if (code != HttpURLConnection.HTTP_OK) {
	            String msg = con.getResponseMessage();
	            throw new IOException("HTTP Error " + code + ": " + msg);
	        }
	    } finally {
	        con.disconnect();
	    }
	}
	
}
