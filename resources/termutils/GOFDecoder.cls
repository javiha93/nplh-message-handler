/// 	
/// 	 _____ _____ _____    ____  _____ _____ _____ ____  _____ _____ 
/// 	|   __|     |   __|  |    \|   __|     |     |    \|   __| __  |
/// 	|  |  |  |  |   __|  |  |  |   __|   --|  |  |  |  |   __|    -|
/// 	|_____|_____|__|     |____/|_____|_____|_____|____/|_____|__|__|
/// 	
///      Usage:
///           Set file = "D:\gitrepos\ventana-connect-core\Cache\ACB\Globals\Master\User_tsysResources.gof"
///           Set file = "D:\gitrepos\ventana-connect-core\Cache\ACB\Globals\Master\User_thcaIEActionsDefault.gof"
///           Write ##class(termutils.GOFDecoder).DecodeGofFile(file)
/// 
Class termutils.GOFDecoder Extends %RegisteredObject
{

// 2020-09-25 homscaum class created

Parameter DataBase64StartTag = "<DataBase64>";

Parameter DataBase64EndTag = "</DataBase64>";

ClassMethod DecodeGofFile(gofFileName As %String) As %String
{
    #Dim fileContent As %String = ..LoadTextFile(gofFileName)

    #Dim out As %String = ""
    #Dim start As %Integer = 1
    While $$$OK
    {
        #Dim openTagEndPos As %String = $Find(fileContent, ..#DataBase64StartTag, start)
        Quit:openTagEndPos=0
        
        Set out = out _ $Extract(fileContent, start, openTagEndPos - 1)
        
        #Dim closeTagEndPos As %String = $Find(fileContent, ..#DataBase64EndTag, openTagEndPos)
        
        #Dim base64 As %String = $Extract(fileContent, openTagEndPos, closeTagEndPos - $Length(..#DataBase64EndTag) - 1)
        Set base64 = $ZSTRIP(base64, "*WC")
        Set out = out _ $$Quote^%qcr($system.Encryption.Base64Decode(base64))
        
        Set start = closeTagEndPos - $Length(..#DataBase64EndTag)
    }
    
    Set out = out _ $Extract(fileContent, start, $Length(fileContent))
    Return out
}

ClassMethod LoadTextFile(fileName As %String) As %String [ Private ]
{
    #Dim r As %String = ""
    #Dim f As %File = ##class(%File).%New(fileName)
    Do f.Open("R")
    #Dim line As %String = f.Read()
    Set r = r _ line _ $Char(13, 10)
    While line '= ""
    {
        Set line = f.Read()
        //Set line = $ZCVT(line, "I", "UTF8")
        Set r = r _ line _ $Char(13, 10)
    }
    Do f.Close()
    Return r
}

}

