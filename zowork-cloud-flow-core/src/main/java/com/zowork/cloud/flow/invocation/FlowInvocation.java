package com.zowork.cloud.flow.invocation;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;
import com.zowork.cloud.flow.FlowException;
import com.zowork.cloud.flow.FlowUtils;
import com.zowork.cloud.flow.node.FlowChooseTagNode;
import com.zowork.cloud.flow.node.FlowElement;
import com.zowork.cloud.flow.node.FlowForwardTagNode;
import com.zowork.cloud.flow.node.FlowSubFlowTagNode;
import com.zowork.cloud.flow.node.FlowTagNode;
import com.zowork.cloud.flow.node.FlowsRootTagNode;

public class FlowInvocation {
	static Logger logger = LoggerFactory.getLogger(FlowInvocation.class);
	final FlowElement startNode;
	final FlowConfiguration configuration;
	final FlowTagNode flow;
	FlowElement nextNode;
	FlowElement currentNode;

	public FlowInvocation(FlowTagNode flow, FlowConfiguration configuration) {
		super();
		this.startNode = flow.getFirst();
		this.nextNode = this.startNode;
		this.currentNode = this.startNode;
		this.configuration = configuration;
		this.flow = flow;
	}

	public Object invoke() throws Exception {
		return invokeNext(this);
	}

	public FlowElement getNextNode() {
		return nextNode;
	}

	public void setNextNode(FlowElement nextNode) {
		this.nextNode = nextNode;
		FlowContext context = FlowContext.getContext();
		context.setNextNode(nextNode);
		if (FlowUtils.getLogger().isDebugEnabled()) {
			FlowUtils.getLogger().debug("next node=" + nextNode);
		}

	}

	public FlowElement getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(FlowElement currentNode) {

		this.currentNode = currentNode;
		FlowContext context = FlowContext.getContext();
		context.setCurrentNode(currentNode);
	}

	public FlowElement getStartNode() {
		return startNode;
	}

	FlowElement findNextNode() {
		return null;
	}

	/**
	 * 查找父级的下一个节点
	 * 
	 * @return
	 */
	FlowElement findParentNext(FlowElement curent) {
		if (curent == null) {
			return null;
		}
		FlowElement parent = curent.parent();
		if (parent == null || FlowUtils.isFlowNode(parent) || FlowUtils.isRootNode(parent)) {
			return null;
		}
		if (FlowUtils.isChooseSubNode(parent)) {// 往上查找到，递归查找父级节点的
			return findParentNext(parent.parent());
		}
		if (FlowUtils.isIfNode(parent) || FlowUtils.isChooseNode(parent) || FlowUtils.isSubFlowNode(parent)) {// 往上查找到，递归查找父级节点的
			FlowElement next = parent.next();
			if (next == null) {
				return findParentNext(parent.parent());
			}
			// TODO elseif 这次暂时不支持
			// TODO else 这次暂时不支持
			return next;

		}
		return parent.next();
	}

	// TODO 处理choose节点关系
	// choose
	// when
	// choose
	// when
	// otherwise
	// choose
	// when
	//
	void nextNode(FlowContext context) {
		if (this.nextNode == null) {
			return;
		}
		/**
		 * 假如当前的节点不等于nextNode证明已经跳转到别的节点，重新从nextNode进行执行， 发送这种情况如下：
		 * 1.script节点执行。2.goto节点执行
		 */
		if (this.nextNode != this.currentNode) {
			this.currentNode = this.nextNode;
			return;
		}
		/**
		 * choose节点的逻辑 假如没有子节点，回溯到父级节点分以下这种情况：
		 * 1.假如在choose节点中，回溯到when的时候，还得继续回溯到choose，执行choose往下的节点
		 * 2.假如在choose节点中，回溯到otherwise的时候，还得继续回溯到choose，执行choose往下的节点
		 */

		/**
		 * if节点的逻辑 1.假如在if节点中，回溯到if节点，判断下一节点是否是else if节点，假如是else
		 * if节点则执行test判断是否执行 2.假如在if节点中，回溯到if节点，判断下一节点是否是else节点，假如是else节点则直接跳过
		 * 这次暂时不实现else if 和else
		 */
		/**
		 * node节点的逻辑 直接执行
		 */
		/**
		 * script节点的逻辑 直接执行
		 */
		/**
		 * goto节点的逻辑 查找到对应的node节点
		 */
		/**
		 * sub-flow逻辑
		 */
		if (this.nextNode instanceof FlowChooseTagNode) {
			FlowChooseTagNode chooseNode = (FlowChooseTagNode) this.nextNode;
			if (CollectionUtils.isNotEmpty(chooseNode.getIfNodeList())) {
				FlowElement tempNode = null;
				for (FlowElement whenNode : chooseNode.getIfNodeList()) {// 判断when的条件，假如有一个为true则往下执行
					if (whenNode.test(context)) {
						if (!whenNode.hasChild()) {
							throw new FlowException("flow configuration error!whenNode=" + whenNode);
						}
						tempNode = whenNode.childs().get(0);// 假如when
															// test=true，返回子节点的第一个，TODO
															// add trace
						break;
					}
				}
				if (tempNode == null) {
					tempNode = chooseNode.getDefaultNode();
					if (tempNode != null) {
						if (!tempNode.hasChild()) {
							throw new FlowException("flow configuration error!defaultNode=" + tempNode);// 假如otherwise，返回子节点的第一个，TODO
																										// add
																										// trace
						}
						tempNode = tempNode.childs().get(0);
					}
				}
				if (tempNode == null) {
					tempNode = this.findParentNext(chooseNode);
				}
				setNextNode(tempNode);
			}
			return;
		}
		if (this.nextNode instanceof FlowSubFlowTagNode) {
			FlowSubFlowTagNode subFlowNode = (FlowSubFlowTagNode) this.nextNode;
			if (StringUtils.isBlank(subFlowNode.getRefFlowId())) {
				if (CollectionUtils.isEmpty(subFlowNode.childs())) {
					throw new FlowException("5000", "sub-flow node childs is empty!namespace=" + flow.getNamespace()
							+ " flowId=" + flow.getId() + " sub-flow id=" + subFlowNode.getId());
				}
				this.nextNode = subFlowNode.childs().get(0);
				setNextNode(this.nextNode);
			} else {
				// this.nextNode = subFlowNode.childs().get(0);
				setNextNode(this.nextNode);
			}
			return;
		}
		if (this.nextNode.hasChild()) {// 假如该节点存在子节点，先执行子节点
			if (this.nextNode.test(context)) {// 假如条件判断通过，则进行执行，否则执行下一个节点或者父节点的下一个节点，假如父节点不存在下一个节点继续查找上级节点的下一个节点
				FlowElement tempNode = this.nextNode.childs().get(0);
				setNextNode(tempNode);
			} else {
				if (this.nextNode.next() == null) {
					FlowElement tempNode = this.findParentNext(this.nextNode);// 找上一级的节点

					setNextNode(tempNode);
				} else {
					FlowElement tempNode = this.nextNode.next();
					setNextNode(tempNode);
				}
			}
		} else {
			if (this.nextNode.next() == null) {// 下一个节点是空，则往上查找可执行节点
				FlowElement tempNode = this.findParentNext(this.nextNode);
				setNextNode(tempNode);
			} else {
				FlowElement tempNode = this.nextNode.next();
				setNextNode(tempNode);
			}
		}

	}

