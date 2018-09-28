package com.zowork.cloud.flow;

public class FlowException extends RuntimeException {
	private static final long serialVersionUID = 189290582814249300L;
	String code;

	public FlowException(String code, String message) {
		super(message);
		this.code = code;
	}

	public FlowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public FlowException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public FlowException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public FlowException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FlowException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
