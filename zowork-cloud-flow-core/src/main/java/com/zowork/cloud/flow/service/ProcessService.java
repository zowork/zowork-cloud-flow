package com.zowork.cloud.flow.service;

public interface ProcessService<M, P> {
	M process(String namespace, String flowId, P param);
}
