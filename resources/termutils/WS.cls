///     
///        The Improved
///        ____           _          _   _____                   _             _ 
///       / ___|__ _  ___| |__   ___( ) |_   _|__ _ __ _ __ ___ (_)_ __   __ _| |
///      | |   / _` |/ __| '_ \ / _ \/    | |/ _ \ '__| '_ ` _ \| | '_ \ / _` | |
///      | |__| (_| | (__| | | |  __/     | |  __/ |  | | | | | | | | | | (_| | |
///       \____\__,_|\___|_| |_|\___|     |_|\___|_|  |_| |_| |_|_|_| |_|\__,_|_|
///        version 0.0.7b
/// 
///        https://code.roche.com/marti.homs_caussa/cache-improved-terminal
/// 
/// 
/// https://docs.intersystems.com/latest/csp/documatic/%25CSP.Documatic.cls?LIBRARY=%25SYS&CLASSNAME=%25Net.HttpRequest
/// d ##class(termutils.WS).Test()
/// 
Class termutils.WS Extends %RegisteredObject
{

ClassMethod Test()
{
	#Dim m As %String = ""
	Set m = m _ "<soapenv:Envelope xmlns:soapenv=""http://schemas.xmlsoap.org/soap/envelope/"" xmlns:web=""http://webservice.dp600.ventana.com/"">"
	Set m = m _ "<soapenv:Header/>"
	Set m = m _ "<soapenv:Body>"
	Set m = m _ "  <web:sendUpdatedSlideStatus>"
	Set m = m _ "    <arg0>CASE1;A;1;1</arg0>"
	Set m = m _ "    <arg1>SCANNED</arg1>"
	Set m = m _ "  </web:sendUpdatedSlideStatus>"
	Set m = m _ "</soapenv:Body>"
	Set m = m _ "</soapenv:Envelope>"
	
	Do ##class(termutils.WS).SoapRequest("127.0.0.1", 80, "/csp/conn/ws.ext.DP600.DP600ToVCWebServiceSoap.CLS", "sendUpdatedSlideStatus", "X-NAVIFY-VCONNECT-APIKEY", "1234567890", m)
}

ClassMethod SoapRequest(ip As %String, port As %Integer, https As %Integer, requestPath As %String, soapAction As %String, apiKeyName As %String, apiKeyValue As %String, requestBody As %String)
{
	#Dim httprequest As %Net.HttpRequest = ##class(%Net.HttpRequest).%New()
	Set httprequest.Https = https
	Set httprequest.Server = ip
	Set httprequest.Port = port
	Do httprequest.SetHeader("Content-type", "text/xml;charset=utf-8")
	Do httprequest.SetHeader("SOAPAction", soapAction)
	If (apiKeyName '= "")
	{
		Do httprequest.SetHeader(apiKeyName, apiKeyValue)
	}
	If (https)
	{
		Set httprequest.SSLConfiguration = "DriverEngine"
	}

    Write "Sending to: " _ ip _ ":" _ port _ requestPath _ "#" _ soapAction, !
    Write "===", !
    Write requestBody, !
	
	Do httprequest.EntityBody.Write(requestBody)
	
	#Dim status As %Status = httprequest.Post(requestPath)
	If ($System.Status.IsError(status))
	{
		Throw ##class(%Exception.General).%New($System.Status.GetErrorText(status))
	}
            
	// Do httprequest.HttpResponse.OutputToDevice()
	
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
	
	Write "Receiving from: " _ ip _ ":" _ port _ requestPath _ "#" _ soapAction, !
	Write "===", !
	Write ..FormatXmlString(response), !
}

ClassMethod FormatXmlString(xml As %String)
{
	#Dim reader As %XML.Reader = ##class(%XML.Reader).%New()
    Do reader.OpenString(xml)
    Do reader.Rewind()
    
    #Dim document As %XML.Document = reader.Document

	// https://docs.intersystems.com/latest/csp/documatic/%25CSP.Documatic.cls?&LIBRARY=%25SYS&CLASSNAME=%25XML.Writer#PROPERTY_SuppressXmlns
	#Dim writer As %XML.Writer = ##class(%XML.Writer).%New()
	Set writer.Indent = $$$OK
	Set writer.Charset = "UTF-8"
   	Do writer.OutputToString()
	Do writer.Document(document)
	Return writer.GetXMLString()
}

}

