package net.thartm.zipdownload.util;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;

public class NodeRefHelper {

	private FileFolderService fileFolderService;

	public NodeRefHelper(ServiceRegistry serviceRegistry) {
		this.fileFolderService = serviceRegistry.getFileFolderService();
	}

	/**
	 * Tries to create a NodeRef with the given reference argument.
	 * 
	 * @param reference An node references String representation
	 * @return May return null if it's not possible to create NodeRef
	 */
	public NodeRef getNodeRef(String reference) {
		if (NodeRef.isNodeRef(reference)) {
			return new NodeRef(reference);
		}else{	
			String[] tokens = reference.split("\\/");
			if (tokens.length > 0) {
				String ref = tokens[0] + "://" + tokens[1] + "/" + tokens[2];
				return new NodeRef(ref);
			}
		}
		throw new InvalidArgumentException("The provided argument " + reference + " was expected to be a valid node reference.");
	}

	/**
	 * Retrieves a nodes name.
	 * 
	 * @param reference
	 * @return
	 */
	public String getNodeName(String reference) {
		NodeRef folder = getNodeRef(reference);
		FileInfo node = fileFolderService.getFileInfo(folder);
		return node.getName();
	}
}