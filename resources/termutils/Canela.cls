Class termutils.Canela Extends %RegisteredObject
{

Property HostBuilder As termutils.HostBuilder [ Private ];

Method %OnNew(hostBuilder As termutils.HostBuilder = {##class(termutils.HostBuilder).%New()}) As %Status
{
	Set ..HostBuilder = hostBuilder
	Return $$$OK
}

Method ActivateAllHosts()
{
	Do ..CheckSession()	
	
	#Dim listOfHosts As %ListOfObjects = ..HostBuilder.HostInfoList
	
	For hostIndex=1:1:listOfHosts.Count()
	{
		#Dim host As termutils.HostParameter = listOfHosts.GetAt(hostIndex)
		#Dim hostActive As %Boolean = host.GetStatus()
		If ('hostActive)
		{
			Do ..ActivateDeactivateHost(host, 1)
		}
	}
}

Method DeactivateAllHosts()
{
	Do ..CheckSession()

	#Dim listOfHosts As %ListOfObjects = ..HostBuilder.HostInfoList

	For hostIndex=1:1:listOfHosts.Count()
	{
		#Dim host As termutils.HostParameter = listOfHosts.GetAt(hostIndex)
		#Dim hostActive As %Boolean = host.GetStatus()
		If ((hostActive) && (hostActive '= -1))
		{
			Do ..ActivateDeactivateHost(host, 0)
		}

	}
}

Method ActivateByHostName(hostName As %String)
{
	Do ..Activate(..HostBuilder.GetHostByName(hostName))
}

Method Activate(host As termutils.HostParameter)
{
	Do ..CheckSession()
	Do ..ActivateDeactivateHost(host, 1)
}

Method DeActivateByHostName(hostName As %String)
{
	Do ..DeActivate(..HostBuilder.GetHostByName(hostName))
}

Method DeActivate(host As termutils.HostParameter)
{
	Do ..CheckSession()
	Do ..ActivateDeactivateHost(.host, 0)
}

Method UpdateWSLocation(hostName As %String, location As %String)
{
	Do ..CheckSession()
	#Dim host As termutils.HostParameter = ..HostBuilder.GetHostByName(hostName)

	#Dim connections As %ListOfObjects = host.Connections
	For connectionIdex=1:1:connections.Count()
	{
		#Dim connection As termutils.ConnectionInfo = connections.GetAt(connectionIdex)
		If (connection.Direction = "OUT")
		{
			#Dim connectionID As %Integer = connection.Id
			&sql(UPDATE SQLUser.tcomPortWSCfg SET Location=:location WHERE ID = :connectionID)
		}
	}
}

Method UpdateApiKey(hostName As %String, apiKeyFile As %String, apiKeyValue As %String)
{
	Do ..CheckSession()
	Do ..HostBuilder.CorrectHostName(hostName)

	#Dim likeExpression As %String =  "%" _ hostName _ "%"

	&sql(UPDATE SQLUser.tcomPortWSCfg SET APIKeyFile=:apiKeyFile, APIKeyValue=:apiKeyValue
   	WHERE ConnectionName LIKE :likeExpression)
}

Method OrderEntryMode()
{
	Do ..CheckSession()

	&sql(SELECT ID, TS_TSDateTime, TS_TSUser  INTO :id, :tsDateTime, :tsUser FROM SQLUser.tsysParameters WHERE ParameterID = 'LIS_PRESENT_IN_LAB')

	#Dim tS As dt.TimeStamping = ##class(fn.cTimeStamping).FormatTS(tsUser,tsDateTime)
	Do ##class(co.sys.cParameters).Mod(id, 0, tS)
}

Method VCDefault()
{
	Do ..CheckSession()

	&sql(SELECT ID, TS_TSDateTime, TS_TSUser  INTO :parameterId, :parameterTSDateTime, :parameterTSUser FROM SQLUser.tsysParameters WHERE ParameterID = 'LIS_PRESENT_IN_LAB')

	#Dim tS As dt.TimeStamping = ##class(fn.cTimeStamping).FormatTS(parameterTSUser, parameterTSDateTime)
	Do ##class(co.sys.cParameters).Mod(parameterId, 1, tS)

	&sql(SELECT ID, TS_TSDateTime, TS_TSUser  INTO :hostId, :hostTSDateTime, :hostTSUser FROM SQLUser.thcahost WHERE HostName = 'LIS_HL7')

	Set tS = ##class(fn.cTimeStamping).FormatTS(hostTSUser, hostTSDateTime)
	#Dim lisActive As %Boolean = ##class(hca.scr.chcaHostStatus).CheckIsActive(, , ,hostId)
	If ('lisActive)
	{
		Do ##class(hca.scr.chcaHost).ModStatus(hostId, 1, tS)
	}

	Do ..ActivateDeactivateHost(..HostBuilder.GetHostByName("LIS_HL7"), 1)
}

ClassMethod ActivateDeactivateHost(host As termutils.HostParameter, enable As %Boolean)
{
	If (host.IsDriver)
	{
		#Dim hostID As %Integer
		&sql(SELECT ID INTO :hostID FROM SQLUser.thcaHost WHERE HostName = :host.HostName)
		Do ##class(Canelita).EnableDriver(hostID, enable)
	}
	Else
	{
		#dim connections As %ListOfObjects = host.Connections
		For connectionIdex=1:1:connections.Count()
		{
			Set $ZERROR = ""
			#dim connection As termutils.ConnectionInfo = connections.GetAt(connectionIdex)
			try
			{
				Do ##class(hca.scr.chcaHostStatus).ModActiveMain("", host.Id, connection.ConnectionType, connection.Id, enable, "")
			}
			catch
			{
				Set $ECODE = ##class(co.utl.Error).Get() _ host.HostName _ " " _ connection.Name
			}
		}
	}
}

ClassMethod CheckSession()
{
	Set $ZERROR = ""
	If ($Get(%gobjSession) = "")
	{
		Set %gobjSession = ##class(User.tsysSessions).%New()
		Set %gobjSession.UserName = "ROCHE"
	}
}

}

