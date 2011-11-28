package net.thartm.treeutil.bulk.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.thartm.treeutil.bulk.nodecreation.data.NodePattern;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;

public class ContentDataHelper {

	private NodeService nodeService;
	private ContentService contentService;
	
	public ContentDataHelper(ContentService contentService, NodeService nodeService){
		this.contentService = contentService;
		this.nodeService = nodeService;
	}
	
	public byte[] copyContentFromExistingNode(NodeRef sourceNode) throws IOException{
		ContentReader reader = contentService.getReader(sourceNode, ContentModel.PROP_CONTENT);
		
		InputStream originalInputStream = reader.getContentInputStream();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		final int BUF_SIZE = 1 << 8; // 1KiB buffer
		byte[] buffer = new byte[BUF_SIZE];
		
		int bytesRead = -1;
		while ((bytesRead = originalInputStream.read(buffer)) > -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		
		originalInputStream.close();
		return outputStream.toByteArray();
	}
	
	public ContentData getExistingContentData(NodeRef sourceNode){
		return (ContentData) nodeService.getProperty(sourceNode, ContentModel.PROP_CONTENT);
	}

	public NodePattern transformToNodePattern(NodeRef sourceNode){
		NodePattern nodePattern = new NodePattern();

		nodePattern.setAspects(nodeService.getAspects(sourceNode));
		nodePattern.setName((String) nodeService.getProperty(sourceNode, ContentModel.PROP_NAME));
		nodePattern.setProperties(nodeService.getProperties(sourceNode));
		nodePattern.setContentData(getExistingContentData(sourceNode));
		
		
		return nodePattern;
	}
	
	public void writeContentData(NodeRef node, final byte[] content){
		ContentWriter writer = contentService.getWriter(node, ContentModel.PROP_CONTENT, true);
		writer.putContent(new ByteArrayInputStream(content));
	}
}
