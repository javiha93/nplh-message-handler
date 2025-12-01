Class Automation.Host.Connection.ConnectionInfo Extends %RegisteredObject
{

Property Id As %String;

Property Direction As %String;

Property Name As %String;

Property Status As %String;

Method %OnNew(id As %Integer, name As %String, direction As %String, status As %Boolean) As %Status
{
	Set ..Id = id
	Set ..Name = name
	Set ..Direction = direction
	Set ..Status = status
	
	Return $$$OK
}

Method ToDynamicObject() As %DynamicObject
{
	#Dim dynamicObject As %DynamicObject = {}
	Do dynamicObject.%Set("id", ..Id)
	Do dynamicObject.%Set("connectionName", ..Name)
	Do dynamicObject.%Set("direction", ..Direction)
	Do dynamicObject.%Set("status", ..Status)
	
	Return dynamicObject
}

}
