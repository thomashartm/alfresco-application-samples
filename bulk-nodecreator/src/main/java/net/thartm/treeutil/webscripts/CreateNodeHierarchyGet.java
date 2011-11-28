package net.thartm.treeutil.webscripts;

import java.util.HashMap;
import java.util.Map;

import net.thartm.treeutil.bulk.BulkNodeCreator;
import net.thartm.treeutil.bulk.job.SimpleNodeCreationJob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class CreateNodeHierarchyGet extends DeclarativeWebScript{

	private final static Log log = LogFactory.getLog(CreateNodeHierarchyGet.class);
	private BulkNodeCreator nodeCreator;
	
	public CreateNodeHierarchyGet(BulkNodeCreator nodeCreator){
		this.nodeCreator = nodeCreator;
	}
	
	/**
	 * Request
	 * 	- baseNode
	 *  - number
	 *  - datagenerator 
	 *  -> creates:
	 *  Type (model type)
	 *    - sampleContentProvider
	 *    - sampleMetadataProvider 
	 */
	@Override
	protected Map<String, Object> executeImpl(final WebScriptRequest request,
			final Status status, final Cache cache) {
		Map<String, Object> results = new HashMap<String, Object>();
		
		log.info("Starting NodeHierarchyCreation Job");
		SimpleNodeCreationJob job = new SimpleNodeCreationJob(request);
		

		log.info("Executing node creation job for " + job.getNumberOfDocs() + " nodes.");
		
		try {
			nodeCreator.execute(job);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		if(job.getParentRef() == null || job.getParentRef().getStoreRef() != null || job.getTemplateRef() != null){
			results.put("p", job.getParentRef().getStoreRef().toString());
			results.put("t", job.getTemplateRef().toString());
		}else{
			results.put("p", "No storeref found");
			results.put("t", "No template ref yet");
		}
		
		results.put("i", String.valueOf(job.getNumberOfDocs()));
        
		return results;
	}
}
