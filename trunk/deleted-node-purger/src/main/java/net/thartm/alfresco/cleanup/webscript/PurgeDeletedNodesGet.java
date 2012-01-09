package net.thartm.alfresco.cleanup.webscript;

import java.util.HashMap;
import java.util.Map;

import net.thartm.alfresco.cleanup.CleanupWorker;
import net.thartm.alfresco.cleanup.JobStatus;
import net.thartm.alfresco.cleanup.RemovalJob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class PurgeDeletedNodesGet extends DeclarativeWebScript{

	private final static Log log = LogFactory.getLog(PurgeDeletedNodesGet.class);
	private CleanupWorker cleanupWorker;
	private JobStatus jobStatus;

	public PurgeDeletedNodesGet(CleanupWorker removalWorker, JobStatus jobStatus){
		this.cleanupWorker = removalWorker;
		this.jobStatus = jobStatus;
	}
	
	@Override
	protected Map<String, Object> executeImpl(final WebScriptRequest request,
			final Status status, final Cache cache) {
		
		Map<String, Object> results = new HashMap<String, Object>();		
		StringBuilder sb = new StringBuilder();
		
		if(jobStatus.getInProgress()){
			sb.append("Job is already in Progress. See status monitor for execution details.");
		}else{	
			log.info("Starting old node removal job");
			
			RemovalJob job = new RemovalJob(request);
			log.info("Executing node removal job for maxCommitTime " + job.getMaxCommitTime() + " with ascend interval: " + job.getAscendInterval());
			
			try {
				cleanupWorker.purgeDeletedNodes(job);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sb.append("Start : " + job.getMaxCommitTime()).append(" | ");
			sb.append("Interval : " + job.getAscendInterval());
		}
		
		results.put("message", sb.toString());
		return results;
	}
}