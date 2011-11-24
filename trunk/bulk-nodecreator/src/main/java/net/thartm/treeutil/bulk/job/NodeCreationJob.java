package net.thartm.treeutil.bulk.job;

import org.springframework.extensions.webscripts.WebScriptRequest;

public class NodeCreationJob extends AbstractNodeCreationJob {
	
	private Integer numberOfIterations;
	private int batchSize = 100;	
	
	public NodeCreationJob(WebScriptRequest request){
		super();
		this.numberOfIterations = Integer.valueOf(request.getParameter("number"));
		setParentRef(request.getParameter("parent"));
		setTemplateRef(request.getParameter("sample"));
	}

	/**
	 * @return the numberOfIterations
	 */
	public Integer getNumberOfIterations() {
		return numberOfIterations;
	}

	/**
	 * @return the batchSize
	 */
	public int getBatchSize() {
		return batchSize;
	}

	/**
	 * @param batchSize the batchSize to set
	 */
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
}
