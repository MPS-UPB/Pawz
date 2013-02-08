import java.util.*;
import java.io.File;

public class ExecUtils {
	
	public static void RunExecutables(HashMap<String,String> listExecutables)
	{
		Set<String> KeySet = listExecutables.keySet();

		for(String key : KeySet)
		{
			
			Exec executable = new Exec(key, listExecutables.get(key));
			executable.Execute();
		}
	}
	
//	public static void RemoveTempFiles()
//	{
//		//remove temp input/output
//		File dir = new File(Utils.PathImagesRelative + Utils.sessionID);
//		if (dir.isDirectory()){
//			for (File child : dir.listFiles()) {
//				if(child.isFile() && !child.getAbsolutePath().toLowerCase().contains("output_"))
//				{
//					child.delete();
//				}
//			}
//		}
//		//remove argfiles xml
//		File dir2 = new File(Utils.PathExe + Utils.sessionID);
//		if (dir.isDirectory()){
//			for (File child : dir.listFiles()) {
//				if(child.isFile() && (child.getAbsolutePath().toLowerCase().contains("xml")))
//				{
//					child.delete();
//				}
//			}
//		}
//		
//	}
}
