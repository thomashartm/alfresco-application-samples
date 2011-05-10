package org.alfresco.extension.procurement.model;

import org.alfresco.service.namespace.QName;

public interface ProcurementModel {
	//STATICS
	public static final String NUMBER_PREFIX = "PROC-";
	
	
	//Namespaces
	public static final String PROC_NAMESPACE = "http://www.alfresco.com/model/procurement/1.0";
	public static final String CM_NAMESPACE = "http://www.alfresco.org/model/content/1.0";
	public static final String WKF_NAMESPACE_URI = "http://www.alfresco.com/model/procurement/wkf/1.0";
	public static final String BPM_NAMESPACE_URI = "http://www.alfresco.org/model/bpm/1.0";
	
	//Custom Types, aspects and properties
	public static final QName TYPE_PROCUREMENT_REQUEST = QName.createQName(PROC_NAMESPACE, "request");
	public static final QName TYPE_PROCESS_CONTAINER = QName.createQName(PROC_NAMESPACE, "processContainer");
	public static final QName TYPE_PROCESS_CONTAINER_WORKFLOW = QName.createQName(PROC_NAMESPACE, "workflow");
	
	public static final QName TYPE_PROCUREMENT_REQUEST_NUMBER = QName.createQName(PROC_NAMESPACE, "number");
	public static final QName TYPE_PROCUREMENT_REQUEST_ISPROJECT = QName.createQName(PROC_NAMESPACE, "isProject");
	public static final QName TYPE_PROCUREMENT_REQUEST_COSTCENTER = QName.createQName(PROC_NAMESPACE, "costCenterId");
	public static final QName TYPE_PROCUREMENT_REQUEST_DEPARTMENT = QName.createQName(PROC_NAMESPACE, "department");
	public static final QName TYPE_PROCUREMENT_REQUEST_STATUS = QName.createQName(PROC_NAMESPACE, "status");
	public static final QName TYPE_PROCUREMENT_REQUEST_DELIVERUNTIL = QName.createQName(PROC_NAMESPACE, "requiredDeliveryDate");
	public static final QName TYPE_PROCUREMENT_REQUEST_APPROVER = QName.createQName(PROC_NAMESPACE, "approve");
	
	public static final QName TYPE_PROCUREMENT_REQUEST_TITLE = QName.createQName(CM_NAMESPACE, "title");
	public static final QName TYPE_PROCUREMENT_REQUEST_BODY = QName.createQName(CM_NAMESPACE, "description");
	//Default Types
	public static final QName TYPE_NAME = QName.createQName(CM_NAMESPACE, "name");
	public static final QName TYPE_PR_NUMBER = QName.createQName(PROC_NAMESPACE, "number");
	public static final QName TYPE_CONTENT = QName.createQName(CM_NAMESPACE, "content");
	
	//workflow
	public static final String REVIEW_WORKFLOW_NAME =  "jbpm$wf:purchaseRequest";
	
}
