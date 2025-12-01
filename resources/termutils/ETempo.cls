///     
///        The Improved
///        ____           _          _   _____                   _             _ 
///       / ___|__ _  ___| |__   ___( ) |_   _|__ _ __ _ __ ___ (_)_ __   __ _| |
///      | |   / _` |/ __| '_ \ / _ \/    | |/ _ \ '__| '_ ` _ \| | '_ \ / _` | |
///      | |__| (_| | (__| | | |  __/     | |  __/ |  | | | | | | | | | | (_| | |
///       \____\__,_|\___|_| |_|\___|     |_|\___|_|  |_| |_| |_|_|_| |_|\__,_|_|
///        version 0.0.7b
/// 
///        https://code.roche.com/homscaum/cache-improved-terminal
/// 
/// W ##class(termutils.ETempo).Compute("08:06", "17:15", "00:45")
/// W ##class(termutils.ETempo).Compute("08:00", "15:30", "00:00")
/// 
Class termutils.ETempo
{

ClassMethod Main()
{
	W !, ##class(termutils.ETempo).Cats("08:60")
	W !, ##class(termutils.ETempo).Cats("08:59")
	W !, ##class(termutils.ETempo).Cats("08:51")
	W !, ##class(termutils.ETempo).Cats("08:30")
	W !, ##class(termutils.ETempo).Cats("08:15")
	W !, ##class(termutils.ETempo).Cats("08:00")
	W !, ##class(termutils.ETempo).Cats("08:01")
	/*W !,!,!
	W !, ##class(termutils.ETempo).Cats("08:11") // 
	W !, ##class(termutils.ETempo).Cats("09:23") //
	W !, ##class(termutils.ETempo).Cats("08:36") //
	W !, ##class(termutils.ETempo).Cats("08:00") //
	W !, ##class(termutils.ETempo).Cats("07:00") //

	W !, ##class(termutils.ETempo).Cats(##class(termutils.ETempo).Compute("08:18", "17:00", "00:45")) //
	W !, ##class(termutils.ETempo).Cats(##class(termutils.ETempo).Compute("08:15", "17:35", "00:45")) //
	W !, ##class(termutils.ETempo).Cats("09:35")
	W !, ##class(termutils.ETempo).Cats("09:19")
	W !, ##class(termutils.ETempo).Cats(##class(termutils.ETempo).Compute("08:30", "16:05", "00:00")) // 14
	W !, ##class(termutils.ETempo).Cats(##class(termutils.ETempo).Compute("08:31", "17:00", "00:45")) // 17*/

	W !, ##class(termutils.ETempo).Cats("08:45")
	W !, ##class(termutils.ETempo).Cats("08:25")
	W !, ##class(termutils.ETempo).Cats("08:23")
	W !, ##class(termutils.ETempo).Cats("07:31")


	W !, ##class(termutils.ETempo).Cats("07:43")
	W !, ##class(termutils.ETempo).Cats("08:23")
	W !, ##class(termutils.ETempo).Cats("07:34")
	W !, ##class(termutils.ETempo).Cats("08:05")
	W !, ##class(termutils.ETempo).Cats("06:28")

	W !, ##class(termutils.ETempo).Cats("07:50")
	W !, ##class(termutils.ETempo).Cats("07:59")
	W !, ##class(termutils.ETempo).Cats("09:06")
	W !, ##class(termutils.ETempo).Cats("08:41")
	W !, ##class(termutils.ETempo).Cats("06:44")
}

ClassMethod Cats(horaTrabajada As %String)
{
    #Dim h As %Integer = $zstrip($Piece(horaTrabajada, ":", 1), "*W")
    #Dim m As %Integer = $Piece(horaTrabajada, ":", 2)
    #Dim minsDec As %Double = (m * 100.0) \ 60.0
    If ($Length(minsDec) < 2)
    {
	    Set minsDec = "0" _ minsDec
    }
    
    //W !, "=> "_((h*60.0+m)/60.0), !
   	Return h _ "." _ minsDec
}

// ======================================================================

ClassMethod Compute(entrada As %String, sortida As %String, correcc As %String = "00:00")
{
    #Dim workedMins As %Integer = ..HoraToMins(sortida) - ..HoraToMins(entrada) - ..HoraToMins(correcc)
    Return ..MinsToHora(workedMins)
}

ClassMethod HoraToMins(hora As %String) As %Integer [ Private ]
{
    #Dim h As %Integer = $Piece(hora, ":", 1)
    #Dim m As %Integer = $Piece(hora, ":", 2)
    Return (h * 60) + m
}

ClassMethod MinsToHora(mins As %Integer) As %String [ Private ]
{
    #Dim h As %String = mins \ 60 // XXX integer division
    #Dim m As %String = mins # 60 // XXX integer modulus
    Return $Justify(h, 2) _ ":" _ $Replace($Justify(m, 2), " ", "0")
}

/*
    Do ##class(termutils.ETempo).PrintTable(
            "dill..dij (15')",
            "7:30", "09:30", "00:15",
            "15:30", "19:00", "00:15",
            "00:45"
    )
    Do ##class(termutils.ETempo).PrintTable(
            "dill..dij (5')",
            "7:30", "09:00", "00:05",
            "16:30", "18:00", "00:05",
            "00:45"
    )
    Do ##class(termutils.ETempo).PrintTable(
            "div (5')",
            "7:30", "09:30", "00:15",
            "14:30", "18:00", "00:15",
            "00:00"
    )
    Do ##class(termutils.ETempo).PrintTable(
            "div (5')",
            "7:30", "09:00", "00:05",
            "14:30", "18:00", "00:05",
            "00:00"
    )
*/
ClassMethod PrintTable(tableName As %String, entradaStart As %String, entradaEnd As %String, entradaStep As %String, sortidaStart As %String, sortidaEnd As %String, sortidaStep As %String, correccioDinar As %String = {"00:00"})
{

    Write !, tableName
    Write !, "========================="

    #Dim header As %String = ..MinsToHora(..HoraToMins(correccioDinar)) _ " |"
    For minsSortida=..HoraToMins(sortidaStart):..HoraToMins(sortidaStep):..HoraToMins(sortidaEnd)
    {
        Set header = header _ " " _ ..MinsToHora(minsSortida)
    }
    Write !, header
    
    #Dim headerSep As %String = ""
    For i=1:1:$Length(header)
    {
        Set:(i'=7) headerSep = headerSep _ "-"
        Set:(i =7) headerSep = headerSep _ "+"
    }
    Write !, headerSep
    
    For minsEntrada=..HoraToMins(entradaStart):..HoraToMins(entradaStep):..HoraToMins(entradaEnd)
    {
        #Dim fila As %String = ..MinsToHora(minsEntrada) _ " |"
        For minsSortida=..HoraToMins(sortidaStart):..HoraToMins(sortidaStep):..HoraToMins(sortidaEnd)
        {
            #Dim workedMins As %Integer = minsSortida - minsEntrada - ..HoraToMins(correccioDinar)
            #Dim workedTime As %String = ..MinsToHora(workedMins)
            Set fila = fila _ " " _ workedTime
        }
        Write !, fila
    }
    Write !
}

}

