package com.zowork.cloud.flow;

public class FlowRequest {

	public <T> T getAttribute(String key) {
		return FlowContext.getContext().getAttribute(key);
	}

	public void setAttribute(String key, Object value) {
		FlowContext.getContext().setAttribute(key, value);
	}

	public void setParameter(String key, Object value) {
		FlowContext.getContext().setParameter(key, value);
	}

	public <T> T getParameter(String key) {
		return FlowContext.getContext().getParameter(key);
	}
}
