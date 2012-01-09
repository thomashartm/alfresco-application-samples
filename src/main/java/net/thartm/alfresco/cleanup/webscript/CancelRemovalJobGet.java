package net.thartm.alfresco.cleanup.webscript;

import java.util.HashMap;
import java.util.Map;

import net.thartm.alfresco.cleanup.CleanupWorker;
import net.thartm.alfresco.cleanup.JobStatus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class CancelRemovalJobGet extends DeclarativeWebScript {

	private final static Log log = LogFactory
			.getLog(PurgeDeletedNodesGet.class);
	private CleanupWorker cleanupWorker;
	private JobStatus jobStatus;

	public CancelRemovalJobGet(CleanupWorker removalWorker, JobStatus jobStatus) {
		this.cleanupWorker = removalWorker;
		this.jobStatus = jobStatus;
	}

	@Override
	protected Map<String, Object> executeImpl(final WebScriptRequest request,
			final Status status, final Cache cache) {
		
		Map<String, Object> results = new HashMap<String, Object>();
		
		if(jobStatus.getInProgress()){
			log.info("Cancel node removal");
			this.cleanupWorker.cancelCurrentJob();
			results.put("jobstatus", "Canceled at " + jobStatus.getStopTimeInMillis());
		}else{
			String jobExecutionStatus = jobStatus.getStartTimeInMillis() > 0 ? "Running... " : "Not yet started.";
			results.put("jobstatus", jobExecutionStatus);
		}
		
		return results;
	}
}
