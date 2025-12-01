Class termutils.DuplicateHost Extends %RegisteredObject
{

Property HostBuilder As termutils.HostBuilder [ Private ];

Method %OnNew(hostBuilder As termutils.HostBuilder = {##class(termutils.HostBuilder).%New()}) As %Status
{
	Set ..HostBuilder = hostBuilder
	Return $$$OK
}

Method DuplicateHostSeveralTimes(hostName As %String, numberIteration As %Integer)
{
	for iteration=1:1:numberIteration
	{
		do ..DuplicateHost(hostName)
	}
}

ClassMethod DuplicateHostWrapper(hostName As %String, newHostNameList As %String = "")
{
	Do ##class(termutils.DuplicateHost).%New().DuplicateHost(hostName, newHostNameList)
}

Method DuplicateHost(hostName As %String, newHostNameList As %String = "")
{
	set newHostList = $ListFromString(newHostNameList)
	set ptr = 0
    while $ListNext(newHostList, ptr, value) {
        do ..DuplicateSingleHost(hostName, value)
    }
    Return
}

Method DuplicateSingleHost(hostName As %String, newHostName As %String = "") As %Boolean
{
	do ..CheckSession()
	
	#dim hostInfo As termutils.HostParameter = ..HostBuilder.GetHostByName(hostName)
	#dim connections As %ListOfObjects = hostInfo.Connections
	#dim hostConfigList As %ListOfObjects = ##class(%ListOfObjects).%New()
	
	if (newHostName '= "")
	{
		#dim hostAlreadyExists As %Boolean = ##class(fn.QueryExecutor).QueryProducesResults("SELECT ID FROM SQLUser.thcaHost WHERE HostName = ?", newHostName)
		if (hostAlreadyExists)
		{
			Return
		}
	}

	#dim duplicateNumber As %Integer = ..GetDuplicateIteration(hostInfo)
	for connectionIdex=1:1:connections.Count()
	{
		#dim connection As termutils.ConnectionInfo = connections.GetAt(connectionIdex)
		#dim rcomTable As %String
		#dim portConfig
		#dim newPortConfig
		if (connection.ConnectionType = ##class(Consts.Constants).#ConnWS)
		{
			set rcomTable = "rcomPortWSCfg"
			set portConfig = ##class(User.tcomPortWSCfg).%OpenId(connection.Id)
			set newPortConfig = ..DuplicateWSPortConfig(portConfig, duplicateNumber, newHostName)
		}
		if (connections.GetAt(connectionIdex).ConnectionType = ##class(Consts.Constants).#ConnTCP)
		{
			set rcomTable = "rcomPortTCPCfg"
			set portConfig = ##class(User.tcomPortTCPCfg).%OpenId(connections.GetAt(connectionIdex).Id)
			set newPortConfig = ..DuplicateTCPPortConfig(portConfig, duplicateNumber)
		}
		do ..DuplicateHostConfig(newPortConfig, portConfig, duplicateNumber, rcomTable, hostInfo.HostName, newHostName, .hostConfigList)
	}
	
	if (newHostName = "")
	{
		set newHostName = hostInfo.HostName _ "[" _ duplicateNumber _ "]"
	}
	#dim isDuplicated = ##class(hca.scr.chcaDuplicate).SaveHost(hostConfigList, newHostName, newHostName _ " Description", hostInfo.HostType, hostInfo.TimeZone, hostInfo.Id)
	return isDuplicated
}

ClassMethod GetDuplicateIteration(hostInfo As termutils.HostParameter) As %Integer
{
	#dim duplicateNumber As %Integer = 2
	#dim isFreeNumber As %Status = '$$$OK
	while ('isFreeNumber)
	{
		#dim auxFind As %Integer = 0
		#dim hostName As %String = hostInfo.HostName
		#dim sql As %SQL.StatementResult = ##class(%SQL.Statement).%ExecDirect(,"SELECT ID from SQLUser.thcaHost WHERE HostName = ?", hostName _ "[" _ duplicateNumber _ "]")
		if (sql.%Next())
		{
			set duplicateNumber = duplicateNumber + 1
			set auxFind = auxFind + 1
			Continue
		}
		#dim connections As %ListOfObjects = hostInfo.Connections
		for connectionIdex=1:1:connections.Count()
		{
			#dim connectionName As %String = connections.GetAt(connectionIdex).Name
			set sql = ##class(%SQL.Statement).%ExecDirect(,"SELECT ID from SQLUser.tcomPortTCPCfg WHERE ConnectionName = ?", connectionName _ "[" _ duplicateNumber _ "]")
			if (sql.%Next())
			{
				set duplicateNumber = duplicateNumber + 1
				set auxFind = auxFind + 1
				continue
			}
		}
		if (auxFind = 0)
		{
			set isFreeNumber = $$$OK
		}
	}
	return duplicateNumber
}

ClassMethod DuplicateWSPortConfig(portConfig As User.tcomPortWSCfg, duplicateNumber As %Integer, newHostName As %String) As User.tcomPortWSCfg
{
	if (newHostName '= "")
	{
		set portConfig.ConnectionName = portConfig.ConnectionName _ "[" _ newHostName _ "]"
	}
	else
	{
		set portConfig.ConnectionName = portConfig.ConnectionName _ "[" _ duplicateNumber _ "]"
	}
	if (portConfig.Location '= "")
	{
		set portConfig.Location = ..GetWSFreePort(portConfig)
	}
	set portConfig.RequestorAddress = ..GetFreeIp(portConfig)
	#dim newPortConfig As User.tcomPortWSCfg = ##class(com.scr.ccomPortWSCfg).Add(portConfig.ConnectionName, portConfig.Encoding, portConfig.Trace, portConfig.MaxLinesTrace,
																				  portConfig.MaxMsgForTimeOut, portConfig.MsgTimeOut, portConfig.PackageName, portConfig.URL,
																				  portConfig.Location, portConfig.URLUser, portConfig.URLPassword, portConfig.APIKeyFile,
																				  portConfig.WSName, portConfig.WSMethod, portConfig.SendAsBinary, portConfig.ModuleCode,
																				  1,portConfig.SSLConfiguration, portConfig.SSLCheckServerIdentity, portConfig.RequestorAddress, portConfig.ClientTimeout)
	if ('##class(co.utl.Error).IsEmpty())
	{
		set $ECODE = ##class(co.utl.Error).Get()
	}
	if (portConfig.APIKeyValue '= "")
	{
		set newPortConfig.APIKeyValue = portConfig.APIKeyValue
		Do newPortConfig.%Save()
	}
	
	return newPortConfig
}

ClassMethod DuplicateTCPPortConfig(portConfig As User.tcomPortTCPCfg, duplicateNumber As %Integer) As User.tcomPortTCPCfg
{
	set portConfig.ConnectionName = portConfig.ConnectionName _ "[" _ duplicateNumber _ "]"
	set portConfig.TCPPort = ..GetFreePort(portConfig)
	#dim newPortConfig As User.tcomPortTCPCfg = ##class(com.scr.ccomPortTCPCfg).Add(portConfig.ConnectionName, portConfig.Encoding, portConfig.Trace, portConfig.MaxLinesTrace,
																					   portConfig.MaxMsgForTimeOut, portConfig.MsgTimeOut, portConfig.IPaddress, portConfig.TCPPort,
																					   portConfig.IsServer, portConfig.DynamicPort, portConfig.OpenTimeOut, portConfig.TimeOutReconect,
																					   portConfig.ModuleCode, 1, portConfig.TLSprofile, portConfig.KeepAlive)

	if ('##class(co.utl.Error).IsEmpty())
	{
		set $ECODE = ##class(co.utl.Error).Get()
	}
	return newPortConfig
}

ClassMethod GetFreePort(connection As User.tcomPortTCPCfg) As %Integer
{
	#dim isConnectionFree As %Boolean = 0
	while ('isConnectionFree)
	{
		set connection.TCPPort = connection.TCPPort + 1
		set isConnectionFree = ##class(com.ccomUTL).IsConnectionFree(connection.IsServer, connection.TCPPort, connection.IPaddress)
	}
	return connection.TCPPort
}

ClassMethod GetWSFreePort(connection As User.tcomPortWSCfg) As %Integer
{
	#dim isConnectionFree As %Boolean = 0
	#dim port = $Extract(connection.Location,  $Find(connection.Location, ":", 10), $Find(connection.Location, "/", 10) - 2)
	while ('isConnectionFree)
	{
		set port = port + 1
		set isConnectionFree = ..IsWSConnectionFree(port)
	}
	return $Extract(connection.Location, 0, $Find(connection.Location, ":", 10) - 1) _ port _  $Extract(connection.Location, $Find(connection.Location, "/", 10) - 1, *)
}

ClassMethod IsWSConnectionFree(port As %Integer) As %Boolean
{
	Return '##class(fn.QueryExecutor).QueryProducesResults("SELECT ID FROM SQLUser.tcomPortWSCfg tpw WHERE Location LIKE '%" _ port _ "%'")
}

ClassMethod GetFreeIp(connection As User.tcomPortWSCfg) As %String
{
	#dim isIpFree As %Boolean = 0
	#dim ipBase = "192.168.1."
	#dim ipOctet = 50
	while ('isIpFree)
	{
		set ipOctet = ipOctet + 1
		set isIpFree = '##class(fn.QueryExecutor).QueryProducesResults("SELECT ID FROM SQLUser.tcomPortWSCfg tpw WHERE WSName = ? And RequestorAddress = ?", connection.WSName, ipBase _ ipOctet)
	}
	return ipBase _ ipOctet
}

ClassMethod DuplicateHostConfig(newConnection, oldConnection, duplicateNumber As %Integer, rcomTable As %String, hostName As %String, newHostName As %String = "", ByRef hostConfigList As %ListOfObjects)
{
	#dim sql As %SQL.StatementResult = ##class(%SQL.Statement).%ExecDirect(,"SELECT * from SQLUser.thcaUsedConfig WHERE " _ rcomTable _ " = ?", oldConnection.%Id())
	while (sql.%Next())
	{
		#dim hostCfg As dt.args.HostCfg = ##class(dt.args.HostCfg).%New()
		set hostCfg.ConnectionType = sql.ConnectionType
		set hostCfg.ChannelID = sql.rhcaChannel
		set hostCfg.HostOriginList = sql.HostOriginList
		set hostCfg.INOUTDirection = sql.INOUTDirection
		if (newHostName '= "")
		{
			set hostCfg.NewCfgName = sql.ConfigName _ "[" _ newHostName _ "]"
		}
		else
		{
			set hostCfg.NewCfgName = sql.ConfigName _ "[" _ duplicateNumber _ "]"
		}
		set hostCfg.NewConnectionID = newConnection.%Id()
		set hostCfg.NewConnectionText = newConnection.ConnectionName
		set hostCfg.ProtocolID = sql.rhcaProtocol
		set hostCfg.Status = sql.Status
		do hostConfigList.Insert(hostCfg)
	}
}

ClassMethod DeleteHost(hostName As %String)
{
}

ClassMethod CheckSession() [ Private ]
{
	do ##class(co.utl.Error).Init()
	set $ZERROR = ""
	if ($Get(%gobjSession) = "")
	{
		set %gobjSession = ##class(User.tsysSessions).%New()
		set %gobjSession.UserName = "ROCHE"
	}
}

}
