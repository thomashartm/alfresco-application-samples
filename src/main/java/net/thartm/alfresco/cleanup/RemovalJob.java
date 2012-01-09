package net.thartm.alfresco.cleanup;

import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class RemovalJob {

	private long maxCommitTime;
	private long stopCommitTime;
	private long ascendInterval;

	public RemovalJob(WebScriptRequest request){
		this.maxCommitTime = getLongValue(request.getParameter("maxcommit"));
		this.stopCommitTime = getLongValue(request.getParameter("stopcommit"));
		this.ascendInterval = getLongValue(request.getParameter("interval"));
	}

	private Long getLongValue(String value){
		if(value != null && value.length() > 0){
			return Long.parseLong(value);
		}
		
		throw new WebScriptException("Illegal parameter. Expected long value");
	}
	
	/**
	 * @return the maxCommitTime
	 */
	public long getMaxCommitTime() {
		return maxCommitTime;
	}

	/**
	 * @param maxCommitTime
	 *            the maxCommitTime to set
	 */
	public void setMaxCommitTime(long maxCommitTime) {
		this.maxCommitTime = maxCommitTime;
	}

	/**
	 * @return the stopCommitTime
	 */
	public long getStopCommitTime() {
		return stopCommitTime;
	}

	/**
	 * @param stopCommitTime
	 *            the stopCommitTime to set
	 */
	public void setStopCommitTime(long stopCommitTime) {
		this.stopCommitTime = stopCommitTime;
	}

	/**
	 * @param ascendInterval
	 */
	public void setAscendInterval(long ascendInterval) {
		this.ascendInterval = ascendInterval;
	}

	/**
	 * @return
	 */
	public long getAscendInterval() {
		return ascendInterval;
	}

}