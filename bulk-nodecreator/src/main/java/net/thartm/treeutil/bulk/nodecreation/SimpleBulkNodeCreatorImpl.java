package net.thartm.treeutil.bulk.nodecreation;

import net.thartm.treeutil.bulk.NodeCreationHelper;
import net.thartm.treeutil.bulk.NodeCreator;
import net.thartm.treeutil.bulk.job.NodeCreationJob;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO Check status using a webscript
 * TODO Create configuration UI
 * 
 * @author thomas
 *
 */
public class SimpleBulkNodeCreatorImpl implements NodeCreator {

	private final static Log log = LogFactory.getLog(SimpleBulkNodeCreatorImpl.class);
	private static final String TXT_EXT = ".txt";
	protected final BulkJobStatus jobStatus;
	private NodeCreationHelper nodeCreationHelper;

	public SimpleBulkNodeCreatorImpl(final NodeCreationHelper nodeCreationHelper, final BulkJobStatus jobStatus) {
		this.nodeCreationHelper = nodeCreationHelper;
		this.jobStatus = jobStatus;	
	}

	/* (non-Javadoc)
	 * @see net.thartm.treeutil.bulk.NodeCreator#execute(net.thartm.treeutil.bulk.job.NodeCreationJob)
	 */
	@Override
	public void execute(final NodeCreationJob job) throws Throwable{
		Runnable backgroundLogic = null;
		Thread       backgroundThread = null;
		
		final String currentUser = AuthenticationUtil.getSystemUserName();
		final byte[] content = nodeCreationHelper.getDataFromContentTemplate(job.getTemplateRef());
		final long requiredIterations = job.getNumberOfIterations();	
		final long workPackageSize = requiredIterations < job.getBatchSize() ? requiredIterations : job.getBatchSize();
		
		backgroundLogic = new Runnable() {
			public void run() {
				AuthenticationUtil.runAs(new RunAsWork<Object>() {
					public Object doWork() throws Exception {
						try {
									
							jobStatus.start();
							long remainingIterations = 0;
							
							//Executes the import and avoid single transaction commits per node by comitting packages
							while(proceedWithBulkJob(requiredIterations)){
								
								remainingIterations = requiredIterations - jobStatus.getNumberOfNodesCreated();
								if(remainingIterations < workPackageSize){
									nodeCreationHelper.createNodes(remainingIterations, job.getParentRef(), content, TXT_EXT, jobStatus);
								}else{
									nodeCreationHelper.createNodes(workPackageSize, job.getParentRef(), content, TXT_EXT, jobStatus);
								}
							}
							jobStatus.stop();
							
                        } catch (Throwable t){
                        	log.error("Node Builder failed.", t);
                        	jobStatus.stop(t);
                        	
                            if (t instanceof Exception)
                            {
                                throw (Exception) t;
                            }
                            else
                            {
                                throw new Exception(t);
                            }
                        }
						return (null);
					}

					private boolean proceedWithBulkJob(final long requiredIterations) {
						return (jobStatus.getNumberOfNodesCreated() < requiredIterations) && (!jobStatus.isCancelled());
					}
				}, currentUser);
			}
		};
		
        backgroundThread = new Thread(backgroundLogic, "BatchNodeCreator-BackgroundThread");
        backgroundThread.setDaemon(true);
        backgroundThread.start();

	}
	
	/* (non-Javadoc)
	 * @see net.thartm.treeutil.bulk.NodeCreator#cancelCurrentJob()
	 */
	@Override
	public void cancelCurrentJob(){
		jobStatus.cancel();
	}
}
