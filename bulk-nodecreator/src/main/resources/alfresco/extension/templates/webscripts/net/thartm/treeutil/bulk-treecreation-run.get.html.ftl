<#macro printPropertyValue p>
    <#if p.value?is_date>"${p.value?datetime?string}"</#if>
    <#if p.value?is_boolean>${p.value?string}</#if>
    <#if p.value?is_number>${p.value?c}</#if>
    <#if p.value?is_string>"${p.value}"</#if>
</#macro>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
    <title>Tree Creation Details</title>
</head>
<body>

Parent Node: ${p} <br />
Template Node: ${t} <br />
Number of copies: ${i} <br />

</body>
</html>