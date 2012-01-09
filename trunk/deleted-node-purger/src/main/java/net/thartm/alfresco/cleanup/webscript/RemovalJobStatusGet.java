package net.thartm.alfresco.cleanup.webscript;

import java.util.HashMap;
import java.util.Map;

import net.thartm.alfresco.cleanup.JobStatus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class RemovalJobStatusGet extends DeclarativeWebScript{

	private final static Log log = LogFactory.getLog(RemovalJobStatusGet.class);
	private JobStatus jobStatus;
	
	public RemovalJobStatusGet(JobStatus jobStatus){
		this.jobStatus = jobStatus;
	}
	
	@Override
	protected Map<String, Object> executeImpl(final WebScriptRequest request,
			final Status status, final Cache cache) {
		Map<String, Object> results = new HashMap<String, Object>();
		
		log.info("Shows job status");
		
		String progress = jobStatus.getInProgress() ? "Active" : "Inactive";
		
		results.put("jobstatus",progress);
		results.put("start",jobStatus.getStartTimeInMillis());
		results.put("lastCommit",jobStatus.getLastHandledCommitTime());
		results.put("stop",jobStatus.getStopTimeInMillis());
		results.put("deletedNodes",jobStatus.getNumberOfDeletedNodes());
		results.put("iterations",jobStatus.getPurgeIterations().get());
		
		return results;
	}

}
