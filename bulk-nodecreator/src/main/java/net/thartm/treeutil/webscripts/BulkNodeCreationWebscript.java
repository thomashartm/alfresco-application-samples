package net.thartm.treeutil.webscripts;

import java.util.HashMap;
import java.util.Map;

import net.thartm.treeutil.bulk.NodeCreator;
import net.thartm.treeutil.bulk.job.NodeCreationJob;

import org.alfresco.service.ServiceRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class BulkNodeCreationWebscript extends DeclarativeWebScript{

	private final static Log log = LogFactory.getLog(BulkNodeCreationWebscript.class);
	private NodeCreator nodeCreator;
	
	public BulkNodeCreationWebscript(ServiceRegistry serviceRegistry, NodeCreator nodeCreator){
		this.nodeCreator = nodeCreator;
	}
	
	@Override
	protected Map<String, Object> executeImpl(final WebScriptRequest request,
			final Status status, final Cache cache) {
		Map<String, Object> results = new HashMap<String, Object>();

		log.debug("Creating batch job definition");
		
		NodeCreationJob batchJob = new NodeCreationJob(request);
		
		log.info("Starting node creation job.");
		
		try {
			nodeCreator.execute(batchJob);
		} catch (Throwable e) {
			log.error("Batch job terminated", e);
		}
		
		results.put("p", batchJob.getParentRef().getStoreRef().toString());
		results.put("t", batchJob.getTemplateRef().toString());
		results.put("i", String.valueOf(batchJob.getNumberOfIterations()));
        
		return results;
	}
	

}
