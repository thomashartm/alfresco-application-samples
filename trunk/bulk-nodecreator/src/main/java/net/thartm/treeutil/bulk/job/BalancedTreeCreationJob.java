package net.thartm.treeutil.bulk.job;

import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class BalancedTreeCreationJob extends AbstractNodeCreationJob{

	private long numberOfNodes;
	private long maxNumbersInFolder;
	private NodeRef parentRef;
	private NodeRef templateRef;
	
	public BalancedTreeCreationJob (WebScriptRequest request){
		super();
		setParentRef(request.getParameter("parent"));
		setTemplateRef(request.getParameter("sample"));
		
		this.numberOfNodes = Long.valueOf(request.getParameter("number"));
		this.maxNumbersInFolder = Long.valueOf(request.getParameter("maxfoldersize"));
	}
	
	/**
	 * @return the parentRef
	 */
	public NodeRef getParentRef() {
		return parentRef;
	}

	/**
	 * @return the templateRef
	 */
	public NodeRef getTemplateRef() {
		return templateRef;
	}

	/**
	 * @return the numberOfNodes
	 */
	public long getNumberOfNodes() {
		return numberOfNodes;
	}

	/**
	 * @return the maxNumbersInFolder
	 */
	public long getMaxNumbersInFolder() {
		return maxNumbersInFolder;
	}
	
}