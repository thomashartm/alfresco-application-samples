function main()
{
   // Get the site
   var siteId = url.templateArgs.site;
   var site = siteService.getSite(siteId);
   
   //TODO determine by a localized properties file
   var containerId = "procurement-requests";
   
   if (site === null)
   {
      status.setCode(status.STATUS_NOT_FOUND, "Site not found: '" + siteId + "'");
      return null;
   }
   
   var docLib = site.getContainer("documentLibrary");
   
   var node = docLib.childByNamePath(containerId);
   if (node === null)
   {
      node = docLib.createFolder(containerId);
  
      if (node === null)
      {
      	 status.setCode(status.STATUS_NOT_FOUND, "Unable to fetch container '" + containerId + "' of site '" + siteId + "'. (No write permission?)");
     	   return null;
      }
   }
   
   model.data = node.toJSON();
}
main();