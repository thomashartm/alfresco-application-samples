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

public class BulkNodeHierarchyCreationWebScript extends DeclarativeWebScript{

	private final static Log log = LogFactory.getLog(BulkNodeCreationWebscript.class);
	private NodeCreator nodeCreator;
	
	public BulkNodeHierarchyCreationWebScript(ServiceRegistry serviceRegistry, 	NodeCreator nodeCreator){
		this.nodeCreator = nodeCreator;
	}
	
	@Override
	protected Map<String, Object> executeImpl(final WebScriptRequest request,
			final Status status, final Cache cache) {
		Map<String, Object> results = new HashMap<String, Object>();
		
		log.info("Creating node creation job.");
		NodeCreationJob job = new NodeCreationJob(request);
		
		log.info("Executing node creation job for " + job.getNumberOfIterations() + " nodes.");
		
		try {
			nodeCreator.execute(job);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;
	}
}
