package net.thartm.treeutil.bulk.nodecreation;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.thartm.treeutil.bulk.nodecreation.data.NodePattern;
import net.thartm.treeutil.bulk.util.ContentDataHelper;
import net.thartm.treeutil.bulk.util.FolderHierarchyHelper;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.transaction.TransactionService;

public class AbstractNodeCreator {

	private NodeService nodeService;
	private FileFolderService fileFolderService;
	private TransactionService transactionService;
	private ContentDataHelper contentDataHelper;
	private FolderHierarchyHelper folderHierarchyHelper;
	
	public AbstractNodeCreator() {	
		super();
	}

	public void createNodes(final long number, final NodeRef parent, final byte[] contentData, String fileExt, final BulkJobStatus jobStatus) {
		
		final Set<String> randomFileNames = generateRandomFileNames(number, fileExt);

		//Executes the creation operations on each node in the name package before committing it
		//Avoids single commits for each node. 
        RetryingTransactionCallback<Object> txnWork = new RetryingTransactionCallback<Object>()
        {
            public NodeRef execute() throws Exception
            {
            	
            	for(String name : randomFileNames){
            		final Map<QName, Serializable> properties = createCmContentPropertyMap(name);
            		NodeRef node = fileFolderService.create(parent, name, ContentModel.TYPE_CONTENT).getNodeRef(); 	
            		
            		if(properties.size() > 0) {
            			nodeService.addProperties(node, properties);
            		}
            		
            		contentDataHelper.writeContentData(node, contentData);      		
            		jobStatus.incrementWriteOperations(node);
            	}
            	return null;
            }
        };
      
        //set read only transaction to false - explicitly
        transactionService.getRetryingTransactionHelper().doInTransaction(txnWork, false);	
	}

	public void createNodes(final long number, final NodePattern nodePattern, final NodeRef parent, final BulkJobStatus jobStatus, final Date timestamp) {
		final Set<String> randomFileNames = generateRandomFileNames(number, ".txt");
			
		//Executes the creation operations on each node in the name package before committing it
		//Avoids single commits for each node. 
        RetryingTransactionCallback<Object> txnWork = new RetryingTransactionCallback<Object>()
        {
            public NodeRef execute() throws Exception
            {
            	NodeRef folder = createDateBasedFolderStructure(parent, timestamp);
            	
            	for(String name : randomFileNames){
            		final Map<QName, Serializable> properties = createCmContentPropertyMap(name);
            		NodeRef node = fileFolderService.create(folder, name, ContentModel.TYPE_CONTENT).getNodeRef(); 	
            		
            		if(properties.size() > 0) {
            			nodeService.addProperties(node, properties);
            		}
            		
            		nodeService.setProperty(node, ContentModel.PROP_CONTENT, nodePattern.getContentData());
            		
            		jobStatus.incrementWriteOperations(node);
            	}
				return null;
            }
        };
        
        //set read only transaction to false - explicitly
        transactionService.getRetryingTransactionHelper().doInTransaction(txnWork, false);	    
	}
	
	/**
	 * Creates a folder hierarchy based on the String representation of a reversed date time e.g. 2010-09-01 12:00 leads to targetNode/2010/09/01/12/00/ ....
	 * 
	 * @param target
	 * @param ts
	 * @return
	 */
	public NodeRef createDateBasedFolderStructure(NodeRef target, String[] folderHierarchy){
		
		NodeRef parent = target;
		NodeRef folder = null;
		
		for(String name : folderHierarchy){
			//Check if folder already exists
			folder = fileFolderService.searchSimple(parent, name);
			
			// If we didn't find an existing item, create a new node in the repo. 
			if(folder == null){
				folder = fileFolderService.create(parent, name, ContentModel.TYPE_FOLDER).getNodeRef(); 
			}
			
			parent = folder;
		}
        
		return folder;
	}
	
	/**
	 * Creates a folder hierarchy based on a reversed date time e.g. 2010-09-01 12:00 leads to targetNode/2010/09/01/12/00/ ....
	 * 
	 * @param target
	 * @param dateTime
	 * @return
	 */
	public NodeRef createDateBasedFolderStructure(NodeRef target, Date dateTime){
		String[] hierarchy = folderHierarchyHelper.convertToHierarchy(dateTime);
		return createDateBasedFolderStructure(target, hierarchy);
	}
		
	private Map<QName, Serializable> createCmContentPropertyMap(String name) {
		Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);

		props.put(ContentModel.PROP_NAME, name);
		return props;
	}
	
	public Set<String> generateRandomFileNames(final long number,
			final String fileExt) {
		final Set<String> names = new HashSet<String>();
		
		//Packages a number of names to a set to ensure that all these operations run with in one transaction
		for(int i = 0; i < number; i++){
			UUID generatedHash = UUID.randomUUID();
			names.add(generatedHash.toString() + fileExt);
		}
		return names;
	}

	/**
	 * @return the nodeService
	 */
	public NodeService getNodeService() {
		return nodeService;
	}

	/**
	 * @param nodeService the nodeService to set
	 */
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	/**
	 * @return the fileFolderService
	 */
	public FileFolderService getFileFolderService() {
		return fileFolderService;
	}

	/**
	 * @param fileFolderService the fileFolderService to set
	 */
	public void setFileFolderService(FileFolderService fileFolderService) {
		this.fileFolderService = fileFolderService;
	}

	/**
	 * @return the transactionService
	 */
	public TransactionService getTransactionService() {
		return transactionService;
	}

	/**
	 * @param transactionService the transactionService to set
	 */
	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	/**
	 * @return the contentDataHelper
	 */
	public ContentDataHelper getContentDataHelper() {
		return contentDataHelper;
	}

	/**
	 * @param contentDataHelper the contentDataHelper to set
	 */
	public void setContentDataHelper(ContentDataHelper contentDataHelper) {
		this.contentDataHelper = contentDataHelper;
	}

	public void setFolderHierarchyHelper(FolderHierarchyHelper folderHierarchyHelper) {
		this.folderHierarchyHelper = folderHierarchyHelper;
	}

	public FolderHierarchyHelper getFolderHierarchyHelper() {
		return folderHierarchyHelper;
	}

}