package net.thartm.treeutil.bulk;

import net.thartm.treeutil.bulk.job.SimpleNodeCreationJob;

public interface BulkNodeCreator {

	/**
	 * Executes the node creation job in the background and updates the batch job's status.
	 * Reused Peter Monk's approach to run background code.
	 * 
	 * @param job
	 * @throws Throwable
	 */
	public abstract void execute(final SimpleNodeCreationJob job) throws Throwable;

	public abstract void cancelCurrentJob();

}