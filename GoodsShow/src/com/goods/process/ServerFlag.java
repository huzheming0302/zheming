package com.goods.process;

public class ServerFlag {

	private ErrorType errorType = ErrorType.lang_err_success;
	
	public ServerFlag()
	{
		
	}
	
	public ServerFlag(ErrorType error)
	{
		this.setErrorType(error);
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}
}
