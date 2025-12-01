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
/// @See https://docs.intersystems.com/latest/csp/docbook/DocBook.UI.Page.cls?KEY=GIOD_TCP#GIOD_tcp_read
/// 
Class termutils.LLP Extends %RegisteredObject
{

/*

w $PRINCIPAL

Set m = ""
Set m = m _ "MSH|^~\&|Ventana Transmit|Sending Lab|Ventana|Receiving Lab|20210129145416||OML^O21|9967470|P|2.4" _ $Char(13, 10)
Set m = m _ "PID|||0001||Smith^John^P||19800101|M|" _ $Char(13, 10)
Set m = m _ "PV1|||||||MB1^Doe^Dr. Jane^H|" _ $Char(13, 10)
Set m = m _ "SAC|||||||20210129145416|" _ $Char(13, 10)

Write ##class(termutils.LLP).SendLlpMessage("127.0.0.1", 55551, m)

d ##class(hca.utl.chcaUTL).CleanOrdersAP()
d ##class(hca.utl.chcaUTL).CleanQueues(1)

h 0.5

<SB 127.0.0.1:55550>
MSH|^~\&|Ventana Transmit|Sending Lab|Ventana|Receiving Lab|20210414232633||OML^O21|{timehash}|P|2.4
PID|||0001||Smith^Jóhn^P||19800101|M|
PV1|||||||MB1^Doe^Dr. Jane^H|
SAC|||||||20210414232633|
ORC|NW|S16-0001^^1|||||||||||||||||||NWMC^Northwest Medical Center|
OBR|1|S16-0001||100^VANTAGE^STAIN^^|||202104132326|||||||202104132326|Liver^Liver Biopsy^Surgical Biopsy^Liver Surgery^^^Left^Left Liver|7369^Doe^John^F|||S16-0001;A;1;1^1^z|S16-0001;A;1^1^z|S16-0001;A^A^z||||||^^^^^""|
ORC|NW|S16-0001^^1|||||||||||||||||||NWMC^Northwest Medical Center|
OBR|2|S16-0001||100^VANTAGE^STAIN^^|||202104132326|||||||202104132326|Liver^Liver Biopsy^Surgical Biopsy^Liver Surgery^^^Left^Left Liver|7369^Doe^John^F|||S16-0001;A;1;2^2^z|S16-0001;A;1^1^z|S16-0001;A^A^z||||||^^^^^""|
<EB>

*/
Parameter VT As %Integer = {$Char($ZHEX("0B"))};

Parameter CR As %Integer = {$Char($ZHEX("0D"))};

Parameter FS As %Integer = {$Char($ZHEX("1C"))};

Property Ip As %String;

Property Port As %Integer;

Property LlpIdent As %String;

ClassMethod SendLlpMessage(ip As %String, port As %Integer, messages As %String) As %String
{
    #Dim llp As termutils.LLP = ##class(termutils.LLP).%New(ip, port)
    
    // Convert the message to UTF-8
    //Set message = $ZConvert(message, "I", "UTF8")
    
    #Dim messagesList As %List = $ListFromString(messages, "<->")

    #Dim e As %Exception.General
    Try 
    {
        Do llp.Open()

		For i=1:1:$ListLength(messagesList)
		{
			#Dim message As %String = $ListGet(messagesList, i)

		    Use $PRINCIPAL Write "Sending to: " _ ip _ ":" _ port, !
		    Use $PRINCIPAL Write "===", !
		    Use $PRINCIPAL Write message

	        Do llp.Send(message)
		}

        Use $PRINCIPAL Write !, "Receiving from: " _ ip _ ":" _ port, !
        Use $PRINCIPAL Write "===", !

        #Dim response As %String = llp.Receive()

        Use $PRINCIPAL Write response

        Do llp.Close()
        Return response
    }
    Catch (e)
    {
        Do llp.Close()
        Throw e
    }
}

Method %OnNew(ip As %String, port As %Integer) As %Status
{
    Set ..Ip = ip
    Set ..Port = port
    Set ..LlpIdent = "|TCP|" _ port
    Return $$$OK
}

Method Open()
{
    Open ..LlpIdent:(..Ip:..Port:"M"):200
    If ('$Test) 
    { 
        Throw ##class(%Exception.General).%New("cannot connect to: " _ ..Ip _ ":" _ ..Port)
    }
}

Method Send(message As %String)
{
    Set message = $Replace(message, $Char(10), ..#CR)
    Set message = $Replace(message, ..#CR _ ..#CR, ..#CR)
    Set message = ..#VT _ message _ ..#FS _ ..#CR

    Use ..LlpIdent Write message
    Use ..LlpIdent Write !
}

Method Receive() As %String
{
    #Dim response As %String = ""
    While ($$$OK)
    {
        Use ..LlpIdent READ *c:1
        If (c = -1)
        {
            Quit
        }
        If ('$Test)
        {
            Throw ##class(%Exception.General).%New("problem reading from: " _ ..Ip _ ":" _ ..Port)
        }
        Set response = response _ $Char(c)
    }

    Set response = $Replace(response, ..#VT, "")
    Set response = $Replace(response, ..#CR, $Char(13, 10))
    Set response = $Replace(response, ..#FS, "")
    Return response
}

Method Close()
{
    Close ..LlpIdent
    Use $PRINCIPAL
}

}

