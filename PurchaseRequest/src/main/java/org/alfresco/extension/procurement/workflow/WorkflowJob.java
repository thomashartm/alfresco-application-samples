package org.alfresco.extension.procurement.workflow;

import java.util.HashSet;
import java.util.Set;

import org.alfresco.service.cmr.repository.NodeRef;

public class WorkflowJob {

	private String workflow;
	private NodeRef assignee;
	private String workflowDescription;
	private String workflowTitle;
	private Set<NodeRef> content;
	
	public WorkflowJob(String workflow, NodeRef assignee, NodeRef... payload) {
		super();
		this.content = new HashSet<NodeRef>();
		this.workflow = workflow;
		this.assignee = assignee;
		
		for(NodeRef node : payload){
			this.content.add(node);
		}
	}

	public String getWorkflowDescription() {
		return workflowDescription != null ? workflowDescription : "Bitte um Bearbeitung";
	}

	public void setWorkflowDescription(String workflowDescription) {
		this.workflowDescription = workflowDescription;
	}

	public String getWorkflowTitle() {
		return workflowTitle != null ? workflowTitle : "Bitte um Bearbeitung";
	}

	public void setWorkflowTitle(String workflowTitle) {
		this.workflowTitle = workflowTitle;
	}

	public String getWorkflow() {
		return workflow;
	}

	public Set<NodeRef> getContent() {
		return content;
	}

	public NodeRef getAssignee() {
		return assignee;
	}

}
