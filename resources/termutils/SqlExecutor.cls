/// ```
///        _____ _____ __       _____ __ __ _____ _____ _____ _____ _____ _____
///       |   __|     |  |     |   __|  |  |   __|     |  |  |_   _|     | __  |
///       |__   |  |  |  |__   |   __|-   -|   __|   --|  |  | | | |  |  |    -|
///       |_____|__  _|_____|  |_____|__|__|_____|_____|_____| |_| |_____|__|__|
///                |__|
///       &select ConnectionName,APIKeyFile,APIKeyValue from SQLUser.tcomPortWSCfg;
/// 
/// 
///       CONN>Do ##class(termutils.SqlExecutor).Execute("select ConnectionName,APIKeyFile,APIKeyValue from SQLUser.tcomPortWSCfg")
///       +--------------------+-------------------------------+-------------+
///       | ConnectionName     | APIKeyFile                    | APIKeyValue |
///       +--------------------+-------------------------------+-------------+
///       | VC to WS VRT       |                               |             |
///       | WS Symphony to VTG |                               |             |
///       | WS Symphony to VC  |                               |             |
///       | WS VRT to VC       |                               |             |
///       | WS HE 600 to VC    |                               |             |
///       | WS HE 600 to VTG   |                               |             |
///       | VC to VSS          |                               |             |
///       | VSS to VC          |                               |             |
///       | VC to UPATH        | C:\\tmp\\UpathCloudApiKey.key | 1234567890  |
///       | UPATH to VC        |                               |             |
///       | VC to UPATH CLOUD  |                               |             |
///       | UPATH CLOUD to VC  |                               |             |
///       +--------------------+-------------------------------+-------------+
///       12 row(s).
/// 
/// 
/// 	CONN>Do ##class(termutils.SqlExecutor).Execute("CREATE TABLE Person (Name VARCHAR(50), Sex VARCHAR(1))")
/// 	CREATE TABLE Person (Name VARCHAR(50), Sex VARCHAR(1))
/// 	0 affected row(s).
/// 	CONN>Do ##class(termutils.SqlExecutor).Execute("INSERT INTO Person (Name,Sex) VALUES ('Linda', 'F')")
/// 	INSERT INTO Person (Name,Sex) VALUES ('Linda', 'F')
/// 	1 affected row(s).
/// 	CONN>Do ##class(termutils.SqlExecutor).Execute("INSERT INTO Person (Name,Sex) VALUES ('Bob', 'M')")
/// 	INSERT INTO Person (Name,Sex) VALUES ('Bob', 'M')
/// 	1 affected row(s).
/// 	CONN>Do ##class(termutils.SqlExecutor).Execute("SELECT * FROM Person")
/// 	SELECT * FROM Person
/// 	+-------+-----+
/// 	| Name  | Sex |
/// 	+-------+-----+
/// 	| Linda | F   |
/// 	| Bob   | M   |
/// 	+-------+-----+
/// 	2 row(s).
/// 	CONN>Do ##class(termutils.SqlExecutor).Execute("DROP TABLE Person")
/// 	DROP TABLE Person
/// 	0 affected row(s).
/// 	CONN>
/// ```
/// 
/// @Author homscaum
/// @Since 2020-08-13
/// 
Class termutils.SqlExecutor
{

ClassMethod Execute(query As %String, displayHorizontalBars As %Boolean = 1)
{
    #Dim tStatement As %SQL.Statement = ##class(%SQL.Statement).%New()
    #Dim tStatus As %Integer = tStatement.%Prepare(query)
    If $System.Status.IsError(tStatus)
    {
        Do $System.Status.DisplayError(tStatus)
        Quit
    }

    #Dim rs As %SQL.StatementResult = tStatement.%Execute()
    #Dim meta As %SQL.StatementMetadata = rs.%GetMetadata()

    Write query

    If rs.%SQLCODE = 100
    {
        Write !, "No data."
    }
    ElseIf rs.%SQLCODE '= 0
    {
        Write !, "%SQLCODE: " _ rs.%SQLCODE _ "(" _ $SYSTEM.SQL.SQLCODE(rs.%SQLCODE) _ ")"
        Write !, "%Message: " _ rs.%Message
        Write !, "$ZError: " _ $ZError
    }
    ElseIf meta = ""
    {
        Write !, rs.%ROWCOUNT _ " affected row(s)."
    }
    Else
    {
        Write !

        /*
            loads the labels as the first row of the results
        */
        #Dim labels As %List = ""
        For i=1:1:meta.columnCount
        {
            #Dim label As %String = meta.columns.GetAt(i).label
            Set labels = labels _ $ListBuild(label)
        }
        /*
            iterate all the query results
        */
        #Dim result As %List = $ListBuild(labels)
        While rs.%Next()
        {
            Set row = ""
            For i=1:1:$ListLength(labels)
            {
                Set value = rs.%GetData(i)
                Set row = row _ $ListBuild(value)
            }
            Set result = result _ $ListBuild(row)
        }
        /*
            compute the column sizes in characters
        */
        #Dim colLengths As %List = ""
        For i=1:1:$ListLength(labels)
        {
            #Dim maxlen As %Integer = -1
            For j=1:1:$ListLength(result)
            {
                Set len = $Length($ListGet($ListGet(result, j), i))
                If maxlen < len
                {
                    set maxlen = len
                }
            }
            Set colLengths = colLengths _ $ListBuild(maxlen)
        }
        /*
            prints the table of results based on the previous steps
        */
        Do:displayHorizontalBars ..DisplayHorizontalBar(colLengths)
        For j=1:1:$ListLength(result)
        {
            Write "| "
            For i=1:1:$ListLength(labels)
            {
                Set width = $ListGet(colLengths, i)
                Set value = $ListGet($ListGet(result, j), i)
                Write value
                For k=1:1:width-$Length(value)
                {
                    Write " "
                }
                Write " | "
            }
            Write !
            If j = 1
            {
                Do:displayHorizontalBars ..DisplayHorizontalBar(colLengths)
            }
        }
        Do:displayHorizontalBars ..DisplayHorizontalBar(colLengths)
        Write rs.%ROWCOUNT _ " row(s)."
    }
}

/// 
/// Displays the table's horizontal lines (for instance "+---------+------+")
/// 
/// @columnLengths a `%List` of the lengths of the columns.
/// 
ClassMethod DisplayHorizontalBar(columnLengths As %List) [ Private ]
{
    Write "+"
    For i=1:1:$ListLength(columnLengths)
    {
        Write "-"
        Set width = $ListGet(columnLengths, i)
        For k=1:1:width
        {
            Write "-"
        }
        Write "-+"
    }
    Write !
}

}

