/// 
/// The "Canelita" class is a Facade for `termutils.Canela`, and also for some VC services
/// like the "Oc.Drivers.DriversService" and "hca.scr.IE.chcaAdvancedStainFiltering", allowing
/// easy-to-call methods for the proper environment configuration before running manual tests,
/// perform a sublime System Demo, or a Requirement Show as well.
/// 
/// As part of the suite of the Improved Terminal, "Canela" and "Canelita" are good tools to
/// automate the configuration of VC via COS statements.
/// 
/// This class is written using Java-style code conventions: using the lower-Camel-Case naming,
/// and using the Java-variant indentation style of the K&R, over the Allman style.
/// 
Class termutils.Canelita [ Abstract ]
{

/// 
/// Clean the queues, kill the ^jou and ^gblEventRecord, and the
/// "tsysLogs" table as well.
/// 
ClassMethod cleanQueues()
{
	do ##class(hca.utl.chcaUTL).CleanOrdersAP()
	do ##class(hca.utl.chcaUTL).CleanQueues(1)

	kill ^jou
	kill ^gblEventRecord
	&sql(DELETE FROM SQLUser.tsysLogs)
}

/// 
/// Performs a "##class(hca.utl.chcaUTL).CargaIniGlo", reimport the "tsysParameters" GOF,
/// and clean the queues.
/// 
/// Optionally cleans the Stain Protocol-related tables.
/// 
/// @see ..cleanQueues
/// 
ClassMethod cargaIniGlo(removeAllStainProtocols As %Boolean = {$$$NO})
{
	do Import^GENUTILS("tsysParameters")
	do ##class(hca.utl.chcaUTL).CargaIniGlo(1,1,1)

	do ..cleanQueues()

	if (removeAllStainProtocols) {
		&sql(DELETE FROM SQLUser.thcaIEUnstainedProtocols)
		&sql(DELETE FROM SQLUser.thcaIEAdvStainFiltering)

		&sql(DELETE FROM SQLUser.tTestVersions)
		&sql(DELETE FROM SQLUser.tGroupTests)
		&sql(DELETE FROM SQLUser.tTests)
	}
}

ClassMethod DeactivateAllHosts()
{
	//do ..checkSession()
	do ##class(termutils.Canela).%New().DeactivateAllHosts()
}

ClassMethod ActivateAllHosts()
{
	//do ..checkSession()
	do ##class(termutils.Canela).%New().ActivateAllHosts()
}

/// 
/// Activate a host identified by its name, and optionally, configures its WS Location, or the API Key.
/// 
ClassMethod activateHost(hostName As %String, wsLocation As %String = "", apiKeyFile As %String = "", apiKeyValue As %String = "")
{
	#dim hostInfo as termutils.HostParameter = ##class(termutils.HostBuilder).%New().GetHostByName(hostName)
	#dim connectionNameOUT as %String = hostInfo.ConnectionNameOUT

	if (wsLocation '= "") {
		&sql(UPDATE SQLUser.tcomPortWSCfg SET Location=:wsLocation WHERE ConnectionName = :connectionNameOUT)
	}
	if ((apiKeyFile '= "") || (apiKeyValue '= "")) {
		#dim likeExpression As %String =  "%" _ hostName _ "%"
		&sql(UPDATE SQLUser.tcomPortWSCfg SET APIKeyFile=:apiKeyFile, APIKeyValue=:apiKeyValue WHERE ConnectionName LIKE :likeExpression)
	}

	do ..checkSession()
	do ##class(termutils.Canela).ActivateDeactivateHost(hostInfo, 1)
}

/// 
/// Delegates to the "co.acb.cTests" service to create a
/// new Stain Protocol in the "tTests" table.
/// 
/// SELECT TestAbbreviation,TestID,TestName FROM tTests
/// +------------------+--------+------------+
/// | TestAbbreviation | TestID | TestName   |
/// +------------------+--------+------------+
/// | H. Pylori1       | 1123   |            |
/// | PIN4             | 250    |            |
/// | New Stain        | 9999   |            |
/// | HnE              | 999    | HE Initial |
/// +------------------+--------+------------+
/// 
ClassMethod createStainProtocol(testIdName As %String, testAbbreviation As %String, testName As %String = "")
{
	do ##class(co.acb.cTests).Add(testIdName, testAbbreviation, testName, 1, 14, 1)
	do ##class(termutils.SqlExecutor).Execute("SELECT ID,TestAbbreviation,TestID,TestName FROM SQLUser.tTests")
}

/// 
/// Find the ID in "thcaIEHostConfig" by the Sender field using a LIKE clause.
/// 
ClassMethod getHostConfigIdBySender(hostName As %String) As %Integer [ Private ]
{
	#dim sender as %String = "%" _ hostName _ "%"
	#dim hostConfigId as %Integer
	&sql(SELECT ID INTO :hostConfigId FROM SQLUser.thcaIEHostConfig WHERE Sender LIKE :sender)
	return hostConfigId
}

/// 
/// Find the ID of an Stain Protocol ("tTests" table) by its "TestID" field.
/// 
ClassMethod getStainProtocolId(testIdName As %String) As %Integer [ Private ]
{
	#dim stainProtocolId as %Integer
	&sql(SELECT ID INTO :stainProtocolId FROM SQLUser.tTests WHERE TestID=:testIdName)
	return stainProtocolId
}

/// 
/// Delegates to the "hca.scr.IE.chcaAdvancedStainFiltering" service to
/// assign an existing Stain Protocol as Advanced.
/// 
/// The argument "hostName" is used in a LIKE clause for "thcaIEHostConfig#Sender"
/// (@see ..getHostConfigIdBySender)
/// 
ClassMethod assignAdvancedStainProtocol(hostName As %String, stainProtocolId As %Integer)
{
	do ..checkSession()
	#dim hostConfigId as %Integer = ..getHostConfigIdBySender(hostName)

	if ('##class(hca.scr.IE.chcaAdvancedStainFiltering).IsAdvanced(hostConfigId, stainProtocolId)) {
		do ##class(hca.scr.IE.chcaAdvancedStainFiltering).Add(hostConfigId, stainProtocolId)
		do ##class(termutils.SqlExecutor).Execute("SELECT * FROM SQLUser.thcaIEAdvStainFiltering")
	}
}

/// 
/// Delegates to the "hca.scr.IE.chcaAdvancedStainFiltering" service to
/// UNassign an Stain Protocol as Advanced.
/// 
/// The argument "hostName" is used in a LIKE clause for "thcaIEHostConfig#Sender"
/// (@see ..getHostConfigIdBySender)
/// 
ClassMethod unassignAdvancedStainProtocol(hostName As %String, stainProtocolId As %Integer)
{
	do ..checkSession()

	do ##class(hca.scr.IE.chcaAdvancedStainFiltering).Del(
		..getHostConfigIdBySender(hostName),
		stainProtocolId)
}

