<script type="text/javascript">//<![CDATA[
    new Alfresco.dashlet.ProcurementActions("${args.htmlid}").setOptions(
   {
      siteId: "${page.url.templateArgs.site!''}"  ,
      destinationNodeRef: "${container.nodeRef}"
   });
    //
   new Alfresco.widget.DashletResizer("${args.htmlid}", "${instance.object.id}");
//]]></script>

<#assign site=page.url.templateArgs.site>

<div class="dashlet procurementActions">
   <div class="title">${msg("procurementActions.header")}</div>
   <div class="toolbar">
      <a id="${args.htmlid}-procurementActions-button" class="theme-color-1" href="#">
        ${msg("procurementActions.create.message")}
      </a>
   </div>

   <div class="body scrollableList" <#if args.height??>style="height: ${args.height}px;"</#if> >
   </div>
   

</div>