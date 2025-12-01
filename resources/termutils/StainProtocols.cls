Class termutils.StainProtocols [ Abstract ]
{

ClassMethod checkSession() [ Private ]
{
	do ##class(termutils.Canela).CheckSession()
}

ClassMethod getStainProtocols(hostName As %String)
{
	do ..checkSession()
	do ##class(termutils.HostBuilder).CorrectHostName(.hostName)
	#dim hostConfigId as %Integer = ..getHostConfigIdBySender(hostName)
	#dim stainProtocols As %DynamicArray = []

	#dim resultSet As %XML.DataSet = ##class(co.acb.cTests).GetListConnectV("", "", "", 1, "")
	while (resultSet.Next())
	{
		
		#dim stainProtocol As %DynamicObject = {}
		do stainProtocol.%Set("id", resultSet.ID)
		do stainProtocol.%Set("protocolId", resultSet.TestID)
		do stainProtocol.%Set("protocolName", resultSet.TestAbbreviation)
		do stainProtocol.%Set("protocolDescription", resultSet.TestName)
		#dim isAssigned As %Boolean = ##class(hca.scr.IE.chcaAdvancedStainFiltering).IsAdvanced(hostConfigId, resultSet.ID)
		do stainProtocol.%Set("isAssigned", $Select(isAssigned: "true",1: "false"))
		
		do stainProtocols.%Push(stainProtocol)
	}
	write stainProtocols.%ToJSON()
}

ClassMethod assignAdvancedStainProtocol(hostName As %String, testIdName As %String, testAbbreviation As %String, testName As %String = "")
{
	do ..checkSession()
	#dim stainProtocolId As %Integer = ..getStainProtocolIdByFullStainProtocol(testIdName, testAbbreviation, testName)
	
	if (stainProtocolId = "")
	{
		do ##class(termutils.Canelita).createStainProtocol(testIdName, testAbbreviation, testName)
		set stainProtocolId = ..getStainProtocolIdByFullStainProtocol(testIdName, testAbbreviation, testName)
	}
	do ##class(termutils.HostBuilder).CorrectHostName(.hostName)
	#dim hostConfigId as %Integer = ..getHostConfigIdBySender(hostName)
	if ('##class(hca.scr.IE.chcaAdvancedStainFiltering).IsAdvanced(hostConfigId, stainProtocolId)) {
		do ##class(hca.scr.IE.chcaAdvancedStainFiltering).Add(hostConfigId, stainProtocolId)
	}
}

ClassMethod unassignAdvancedStainProtocol(hostName As %String, testIdName As %String, testAbbreviation As %String, testName As %String = "")
{
	do ..checkSession()
	#dim stainProtocolId As %Integer = ..getStainProtocolIdByFullStainProtocol(testIdName, testAbbreviation, testName)
	do ##class(termutils.HostBuilder).CorrectHostName(.hostName)
	#dim hostConfigId as %Integer = ..getHostConfigIdBySender(hostName)
	do ##class(hca.scr.IE.chcaAdvancedStainFiltering).Del(hostConfigId, stainProtocolId)
}

ClassMethod deleteAdvancedStainProtocol(testIdName As %String, testAbbreviation As %String, testName As %String = "")
{
	do ..checkSession()
	#dim stainProtocolId As %Integer = ..getStainProtocolIdByFullStainProtocol(testIdName, testAbbreviation, testName)
	do ##class(co.acb.cTests).DeleteTest(stainProtocolId)
}

ClassMethod unassignAllAdvancedStainProtocols(hostName As %String)
{
	do ..checkSession()
	
	do ##class(termutils.HostBuilder).CorrectHostName(.hostName)
	#dim hostConfigId as %Integer = ..getHostConfigIdBySender(hostName)
	#dim advStainProtocols as %ListOfDataTypes = ##class(hca.scr.IE.chcaAdvancedStainFiltering).GetList(hostConfigId)

	for i=1:1:advStainProtocols.Count() {
		do ##class(hca.scr.IE.chcaAdvancedStainFiltering).Del(hostConfigId, advStainProtocols.GetAt(i))
	}
}

ClassMethod assignAllAdvancedStainProtocols(hostName As %String)
{
	do ..checkSession()
	
	do ##class(termutils.HostBuilder).CorrectHostName(.hostName)
	#dim hostConfigId as %Integer = ..getHostConfigIdBySender(hostName)
	#dim resultSet As %XML.DataSet = ##class(co.acb.cTests).GetListConnectV("", "", "", 1, "")
	while (resultSet.Next())
	{
		#dim stainProtocolId As %Integer = resultSet.ID
		if ('##class(hca.scr.IE.chcaAdvancedStainFiltering).IsAdvanced(hostConfigId, stainProtocolId)) 
		{
			do ##class(hca.scr.IE.chcaAdvancedStainFiltering).Add(hostConfigId, stainProtocolId)
		}
	}
}

ClassMethod deleteAllAdvancedStainProtocols()
{
	do ..checkSession()
	
	#dim resultSet As %XML.DataSet = ##class(co.acb.cTests).GetListConnectV("", "", "", 1, "")
	while (resultSet.Next())
	{
		#dim stainProtocolId As %Integer = resultSet.ID
		do ##class(co.acb.cTests).DeleteTest(stainProtocolId)
	}
}

ClassMethod getStainProtocolIdByFullStainProtocol(testIdName As %String, testAbbreviation As %String, testName As %String = "") As %Integer [ Private ]
{
	#dim testId As %Integer
	if (testName '= "") 
	{
		set testId = ##class(fn.QueryUtils).QueryForScalar("select ID from tTests WHERE TestAbbreviation = ? AND TestID = ? AND TestName = ?", testAbbreviation, testIdName, testName)
	}
	else
	{
		set testId = ##class(fn.QueryUtils).QueryForScalar("select ID from tTests WHERE TestAbbreviation = ? AND TestID = ?", testAbbreviation, testIdName)
	}
	return testId
}

ClassMethod getHostConfigIdBySender(hostName As %String) As %Integer [ Private ]
{
	#dim hostConfigId as %Integer
	&sql(SELECT ID INTO :hostConfigId FROM SQLUser.thcaIEHostConfig WHERE Sender = :hostName)
	return hostConfigId
}

}

