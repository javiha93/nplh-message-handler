Class Automation.HostController [ Abstract ]
{

ClassMethod GetHostsStatus() As %String
{
	#Dim jsonPayload As %DynamicArray = []
	#Dim hostBuilder As Automation.Host.Builder = ##class(Automation.Host.Builder).%New()
	Set listOfHosts = hostBuilder.HostInfoList
	For hostIndex=1:1:listOfHosts.Count()
	{
		#Dim host As Automation.Host.HostParameter = listOfHosts.GetAt(hostIndex)
		#Dim dynamicObject As %DynamicObject = host.ToDynamicObject()
		Do jsonPayload.%Push({}.%FromJSON(dynamicObject.%ToJSON()))
	}
	Return jsonPayload.%ToJSON()
}

ClassMethod EnableTCPConnection(hostName As %String, connectionId As %Integer)
{
	#Dim hostId As %Integer = ##class(hca.chcaUTL).GetHostID(hostName, "","")
	Do ##class(hca.scr.chcaHostStatus).ModActiveMain("", hostId, ##class(Consts.Constants).#ConnTCP, connectionId, 1, "")
	If (..CheckTCPConnectionStatus(connectionId))
	{
		//Retry One
		Do ##class(hca.scr.chcaHostStatus).ModActiveMain("", hostId, ##class(Consts.Constants).#ConnTCP, connectionId, 1, "")
	}
}

ClassMethod CheckTCPConnectionStatus(connectionId As %Integer) As %Boolean
{
	Return ##class(fn.QueryExecutor).QueryForScalar("SELECT TOP 1 Active from thcaUsedConfig where rcomPortTCPCfg = ?", connectionId)
}

ClassMethod EnableWSConnection(hostName As %String, connectionId As %Integer)
{
	#Dim hostId As %Integer = ##class(hca.chcaUTL).GetHostID(hostName, "","")
	Do ##class(hca.scr.chcaHostStatus).ModActiveMain("", hostId, ##class(Consts.Constants).#ConnWS, connectionId, 1, "")
}

ClassMethod CheckWSConnectionStatus(connectionId As %Integer) As %Boolean
{
	Return ##class(fn.QueryExecutor).QueryForScalar("SELECT TOP 1 Active from thcaUsedConfig where rcomPortWSCfg = ?", connectionId)
}

ClassMethod ConfigWSConnection(connectionId As %Integer, wsLocation As %String = "", apiKeyFile As %String = "", apiKeyValue As %String = "")
{
	If (wsLocation '= "") {
		Do ..ConfigConnectionWsLocation(connectionId, wsLocation)
	}
	If ((apiKeyFile '= "") || (apiKeyValue '= "")) {
		Do ..ConfigConnectionWsApiKey(connectionId, apiKeyFile, apiKeyValue)
	}
}

ClassMethod ConfigConnectionWsLocation(connectionId As %String, wsLocation As %String)
{
	#Dim actualLocation = ##class(fn.QueryExecutor).QueryForScalar("SELECT Location from tcomPortWSCfg WHERE ID = ?", connectionId)
	If (actualLocation '= wsLocation)
	{
		&sql(UPDATE SQLUser.tcomPortWSCfg SET Location=:wsLocation WHERE ID=:connectionId)
	}
}

ClassMethod ConfigConnectionWsApiKey(connectionId As %String, apiKeyFile As %String = "C:\\TMP\\API.key", apiKeyValue As %String = "PruebaAPIKey1")
{
	#Dim actualApiKeyFile = ##class(fn.QueryExecutor).QueryForScalar("SELECT APIKeyFile from tcomPortWSCfg WHERE ID = ?", connectionId)
	#Dim actualAPIKeyValue = ##class(fn.QueryExecutor).QueryForScalar("SELECT APIKeyValue from tcomPortWSCfg WHERE ID = ?", connectionId)
	
	If ((actualApiKeyFile '= apiKeyFile) || (actualAPIKeyValue '= apiKeyValue))
	{
		&sql(UPDATE SQLUser.tcomPortWSCfg SET APIKeyFile=:apiKeyFile, APIKeyValue=:apiKeyValue WHERE ID=:connectionId)
	}
}

}

