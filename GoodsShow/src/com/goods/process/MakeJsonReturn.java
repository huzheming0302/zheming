package com.goods.process;

import java.util.Map;

import net.sf.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MakeJsonReturn {
	private static final String TAG="MakeJsonReturn";
	protected String MakeJsonReturn(ServerFlag flag) {
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(new ServerData(flag));
		} catch (JsonProcessingException e) {
			//NetLog.error(TAG, e.getMessage());
			json = ServerData.makeJsonValue(ErrorType.lang_err_system);
		}

		//NetLog.debug(TAG, "json = " + json);
		
		return json;
	}
	
	protected String MakeJsonReturn(ServerFlag flag, Map<String, Object> data) {
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(new ServerData(flag, data));
		} catch (JsonProcessingException e) {
			//NetLog.error(TAG, e.getMessage());
			json = ServerData.makeJsonValue(ErrorType.lang_err_system);
		}

		//NetLog.debug(TAG, "json = " + json);
		
		return json;
	}
	protected String MakeJsonReturn(ServerFlag flag, JSONArray jsonArray) {
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(new ServerData(flag, jsonArray));
		} catch (JsonProcessingException e) {
			//NetLog.error(TAG, e.getMessage());
			json = ServerData.makeJsonValue(ErrorType.lang_err_system);
		}

		//NetLog.debug(TAG, "json = " + json);
		
		return json;
	}

}
