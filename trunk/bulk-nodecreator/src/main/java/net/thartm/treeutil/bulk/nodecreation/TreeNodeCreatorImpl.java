package net.thartm.treeutil.bulk.nodecreation;

import java.util.Calendar;
import java.util.Date;

import net.thartm.treeutil.bulk.BulkNodeCreator;
import net.thartm.treeutil.bulk.job.SimpleNodeCreationJob;
import net.thartm.treeutil.bulk.nodecreation.data.NodePattern;
import net.thartm.treeutil.bulk.util.ContentDataHelper;

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
public class TreeNodeCreatorImpl extends AbstractNodeCreator implements BulkNodeCreator {

	private final static Log log = LogFactory.getLog(TreeNodeCreatorImpl.class);
	protected final BulkJobStatus jobStatus;
	private ContentDataHelper contentDataHelper;

	public TreeNodeCreatorImpl(final BulkJobStatus jobStatus, final ContentDataHelper contentDataHelper) {
		this.jobStatus = jobStatus;	
		this.contentDataHelper = contentDataHelper;
	}
	

	/* (non-Javadoc)
	 * @see net.thartm.treeutil.bulk.NodeCreator#execute(net.thartm.treeutil.bulk.job.NodeCreationJob)
	 */
	@Override
	public void execute(final SimpleNodeCreationJob job) throws Throwable{
		Runnable backgroundLogic = null;
		Thread       backgroundThread = null;
		
		final String currentUser = AuthenticationUtil.getSystemUserName();
		final NodePattern nodePattern = contentDataHelper.transformToNodePattern(job.getTemplateRef());
		final long totalNumberOfDocuments = job.getNumberOfDocs();	
		final long workPackageSize = calculateWorkPackageSize(job);
		
		backgroundLogic = new Runnable() {
			public void run() {
				AuthenticationUtil.runAs(new RunAsWork<Object>() {
					public Object doWork() throws Exception {
						try {
									
							jobStatus.start();
							long remainingDocs = 0;
							
							
							Date timestamp = getFreshTimestamp();
							//Executes the import and avoid single transaction commits per node by comitting batches
							while(proceedWithBulkJob(totalNumberOfDocuments)){
								
								//create the parent structure based on an inverted date
								
								remainingDocs = totalNumberOfDocuments - jobStatus.getNumberOfNodesCreated();
								
								//now let's create a batch of target nodes
								if(remainingDocs < workPackageSize){
									createNodes(remainingDocs, nodePattern, job.getParentRef(), jobStatus, timestamp);
								}else{
									createNodes(workPackageSize, nodePattern, job.getParentRef(), jobStatus, timestamp);
								}
								
								timestamp = substractMinute(timestamp);
							}
							jobStatus.stop();
							
                        } catch (Throwable t){
                        	log.error("Failed to create nodes.", t);
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
					
					private boolean proceedWithBulkJob(final long totalNumberOfDocuments) {
						return (jobStatus.getNumberOfNodesCreated() < totalNumberOfDocuments) && (!jobStatus.isCanceled());
					}
				}, currentUser);
			}
		};
		
        backgroundThread = new Thread(backgroundLogic, "BatchNodeCreator-BackgroundThread");
        backgroundThread.setDaemon(true);
        backgroundThread.start();

	}
	
	private Date getFreshTimestamp(){
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	private Date substractMinute(Date baseTimestamp){
		Calendar cal = Calendar.getInstance();
		long reCalculatedTimestamp = baseTimestamp.getTime() - (60*1000);
		cal.setTimeInMillis(reCalculatedTimestamp);
		
		return cal.getTime();
	}
	
	private long calculateWorkPackageSize(final SimpleNodeCreationJob job) {
		return job.getNumberOfDocs() < job.getBatchSize() ? job.getNumberOfDocs() : job.getBatchSize();
	}	
	
	/* (non-Javadoc)
	 * @see net.thartm.treeutil.bulk.NodeCreator#cancelCurrentJob()
	 */
	@Override
	public void cancelCurrentJob(){
		jobStatus.cancel();
	}
}
