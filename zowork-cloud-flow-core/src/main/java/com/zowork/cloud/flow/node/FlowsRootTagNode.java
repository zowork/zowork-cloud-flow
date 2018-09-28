package com.zowork.cloud.flow.node;

public class FlowsRootTagNode extends BaseTagNode implements FlowElement { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 9097519770526794660L;
	String namespace;
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	} 

}
