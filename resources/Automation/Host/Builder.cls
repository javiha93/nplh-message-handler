Class Automation.Host.Builder Extends %RegisteredObject
{

Property HostInfoList As list Of Automation.Host.HostParameter;

Method %OnNew() As %Status
{
	#dim sqlHost As %SQL.StatementResult = ##class(%SQL.Statement).%ExecDirect(,"SELECT ID, HostName, HostType, blnDefault, rTimeZone, DriverInfo from SQLUser.thcaHost WHERE HostName != 'LIS_TEXT' AND HostName != 'VIP' AND HostName != 'Automation SW' AND HostName NOT LIKE '%VIRTUOSO%'")
	While (sqlHost.%Next())
	{
		#dim connectionsType As %String
		#dim connections As %ListOfObjects = ##class(%Library.ListOfObjects).%New()
		#dim sqlUsedConfig As %SQL.StatementResult = ##class(%SQL.Statement).%ExecDirect(,"SELECT rcomPortTCPCfg, rcomPortWSCfg, INOUTDirection from SQLUser.thcaUsedConfig WHERE " _
																						"rhcaHost = ? " _
																						"AND ConfigName NOT LIKE '%ACK%' AND ConfigName NOT LIKE '%ORL%' " _
		
																						"group by rcomPortTCPCfg, rcomPortWSCfg", sqlHost.ID)
		While (sqlUsedConfig.%Next())
		{
			Set connectionsType = $Select(sqlUsedConfig.rcomPortTCPCfg '= "": "HL7", 1: "WS")
			Do ..GetConnection(sqlUsedConfig.rcomPortTCPCfg, sqlUsedConfig.rcomPortWSCfg, sqlUsedConfig.INOUTDirection, .connections)
		}
		If (sqlHost.DriverInfo '= "")
		{
			Do ..InsertOCConnections(sqlHost.ID, .connections)
		}
		Do ..HostInfoList.Insert(##class(Automation.Host.HostParameter).%New(sqlHost.ID, sqlHost.HostName, ..CorrectHostType(sqlHost.HostType), sqlHost.blnDefault, connections, connectionsType))

	}
	
	
	Do ..AddAutomationSWHost()

	Return $$$OK
}

Method GetHostByName(hostName As %String) As Automation.Host.HostParameter
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

ClassMethod CorrectHostType(hostType As %String) As %String
{
	Return $Case(hostType,  "HE 600"     : "HE600",
									     : hostType)
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
									"VTG WS"     : "VANTAGE WS",
									"UPATH 1.1"  : "UPATH",
									"DAKO"       : "DAKO LINK DRIVER",
									"DAKO LINK"  : "DAKO LINK DRIVER",
									"LEICA"      : "LEICA BOND DRIVER",
									"LEICA BOND" : "LEICA BOND DRIVER",
									             : hostName)
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
	#dim ip As %String
	#dim rComport As %String
	#dim status As %Boolean
	#dim connectionType As %String = ##class(Consts.Constants).#ConnTCP

	&sql(SELECT ConnectionName, TCPPort, IPaddress, ID INTO :connectionName, :port, :ip, :rComport FROM SQLUser.tcomPortTCPCfg WHERE ID = :id)
	&sql(SELECT TOP 1 Active INTO :status FROM SQLUser.thcaUsedConfig WHERE rcomPortTCPCfg = :rComport)
	
	Do connections.Insert(##class(Automation.Host.Connection.HL7Connection).%New(id, connectionName, direction, status, ip, port))
}

ClassMethod GetWSConnection(id As %Integer, direction As %String, ByRef connections As %ListOfObjects)
{
	#dim connectionName As %String
	#dim connectionType As %String = ##class(Consts.Constants).#ConnWS
	#dim apiKeyFile As %String = ""
	#dim apiKeyValue As %String
	#dim location As %String
	#dim rComport As %String
	#dim wsName As %String
	#dim requestorAddress As %String
	#dim status As %Boolean

	&sql(SELECT ConnectionName, APIKeyFile, APIKeyValue, Location, WSName, RequestorAddress, ID INTO  :connectionName, :apiKeyFile, :apiKeyValue, :location, :wsName, :requestorAddress, :rComport FROM SQLUser.tcomPortWSCfg WHERE ID = :id)
	&sql(SELECT TOP 1 Active INTO :status FROM SQLUser.thcaUsedConfig WHERE rcomPortWSCfg = :rComport)
	
	Do connections.Insert(##class(Automation.Host.Connection.WSConnection).%New(id, connectionName, direction, status, location, requestorAddress, wsName, apiKeyFile, apiKeyValue))
}

ClassMethod InsertOCConnections(hostID As %Integer, ByRef connections As %ListOfObjects)
{
	#Dim host As User.thcaHost = ##class(User.thcaHost).%OpenId(hostID)
	#Dim driver As Oc.dt.Driver = ##class(Oc.dt.Driver).%New(host.DriverInfo)
	
	#Dim connectionName As %String = "nPLH to " _ host.HostName
	Do connections.Insert(##class(Automation.Host.Connection.HL7Connection).%New(0, connectionName, ##class(Consts.Constants).#OutboundDirection, host.Status, driver.DriverInfo.OutboundIp, driver.DriverInfo.OutboundPort))
	Set connectionName = host.HostName _ " to nPLH"
	Do connections.Insert(##class(Automation.Host.Connection.HL7Connection).%New(0, connectionName, ##class(Consts.Constants).#InboundDirection, host.Status, driver.DriverInfo.OutboundIp, driver.DriverInfo.InboundPort))
}

Method AddAutomationSWHost()
{
	#dim installDirectory As %String = $Piece(##class(%SYSTEM.Util).InstallDirectory(), "\", 1, 3)_"\OpenConnect\application.yml"

	#dim file As %Stream.FileCharacter = ##class(%Stream.FileCharacter).%New()
	#dim status As %Status
	Try {
		set status = file.LinkToFile(installDirectory)
		
		set automationClientName = ""
		set automationApi = ""
		while ('file.AtEnd) 
		{
			set line = file.ReadLine()
			if $Find(line, "devices")
			{
				while (('$Find(line, "codemeter")) && ('file.AtEnd))
				{
					set line = file.ReadLine()
					if ($Find(line, "name"))
					{
						set automationClientName = $Piece(line, "name: ", 2)
					}
					if ($Find(line, "apiKeyValue"))
					{
						set automationApi = $Piece(line, "apiKeyValue: ", 2)
						
						if ((automationClientName '= "") && (automationApi '= ""))
						{
							#dim connections As %ListOfObjects = ##class(%Library.ListOfObjects).%New()
							Do connections.Insert(##class(Automation.Host.Connection.RestConnection).%New("", automationClientName, automationApi, 1))
							Do ..HostInfoList.Insert(##class(Automation.Host.HostParameter).%New("", automationClientName, "AUTOMATION_SW", 1, connections, "AUTOMATION_SW"))
						}
						set automationApi = ""
						set automationClientName = ""
					}
				}
			}
		}
	}
	Catch ex {
		Write "Error al procesar archivo: ", ex.DisplayString(), !
	}
	Do file.%Close()
}

Storage Default
{
<Data name="BuilderDefaultData">
<Value name="1">
<Value>%%CLASSNAME</Value>
</Value>
<Value name="2">
<Value>HostInfoList</Value>
</Value>
</Data>
<DataLocation>^Automation.Host.BuilderD</DataLocation>
<DefaultData>BuilderDefaultData</DefaultData>
<IdLocation>^Automation.Host.BuilderD</IdLocation>
<IndexLocation>^Automation.Host.BuilderI</IndexLocation>
<StreamLocation>^Automation.Host.BuilderS</StreamLocation>
<Type>%Storage.Persistent</Type>
}

}

