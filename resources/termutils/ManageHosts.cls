Class termutils.ManageHosts [ Abstract ]
{

ClassMethod ActivateAllHosts()
{
	do ##class(termutils.Canela).%New().ActivateAllHosts()
}

ClassMethod DeactivateAllHosts()
{
	do ##class(termutils.Canela).%New().DeactivateAllHosts()
}

ClassMethod UpdateHostStatus(hostName As %String)
{
	#dim host as termutils.HostParameter = ##class(termutils.HostBuilder).%New().GetHostByName(hostName)
	do ..UpdateHostStatusByHostParameter(host)
}

ClassMethod UpdateHostStatusByHostParameter(host As termutils.HostParameter)
{
	#dim status as %Boolean = host.GetStatus()
	if (status)
	{
		do ##class(termutils.Canela).%New().DeActivate(host)
		Write $Select(host.GetStatus(): "true",1: "false")
	}
	else
	{
		do ##class(termutils.Canela).%New().Activate(host)
		Write $Select(host.GetStatus(): "true",1: "false")
	}
}

ClassMethod GetHostsStatus()
{
	#Dim jsonPayload As %DynamicArray = []
	#Dim hostBuilder As termutils.HostBuilder = ##class(termutils.HostBuilder).%New()
	Set listOfHosts = hostBuilder.HostInfoList
	For hostIndex=1:1:listOfHosts.Count()
	{
		#Dim host As termutils.HostParameter = listOfHosts.GetAt(hostIndex)
		#Dim dynamicObject As %DynamicObject = host.ToDynamicObject()
		Do jsonPayload.%Push({}.%FromJSON(dynamicObject.%ToJSON()))
	}
	Write jsonPayload.%ToJSON()
}

ClassMethod DeleteHost(hostName As %String)
{
	do ##class(hca.utl.chcaUTL).CleanOrdersAP()
	do ##class(hca.utl.chcaUTL).CleanQueues(1)
	#dim host as termutils.HostParameter = ##class(termutils.HostBuilder).%New().GetHostByName(hostName)
	do ##class(hca.utl.chcaUTL).DelHost(host.HostName)
}

}
