import java.util.ArrayList;
import java.util.HashMap;


class Option{
	//numele optiunii
	String name;
	//HashMap de tip [nume_parametru, valoare parametru]
	HashMap<String, String> params = new HashMap<String,String>();
	//lista cu parametrii folositi
	ArrayList<String> paramsList = new ArrayList<String>();
	
	
	Option(String name, HashMap<String, String> map, ArrayList<String> list){
		this.name=name;
		for (String key:list){
			//System.out.println(key);
			this.paramsList.add(key);
			this.params.put(key, map.get(key));
		}
	}


}
