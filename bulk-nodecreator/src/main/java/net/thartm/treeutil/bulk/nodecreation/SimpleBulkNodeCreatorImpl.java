package net.thartm.treeutil.bulk.nodecreation;

import net.thartm.treeutil.bulk.BulkNodeCreator;
import net.thartm.treeutil.bulk.job.SimpleNodeCreationJob;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Executes a simple single threaded node creation job and creates the defined number of documents based on the 
 * provided piece of sample content.
 * Just supports cm.content at the moment.
 * 
 * TODO Support different types
 * TODO Support linked content to avoid duplication and decrease the consumed amount of data on test machines
 * TODO Check status using a webscript
 * TODO Create configuration UI
 * 
 * @author Thomas Hartmann <thomas.hartmann@alfresco.com>
 *
 */
public class SimpleBulkNodeCreatorImpl extends AbstractNodeCreator implements BulkNodeCreator {

	private final static Log log = LogFactory.getLog(SimpleBulkNodeCreatorImpl.class);
	private static final String THREAD_NAME = "BatchNodeCreator-BackgroundThread";
	private static final String TXT_EXT = ".txt";
	protected final BulkJobStatus jobStatus;

	public SimpleBulkNodeCreatorImpl(final BulkJobStatus jobStatus) {
		super();
		this.jobStatus = jobStatus;
	}

	/* (non-Javadoc)
	 * @see net.thartm.treeutil.bulk.NodeCreator#execute(net.thartm.treeutil.bulk.job.NodeCreationJob)
	 */
	@Override
	public void execute(final SimpleNodeCreationJob job) throws Throwable{
		Runnable backgroundLogic = null;
		Thread       backgroundThread = null;
		
		final String currentUser = AuthenticationUtil.getSystemUserName();
		
		final long requiredDocsCount = job.getNumberOfDocs();	
		final long workPackageSize = requiredDocsCount < job.getBatchSize() ? requiredDocsCount : job.getBatchSize();
		final byte[] content = getContentDataHelper().copyContentFromExistingNode(job.getTemplateRef());
		
		backgroundLogic = new Runnable() {
			public void run() {
				AuthenticationUtil.runAs(new RunAsWork<Object>() {
					public Object doWork() throws Exception {
						try {
									
							jobStatus.start();
							long remainingDocsCount = 0;
							//Executes the import and avoid single transaction commits per node by comitting packages
							while(proceedWithBulkJob(requiredDocsCount)){
								
								remainingDocsCount = requiredDocsCount - jobStatus.getNumberOfNodesCreated();
								if(remainingDocsCount < workPackageSize){
									createNodes(remainingDocsCount, job.getParentRef(), content, TXT_EXT, jobStatus);
								}else{
									createNodes(workPackageSize, job.getParentRef(), content, TXT_EXT, jobStatus);
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
						return (jobStatus.getNumberOfNodesCreated() < requiredIterations) && (!jobStatus.isCanceled());
					}
				}, currentUser);
			}
		};
		
        backgroundThread = new Thread(backgroundLogic, THREAD_NAME);
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