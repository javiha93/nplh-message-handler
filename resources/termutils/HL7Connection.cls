Class termutils.HL7Connection Extends termutils.ConnectionInfo
{

Property Port As %String;

Method %OnNew(id As %Integer, name As %String, connectionType As %String, direction As %String, port As %String) As %Status
{
	Set ..Port = port
	
	Return ##super(id, name, connectionType, direction)
}

Method ToDynamicObject() As %DynamicObject
{
	#Dim dynamicObject As %DynamicObject = ##super()
	Do dynamicObject.%Set("port", ..Port)
	
	Return dynamicObject
}

}

