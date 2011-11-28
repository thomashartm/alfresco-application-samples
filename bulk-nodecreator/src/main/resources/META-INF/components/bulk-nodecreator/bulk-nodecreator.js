if (typeof AlfExt == "undefined" || !AlfExt) {
	var AlfExt = {};
}

(function() {
	/**
	 * YUI Library aliases
	 */
	var Dom = YAHOO.util.Dom, Event = YAHOO.util.Event, Element = YAHOO.util.Element;

	/**
	 * Alfresco Slingshot aliases
	 */
	var $html = Alfresco.util.encodeHTML, $hasEventInterest = Alfresco.util.hasEventInterest;

	/**
	 * ConsoleApplication constructor.
	 * 
	 * @param {String} htmlId The HTML id üof the parent element
	 * @return {Alfresco.ConsoleApplication} The new ConsoleApplication instance
	 * @constructor
	 */
	AlfExt.BulkNodeCreatorAdminConsole = function(htmlId) {

		this.name = "AlfExt.BulkNodeCreatorAdminConsole";

		AlfExt.BulkNodeCreatorAdminConsole.superclass.constructor.call(this, htmlId);

		/* Register this component */
		Alfresco.util.ComponentManager.register(this);

		/* Load YUI Components */
		Alfresco.util.YUILoaderHelper.require([ "button", "container", "json",
				"history" ], this.onComponentsLoaded, this);

		/* Define panel handlers */
		var parent = this;
		
		/* Initialise prototype properties */
		this.auditEnabled = false; 
		
		// NOTE: the panel registered first is considered the "default" view and
		// is displayed first

		/* Options Panel Handler */
		BulkNodeCreatorAdminPanelHandler = function BulkNodeCreatorAdminPanelHandler_constructor() {
			BulkNodeCreatorAdminPanelHandler.superclass.constructor.call(this, "bulk-nodecreator");
		};


		
		YAHOO.extend(BulkNodeCreatorAdminPanelHandler, Alfresco.ConsolePanelHandler, {

			onLoad : function onLoad() {

				//TODO FIXME
	            parent.widgets.auditEnableSwitch = Alfresco.util.createYUIButton(parent, AUDIT_SWITCH_TURNON, parent.onAuditEnableSwitchEvent,
	            {
	            	disabled: false
	            });
	            
				parent.setAuditStatus();

				//Finds out if auditing is enabled 
		         
			},
			onSuccess : function OptionsPanel_onSuccess(response) {

			}

		});
		new BulkNodeCreatorAdminPanelHandler();

		return this;
	};

	YAHOO.extend(AlfExt.BulkNodeCreatorAdminConsole,Alfresco.ConsoleTool,
	{

			auditEnabled : false,

			/**
			 * Fired by YUILoaderHelper when required component script files have
			 * been loaded into the browser.
			 *
			 * @method onComponentsLoaded
			 */
			onComponentsLoaded : function AuditAdminConsole_onComponentsLoaded() {
				Event.onContentReady(this.id, this.onReady, this, true);
			},

			/**
			 * Fired by YUI when parent element is available for scripting.
			 * Component initialisation, including instantiation of YUI widgets and event listener binding.
			 *
			 * @method onReady
			 */
			onReady : function AuditAdminConsole_onReady() {

				// Call super-class onReady() method
				AlfExt.BulkNodeCreatorAdminConsole.superclass.onReady.call(this);

			},
/**			
			_changeAuditStatus : function AuditAdminConsole_changeAuditStatus(status){
				var elStatus = Dom.get(this.id + "-audit-status");
				this.auditEnabled = status;
				elStatus.innerHTML = this.auditEnabled ? this._msg("audit-admin.enabled") : this._msg("audit-admin.disabled");
			},
			
*/
			
			/**
			 * Extracts the JSON part of the response.
			 */
			_parseJsonResponse : function BulkNodeCreatorAdminConsole_parseJsonResponse(response) {
				var rawResponse = response.serverResponse.responseText
						+ "";
				//FIX JSON output quotation bug in 3.4.x
				var regex = /:(\w+)\,/g;
				var requoted = rawResponse.replace(regex,
						":\"$1\",");

				return new Alfresco.util.parseJSON(requoted, true);
			},
			
			/**
			 * YUI WIDGET EVENT HANDLERS Handlers for standard
			 * events fired from YUI widgets, e.g. "click"
			 */

			/**
			 * Gets a custom message
			 * 
			 * @method _msg
			 * @param messageId
			 *            {string} The messageId to retrieve
			 * @return {string} The custom message
			 * @private
			 */
			_msg : function BulkNodeCreatorAdminConsole__msg(messageId) {
				return Alfresco.util.message.call(this, messageId,
						"AlfExt.BulkNodeCreatorAdminConsole",
							Array.prototype.slice.call(arguments)
								.slice(1));
			}
		});

})();