///       _____ _____ _____    _____ _____ _ __    _____ 
///      |   __|     |   __|  |  |  |_   _|_|  |  |   __| The GOF <=> Plain Text Converter
///      |  |  |  |  |   __|  |  |  | | | | |  |__|__   | by homscaum
///      |_____|_____|__|     |_____| |_| |_|_____|_____| 2021
///                                                      
/// 
Class termutils.GOFUtils3 Extends %RegisteredObject
{

// 2021-01-06 homscaum class created

/*    
    Set gofsPath = "D:\ROCHE\Globals\Master\"
    Do ##class(termutils.GOFUtils3).DecodeGofToPlainFile(gofsPath_"User_tsysResources.gof")
    Do ##class(termutils.GOFUtils3).EncodePlainToGofFile(gofsPath_"User_tsysResources.gof.plain")
    Do ##class(termutils.GOFUtils3).DecodeGofToConsole("D:\ROCHE\Globals\Master\User_tsysResources.gof")

    Set gofsPath = "D:\gitrepos\ventana-connect-core\Cache\ACB\Globals\Master\"
    Do ##class(termutils.GOFUtils3).DecodeGofToPlainFile(gofsPath_"User_thcaFieldDefinition.gof")
    Do ##class(termutils.GOFUtils3).EncodePlainToGofFile(gofsPath_"User_thcaFieldDefinition.gof.plain")
*/
Parameter DataBase64StartTag As %String = "<DataBase64>";

Parameter DataBase64EndTag As %String = "</DataBase64>";

Parameter ActionDecode As %Integer = 1;

Parameter ActionEncode As %Integer = 2;

ClassMethod DecodeGofToConsole(originGofFileName As %String, targetPlainFileName As %String = {originGofFileName _ ".plain"})
{
    #Dim fileContent As %String = ..LoadTextFile(originGofFileName)
    #Dim decodedGofContent As %String = ..CodecGof(fileContent, ..#ActionDecode)
    Write decodedGofContent
}

ClassMethod DecodeGofToPlainFile(originGofFileName As %String, targetPlainFileName As %String = {originGofFileName _ ".plain"})
{
    #Dim fileContent As %String = ..LoadTextFile(originGofFileName)
    #Dim decodedGofContent As %String = ..CodecGof(fileContent, ..#ActionDecode)
    Do ..StoreTextFile(targetPlainFileName, decodedGofContent)
}

ClassMethod EncodePlainToGofFile(originPlainFileName As %String, targetGofFileName As %String = {originPlainFileName _ ".gof2"})
{
    #Dim fileContent As %String = ..LoadTextFile(originPlainFileName)
    #Dim decodedGofContent As %String = ..CodecGof(fileContent, ..#ActionEncode)
    Do ..StoreTextFile(targetGofFileName, decodedGofContent)
}

ClassMethod CodecGof(fileContent As %String, action As %Integer) As %String
{
    #Dim out As %String = ""
    #Dim start As %Integer = 1
    While $$$OK
    {
        #Dim openTagEndPos As %String = $Find(fileContent, ..#DataBase64StartTag, start)
        Quit:openTagEndPos=0
        
        Set out = out _ $Extract(fileContent, start, openTagEndPos - 1)
        
        #Dim closeTagEndPos As %String = $Find(fileContent, ..#DataBase64EndTag, openTagEndPos)
        
        #Dim tagContent As %String = $Extract(fileContent, openTagEndPos, closeTagEndPos - $Length(..#DataBase64EndTag) - 1)
        If action = ..#ActionDecode 
        {
            Set tagContent = $ZSTRIP(tagContent, "*WC")
            Set out = out _ $$Quote^%qcr($system.Encryption.Base64Decode(tagContent))
        }
        ElseIf action = ..#ActionEncode  
        {
            #Dim value As %String = ""
            Xecute ("(out) Set out = " _ tagContent, .value)
            Set out = out _ $system.Encryption.Base64Encode(value) _ $Char(13, 10)
        }
        Else 
        {
            Throw ##class(%Exception.General).%New("unknown action: " _ action)
        }
        
        Set start = closeTagEndPos - $Length(..#DataBase64EndTag)
    }
    
    Set out = out _ $Extract(fileContent, start, $Length(fileContent))
    Return out
}

ClassMethod LoadTextFile(fileName As %String) As %String [ Private ]
{
    #Dim r As %String = ""
    #Dim stream As %FileCharacterStream = ..PrepareFileStream(fileName)
    While 'stream.AtEnd {
        Set line=stream.ReadLine()
        Set r = r _ line _ $Char(13, 10)
    }
    Return r
}

ClassMethod StoreTextFile(fileName As %String, content As %String) [ Private ]
{
    #Dim stream As %FileCharacterStream = ..PrepareFileStream(fileName)
    Do stream.Write(content)
    Do stream.%Save()
}

ClassMethod PrepareFileStream(fileName As %String) As %FileCharacterStream [ Private ]
{
    #Dim stream As %FileCharacterStream = ##class(%FileCharacterStream).%New()
    Do stream.TranslateTableSet("UTF8")
    Set stream.BOM = $Char(239, 187, 191)
    Set stream.Filename = fileName
    Return stream
}

}

