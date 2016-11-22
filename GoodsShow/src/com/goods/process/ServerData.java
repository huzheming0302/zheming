package com.goods.process;

public class ServerData {

	private ServerFlag flag = null;
	private Object data = null;
	
	private final static String format = "{\"flag\":{\"errorType\":\"%s\"},\"data\":null}";

	public static String makeJsonValue(ErrorType error)
	{
		return String.format(format, error);
	}
	
	public ServerData(ServerFlag flag)
	{
		this.setFlag(flag);
		this.setData(null);
	}
	
	public ServerData(ServerFlag flag, Object data)
	{
		this.setFlag(flag);
		this.setData(data);
	}

	public ServerFlag getFlag() {
		return flag;
	}

	public void setFlag(ServerFlag flag) {
		this.flag = flag;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
