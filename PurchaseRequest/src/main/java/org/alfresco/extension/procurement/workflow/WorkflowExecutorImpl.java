package org.alfresco.extension.procurement.workflow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.workflow.WorkflowModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.invitation.InvitationException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.alfresco.service.cmr.workflow.WorkflowPath;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.alfresco.service.cmr.workflow.WorkflowTask;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The WorkflowExecutorImpl start runs a @link(WorkflowJob). 
 * It creates a workflow package, adds all workflow properties and starts the workflow.
 * 
 * @author Thomas Hartmann
 *
 */
public class WorkflowExecutorImpl {

	private static final String PACKAGE_TO_PAYLOAD_ASSOCIATION_NAME = "Workflowcontent";
	private final static Log log = LogFactory.getLog(WorkflowExecutorImpl.class);
	private WorkflowService workflowService;
	private NodeService nodeService;

	public WorkflowExecutorImpl(ServiceRegistry serviceRegistry) {
		this.workflowService = serviceRegistry.getWorkflowService();
		this.nodeService = serviceRegistry.getNodeService();
	}

	/**
	 * Runs the specified workflow job (including a payload) and assignes it to the specified assignee.
	 * 
	 * @param job A job description that contains the relevant content and meta information for each workflow.
	 */
	public void doRun(WorkflowJob job) {
		NodeRef wfPackage = createWorkflowPackage(job.getContent());
		Map<QName, Serializable> workflowProperties = createWorkflowPropertiesPackage(job, wfPackage);
		
		// find the workflow definition and the associated tasks 
		WorkflowDefinition wfDefinition = retrieveWorkflowDefinition(job.getWorkflow());
		List<WorkflowTask> wfTasks = createWorkflowTaskList(wfDefinition, workflowProperties);
		
		startWorkflow(wfDefinition.getName(), wfTasks);
	}

	/**
	 * Tries to fiend a {@link WorkflowDefinition} that matches to the argument.
	 * All {@link WorkflowDefinition}s are based on jBPMN *_processdefinition.xml files.
	 * 
	 * @param definitionName Name of the {@link WorkflowDefinition} 
	 * @return A ready to use {@link WorkflowDefinition}.
	 */
	private WorkflowDefinition retrieveWorkflowDefinition(String definitionName) {
		WorkflowDefinition wfDefinition = this.workflowService
				.getDefinitionByName(definitionName);

		if (wfDefinition == null) {
			// throw RT Exception if workflow definition does not exist
			Object objs[] = { definitionName };
			throw new InvitationException("No workflow definition found. Please check if you assigned a correct workflow ID.", objs);
		}

		return wfDefinition;
	}

	private Map<QName, Serializable> createWorkflowPropertiesPackage(WorkflowJob job, NodeRef wfPackage) {
		Map<QName, Serializable> workflowProps = new HashMap<QName, Serializable>(16);
		workflowProps.put(WorkflowModel.ASSOC_PACKAGE, wfPackage);
		workflowProps.put(WorkflowModel.ASSOC_ASSIGNEE, job.getAssignee());
		
		workflowProps.put(ContentModel.PROP_TITLE, job.getWorkflowTitle());
		workflowProps.put(WorkflowModel.PROP_COMMENT, job.getWorkflowTitle());
		workflowProps.put(WorkflowModel.PROP_DESCRIPTION, job.getWorkflowDescription());
		return workflowProps;
	}

	/**
	 * Creates an empty workflow package and adds the provided set of {@link NodeRef}s to the new package.
	 * 
	 * @param payload {@link NodeRef} of the new workflow package
	 * @return
	 */
	private NodeRef createWorkflowPackage(Set<NodeRef> payload) {
		NodeRef wfPackage = this.workflowService.createPackage(null);
		for(NodeRef node : payload){
			nodeService.addChild(wfPackage, node, ContentModel.ASSOC_CONTAINS,
					QName.createQName(NamespaceService.CONTENT_MODEL_PREFIX, PACKAGE_TO_PAYLOAD_ASSOCIATION_NAME));
		}
		return wfPackage;
	}

	private void startWorkflow(String workflowDefinitionName, List<WorkflowTask> wfTasks) {

		// throw an exception if no tasks where found on the workflow path
		if (wfTasks.size() == 0) {
			Object objs[] = { workflowDefinitionName };
			throw new InvitationException("No tasks found. Terminating workflow execution.", objs);
		}

		try {
			WorkflowTask wfStartTask = wfTasks.get(0);
			this.workflowService.endTask(wfStartTask.id, null);
		} catch (RuntimeException err) {
			log.error("Failed - caught error during workflow transition: " + err.getMessage());
			throw err;

		}
	}

	private List<WorkflowTask> createWorkflowTaskList(WorkflowDefinition definition, Map<QName, Serializable> properties) {
		// start the workflow
		WorkflowPath wfPath = this.workflowService.startWorkflow(definition.getId(), properties);
		String wfPathId = wfPath.id;
		return this.workflowService.getTasksForWorkflowPath(wfPathId);
	}
}
