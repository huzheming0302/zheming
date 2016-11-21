package com.goods.process;

import java.util.Map;
import java.util.Set;

public class Map2Json {
	public static String simpleMapToJsonStr(Map<String ,String > map){  
        if(map==null||map.isEmpty()){  
            return "null";  
        }  
        String jsonStr = "{";  
        Set<?> keySet = map.keySet();  
        for (Object key : keySet) {  
            jsonStr += "\""+key+"\":\""+map.get(key)+"\",";       
        }  
        jsonStr = jsonStr.substring(1,jsonStr.length()-2);  
        jsonStr += "}";  
        return jsonStr; 
	} 
}
