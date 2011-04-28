package org.alfresco.extension.procurement.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;

public class ProcurementRequest {

	private String number;
	private Boolean isProject;
	private String costCenterId;
	private String department;
	private String status;
	private Date deliverUntil;
	private String title;
	private String orderBody;

	public ProcurementRequest(NodeRef node, NodeService noderService){
		Map<QName, Serializable> properties = noderService.getProperties(node);
		
		this.number = (String) properties.get(ProcurementModel.TYPE_PROCUREMENT_REQUEST_NUMBER);
		this.isProject = (Boolean) properties.get(ProcurementModel.TYPE_PROCUREMENT_REQUEST_ISPROJECT);
		this.costCenterId = (String) properties.get(ProcurementModel.TYPE_PROCUREMENT_REQUEST_COSTCENTER);
		this.department = (String) properties.get(ProcurementModel.TYPE_PROCUREMENT_REQUEST_DEPARTMENT);
		this.status = (String) properties.get(ProcurementModel.TYPE_PROCUREMENT_REQUEST_STATUS);
		this.deliverUntil = (Date) properties.get(ProcurementModel.TYPE_PROCUREMENT_REQUEST_DELIVERUNTIL);
		this.title = (String) properties.get(ProcurementModel.TYPE_PROCUREMENT_REQUEST_TITLE);
		this.orderBody = (String) properties.get(ProcurementModel.TYPE_PROCUREMENT_REQUEST_BODY);
	}

	public String getNumber() {
		return number != null ? number : "";
	}

	public Boolean getIsProject() {
		return isProject != null ? isProject : false;
	}

	public String getCostCenterId() {
		return costCenterId != null ? costCenterId : "";
	}

	public String getDepartment() {
		return department != null ? department : "";
	}

	public String getStatus() {
		return status != null ? status : "";
	}

	public Date getDeliverUntil() {
		return deliverUntil != null ? deliverUntil : new Date();
	}	
	
	public String getTitle() {
		return title;
	}

	public String getOrderBody() {
		return orderBody;
	}

	public String createTextOutput(){
		StringBuilder sb = new StringBuilder();
		sb.append("Nummer: ").append(getNumber()).append("\n");
		sb.append("Titel: ").append(getTitle()).append("\n");
		sb.append("-----------------------------").append("\n");
		sb.append("Bestellung: ").append("\n").append(getOrderBody()).append("\n");
		sb.append("-----------------------------").append("\n");
		sb.append("Projekt: ").append(getIsProject()).append("\n");
		sb.append("Abteilung: ").append(getDepartment()).append("\n");
		sb.append("Status: ").append(getStatus()).append("\n");
		sb.append("-----------------------------").append("\n");
		sb.append("Delivery until: ").append(getDeliverUntilReadableDate(""));
		
		return sb.toString();
	}

	private String getDeliverUntilReadableDate(String pattern){
		Calendar cal = Calendar.getInstance(Locale.GERMAN);
		cal.setTime(getDeliverUntil());
		
		int year = cal.get( Calendar.YEAR  );
		int month = cal.get( Calendar.MONTH ) + 1;
		int date = cal.get( Calendar.DATE  );
		
		return  date + "." + month + "." + year;
	}
}