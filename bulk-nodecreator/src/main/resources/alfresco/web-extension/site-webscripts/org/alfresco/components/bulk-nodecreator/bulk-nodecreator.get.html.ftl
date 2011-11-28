<!--[if IE]>
<iframe id="yui-history-iframe" src="${url.context}/res/yui/history/assets/blank.html"></iframe> 
<![endif]-->
<input id="yui-history-field" type="hidden" />

<#assign el=args.htmlid?html>
<script type="text/javascript">//<![CDATA[
   new AlfExt.BulkNodeCreatorAdminConsole("${el}").setMessages(${messages});
//]]></script>

<div id="${el}-body" class="bulk-nodecreator-console">
   
	<div class="bulk-nodecreator-status">

	</div>

</div>