<#macro printPropertyValue p>
    <#if p.value?is_date>"${p.value?datetime?string}"</#if>
    <#if p.value?is_boolean>${p.value?string}</#if>
    <#if p.value?is_number>${p.value?c}</#if>
    <#if p.value?is_string>"${p.value}"</#if>
</#macro>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
    <title>Removal Operation Status</title>
</head>
<body>

Status: ${jobstatus} <br />
Starting with maxCommitTime: ${start} <br />
Stopping at timestamp: ${stop} <br />

Runtime information: <br />
Last Commit: ${lastCommit} <br />
Deleted Nodes: ${deletedNodes} <br />
Iterations: ${iterations} <br />

</body>
</html>