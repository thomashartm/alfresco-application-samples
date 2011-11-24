package net.thartm.treeutil.webscripts;

import java.util.HashMap;
import java.util.Map;

import net.thartm.treeutil.bulk.NodeCreator;
import net.thartm.treeutil.bulk.nodecreation.BulkJobStatus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class BulkNodeCreationStatusWebscript extends DeclarativeWebScript{

	
	private final static Log log = LogFactory.getLog(BulkNodeCreationStatusWebscript.class);	
	protected final BulkJobStatus jobStatus;
	private NodeCreator executor;
	private static final String STOP_JOB = "stop";
	
	public BulkNodeCreationStatusWebscript(BulkJobStatus jobStatus){
		this.jobStatus = jobStatus;
	}
	
	@Override
	protected Map<String, Object> executeImpl(final WebScriptRequest request,
			final Status status, final Cache cache) {
		Map<String, Object> results = new HashMap<String, Object>();

		log.debug("Retrieving job execution status");

		results.put("start", jobStatus.getStartDate().toString());
		//results.put("end", jobStatus.getEndDate().toString());
		results.put("comitted", String.valueOf(jobStatus.getNumberOfNodesCreated()));

		if(cancelJob(request)){
			executor.cancelCurrentJob();
		}
		
		results.put("running", getExecutingStatusMessage());
		
		return results;
	}
	
	private String getExecutingStatusMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(jobStatus.getInProgress().get()));
		sb.append(" ");
		
		if(jobStatus.getEndDate() != null){
			sb.append("Job done.");
		}
		
		if(jobStatus.isCancelled()) {
			sb.append("Job cancelled.");
		}
		
		return sb.toString();
	}

	private boolean cancelJob(final WebScriptRequest request){
		String stop = request.getParameter(STOP_JOB);
		if(stop != null){
			if(stop.equalsIgnoreCase("true") || stop.equalsIgnoreCase("false")){
				return Boolean.valueOf(stop);
			}
		}
		return false;
	}
	
}
