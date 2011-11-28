package net.thartm.treeutil.bulk.nodecreation;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BulkJobStatus {

	private final static Log log = LogFactory.getLog(BulkJobStatus.class);
	private Date startDate = null;
	private Date endDate;
	private AtomicBoolean inProgress = new AtomicBoolean();
	private AtomicLong numberOfNodesCreated = new AtomicLong();
	private Throwable lastException;
	private AtomicBoolean cancelled = new AtomicBoolean();
	
	public void start() {
        if (!inProgress.compareAndSet(false, true))
        {
            throw new RuntimeException("Import already in progress.");
        }
        cancelled.set(false);
        this.startDate = new Date();
        numberOfNodesCreated.set(0); 
	}

	public void incrementWriteOperations(NodeRef node) {
        log.info("Created node " + node.getId());
		numberOfNodesCreated.getAndIncrement();
	}
	
	/**
	 * Can be set by an external process as an indicator for the creation process to stop the bulk import. 
	 */
	protected void cancel(){
        if (!inProgress.compareAndSet(true, false))
        {
            throw new RuntimeException("Import not in progress.");
        }
        cancelled.set(false);
	}
	
	public void stop() {
        if (!inProgress.compareAndSet(true, false))
        {
            throw new RuntimeException("Import not in progress.");
        }
        
		this.endDate = new Date();
		System.out.println("Batch job done.");
	}
	
	public void stop(Throwable lastException) {
        stop();
        this.lastException = lastException;
        System.out.println("Batch job terminated with errors.");
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return the inProgress
	 */
	public AtomicBoolean getInProgress() {
		return inProgress;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return the numberOfNodesCreated
	 */
	public long getNumberOfNodesCreated() {
		return numberOfNodesCreated.get();
	}

	public Throwable getLastException() {
		return lastException;
	}

	/**
	 * @return the canceled
	 */
	public Boolean isCanceled() {
		return cancelled.get();
	}
}
