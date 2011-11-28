/**
 * Copyright (C) 2005-2010 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

(function() {

	   /**
	    * YUI Library aliases
	    */
	   var Dom = YAHOO.util.Dom,
	      Event = YAHOO.util.Event;
	   Alfresco.custom = Alfresco.custom || {};		   
Alfresco.custom.createPropertyTable = function(entries){
	var html = ""; 
	var child, row, cell;
	
	var pTable = document.createElement("table");
	//pTable.className"property-table";
	
	row = document.createElement("tr");
	pTable.appendChild(row);
	
	cell = document.createElement("th");
	row.appendChild(cell);
	cell.innerHTML = "Name";
	cell.className="property-header";
	
	cell = document.createElement("th");
    row.appendChild(cell);
    cell.innerHTML = "Zeit";
    cell.className="property-header";
    

    for (var i = 0; i < entries.length; i++){ 
		child = entries[i];
		row = document.createElement("tr");    
		pTable.appendChild(row);
	
		cell = document.createElement("td");
		row.appendChild(cell);
		cell.innerHTML = child.user;
		cell.className="property-label";
		
		var time = child.time.replace("T"," ")
		
		cell = document.createElement("td");
	    row.appendChild(cell);
	    cell.innerHTML = time;
	    cell.className="property-value";
		
    }
    
	return pTable; 
};

Alfresco.DocumentActions.prototype.onActionShowNodeAuditReport = function DL_onActionShowNodeAuditReport(asset) { 
	
	  var nodeRef = asset.nodeRef;
	  var queryValueType = "org.alfresco.service.cmr.repository.NodeRef";
	  
	  var queryApi = "api/audit/query/AuditFileAccessExtractors/auditfileaccessextractor/args/nodeRef/b";
	  
	  var Dom = YAHOO.util.Dom;
	  
	  /**
		 * Alfresco Slingshot aliases
		 */
	  var $html = Alfresco.util.encodeHTML;
	  
	  Alfresco.util.Ajax.request(
	  {
		  //+"&limit=15"
		 url: Alfresco.constants.PROXY_URI + queryApi + "?valueType=" + queryValueType + "&value=" + nodeRef + "&forward=false&limit=15",
	     successCallback:
	     {
	        fn: function dlA_onActionDetails_refreshSuccess(response)
		    {

	        	var rawResponse = response.serverResponse.responseText+"";	 
	        	var containerDiv = document.createElement("div");
	        	
	        	var regex = /:(\w+)\,/g;
	        	var requoted = rawResponse.replace(regex, ":\"$1\","); 
	        	
	        	
	        	var json = new Alfresco.util.parseJSON(requoted,true);

	        	var pTable = Alfresco.custom.createPropertyTable(json.entries);
	        	var pTableCaption = pTable.createCaption();
	        	pTableCaption.innerHtml = nodeRef.uri;
	        	
	        	containerDiv.appendChild(pTable);
	        	containerDiv.className = "node-properties";

	        	this.widgets.panel = Alfresco.util.createYUIPanel(containerDiv, { draggable: true, });

	        	// Show the panel
	          	this.widgets.panel.show();
		    },
	        scope: this
	     },
	     execScripts: true,
	     failureMessage: "Could not load datatable. Please contact your administrator!"
	  }); 
	};
})();