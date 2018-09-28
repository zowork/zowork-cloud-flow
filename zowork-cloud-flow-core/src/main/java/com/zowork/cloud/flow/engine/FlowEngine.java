package com.zowork.cloud.flow.engine;

import com.zowork.cloud.flow.FlowContext;
import com.zowork.cloud.flow.trace.FlowTrace;

public interface FlowEngine<T> {
    /**
     * 处理流程执行
     * @param context
     * @return
     */
	T process(FlowContext context);
    /**
     * 跟踪流程执行过程输出
     * @param context
     * @return
     */
	FlowTrace trace(FlowContext context);
}
