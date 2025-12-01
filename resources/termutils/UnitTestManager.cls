Class termutils.UnitTestManager
{

/*
	This method iterates the global "^UnitTest.Result" in order to extract some statistics from the current UT execution.

	The global "^UnitTest.Result" has the form:
	
		^UnitTest.Result(459,"Rule\routing","UnitTest.Rule.routing.RoutingEngine","TestWhenError")=$lb(0,.000363,"","There are failed TestAsserts")
		^UnitTest.Result(459,"Rule\routing","UnitTest.Rule.routing.RoutingEngine","TestWhenError",1)=$lb(1,"AssertEquals","msgLIS is null")
		^UnitTest.Result(459,"Rule\routing","UnitTest.Rule.routing.RoutingEngine","TestWhenError",2)=$lb(0,"AssertEquals","msgLIS is null")
		^UnitTest.Result(459,"Rule\routing","UnitTest.Rule.routing.RoutingEngine","TestWhenError",3)=$lb(1,"LogMessage","Duration of execution: .000363 sec.")


	An example of the output in a case of failing tests:
			
		FAILED: co\fse:UnitTest.co.fse.cMonitoring:TestGetMessagesToDelete
		FAILED: hca\exp:UnitTest.hca.exp.chcaExport:TestAbleToSend
		FAILED: hca\imp:UnitTest.hca.imp.chcaAPOrderUtils:TestCheckPrefixSuffix
		FAILED: hca\imp:UnitTest.hca.imp.chcaAPOrderUtils:TestValidPreSuf
		FAILED: hca\scr:UnitTest.hca.scr.chcaHost:TestMod
		FAILED: hca\scr:UnitTest.hca.scr.chcaIEConf:TestImportFromFile
		FAILED: hca\scr\IE:UnitTest.hca.scr.IE.chcaManualHostUpload:TestFillEndInformation
		FAILED: hca\scr\IE:UnitTest.hca.scr.IE.chcaManualHostUpload:TestRemoveUnwantedObjects_When_DifferentIdentifiers

		===========================================
		Total executed tests:  708
		Total passed tests:    700
		Total failed tests:    8
		Total elapsed time:    175.700928
		===========================================
		Some tests FAILED in suites:
		  co\fse,hca\exp,hca\imp,hca\scr,hca\scr\IE,hca\validate
*/
ClassMethod PrintTestExecutionReport(testId As %Integer)
{
	Write !
	
	Set testsOK = 0
	Set testsKO = 0
	Set testsTotal = 0
	Set timeElapsed = 0.0
	Set totalAsserts = 0
	Set node = $Query(^UnitTest.Result(testId))
	Set currentTestId = testId
	While node '= ""
	{
		Set indexes = $Piece($Piece(node, "(", 2), ")", 1)

		Set currentTestId = +$Piece(indexes, ",", 1)
		If currentTestId '= testId 
		{
			Quit
		}

		/* "deep" is the current deep in the tree (the # of indexes in the multidim) */
		Set deep = $Length(indexes, ",")

		If deep = 4
		{
			Set list = @node
			Set testResult = $List(list, 1)
			If testResult = 1 {
				Set testsOK = testsOK + 1
			} Else {
				Set testsKO = testsKO + 1
				
				// extract the name of the failed test from the indexes
				Set indexTestPackage = $Piece($Piece(indexes, ",", 2), """", 2)
				Set indexTestClass = $Piece($Piece(indexes, ",", 3), """", 2)
				Set indexTestMethod = $Piece($Piece(indexes, ",", 4), """", 2)
				Write !,"FAILED: "_indexTestPackage_":"_indexTestClass_":"_indexTestMethod
			}
			Set testsTotal = testsTotal + 1
			Set timeElapsed = timeElapsed + $List(list, 2)
		}
		If deep = 5
		{
			Set totalAsserts = totalAsserts + 1
		}
		
		Set node = $Query(@node)
	}
	
	Write !
	Write !, "==========================================="
	Write !, "Total executed tests:  "_testsTotal
	Write !, "Total passed tests:    "_testsOK
	Write !, "Total failed tests:    "_testsKO
	Write !, "Total asserts:         "_totalAsserts
	Write !, "Total elapsed time:    "_(timeElapsed\1)_" seconds"
	Write !, "==========================================="
}

}

