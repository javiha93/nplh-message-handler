Class termutils.ManageConnections [ Abstract ]
{

ClassMethod checkSession() [ Private ]
{
	do ##class(termutils.Canela).CheckSession()
}

ClassMethod configConnectionWsLocation(connectionName As %String, wsLocation As %String)
{
	do ##class(termutils.Canelita).configConnectionWsLocation(connectionName, wsLocation)
	#dim host As termutils.HostParameter = ..GetHostByConnectionName(connectionName)
	do ##class(termutils.ManageHosts).UpdateHostStatusByHostParameter(host)
	do ##class(termutils.ManageHosts).UpdateHostStatusByHostParameter(host)
}

ClassMethod configConnectionWsApiKey(connectionName As %String, apiKeyFile As %String = "C:\\TMP\\API.key", apiKeyValue As %String = "PruebaAPIKey1")
{
	do ##class(termutils.Canelita).configConnectionWsApiKey(connectionName, apiKeyFile, apiKeyValue)
	#dim host As termutils.HostParameter = ..GetHostByConnectionName(connectionName)
	do ##class(termutils.ManageHosts).UpdateHostStatusByHostParameter(host)
	do ##class(termutils.ManageHosts).UpdateHostStatusByHostParameter(host)
}

ClassMethod configConnectionPort(connectionName As %String, port As %Integer)
{
	do ..checkSession()
	
	#Dim connection As termutils.ConnectionInfo = ..GetConnectionByConnectionName(connectionName)
	#Dim hostParameter As termutils.HostParameter = ..GetHostByConnectionName(connectionName)
	
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

ClassMethod GetHostByConnectionName(connectionName As %String) As termutils.HostParameter [ Private ]
{
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
				Return hostParameter
			}
		}	
	}
}

ClassMethod GetConnectionByConnectionName(connectionName As %String) As termutils.ConnectionInfo [ Private ]
{
	#Dim host As termutils.HostParameter = ..GetHostByConnectionName(connectionName)
	set connections = host.Connections
	For connectionsIndex=1:1:connections.Count()
	{
		#Dim connection As termutils.ConnectionInfo = connections.GetAt(connectionsIndex)
		If (connection.Name = connectionName)
		{
			Return connection
		}
	}
}

}
