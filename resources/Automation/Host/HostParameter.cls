Class Automation.Host.HostParameter Extends %RegisteredObject
{

Property Id As %Integer;

Property HostName As %String;

Property HostType As %String;

Property Default As %Boolean;

Property ConnectionsType As %String;

Property Connections As list Of Automation.Host.Connection.ConnectionInfo;

Method %OnNew(id As %Integer, hostName As %String, hostType As %String, default As %Boolean, connections As %List, connectionsType As %String) As %Status
{
	Set ..Id = id
	Set ..HostName = hostName
	Set ..HostType = $Select((hostType = ""): "LIS", 1: hostType)
	Set ..Default = default
	Set ..ConnectionsType = connectionsType
	Set ..Connections = connections

	Return $$$OK
}

Method ToDynamicObject() As %DynamicObject
{
	#Dim dynamicObject As %DynamicObject = {}
	Do dynamicObject.%Set("hostId", ..Id)
	Do dynamicObject.%Set("hostName", ..HostName)
	Do dynamicObject.%Set("hostType", ..HostType)
	Do dynamicObject.%Set("isDefault", ..Default)
	Do dynamicObject.%Set("connectionType", ..ConnectionsType)
	#Dim connections As %DynamicArray = []
	For connectionIdex=1:1:..Connections.Count()
	{
		Do connections.%Push(..Connections.GetAt(connectionIdex).ToDynamicObject())
	}
	
	Do dynamicObject.%Set("connections", connections)
		
	
	Return dynamicObject
}

}

