package net.thartm.treeutil.bulk.job;

import org.springframework.extensions.webscripts.WebScriptRequest;

public class SimpleNodeCreationJob extends AbstractNodeCreationJob {
	
	private Integer numberOfDocs;
	private int batchSize = 100;	
	
	public SimpleNodeCreationJob(WebScriptRequest request){
		super();
		this.numberOfDocs = Integer.valueOf(request.getParameter("number"));
		setParentRef(request.getParameter("parent"));
		setTemplateRef(request.getParameter("sample"));
	}

	/**
	 * @return the numberOfIterations
	 */
	public Integer getNumberOfDocs() {
		return numberOfDocs;
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
