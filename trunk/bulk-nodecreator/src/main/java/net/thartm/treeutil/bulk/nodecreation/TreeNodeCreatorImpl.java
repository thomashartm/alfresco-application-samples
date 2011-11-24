package net.thartm.treeutil.bulk.nodecreation;

import java.util.Calendar;
import java.util.Date;

import net.thartm.treeutil.bulk.NodeCreationHelper;
import net.thartm.treeutil.bulk.NodeCreator;
import net.thartm.treeutil.bulk.job.NodeCreationJob;
import net.thartm.treeutil.bulk.util.DateUtil;
import net.thartm.treeutil.bulk.util.Timestamp;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO Check status using a webscript
 * TODO Create configuration UI
 * 
 * @author thomas
 *
 */
public class TreeNodeCreatorImpl implements NodeCreator {

	private final static Log log = LogFactory.getLog(TreeNodeCreatorImpl.class);
	private static final String TXT_EXT = ".txt";
	protected final BulkJobStatus jobStatus;
	private NodeCreationHelper nodeCreationHelper;
	private int year = 2001;
	private DateUtil dateUtil;

	public TreeNodeCreatorImpl(final NodeCreationHelper nodeCreationHelper, final BulkJobStatus jobStatus, final DateUtil dateUtil) {
		this.nodeCreationHelper = nodeCreationHelper;
		this.jobStatus = jobStatus;	
		this.dateUtil = dateUtil;
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
		
		//TODO not so pretty
		final Date baseDate = calculateBaseDate();
		
		
		backgroundLogic = new Runnable() {
			public void run() {
				AuthenticationUtil.runAs(new RunAsWork<Object>() {
					public Object doWork() throws Exception {
						try {
									
							jobStatus.start();
							long remainingIterations = 0;

							//Executes the import and avoid single transaction commits per node by comitting batches
							while(proceedWithBulkJob(requiredIterations)){
								
								Timestamp ts = new Timestamp(dateUtil.getRandomDateTime(baseDate));
								NodeRef parent = nodeCreationHelper.createDateBasedFolderStructure(job.getParentRef(), ts);
								remainingIterations = requiredIterations - jobStatus.getNumberOfNodesCreated();
								
								if(remainingIterations < workPackageSize){
									nodeCreationHelper.createNodes(remainingIterations, parent, content, TXT_EXT, jobStatus);
								}else{
									nodeCreationHelper.createNodes(workPackageSize, parent, content, TXT_EXT, jobStatus);
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
	
	private Date calculateBaseDate(){
		Calendar cal = Calendar.getInstance();
		cal.set(year, 01, 01, 0, 0);
		return cal.getTime();
	}
	
	
	/* (non-Javadoc)
	 * @see net.thartm.treeutil.bulk.NodeCreator#cancelCurrentJob()
	 */
	@Override
	public void cancelCurrentJob(){
		jobStatus.cancel();
	}
}
