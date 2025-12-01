Class termutils.ConnectionInfo Extends %RegisteredObject
{

Property Id As %String;

Property Direction As %String;

Property Name As %String;

Property ConnectionType As %String;

Method %OnNew(id As %Integer, name As %String, connectionType As %String, direction As %String) As %Status
{
	Set ..Id = id
	Set ..Name = name
	Set ..ConnectionType = connectionType
	Set ..Direction = direction
	
	Return $$$OK
}

Method ToDynamicObject() As %DynamicObject
{
	#Dim dynamicObject As %DynamicObject = {}
	Do dynamicObject.%Set("id", ..Id)
	Do dynamicObject.%Set("connectionName", ..Name)
	Do dynamicObject.%Set("connectionType", ..ConnectionType)
	Do dynamicObject.%Set("direction", ..Direction)
	
	Return dynamicObject
}

}

