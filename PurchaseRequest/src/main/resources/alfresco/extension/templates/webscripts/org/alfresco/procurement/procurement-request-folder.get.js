function main()
{
   // Get the site
   var siteId = url.templateArgs.site;
   var site = siteService.getSite(siteId);
   
   //TODO determine by a localized properties file
   var containerId = "Procurement";
   
   if (site === null)
   {
      status.setCode(status.STATUS_NOT_FOUND, "Site not found: '" + siteId + "'");
      return null;
   }
   
   //var companyHome = companyhome.childByNamePath()
   var docLib = site.getContainer("documentLibrary");
   
   var node = docLib.childByNamePath(containerId);
   //var node = companyhome.childByNamePath(containerId);
   if (node === null)
   {
      node = docLib.createFolder(containerId);
      node.addAspect("proc:workflowConfiguration");
      if (node === null)
      {
      	 status.setCode(status.STATUS_NOT_FOUND, "Unable to create container '" + containerId + "' within site '" + siteId + "'. (No write permission?)");
     	   return null;
      }
   }
   
   model.data = node.toJSON();
}
main();