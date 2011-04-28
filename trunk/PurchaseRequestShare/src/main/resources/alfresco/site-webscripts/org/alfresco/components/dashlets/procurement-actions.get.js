function main() {
	
	var site = page.url.templateArgs.site;
	var uri = "/procurement/api/requestdestination/site/" + site + "";
   	var json = remote.call(uri);
	var node = eval('(' + json + ')');
    
    model.container = node;
}

main();