/// 
/// Uses the "hca.scr.IE.chcaAdvancedStainFiltering" service to
/// UNassign all the Stain Protocol as Advanced of the given host.
/// 
/// The argument "hostName" is used in a LIKE clause for "thcaIEHostConfig#Sender"
/// (@see ..getHostConfigIdBySender)
/// 
ClassMethod unassignAllAdvancedStainProtocols(hostName As %String)
{
	do ..checkSession()

	#dim hostConfigId as %Integer = ..getHostConfigIdBySender(hostName)
	#dim advStainProtocols as %ListOfDataTypes = ##class(hca.scr.IE.chcaAdvancedStainFiltering).GetList(hostConfigId)

	for i=1:1:advStainProtocols.Count() {
		do ##class(hca.scr.IE.chcaAdvancedStainFiltering).Del(
			hostConfigId,
			advStainProtocols.GetAt(i))
	}

	do ##class(termutils.SqlExecutor).Execute("SELECT * FROM SQLUser.thcaIEAdvStainFiltering")
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

/// 
/// Installs a ZIP driver file.
/// 
/// @param sourceZipPath "D:\gitrepos\ventana-connect-oc-driver-engine\src\main\resources\"
/// @param driverZipFileName "dako-link-driver-0.0.0.0.zip"
/// @return the ID of the driver in the "thcaHost" table.
/// 
ClassMethod installDriver(sourceZipPath As %String, driverZipFileName As %String, sessionId As %String = "AutomaticProcessDaemonRTCMW032767CONN") As %Integer
{
	do ..checkSession()
	do ..checkRestSession(sessionId)

	// The shared path, usually "C:\TMP\", plus our folder name ("oc/")
	#dim destZipPath as %String = ##class(fn.FileUtils).%New().GetDriversAbsoluteFolder()

	do ##class(%File).CopyFile(sourceZipPath _ driverZipFileName, destZipPath _ driverZipFileName)

	#dim driverId as %Integer = ##class(Oc.Drivers.DriversService).CreateDriver(destZipPath, driverZipFileName)

	do ##class(termutils.SqlExecutor).Execute("SELECT ID,HostName,Description,Status,HostType FROM thcaHost WHERE DriverInfo IS NOT NULL")
	write !, !, "=> Driver installed with id=" _ driverId, !

	return driverId
}

