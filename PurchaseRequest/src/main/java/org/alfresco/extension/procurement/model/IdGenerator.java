package org.alfresco.extension.procurement.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;

public class IdGenerator {

	private static final Integer[] TIME_BASED_KEY_CREATION_STRUCTURE = new Integer[]{
		Calendar.YEAR,Calendar.MONTH,Calendar.DATE,Calendar.HOUR_OF_DAY, Calendar.MINUTE,Calendar.SECOND, Calendar.MILLISECOND
	};
	private NodeService nodeService;
	
	
	
	public IdGenerator(ServiceRegistry serviceRegistry){
		this.nodeService = serviceRegistry.getNodeService();
	}

	/**
	 * Add the id generation to an external bean caching the synchronized id
	 * state to avoid extensive queries and list operations and to keep a unique
	 * id value;
	 * 
	 * @param childAssocRef
	 * @param request
	 */
	public String generateProcessId(ChildAssociationRef childAssocRef, NodeRef request) {
		Date creationDate = (Date) nodeService.getProperty(request, ContentModel.PROP_CREATED);		
		Calendar cal = Calendar.getInstance(Locale.GERMAN);
		cal.setTime(creationDate);

		return "REQ-" + createFormattedDateString(cal,"-",TIME_BASED_KEY_CREATION_STRUCTURE);
	}

	/**
	 * Creates a date based string. Uses the Integer constants for certain aspects of a datetime.
	 * 
	 * @param cal A {@link Calendar} object.
	 * @param separator A separator to seprarate the retrieved date values like e.g. a dot or a dash.
	 * @param dateTokens An array of Calendar constants like e.g. Calendar.YEAR ... 
	 * @return A formatted datetime string.
	 */
	private String createFormattedDateString(Calendar cal, String separator, Integer... dateTokens){
		StringBuilder sb = new StringBuilder();
		int lastIndex = dateTokens.length-1;
		
		for(int token : dateTokens){
			sb.append(cal.get(token));	
			if(token != dateTokens[lastIndex]){
				sb.append("-");
			}
		}
		return sb.toString();
	}
}
