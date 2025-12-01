Class Automation.Host.Connection.RestConnection Extends Automation.Host.Connection.ConnectionInfo
{

Property ApiKeyValue As %String;

Method %OnNew(id As %Integer, name As %String, apiKeyValue As %String, status As %Boolean) As %Status
{
	Set ..ApiKeyValue = apiKeyValue
	
	Return ##super("", name, "IN", 1)
}

Method ToDynamicObject() As %DynamicObject
{
	#Dim dynamicObject As %DynamicObject = ##super()
	Do dynamicObject.%Set("apiKeyValue", ..ApiKeyValue)
	
	Return dynamicObject
}

}
