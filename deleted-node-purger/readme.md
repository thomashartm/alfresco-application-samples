#Project purpose:
The project's purpose is to do the removal work that is usually done by the DeleteNodesCleanupWorker 
in smaller batches.
It uses a start timestamp and endtimestamp and an incrementation interval to remove the nodes that fit the 
actual commit time interval.

##The original use case
The tool was originally implemented and used for cleaning up a >20 million docs repository after a more or less failed migration.
Versioning was accidentially enabled and trippled the database size. 
The versioned nodes were deleted and the tnode-purger tool took care of getting rid of all deleted nodes.

##Disclaimer
The project is intended to be used by administrative users only. It provides three webscripts to interact with the admininstrator:

#How to use:
Compile and package the project and drop the resulting jar to the alfresco/WEB-INF/lib directory.
!!!Remove after the process has been finished!!!


##Webscript: delete
URL: /cleanup/delete?maxcommit={maxcommit}&amp;interval={interval}&amp;stopcommit={stopcommit?}

Triggers the removal process executed by a background runnable.
Only one runnable can be active at a time.
Please deactive the minRecordPurgeAgeDays by setting to -1
index.tracking.minRecordPurgeAgeDays=-1
Please run it on one cluster node only. Deactivate the other cluster nodes to avoid any locking issues.

##Webscript: jobstatus
URL: /cleanup/status 

Displays the execution status, the iteration, the last visited commit time and the number of purged nodes.

##Webscript: cancel
URL: /cleanup/cancel

Cancels the whole process
