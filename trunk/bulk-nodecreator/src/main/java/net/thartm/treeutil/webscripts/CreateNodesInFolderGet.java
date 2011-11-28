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

public class CreateNodesInFolderGet extends DeclarativeWebScript{

	private final static Log log = LogFactory.getLog(CreateNodesInFolderGet.class);
	private BulkNodeCreator nodeCreator;
	
	public CreateNodesInFolderGet(BulkNodeCreator nodeCreator){
		this.nodeCreator = nodeCreator;
	}
	
	@Override
	protected Map<String, Object> executeImpl(final WebScriptRequest request,
			final Status status, final Cache cache) {
		Map<String, Object> results = new HashMap<String, Object>();

		log.debug("Creating batch job definition");
		
		SimpleNodeCreationJob batchJob = new SimpleNodeCreationJob(request);
		
		log.info("Starting node creation job.");
		
		try {
			nodeCreator.execute(batchJob);
		} catch (Throwable e) {
			log.error("Batch job terminated", e);
		}
		
		results.put("p", batchJob.getParentRef().getStoreRef().toString());
		results.put("t", batchJob.getTemplateRef().toString());
		results.put("i", String.valueOf(batchJob.getNumberOfDocs()));
        
		return results;
	}
	

}
