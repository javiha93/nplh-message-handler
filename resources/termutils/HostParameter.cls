Class termutils.HostParameter Extends %RegisteredObject
{

Property Id As %Integer;

Property HostStatus As %Boolean;

Property HostName As %String;

Property Connections As list Of termutils.ConnectionInfo;

Property HostType As %String;

Property TimeZone As User.tsysTimeZones;

Property OriginalHostName As %String;

Property IsHL7 As %Boolean;

Property IsDriver As %Boolean;

Method %OnNew(id As %Integer, hostName As %String, hostType As %String, timeZone As User.tsysTimeZones, connections As %List, originalHostName As %String, isDriver As %Boolean = 0, isHL7 As %Boolean = 1) As %Status
{
	Set ..Id = id
	Set ..HostName = hostName
	Set ..Connections = connections
	Set ..HostType = hostType
	Set ..TimeZone = timeZone
	Set ..OriginalHostName = originalHostName
	Set ..IsHL7 = isHL7
	Set ..IsDriver = isDriver
	Set ..HostStatus = ..GetStatus()

	Return $$$OK
}

Method GetStatus() As %Boolean
{
	set statusInfo = 0
	set hostName = ..HostName
	&sql(SELECT Status INTO :status FROM SQLUser.thcahost WHERE HostName = :hostName)
	if (status)
	{
		set statusInfo = 1
		if (..IsDriver)
		{
			return statusInfo
		}
		#dim isActive As %Boolean = $$$NO

		for connectionIndex=1:1:..Connections.Count()
		{
			#dim connection As termutils.ConnectionInfo = ..Connections.GetAt(connectionIndex)
			set isActive = ##class(hca.scr.chcaHostStatus).CheckIsActive(, , connection.Direction, ..Id, , , connection.Id, connection.ConnectionType)
			if ('isActive)
			{
				set statusInfo = "0"
			}
		}
	}
	elseif (((..Connections.Count() = 0) && (..Id '= "")) || ((..IsDriver) && (..Id '= "")))
	{
		set statusInfo = 0
	}
	
	Return statusInfo
}

Method ToDynamicObject() As %DynamicObject
{
	#Dim dynamicObject As %DynamicObject = {}
	Do dynamicObject.%Set("hostId", ..Id)
	Do dynamicObject.%Set("hostName", ..HostName)
	Do dynamicObject.%Set("hostStatus", $Select(..HostStatus: "true",1: "false"))
	Do dynamicObject.%Set("originalHostName", ..OriginalHostName)
	Do dynamicObject.%Set("isHL7", $Select(..IsHL7: "true",1: "false"))
	Do dynamicObject.%Set("isDriver", $Select(..IsDriver: "true",1: "false"))
	#Dim connections As %DynamicObject = {}
	For connectionIdex=1:1:..Connections.Count()
	{
		Do connections.%Set(..Connections.GetAt(connectionIdex).Name, ..Connections.GetAt(connectionIdex).ToDynamicObject())
	}
	
	Do dynamicObject.%Set("connections", connections)
		
	
	Return dynamicObject
}

}

