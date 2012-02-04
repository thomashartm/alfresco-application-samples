package net.thartm.zipdownload.webscripts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.thartm.zipdownload.util.InvalidArgumentException;
import net.thartm.zipdownload.util.NodeRefHelper;

import org.alfresco.repo.web.scripts.content.StreamContent;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;


public class PostZipNodesWebscript extends StreamContent {

	private final static Log logger = LogFactory.getLog(PostZipNodesWebscript.class);
	private static final String MIMETYPE_APPLICATION_ZIP = "application/zip";
	private static final String FILES_PARAMETER = "files";
	private static final String PARENT_NODE_PARAMETER = "parent";
	private FileFolderService fileFolderService;
	private NodeRefHelper nodeRefHelper;
	private ZipToOutputStreamWriter streamWriter;

	public PostZipNodesWebscript(ServiceRegistry serviceRegistry, ZipToOutputStreamWriter streamWriter, NodeRefHelper nodeRefHelper) {
		this.fileFolderService = serviceRegistry.getFileFolderService();
		this.streamWriter = streamWriter;
		this.nodeRefHelper = nodeRefHelper;
	}

	/**
	 * @see org.alfresco.web.scripts.WebScript#execute(org.alfresco.web.scripts.WebScriptRequest,
	 *      org.alfresco.web.scripts.WebScriptResponse)
	 */
	public void execute(WebScriptRequest request, WebScriptResponse response) {

		try {
			final List<FileInfo> files = retrieveFilesArgument(request);
			final String zipFileName = retrieveParentNodeArgument(request);
			
			// content type and caching
			response.setContentType(MIMETYPE_APPLICATION_ZIP);
			setResponseCacheProperties(response);
			setResponseAttachmentHeader(response, zipFileName);
			// TODO add content length
			
			logger.debug("Starting to stream files");
			
			streamWriter.write(files.toArray(new FileInfo[files.size()]), response.getOutputStream());
		} catch (IOException ioe) {
			throw new WebScriptException(Status.STATUS_BAD_REQUEST, "Could not stream zip file: " + ioe.getMessage(), ioe);
		} catch(InvalidArgumentException e){
			throw new WebScriptException(Status.STATUS_BAD_REQUEST, "Provided arguments have not been accepted for further processing.", e);
		}
		

	}

	private List<FileInfo> retrieveFilesArgument(WebScriptRequest request) {
		List<FileInfo> results = new ArrayList<FileInfo>();
		String param = request.getParameter(FILES_PARAMETER);
		
		if (param != null) {
			String[] refs = param.split(",");
			for (String ref : refs) {
				NodeRef nodeRef = nodeRefHelper.getNodeRef(ref);
				if (nodeRef != null) {
					FileInfo fileInfo = fileFolderService.getFileInfo(nodeRef);
					results.add(fileInfo);
				}
			}
		}

		return results;
	}
	
	private String retrieveParentNodeArgument(WebScriptRequest request){
		String reference = request.getParameter(PARENT_NODE_PARAMETER);
		
		if (reference != null && reference.length() > 0) {
			return nodeRefHelper.getNodeName(reference);
		}
		throw new InvalidArgumentException("Reference Argument " + reference + " must not be null or empty");
	}
	
	private void setResponseCacheProperties(WebScriptResponse response) {
		Cache cache = new Cache();
		cache.setNeverCache(true);
		cache.setMustRevalidate(true);
		cache.setMaxAge(0L);
		response.setCache(cache);
	}

	private void setResponseAttachmentHeader(WebScriptResponse response, String fileName) {
		String headerValue = "attachment; filename=\"" + fileName + ".zip\"";

		// set header based on filename - will force a Save As
		response.setHeader("Content-Disposition", headerValue);
	}

	public void setNodeRefHelper(NodeRefHelper nodeRefHelper) {
		this.nodeRefHelper = nodeRefHelper;
	}
}