Class termutils.WSConnection Extends termutils.ConnectionInfo
{

Property WSLocation As %String;

Property ApiKeyFile As %String;

Property ApiKeyValue As %String;

Method %OnNew(id As %Integer, name As %String, connectionType As %String, direction As %String, wsLocation As %String = "", apiKeyFile As %String = "", apiKeyValue As %String = "") As %Status
{
	Set ..WSLocation = wsLocation
	Set ..ApiKeyFile = apiKeyFile
	Set ..ApiKeyValue = apiKeyValue
	
	Return ##super(id, name, connectionType, direction)
}

Method ToDynamicObject() As %DynamicObject
{
	#Dim dynamicObject As %DynamicObject = ##super()
	Do dynamicObject.%Set("wsLocation", ..WSLocation)
	Do dynamicObject.%Set("apiKeyFile", ..ApiKeyFile)
	Do dynamicObject.%Set("apiKeyValue", ..ApiKeyValue)
	
	Return dynamicObject
}

}

