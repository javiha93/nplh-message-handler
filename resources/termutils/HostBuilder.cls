Class termutils.HostBuilder Extends %RegisteredObject
{

Property HostInfoList As list Of termutils.HostParameter;

Method %OnNew() As %Status
{
	#dim sqlHost As %SQL.StatementResult = ##class(%SQL.Statement).%ExecDirect(,"SELECT ID, HostName, HostType, rTimeZone, DriverInfo from SQLUser.thcaHost WHERE HostName != 'LIS_TEXT' AND HostName != 'VIP'")
	While (sqlHost.%Next())
	{
		#dim isHL7 As %Boolean
		#dim connections As %ListOfObjects = ##class(%Library.ListOfObjects).%New()
		#dim sqlUsedConfig As %SQL.StatementResult = ##class(%SQL.Statement).%ExecDirect(,"SELECT rcomPortTCPCfg, rcomPortWSCfg, INOUTDirection from SQLUser.thcaUsedConfig WHERE " _
																						"rhcaHost = ? " _
																						"AND ConfigName NOT LIKE '%ACK%' AND ConfigName NOT LIKE '%ORL%' " _
																						"group by rcomPortTCPCfg, rcomPortWSCfg", sqlHost.ID)
		While (sqlUsedConfig.%Next())
		{
			Set isHL7 = ..IsHL7(sqlUsedConfig.rcomPortTCPCfg, sqlUsedConfig.rcomPortWSCfg)
			Do ..GetConnection(sqlUsedConfig.rcomPortTCPCfg, sqlUsedConfig.rcomPortWSCfg, sqlUsedConfig.INOUTDirection, .connections)
		}
		#Dim isDriver As %Boolean = $Select((sqlHost.DriverInfo '= "") : 1, 1 : 0)
		If (isDriver)
		{
			Set isHL7 = 1
			Do ..InsertOCConnections(sqlHost.ID, .connections)
		}
		#Dim orginalHostName As %String = ..GetOriginalHostName(sqlHost.HostName)
		
		Do ..HostInfoList.Insert(##class(termutils.HostParameter).%New(sqlHost.ID, sqlHost.HostName, sqlHost.HostType, sqlHost.rTimeZone, connections, orginalHostName, isDriver, isHL7))

	}

	Return $$$OK
}

Method GetHostByName(hostName As %String) As termutils.HostParameter
{
	#Dim listOfHosts As %ListOfObjects = ..HostInfoList
	Do ..CorrectHostName(.hostName)

	For hostIndex=1:1:listOfHosts.Count()
	{
		If ($ZConvert($ZSTRIP(hostName , "<>WC"), "U") = $ZConvert($ZSTRIP(listOfHosts.GetAt(hostIndex).HostName , "<>WC"), "U"))
		{
			Return listOfHosts.GetAt(hostIndex)
		}
	}
	Throw ##class(%Exception.StatusException).%New("Not existing host: " + hostName)
}

ClassMethod CorrectHostName(ByRef hostName As %String)
{
	Set hostName = $ZConvert($ZSTRIP(hostName , "<>WC"), "U")

	Set hostName = $Case(hostName,  "LIS"        : "LIS_HL7",
									"VANTAGE"    : "VTG",
									"VTG HL7"    : "VTG",
									"VTG_HL7"    : "VTG",
									"VRT"        : "VIRTUOSO",
									"VANTAGEWS"  : "VANTAGE WS",
									"UPATH 1.1"  : "UPATH",
									"DAKO"       : "DAKO LINK DRIVER",
									"DAKO LINK"  : "DAKO LINK DRIVER",
									"LEICA"      : "LEICA BOND DRIVER",
									"LEICA BOND" : "LEICA BOND DRIVER",
									             : hostName)
}

ClassMethod IsHL7(portTCPCfg As %Integer, portWSCFfg As %Integer) As %Boolean
{
	If (portTCPCfg '= "")
	{
		Return 1
	}
	Return 0
}

ClassMethod GetConnection(portTCPCfg As %Integer, portWSCFfg As %Integer, direction As %String, ByRef connections As %ListOfObjects)
{
	If (portTCPCfg '= "")
	{
		Do ..GetTCPConnection(portTCPCfg, direction, .connections)
	}
	If (portWSCFfg '= "")
	{
		Do ..GetWSConnection(portWSCFfg, direction, .connections)
	}
}

ClassMethod GetTCPConnection(id As %Integer, direction As %String, ByRef connections As %ListOfObjects)
{
	#dim connectionName As %String
	#dim port As %String
	#dim connectionType As %String = ##class(Consts.Constants).#ConnTCP

	&sql(SELECT ConnectionName, TCPPort INTO :connectionName, :port FROM SQLUser.tcomPortTCPCfg WHERE ID = :id)
	Do connections.Insert(##class(termutils.HL7Connection).%New(id, connectionName, connectionType, direction, port))
}

ClassMethod GetWSConnection(id As %Integer, direction As %String, ByRef connections As %ListOfObjects)
{
	#dim connectionName As %String
	#dim connectionType As %String = ##class(Consts.Constants).#ConnWS
	#dim apiKeyFile As %String
	#dim apiKeyValue As %String
	#dim location As %String

	&sql(SELECT ConnectionName, APIKeyFile, APIKeyValue, Location INTO  :connectionName, :apiKeyFile, :apiKeyValue, :location FROM SQLUser.tcomPortWSCfg WHERE ID = :id)
	Do connections.Insert(##class(termutils.WSConnection).%New(id, connectionName, connectionType, direction, location, apiKeyFile, apiKeyValue))
}

ClassMethod InsertOCConnections(hostID As %Integer, ByRef connections As %ListOfObjects)
{
	#Dim host As User.thcaHost = ##class(User.thcaHost).%OpenId(hostID)
	#Dim driver As Oc.dt.Driver = ##class(Oc.dt.Driver).%New(host.DriverInfo)
	
	#Dim connectionName As %String = "nPLH to " _ host.HostName
	Do connections.Insert(##class(termutils.HL7Connection).%New(0, connectionName, ##class(Consts.Constants).#ConnTCP, ##class(Consts.Constants).#OutboundDirection, driver.DriverInfo.OutboundPort))
	Set connectionName = host.HostName _ " to nPLH"
	Do connections.Insert(##class(termutils.HL7Connection).%New(0, connectionName, ##class(Consts.Constants).#ConnTCP, ##class(Consts.Constants).#InboundDirection, driver.DriverInfo.InboundPort))
}

Method GetOriginalHostName(hostName As %String) As %String
{
	If ($Find(hostName, "[") '= 0)
	{
		Return $Reverse($Piece($Reverse(hostName), "[", *))
	}
	Return ""
}

}
