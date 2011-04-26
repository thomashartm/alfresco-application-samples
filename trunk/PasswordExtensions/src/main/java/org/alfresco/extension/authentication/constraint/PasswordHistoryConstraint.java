package org.alfresco.extension.authentication.constraint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.extension.authentication.aop.Credentials;
import org.alfresco.extension.authentication.aop.ExtendedAuthorizationModel;
import org.alfresco.repo.security.authentication.AuthenticationException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.namespace.QName;

public class PasswordHistoryConstraint implements CredentialConstraint{

	private NodeService nodeService;
	private PersonService personService;
	private int maxHistoryEntries;
	
	public void process(Credentials credentials){
		NodeRef person = personService.getPerson(credentials.getUserName());

		if (hasAuthorizationExtensionAspect(person)) {
			compareWithHistoryAndUpdate(person, credentials.getNewPasswordHash());
		} else {
			nodeService.addAspect(person, 
					ExtendedAuthorizationModel.ASPECT_PASSWORD_EXTENSION,
					createPropertiesMap(credentials.getOldPasswordHash(), 
					credentials.getNewPasswordHash()));
		}
	}
	
	private void compareWithHistoryAndUpdate(NodeRef person, String newPw) {
		ArrayList<String> passwordHistory = retrievePasswordHistory(person);
		if (passwordHistory.contains(newPw)) {
			throw new AuthenticationException("The password does not match the validation constraints.");
		}
	
		passwordHistory.add(newPw);
		if(passwordHistory.size() > maxHistoryEntries){
			passwordHistory.remove(0);
		}
		nodeService.setProperty(person, ExtendedAuthorizationModel.PROPERTY_PASSWORD_HISTORY, passwordHistory);
	}
	
	private Map<QName, Serializable> createPropertiesMap(String... passwords) {
		ArrayList<String> passwordHistory = new ArrayList<String>();
		passwordHistory.addAll(Arrays.asList(passwords));

		Map<QName, Serializable> properties = new HashMap<QName, Serializable>();
		properties.put(ExtendedAuthorizationModel.PROPERTY_PASSWORD_HISTORY, passwordHistory);
		return properties;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<String> retrievePasswordHistory(NodeRef nodeRef) {
		Serializable passwordHistoryProperty = nodeService.getProperty(nodeRef,
				ExtendedAuthorizationModel.PROPERTY_PASSWORD_HISTORY);

		ArrayList<String> passwordHistory = (ArrayList<String>) passwordHistoryProperty;
		return passwordHistory;
	}

	private boolean hasAuthorizationExtensionAspect(NodeRef nodeRef) {
		return nodeService.hasAspect(nodeRef, ExtendedAuthorizationModel.ASPECT_PASSWORD_EXTENSION);
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public void setMaxHistoryEntries(int maxHistoryEntries) {
		this.maxHistoryEntries = maxHistoryEntries;
	}
}
