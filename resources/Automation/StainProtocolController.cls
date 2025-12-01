Class Automation.StainProtocolController [ Abstract ]
{

ClassMethod AssignAdvancedStainProtocol(hostName As %String, stainProtocolId As %Integer)
{
	#dim hostConfigId as %Integer = ..GetHostConfigIdBySender(hostName)

	if ('##class(hca.scr.IE.chcaAdvancedStainFiltering).IsAdvanced(hostConfigId, stainProtocolId)) {
		do ##class(hca.scr.IE.chcaAdvancedStainFiltering).Add(hostConfigId, stainProtocolId)
		//do ##class(fn.QueryExecutor).Execute("SELECT * FROM SQLUser.thcaIEAdvStainFiltering")
	}
}

ClassMethod GetHostConfigIdBySender(hostName As %String) As %Integer [ Private ]
{
	#dim sender as %String = "%" _ hostName _ "%"
	#dim hostConfigId as %Integer
	&sql(SELECT ID INTO :hostConfigId FROM SQLUser.thcaIEHostConfig WHERE Sender LIKE :sender)
	return hostConfigId
}

ClassMethod GetStainProtocolId(testIdName As %String) As %Integer [ Private ]
{
	#dim stainProtocolId as %Integer
	&sql(SELECT ID INTO :stainProtocolId FROM SQLUser.tTests WHERE TestID=:testIdName)
	return stainProtocolId
}

ClassMethod CreateStainProtocol(testIdName As %String, testAbbreviation As %String, testName As %String = "")
{
	do ##class(co.acb.cTests).Add(testIdName, testAbbreviation, testName, 1, 14, 1)
	//do ##class(fn.QueryExecutorAdapter).%New().Execute("SELECT ID,TestAbbreviation,TestID,TestName FROM SQLUser.tTests")
}

ClassMethod CreateStainProtocolAndAssign(hostName As %String, testIdName As %String, testAbbreviation As %String, testName As %String = "")
{
	do ##class(termutils.Canela).CheckSession()
	if ('##class(fn.QueryExecutor).QueryProducesResults("select * from tTests WHERE TestID = ? AND TestAbbreviation = ?", testIdName, testAbbreviation))
	{
		do ..CreateStainProtocol(testIdName, testAbbreviation, testName)
	}
	
	#dim stainProtcolId As %Integer = ..GetStainProtocolId(testIdName)
	do ..AssignAdvancedStainProtocol(hostName, stainProtcolId)
}

}

