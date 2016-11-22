package com.goods.process;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class List2Map {
	public <T> Map<String, T> listToMap(String keyName, List<T> list){
		Map<String, T> m = new HashMap<String, T>();
		try {
			for (T t : list) {
				PropertyDescriptor pd = new PropertyDescriptor(keyName,
						t.getClass());
				Method getMethod = pd.getReadMethod();// ���get����
				Object o = getMethod.invoke(t);// ִ��get��������һ��Object
				m.put(o.toString(), t);
			}
			return m;
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		return null;
	}
}
