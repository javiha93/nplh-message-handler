Class Automation.Host.Connection.WSConnection Extends Automation.Host.Connection.ConnectionInfo
{

Property WSLocation As %String;

Property WSName As %String;

Property Port As %String;

Property Path As %String;

Property RequestorAddress As %String;

Property ApiKeyFile As %String;

Property ApiKeyValue As %String;

Property ApiKeyTag As %String;

Method %OnNew(id As %Integer, name As %String, direction As %String, status As %Boolean, wsLocation As %String = "", requestorAdress As %String = "", wsName As %String = "", apiKeyFile As %String = "", apiKeyValue As %String = "") As %Status
{
	Do ##super(id, name, direction, status)
	
	Set ..WSLocation = wsLocation
	If ((..Direction = "OUT") && (wsLocation = "") && (..Id '= ""))
	{
		Do ..FillwsLocation()
	}
	
	Set ..Port = $Piece($Piece(..WSLocation, ":", 3), "/", 1)
	Set ..Path = "/" _ $Piece($Piece(..WSLocation, ":", 3), "/", 2)
	Set ..WSName = "/csp/conn/" _ wsName
	Set ..RequestorAddress = requestorAdress
	Set ..ApiKeyFile = apiKeyFile
	Set ..ApiKeyValue = apiKeyValue
	
	If ((..ApiKeyFile = "") && (..ApiKeyValue = ""))
	{
		Do ..FillApiKey()
	}
	
	if (apiKeyValue '= "")
	{
		set ..ApiKeyTag = $Select($Find(wsName, "VANTAGEWS"): "X-VANTAGE-VCONNECT-APIKEY", 1: "X-NAVIFY-VCONNECT-APIKEY")
	}
	
	Return $$$OK
}

Method FillwsLocation()
{
	//Fill VSS, DP600, UPATH, UPATH CLOUD and VTGWS deafult connections
	Set ..WSLocation = $CASE(..Id,
								24: "http://127.0.0.1:22508/mockVSS",
								26: "http://127.0.0.1:22509/mockVCToUPATH11",
								28: "http://127.0.0.1:22510/mockVCToUPATHCLOUD",
								30: "http://127.0.0.1:22600/mockVCToDP600",
								32: "http://127.0.0.1:22511/mockVCToVTGWS",
								:"")
}

Method FillApiKey()
{
	//Fill UPATH CLOUD and VTGWS deafult connections
	Set ..ApiKeyFile = $CASE(..Id,
								28: "C:\\TMP\\API.key",
								29: "C:\\TMP\\API.key",
								32: "C:\\TMP\\API.key",
								33: "C:\\TMP\\API.key",
								:"")
	Set ..ApiKeyValue = $CASE(..Id,
								28: "PruebaAPIKey1",
								29: "PruebaAPIKey1",
								32: "PruebaAPIKey1",
								33: "PruebaAPIKey1",
								:"")
}

Method ToDynamicObject() As %DynamicObject
{
	
	#Dim dynamicObject As %DynamicObject = ##super()
	Do dynamicObject.%Set("port", ..Port)
	Do dynamicObject.%Set("path", ..Path)
	Do dynamicObject.%Set("wsLocation", ..WSLocation)
	Do dynamicObject.%Set("requestorAddress", ..RequestorAddress)
	Do dynamicObject.%Set("wsName", ..WSName)
	Do dynamicObject.%Set("apiKeyFile", ..ApiKeyFile)
	Do dynamicObject.%Set("apiKeyValue", ..ApiKeyValue)
	Do dynamicObject.%Set("apiKeyTag", ..ApiKeyTag)
	
	Return dynamicObject
}

}

