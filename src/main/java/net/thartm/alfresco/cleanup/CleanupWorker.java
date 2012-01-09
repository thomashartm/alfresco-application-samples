package net.thartm.alfresco.cleanup;

import org.alfresco.repo.domain.node.NodeDAO;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.transaction.TransactionService;

public class CleanupWorker {

	private static final String THREAD_NAME = "PurgeDeletedNodes-BackgroundThread";
	protected final JobStatus jobStatus;
	private TransactionService transactionService;
	
	/* 
	 * You must NEVER inject the nodeDAO in your code 
	 * But in our case it's necessary to get access to the required purge method. 
	 */
	private NodeDAO nodeDao; 
	
	public CleanupWorker(ServiceRegistry serviceRegistry, NodeDAO nodeDao,  JobStatus jobStatus) {
		this.nodeDao = nodeDao;
		this.jobStatus = jobStatus;
		this.transactionService = serviceRegistry.getTransactionService();
	}
	

	public void cancelCurrentJob(){
		jobStatus.cancel();
	}
	
	public void purgeDeletedNodes(final RemovalJob job) {
		final String execUser = AuthenticationUtil.getSystemUserName();

		Runnable backgroundLogic  = new Runnable() {
			public void run() {
				AuthenticationUtil.runAs(new RunAsWork<Object>() {
					

					public Object doWork() throws Exception {
						try {
					        
							jobStatus.start(job);
							long runnableStartTime = System.currentTimeMillis();
							long maxCommitTimeInMillis = job.getMaxCommitTime();
							
							while(proceed(maxCommitTimeInMillis, runnableStartTime)){
								purgeOldNodesByMaxCommitTime(maxCommitTimeInMillis);
								maxCommitTimeInMillis = maxCommitTimeInMillis + job.getAscendInterval();		
							}
							
					        jobStatus.stop();
						} catch (Throwable t) {                                                                                                                                   
							if (t instanceof Exception) {
								throw (Exception) t;
							} else {
								throw new Exception(t);
							}
						}
						return (null);
					}

					private boolean proceed(long actualMaxCommitTime, long runnableStartTime) {		
						if(!jobStatus.isCanceled()){
							// not canceled so proceed and check if if maxCommitTime is lower then the the runnable's start time
							if(actualMaxCommitTime < runnableStartTime){						
								return actualMaxCommitTime < jobStatus.getStopTimeInMillis();
							}
						}
						// has been canceled manually ... stop purging
						return false;
					}

					private void purgeOldNodesByMaxCommitTime(final long maxCommitTimeMs) {
						RetryingTransactionCallback<Integer> purgeNodesInTxnCallback = new RetryingTransactionCallback<Integer>()
						{
						    public Integer execute() throws Throwable
						    {				        
								return nodeDao.purgeNodes(maxCommitTimeMs);
						    }
						};
						
						Integer deletedNodesCount = transactionService.getRetryingTransactionHelper().doInTransaction(purgeNodesInTxnCallback, false);	
						
						jobStatus.addDeleteOperationCount(deletedNodesCount);
						jobStatus.incrementPurgeIterations();
						jobStatus.setLastHandledCommitTime(maxCommitTimeMs);
					}
					
				}, execUser);
			}
		};
		
		Thread backgroundThread = new Thread(backgroundLogic, THREAD_NAME);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
	}
}