ClassMethod uninstallDriver(hostName As %String, sessionId As %String = "AutomaticProcessDaemonRTCMW032767CONN")
{
	do ..checkSession()
	do ..checkRestSession(sessionId)
	
	#dim host as termutils.HostParameter = ##class(termutils.HostBuilder).%New().GetHostByName(hostName)
	Do ##class(Oc.Drivers.DriversService).DeleteDriverById(host.Id)
}

ClassMethod EnableDriver(driverId As %Integer, status As %Boolean = {$$$YES}, sessionId As %String = "AutomaticProcessDaemonRTCMW032767CONN")
{
	//do ..checkSession()
	do ..checkRestSession(sessionId)
	do ##class(Oc.Drivers.DriversService).DriverStatusUpdate(driverId, status)

	&sql(SELECT Status INTO :updatedStatus FROM thcaHost WHERE ID = :driverId)
}

ClassMethod checkSession() [ Private ]
{
	do ##class(termutils.Canela).CheckSession()
}

ClassMethod checkRestSession(sessionId As %String) [ Private ]
{
	do ##class(co.utl.Error).Init()
	if ($get(%session) = "") {
		set %session = {}
		set %session.UserName = sessionId
		set %session.SessionId = sessionId
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

ClassMethod UpdateHostStatus(hostName As %String)
{
	#dim host as termutils.HostParameter = ##class(termutils.HostBuilder).%New().GetHostByName(hostName)
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

ClassMethod configConnection(connectionName As %String, wsLocation As %String = "", apiKeyFile As %String = "", apiKeyValue As %String = "")
{
	do ..checkSession()

	if (wsLocation '= "") {
		do ..configConnectionWsLocation(connectionName, wsLocation)
	}
	if ((apiKeyFile '= "") || (apiKeyValue '= "")) {
		do ..configConnectionWsApiKey(connectionName, apiKeyFile, apiKeyValue)
	}
}

ClassMethod configConnectionWsLocation(connectionName As %String, wsLocation As %String)
{
	do ..checkSession()
	&sql(UPDATE SQLUser.tcomPortWSCfg SET Location=:wsLocation WHERE ConnectionName=:connectionName)
}

ClassMethod configConnectionWsApiKey(connectionName As %String, apiKeyFile As %String = "C:\\TMP\\API.key", apiKeyValue As %String = "PruebaAPIKey1")
{
	do ..checkSession()
	&sql(UPDATE SQLUser.tcomPortWSCfg SET APIKeyFile=:apiKeyFile, APIKeyValue=:apiKeyValue WHERE ConnectionName=:connectionName)
}

ClassMethod configConnectionPort(connectionName As %String, port As %Integer)
{
	do ..checkSession()
	#Dim hostBuilder As termutils.HostBuilder = ##class(termutils.HostBuilder).%New()
	Set listOfHosts = hostBuilder.HostInfoList
	For hostIndex=1:1:listOfHosts.Count()
	{
		#Dim hostParameter As termutils.HostParameter = listOfHosts.GetAt(hostIndex)
		set connections = hostParameter.Connections
		For connectionsIndex=1:1:connections.Count()
		{
			#Dim connection As termutils.ConnectionInfo = connections.GetAt(connectionsIndex)
			If (connection.Name = connectionName)
			{
				If (hostParameter.IsDriver)
				{
					#Dim host As User.thcaHost = ##class(User.thcaHost).%OpenId(hostParameter.Id)
					#Dim driver As Oc.dt.Driver = ##class(Oc.dt.Driver).%New(host.DriverInfo)
					If (connection.Direction = ##class(Consts.Constants).#OutboundDirection)
					{
						Set driver.DriverInfo.OutboundPort = port
					}
					ElseIf (connection.Direction = ##class(Consts.Constants).#InboundDirection)
					{
						Set driver.DriverInfo.InboundPort = port
					}
					Set host.DriverInfo = driver.ToStream()
					Do host.%Save()
				}
				Else
				{
					#Dim connectionId As %Integer = connection.Id
					&sql(UPDATE SQLUser.tcomPortTCPCfg SET TCPPort=:port WHERE ID=:connectionId)
				}
			}
		}

	}
}

}

