package org.alfresco.extension.procurement.model;

import java.io.Serializable;
import java.util.Map;

import org.alfresco.extension.procurement.workflow.WorkflowExecutorImpl;
import org.alfresco.extension.procurement.workflow.WorkflowJob;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.content.filestore.FileContentReader;
import org.alfresco.repo.content.transform.ContentTransformer;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO Implement behaviour that: - sets number and initial state - creates the
 * rendered order ... as a PDF and adds it to the objects content property -
 * adds the record to the approval workflow
 * 
 */
public class ProcurementRequestType implements ProcurementModel {

	private final static Log log = LogFactory.getLog(ProcurementRequestType.class);

	private PolicyComponent policyComponent;
	private NodeService nodeService;
	private ContentService contentService;
	private WorkflowExecutorImpl workflowExecutor;
	private PersonService personService;
	private UniqueIdGenerator idGenerator;

	public ProcurementRequestType(ServiceRegistry serviceRegistry, WorkflowExecutorImpl workflowExecutor) {
		this.nodeService = serviceRegistry.getNodeService();
		this.contentService = serviceRegistry.getContentService();
		this.workflowExecutor = workflowExecutor;
		this.personService = serviceRegistry.getPersonService();
	}

	public void init() {
		log.debug("Registering type behaviour");
		policyComponent.bindClassBehaviour(NodeServicePolicies.OnCreateNodePolicy.QNAME, TYPE_PROCUREMENT_REQUEST,
				new JavaBehaviour(this, "onCreateNode"));

		policyComponent.bindClassBehaviour(NodeServicePolicies.OnUpdatePropertiesPolicy.QNAME,
				TYPE_PROCUREMENT_REQUEST, new JavaBehaviour(this, "onUpdateProperties"));
	}

	/**
	 * On create node behaviour
	 * 
	 * @param childAssocRef
	 *            child association reference
	 */
	public void onCreateNode(ChildAssociationRef childAssocRef) {
		NodeRef node = childAssocRef.getChildRef();

		// final String name = (String)
		// nodeService.getProperty(request,ProcurementModel.TYPE_NAME);
		String id = idGenerator.generateProcessId(childAssocRef, node);
		nodeService.setProperty(node, ProcurementModel.TYPE_PR_NUMBER, id);
		
		NodeRef parent = childAssocRef.getParentRef();
		String workflowId = getProcessWorkflowDefinition(parent);
		log.debug("Started processing node " + node.getId() + " using workflow id " + workflowId);

		startAndAssignApprovalWorkflow(node);
	}

	private void startAndAssignApprovalWorkflow(NodeRef node) {
		NodeRef assignee = personService.getPerson("christian");
		if (assignee != null) {
			WorkflowJob job = new WorkflowJob(ProcurementModel.REVIEW_WORKFLOW_NAME, node, assignee);
			job.setWorkflowDescription("Bitte um Freigabe");
			workflowExecutor.doRun(job);
		} else {
			log.error("Tried to assign a workflow to thomas ");
		}
	}

	private String getProcessWorkflowDefinition(NodeRef parent) {
		if (nodeService.hasAspect(parent, ProcurementModel.TYPE_PROCESS_CONTAINER)) {
			return (String) nodeService.getProperty(parent, ProcurementModel.TYPE_PROCESS_CONTAINER_WORKFLOW);
		}
		return "";
	}

	/**
	 * On update node properties behaviour
	 * 
	 */
	public void onUpdateProperties(NodeRef node, Map<QName, Serializable> before, Map<QName, Serializable> after) {
		log.debug("Node " + node.getId() + " properties have been updates. Creating property rendition.");
		createBinaryRequestDocument(node);
	}

	/**
	 * Creates a PDF order and stores it inside the order nodes content
	 * property.
	 * 
	 * @param request
	 */
	private void createBinaryRequestDocument(NodeRef request) {
		ContentTransformer transformer = contentService.getTransformer(MimetypeMap.MIMETYPE_TEXT_PLAIN,
				MimetypeMap.MIMETYPE_PDF);

		ProcurementRequest procurementRequest = new ProcurementRequest(request, this.nodeService);
		//
		// null argument is necessary to create a tempReader ... needed to get
		// the ContentReader
		ContentReader reader = FileContentReader.getSafeContentReader(null, procurementRequest.createTextOutput());
		ContentWriter writer = contentService.getWriter(request, TYPE_CONTENT, true);

		writer.setMimetype(MimetypeMap.MIMETYPE_PDF);
		writer.setEncoding("UTF-8");

		transformer.transform(reader, writer);
	}

	public PolicyComponent getPolicyComponent() {
		return policyComponent;
	}
	
	public void setPolicyComponent(PolicyComponent policyComponent) {
		this.policyComponent = policyComponent;
	}
	
	public void setIdGenerator(UniqueIdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
}
