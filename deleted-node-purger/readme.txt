The project purpose is to execute the removal work that usually done by the DeleteNodesCleanupWorker 
in smaller batches.
It uses a start timestamp and an endtimestamp and an incrementation interval to remove the nodes that fit the 
actual commit time interval.
The project contains three webscripts to interact with an admin:

_
Compile and package the project and drop the resulting jar to the alfresco/WEB-INF/lib directory.
Remove after the process has been finished.
_____________________
Webscript: delete
URL: /cleanup/delete?maxcommit={maxcommit}&amp;interval={interval}&amp;stopcommit={stopcommit?}

Triggers the removal process executed by a background runnable.
Only one runnable can be active at a time.
Please deactive the minRecordPurgeAgeDays by setting to -1
index.tracking.minRecordPurgeAgeDays=-1
Please run it on one cluster node. Deactivate the othe rcluster nodes to avoid any locking issues.

______________________
Webscript: jobstatus
URL: /cleanup/status 

Displays the execution status, the iteration, the last visited commit time and the number of purged nodes.
______________________
Webscript: cancel
URL: /cleanup/cancel

Cancels the whole process