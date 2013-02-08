import java.io.InputStream;

public class Exec {

	String execName;
	String argFilename;
	
	public Exec(String execName, String argName)
	{
		this.execName = execName;
		this.argFilename = argName;
	}
	
	
	void Execute()
	{
		try
		{
			String completePathExe = Utils.PathExe + this.execName;
			String completePathArgs = this.argFilename;
			Process p = new ProcessBuilder(completePathExe, completePathArgs).start();
			
			
			p.waitFor();
			p.destroy();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