	boolean isFlowEnd() {
		return this.nextNode == null || this.nextNode instanceof FlowTagNode
				|| this.nextNode instanceof FlowsRootTagNode;
	}

	void checkInvoke(FlowContext context) {
		if (CollectionUtils.isNotEmpty(context.getNodeStack())
				&& context.getNodeStack().size() > context.getInvocation().getConfiguration().getMaxFlowStackSize()) {
			throw new FlowException("5000", "flow stack overflow!stack size=" + context.getNodeStack().size());
		}
	}

	public Object invokeNext(FlowInvocation invocation) throws Exception {
		FlowContext context = FlowContext.getContext();
		setCurrentNode(this.nextNode);//设置当前执行节点
		if (!isFlowEnd()) {
			checkInvoke(context);// 检查执行情况，是否超过最大调用限制，防止循环调用

			if (FlowUtils.isExecutable(this.nextNode) && this.nextNode.test(context)) {// 执行通过之后进行
				FlowInvocationProxy invocationProxy = new FlowInvocationProxy(this.nextNode, this, context);
				Object result = invocationProxy.invokeActionOnly();
				if (FlowUtils.isReturnResult(context, result)) {
					this.nextNode = null;
					return result;
				}
				if (this.nextNode == this.currentNode) {// 将currentNode==this.nextNode的时候节点没有在script或者通过方法进行跳转
					nextNode(context);// 跳转到下一个执行节点
				}

				result = invokeNext(invocation);
				if (FlowUtils.isReturnResult(context, result)) {
					this.nextNode = null;
					return result;
				}

			}
			if (FlowUtils.isForwardNode(nextNode)) {// 假如是goto节点,直接跳转到下一个节点
				FlowForwardTagNode gotoNode = (FlowForwardTagNode) this.nextNode;
				this.nextNode = this.flow.getNodeById(gotoNode.getId());
				this.setNextNode(nextNode);
				Object result = invokeNext(invocation);
				if (FlowUtils.isReturnResult(context, result)) {
					this.nextNode = null;
					return result;
				}
			}
			if (this.nextNode instanceof FlowSubFlowTagNode) {
				FlowSubFlowTagNode subFlowNode = (FlowSubFlowTagNode) this.nextNode;
				if (StringUtils.isNotBlank(subFlowNode.getRefFlowId())) {
					FlowTagNode refSubFlow = this.getConfiguration().getFlow(flow.getNamespace(),
							subFlowNode.getRefFlowId());
					if (refSubFlow == null) {
						throw new FlowException("5000", "flow not found!namespace=" + flow.getNamespace() + ",flowId="
								+ subFlowNode.getRefFlowId());
					}
					FlowInvocation subFlowInvocation = new FlowInvocation(refSubFlow, this.configuration);
					Object subResult = subFlowInvocation.invoke();
					if (FlowUtils.isReturnResult(context, subResult)) {
						this.nextNode = null;
						return subResult;
					}
					if (this.nextNode.next() != null) {
						this.setNextNode(this.nextNode.next());
					} else {
						this.setNextNode(this.findParentNext(this.nextNode));
					}

				}
			}
			nextNode(context);// 跳转到下一个执行节点
			Object result = invokeNext(invocation);

			if (FlowUtils.isReturnResult(context, result)) {
				this.nextNode = null;
				return result;
			}
		}
		return null;

	}

	public void forward(String id) throws Exception {
		FlowElement node = flow.getNodeById(id);
		if (node == null) {
			throw new FlowException("5000", "node not found!id=" + id);
		}
		FlowContext context = FlowContext.getContext();
		FlowElement historyNode = context.getHistory(id);
		if (historyNode != null) {
			logger.warn("invoke before!are you sure want this?historyNode=" + historyNode);
		}
		this.setNextNode(node);
	}

	public FlowConfiguration getConfiguration() {
		return configuration;
	}

}
