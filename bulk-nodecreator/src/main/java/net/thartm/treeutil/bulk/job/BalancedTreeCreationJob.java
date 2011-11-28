package net.thartm.treeutil.bulk.job;

import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class BalancedTreeCreationJob extends AbstractNodeCreationJob{

	private long numberOfNodes;
	private long maxNumbersInFolder;
	private NodeRef parentRef;
	private NodeRef templateRef;
	private String contentType;
	
 	
	public BalancedTreeCreationJob (WebScriptRequest request){
		super();
		setParentRef(request.getParameter("parent"));
		
		this.setContentType(request.getParameter("contentType"));
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

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}
	
}