package org.alfresco.extension.procurement.action;

import java.util.List;

import org.alfresco.extension.procurement.model.ProcurementModel;
import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;

public class ChangeRequestState extends ActionExecuterAbstractBase{
 
	public final static String PARAM_PROGRESS = "progress";
	private NodeService nodeService;
	
	@Override
	protected void executeImpl(Action action, NodeRef nodeRef) {
		QName nodeType = nodeService.getType(nodeRef);
		if(ProcurementModel.TYPE_PROCUREMENT_REQUEST.isMatch(nodeType)){
			String progress = (String)action.getParameterValue(PARAM_PROGRESS);
			
			nodeService.setProperty(nodeRef, ProcurementModel.TYPE_PROCUREMENT_REQUEST_STATUS, progress);
		}
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		// Create a new parameter definition to add to the list
		paramList.add(
		         new ParameterDefinitionImpl(                      
		            PARAM_PROGRESS,                              	// The name used to identify the parameter
		            DataTypeDefinition.TEXT,                       // The parameter value type
		            true,                                         // Indicates whether the parameter is mandatory
		            "Progress"));      // The parameters display label
		//TODO: add getParamDisplayLabel(PARAM_PROGRESS)
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}
}
