Class Automation.Host.Connection.HL7Connection Extends Automation.Host.Connection.ConnectionInfo
{

Property Ip As %String;

Property Port As %String;

Method %OnNew(id As %Integer, name As %String, direction As %String, status As %Boolean, ip As %String, port As %String) As %Status
{
	Set ..Ip = ip
	Set ..Port = port
	
	Return ##super(id, name, direction, status)
}

Method ToDynamicObject() As %DynamicObject
{
	#Dim dynamicObject As %DynamicObject = ##super()
	Do dynamicObject.%Set("ip", ..Ip)
	Do dynamicObject.%Set("port", ..Port)
	
	Return dynamicObject
}

}
