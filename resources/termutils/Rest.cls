/// ```
///        The Improved
///        ____           _          _   _____                   _             _
///       / ___|__ _  ___| |__   ___( ) |_   _|__ _ __ _ __ ___ (_)_ __   __ _| |
///      | |   / _` |/ __| '_ \ / _ \/    | |/ _ \ '__| '_ ` _ \| | '_ \ / _` | |
///      | |__| (_| | (__| | | |  __/     | |  __/ |  | | | | | | | | | | (_| | |
///       \____\__,_|\___|_| |_|\___|     |_|\___|_|  |_| |_| |_|_|_| |_|\__,_|_|
///        v0.0.7b
/// 
///        https://code.roche.com/homscaum/cache-improved-terminal
/// ```
/// 
/// @See https://docs.intersystems.com/latest/csp/documatic/%25CSP.Documatic.cls?LIBRARY=%25SYS&CLASSNAME=%25Net.HttpRequest
/// @See https://docs.intersystems.com/latest/csp/docbook/DocBook.UI.Page.cls?KEY=GNET_http
/// 
Class termutils.Rest Extends %RegisteredObject
{

ClassMethod Test()
{
	#Dim restTemplate As termutils.Rest = ##class(termutils.Rest).%New("127.0.0.1", 80)
	Do restTemplate.DoRestRequest("/csp/conn/rest/oc/driver", "GET", "X_API_KEY", "AutomaticProcessDaemonRTCMW031583CONN", "")
}

Parameter Get As %String = "GET";

Parameter Post As %String = "POST";

Parameter Put As %String = "PUT";

Parameter Patch As %String = "PATCH";

Parameter Delete As %String = "DELETE";

Property Ip As %String;

Property Port As %Integer;

/// ***************************************************************************************
/// * Function: %OnNew
/// * Desc.   : 
/// * Param.  : ip (%String): 
/// *           port (%Integer): 
/// * Returns : %Status
/// ***************************************************************************************
Method %OnNew(ip As %String, port As %Integer) As %Status
{
	Set ..Ip = ip
	Set ..Port = port
	Quit $$$OK
}

Method DoRestRequest(requestPath As %String, methodName As %String, apiKeyName As %String, apiKeyValue As %String, requestBody As %String = {$$$NULLOREF})
{
	
	#Dim responseBody As %String = ""
	#Dim headersMultiDim = ""
	
	Set headersMultiDim("Content-type") = "application/json;charset=utf-8"
	If (apiKeyName '= "")
	{
		Set headersMultiDim(apiKeyName) = apiKeyValue
	}
	
	#Dim httpStatusCode As %Integer = ..DoHttpRequest(requestPath, methodName, .headersMultiDim, requestBody, .responseBody)
	Write !, httpStatusCode _ " - " _ responseBody
}

/// ***************************************************************************************
/// * Function: DoHttpRequest
/// * Desc.   : 
/// * Param.  : requestPath (%String): 
/// *           methodName (%String): 
/// *           &headersMultiDim: 
/// *           requestBody (%String=$$$NULLOREF): 
/// *           &responseBody (%String): 
/// *           debug (%Boolean=0): 
/// * Returns : %Integer
/// ***************************************************************************************
Method DoHttpRequest(requestPath As %String, methodName As %String, ByRef headersMultiDim, requestBody As %String = {$$$NULLOREF}, ByRef responseBody As %String, debug As %Boolean = 0) As %Integer
{
	#Dim httprequest As %Net.HttpRequest = ##class(%Net.HttpRequest).%New()
	Set httprequest.Https = '$$$OK
	Set httprequest.Server = ..Ip
	Set httprequest.Port = ..Port

	#Dim index As %String = $Order(headersMultiDim(""))
	While (index '= $$$NULLOREF) 
	{
		If (debug)
		{
			Write !,"Header: " _ index_" => "_headersMultiDim(index)
		}
	   Do httprequest.SetHeader(index, headersMultiDim(index))
	   Set index = $Order(headersMultiDim(index))
	}
	
	If (requestBody '= $$$NULLOREF)
	{
		Do httprequest.EntityBody.Write(requestBody)
	}
	
	#Dim status As %Status
	If (methodName = ..#Get)
	{
		Set status = httprequest.Get(requestPath)
	}
	ElseIf (methodName = ..#Post)
	{
		Set status = httprequest.Post(requestPath)
	}
	ElseIf (methodName = ..#Put)
	{
		Set status = httprequest.Put(requestPath)
	}
	ElseIf (methodName = ..#Patch)
	{
		Set status = httprequest.Patch(requestPath)
	}
	ElseIf (methodName = ..#Delete)
	{
		Set status = httprequest.Delete(requestPath)
	}
	Else 
	{
		Throw ##class(%Exception.General).%New("HTTP method not defined: '" _ methodName _ "'")
	}
	
	If ($System.Status.IsError(status))
	{
		Throw ##class(%Exception.General).%New($System.Status.GetErrorText(status))
	}
    
    If (debug) 
    {        
		Do httprequest.HttpResponse.OutputToDevice()
    }
	
	/// %Net.HttpResponse:
	/// The stream or a string contains all the data sent by the web server after the HTTP headers.
	/// You can test if this is a stream with $isobject(response.Data) and if it is not a stream
	/// then it is a string with the data in it.
	#Dim response As %String
	If ($IsObject(httprequest.HttpResponse.Data))
	{
	    Set response = ""
	    #Dim stream As %Stream.Object = httprequest.HttpResponse.Data
	    While ('stream.AtEnd)
	    {
	        #Dim line As %String = stream.ReadLine()
	        Set response = response _ line _ $Char(13, 10)
	    }
	} 
	Else
	{
	    Set response = httprequest.HttpResponse.Data
	}
	
	Set responseBody = response
	Return httprequest.HttpResponse.StatusCode
}

}

