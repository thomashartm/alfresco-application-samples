package net.thartm.alfresco.cleanup;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class JobStatus {

	private long startTimeInMillis;
	private long endTimeInMillis;
	private long stopTimeInMillis;

	private AtomicLong lastHandledCommitTime = new AtomicLong();
	private AtomicBoolean inProgress = new AtomicBoolean();
	private AtomicLong purgeIterations = new AtomicLong();
	private AtomicLong numberOfDeletedNodes = new AtomicLong();
	
	private Throwable lastException;
	private AtomicBoolean canceled = new AtomicBoolean();
	
	public void start(RemovalJob job) {
        if (!inProgress.compareAndSet(false, true))
        {
            throw new RuntimeException("Import already in progress.");
        }
        
        canceled.set(false);
        this.startTimeInMillis = job.getMaxCommitTime();
        this.stopTimeInMillis = job.getStopCommitTime();
        
        if(this.stopTimeInMillis < this.startTimeInMillis)
        {
            throw new RuntimeException("Stop time can not be lower then start time.");
        }

        lastHandledCommitTime.set(startTimeInMillis);
        numberOfDeletedNodes.set(0); 
        purgeIterations.set(0);
        inProgress.set(true);
	}

	
	public void incrementPurgeIterations() {
		purgeIterations.getAndIncrement();
	}

	/**
	 * Can be set by an external process as an indicator for the creation process to stop the bulk import. 
	 */
	protected void cancel(){
        stop();
        canceled.set(true);
	}
	
	public void stop() {
        if (!inProgress.compareAndSet(true, false))
        {
    		this.endTimeInMillis = System.currentTimeMillis();
    		System.out.println("Batch job done.");        
    	}
	}
	
	public void stop(Throwable lastException) {
        stop();
        this.lastException = lastException;
        System.out.println("Batch job terminated with errors.");
	}

	/**
	 * @return the lastException
	 */
	public Throwable getLastException() {
		return lastException;
	}

	/**
	 * @param lastException the lastException to set
	 */
	public void setLastException(Throwable lastException) {
		this.lastException = lastException;
	}

	/**
	 * @return the cancelled
	 */
	public boolean isCanceled() {
		return canceled.get();
	}

	/**
	 * @return the startTimeInMillis
	 */
	public long getStartTimeInMillis() {
		return startTimeInMillis;
	}

	/**
	 * @return the endTimeInMillis
	 */
	public long getEndTimeInMillis() {
		return endTimeInMillis;
	}

	/**
	 * @return the lastHandledCommitTime
	 */
	public AtomicLong getLastHandledCommitTime() {
		return lastHandledCommitTime;
	}

	/**
	 * @return the inProgress
	 */
	public Boolean getInProgress() {
		return inProgress.get();
	}

	/**
	 * @return the purgeIterations
	 */
	public AtomicLong getPurgeIterations() {
		return purgeIterations;
	}

	/**
	 * @return the numberOfDeletedNodes
	 */
	public AtomicLong getNumberOfDeletedNodes() {
		return numberOfDeletedNodes;
	}

	/**
	 * @return the stopTimeInMillis
	 */
	public long getStopTimeInMillis() {
		return stopTimeInMillis;
	}

	/**
	 * @param lastHandledCommitTime the lastHandledCommitTime to set
	 */
	public void setLastHandledCommitTime(Long lastHandledCommitTime) {
		this.lastHandledCommitTime.set(lastHandledCommitTime);
	}
	
	public void addDeleteOperationCount(long additionalDeleteOperations){
		numberOfDeletedNodes.addAndGet(additionalDeleteOperations);
	}
}