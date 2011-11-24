package net.thartm.treeutil.bulk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.thartm.treeutil.bulk.nodecreation.BulkJobStatus;
import net.thartm.treeutil.bulk.util.Timestamp;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.transaction.TransactionService;

public class NodeCreationHelper {
	
	private FileFolderService fileFolderService;
	private NodeService nodeService;
	private ContentService contentService;
	private TransactionService transactionService;
	
	public NodeCreationHelper(TransactionService transactionService, ContentService contentService, FileFolderService fileFolderService, NodeService nodeService){
		this.fileFolderService = fileFolderService;
		this.contentService = contentService;
		this.nodeService = nodeService;
		this.transactionService = transactionService;
	}
	

	public byte[] getDataFromContentTemplate(NodeRef contentTemplateNode) throws IOException{
		ContentReader reader = contentService.getReader(contentTemplateNode, ContentModel.PROP_CONTENT);
		
		InputStream originalInputStream = reader.getContentInputStream();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		final int BUF_SIZE = 1 << 8; // 1KiB buffer
		byte[] buffer = new byte[BUF_SIZE];
		
		int bytesRead = -1;
		while ((bytesRead = originalInputStream.read(buffer)) > -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		
		originalInputStream.close();
		return outputStream.toByteArray();
	}

	/**
	 * Creates a folder hierarchy based on a reversed date time e.g. 2010-09-01 12:00 leads to targetNode/2010/09/01/12/00/ ....
	 * 
	 * @param target
	 * @param ts
	 * @return
	 */
	public NodeRef createDateBasedFolderStructure(NodeRef target, Timestamp ts){
		String[] folderHierarchy = ts.getHierarchy();
		
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
	
	public void createNodes(final long number, final NodeRef parent, final byte[] content, final String fileExt, final BulkJobStatus jobStatus) {
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
            		
            		if(properties.size() > 0) nodeService.addProperties(node, properties);
            		
            		ContentWriter writer = contentService.getWriter(node, ContentModel.PROP_CONTENT, true);
            		writer.putContent(new ByteArrayInputStream(content));
            		
            		jobStatus.incrementWriteOperations(node);
            	}
            	return null;
            }
        };
      
        //set read only transaction to false - explicitly
        transactionService.getRetryingTransactionHelper().doInTransaction(txnWork, false);	
	}

	private Map<QName, Serializable> createCmContentPropertyMap(String name) {
		Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);

		props.put(ContentModel.PROP_NAME, name);
		return props;
	}
	
	private Set<String> generateRandomFileNames(final long number,
			final String fileExt) {
		final Set<String> names = new HashSet<String>();
		
		//Packages a number of names to a set to ensure that all these operations run with in one transaction
		for(int i = 0; i < number; i++){
			UUID generatedHash = UUID.randomUUID();
			names.add(generatedHash.toString() + fileExt);
		}
		return names;
	}
}
