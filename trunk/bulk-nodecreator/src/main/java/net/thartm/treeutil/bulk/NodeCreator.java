package net.thartm.treeutil.bulk;

import net.thartm.treeutil.bulk.job.NodeCreationJob;

public interface NodeCreator {

	/**
	 * Executes the node creation job in the background and updates the batch job's status.
	 * Reused Peter Monk's approach to run background code.
	 * 
	 * @param job
	 * @throws Throwable
	 */
	public abstract void execute(final NodeCreationJob job) throws Throwable;

	public abstract void cancelCurrentJob();

}