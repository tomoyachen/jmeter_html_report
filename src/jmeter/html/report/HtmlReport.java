package jmeter.html.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class HtmlReport {
	
	
	HtmlReport(LinkedHashMap<String, ArrayList> reportMap){
		HtmlReport.reportMap  = reportMap;
		
	}

	private static String title = "";
	
	private static String tester = "";
	
	private static String startTime = "";
	
	private static String totalTime = "";
	
	public static LinkedHashMap<String, ArrayList> reportMap = null;
	
	
	public static void setReportInfo(String title, String tester, String startTime, String totalTime) {
		
		HtmlReport.title = title;
		HtmlReport.tester = tester;
		HtmlReport.startTime = startTime;
		HtmlReport.totalTime = totalTime;
		
	}
	
	
	private static String addTestSuite(int TestSuiteIndex, String TestStuteName, String TestCaseCount, String PassCount, String FailCount, String ErrorCount){
		String TestSuite = "";
		String id = "c" + TestSuiteIndex;
		TestSuite = "" + 
				"<tr class='passClass warning'>\r\n" + 
				"    <td>" + TestStuteName + "</td>\r\n" + 
				"    <td class=\"text-center\">" + TestCaseCount + "</td>\r\n" + 
				"    <td class=\"text-center\">" + PassCount + "</td>\r\n" + 
				"    <td class=\"text-center\">" + FailCount + "</td>\r\n" + 
				"    <td class=\"text-center\">" + ErrorCount + "</td>\r\n" + 
				"    <td class=\"text-center\"><a href=\"javascript:showClassDetail('" + id + "', " + TestCaseCount + ")\" class=\"detail\" id='" + id + "'>详细</a></td>\r\n" + 
				"</tr>";
		
		return TestSuite;
	}
	
	
	private static String addTestCase(int TestSuiteIndex, int TestCaseIndex, String TestCaseName, String pre){
		String TestCase = "";
		String id = "pt" + TestSuiteIndex + "_" + TestCaseIndex;
		TestCase = "" + 
				"<tr id='" + id +"' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>" + TestCaseName + "</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_"  + id + "' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_"  + id + "'>通过</button>\r\n" + 
				"    <div id='div_"  + id + "' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_"  + id + "' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_"  + id + "'>通过</button>\r\n" + 
				"    <div id='div_"  + id + "' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				pre + 
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>";
		
		return TestCase;
	}
	

	public static String genHtmlBody() {
		
		String htmlBody = "";
		Map map = HtmlReport.reportMap;
		Iterator testSuites = reportMap.entrySet().iterator();
		
		//循环testsuites
		int testSuiteIndex = 1;
		while (testSuites.hasNext()) {
			Map.Entry testSuite = (Map.Entry) testSuites.next();
			String key = (String) testSuite.getKey(); //测试集名称
			ArrayList val = (ArrayList) testSuite.getValue();
//			System.out.println(val);
			String TestCaseCount = String.valueOf(val.size());
			htmlBody += addTestSuite(testSuiteIndex, key, TestCaseCount, "9", "9", "9");
			
//			System.out.println(addTestSuite(key, "9", "9", "9", "9"));
			

			
			//循环testcases
			Iterator testCases = val.iterator();
			int testCaseIndex = 1;
			while (testCases.hasNext()) {
				HashMap testCase =  (HashMap) testCases.next();
				String testCaseName = (String) testCase.get("label");
				System.out.println(testSuiteIndex + ", "  + testCaseIndex);
				htmlBody += addTestCase(testSuiteIndex, testCaseIndex, testCaseName, testCaseName);
				
				testCaseIndex ++;

			}
			
			testSuiteIndex ++;
			
		}
		
		return htmlBody;
	}
	
	
	
	
	
	
	public static String genHtmlReport() {
		
		String htmlReport = "" + 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n" + 
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + 
				"<head>\r\n" + 
				"    <title>" + HtmlReport.title + "</title>\r\n" + 
				"\r\n" + 
				"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\r\n" + 
				"    <link href=\"https://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.css\" rel=\"stylesheet\">\r\n" + 
				"    <link rel=\"stylesheet\" href=\"https://pypi.org/static/css/warehouse.d8da1ae4.css\">\r\n" + 
				"    <link rel=\"stylesheet\" href=\"https://pypi.org/static/css/fontawesome.3173d2f0.css\">\r\n" + 
				"    <link rel=\"stylesheet\" href=\"https://pypi.org/static/css/regular.19624371.css\">\r\n" + 
				"    <link rel=\"stylesheet\" href=\"https://pypi.org/static/css/solid.f478cfb1.css\">\r\n" + 
				"    <link rel=\"stylesheet\" href=\"https://pypi.org/static/css/brands.1ea560bf.css\">\r\n" + 
				"    <script src=\"https://cdn.bootcss.com/jquery/2.1.4/jquery.js\"></script>\r\n" + 
				"    <script src=\"https://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js\"></script>\r\n" + 
				"\r\n" + 
				"    \r\n" + 
				"<style type=\"text/css\" media=\"screen\">\r\n" + 
				"body        { font-family: Microsoft YaHei,Tahoma,arial,helvetica,sans-serif;padding: 20px; font-size: 80%; }\r\n" + 
				"table       { font-size: 100%; }\r\n" + 
				"\r\n" + 
				"/* -- heading ---------------------------------------------------------------------- */\r\n" + 
				".heading {\r\n" + 
				"    margin-top: 0ex;\r\n" + 
				"    margin-bottom: 1ex;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				".heading .description {\r\n" + 
				"    margin-top: 4ex;\r\n" + 
				"    margin-bottom: 6ex;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"/* -- report ------------------------------------------------------------------------ */\r\n" + 
				"#total_row  { font-weight: bold; }\r\n" + 
				".passCase   { color: #5cb85c; }\r\n" + 
				".failCase   { color: #d9534f; font-weight: bold; }\r\n" + 
				".errorCase  { color: #f0ad4e; font-weight: bold; }\r\n" + 
				".hiddenRow  { display: none; }\r\n" + 
				".testcase   { margin-left: 2em; }\r\n" + 
				"</style>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    <script language=\"javascript\" type=\"text/javascript\">\r\n" + 
				"output_list = Array();\r\n" + 
				"\r\n" + 
				"/*level 调整增加只显示通过用例的分类 --Findyou\r\n" + 
				"0:Summary //all hiddenRow\r\n" + 
				"1:Failed  //pt hiddenRow, ft none\r\n" + 
				"2:Pass    //pt none, ft hiddenRow\r\n" + 
				"3:All     //pt none, ft none\r\n" + 
				"*/\r\n" + 
				"function showCase(level) {\r\n" + 
				"    trs = document.getElementsByTagName(\"tr\");\r\n" + 
				"    for (var i = 0; i < trs.length; i++) {\r\n" + 
				"        tr = trs[i];\r\n" + 
				"        id = tr.id;\r\n" + 
				"        if (id.substr(0,2) == 'ft') {\r\n" + 
				"            if (level == 2 || level == 0 ) {\r\n" + 
				"                tr.className = 'hiddenRow';\r\n" + 
				"            }\r\n" + 
				"            else {\r\n" + 
				"                tr.className = '';\r\n" + 
				"            }\r\n" + 
				"        }\r\n" + 
				"        if (id.substr(0,2) == 'pt') {\r\n" + 
				"            if (level < 2) {\r\n" + 
				"                tr.className = 'hiddenRow';\r\n" + 
				"            }\r\n" + 
				"            else {\r\n" + 
				"                tr.className = '';\r\n" + 
				"            }\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    //加入【详细】切换文字变化 --Findyou\r\n" + 
				"    detail_class=document.getElementsByClassName('detail');\r\n" + 
				"	//console.log(detail_class.length)\r\n" + 
				"	if (level == 3) {\r\n" + 
				"		for (var i = 0; i < detail_class.length; i++){\r\n" + 
				"			detail_class[i].innerHTML=\"收起\"\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"	else{\r\n" + 
				"			for (var i = 0; i < detail_class.length; i++){\r\n" + 
				"			detail_class[i].innerHTML=\"详细\"\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"function showClassDetail(cid, count) {\r\n" + 
				"    var id_list = Array(count);\r\n" + 
				"    var toHide = 1;\r\n" + 
				"    for (var i = 0; i < count; i++) {\r\n" + 
				"        //ID修改 点 为 下划线 -Findyou\r\n" + 
				"        tid0 = 't' + cid.substr(1) + '_' + (i+1);\r\n" + 
				"        tid = 'f' + tid0;\r\n" + 
				"        tr = document.getElementById(tid);\r\n" + 
				"        if (!tr) {\r\n" + 
				"            tid = 'p' + tid0;\r\n" + 
				"            tr = document.getElementById(tid);\r\n" + 
				"        }\r\n" + 
				"        id_list[i] = tid;\r\n" + 
				"        if (tr.className) {\r\n" + 
				"            toHide = 0;\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"    for (var i = 0; i < count; i++) {\r\n" + 
				"        tid = id_list[i];\r\n" + 
				"        //修改点击无法收起的BUG，加入【详细】切换文字变化 --Findyou\r\n" + 
				"        if (toHide) {\r\n" + 
				"            document.getElementById(tid).className = 'hiddenRow';\r\n" + 
				"            document.getElementById(cid).innerText = \"详细\"\r\n" + 
				"        }\r\n" + 
				"        else {\r\n" + 
				"            document.getElementById(tid).className = '';\r\n" + 
				"            document.getElementById(cid).innerText = \"收起\"\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"function html_escape(s) {\r\n" + 
				"    s = s.replace(/&/g,'&amp;');\r\n" + 
				"    s = s.replace(/</g,'&lt;');\r\n" + 
				"    s = s.replace(/>/g,'&gt;');\r\n" + 
				"    return s;\r\n" + 
				"}\r\n" + 
				"</script>\r\n" + 
				"</head>\r\n" + 
				"<body data-controller=\"viewport-toggle\" style=\"padding-top: 0px;\">\r\n" + 
				"    <header class=\"site-header \">\r\n" + 
				"        <div class=\"site-container\">\r\n" + 
				"            <div class=\"split-layout\">\r\n" + 
				"                <div data-html-include=\"/_includes/current-user-indicator/\">\r\n" + 
				"                    <nav id=\"user-indicator\" class=\"horizontal-menu horizontal-menu--light horizontal-menu--tall\" aria-label=\"Main navigation\">\r\n" + 
				"                    </nav>\r\n" + 
				"                </div>\r\n" + 
				"            </div>\r\n" + 
				"          </div>\r\n" + 
				"    </header>\r\n" + 
				"    <main id = \"content\">\r\n" + 
				"        <section data-controller=\"project-tabs\" data-project-tabs-content=\"description\">\r\n" + 
				"            <div class=\"tabs-container\">\r\n" + 
				"                <div class=\"vertical-tabs\">\r\n" + 
				"                    <div class = \"vertical-tabs__tabs\">\r\n" + 
				"                        <div class=\"sidebar-section\">\r\n" + 
				"                            <h3 class=\"sidebar-section__title\">常规</h3>\r\n" + 
				"                            <nav role=\"tablist\">\r\n" + 
				"                              <a id=\"description-tab\" class=\"vertical-tabs__tab \"  role=\"tab\">\r\n" + 
				"                                测试人员 :  " + HtmlReport.tester + "\r\n" + 
				"                              </a>\r\n" + 
				"                              <a id=\"description-tab\" class=\"vertical-tabs__tab \"  role=\"tab\">\r\n" + 
				"                                开始时间 :  " + HtmlReport.startTime + "\r\n" + 
				"                              </a>\r\n" + 
				"                              <a id=\"description-tab\" class=\"vertical-tabs__tab \"  role=\"tab\">\r\n" + 
				"                                合计耗时 :  " + HtmlReport.totalTime + "\r\n" + 
				"                              </a>\r\n" + 
				"                            </nav>\r\n" + 
				"                        </div>\r\n" + 
				"\r\n" + 
				"                        <div class=\"sidebar-section\">\r\n" + 
				"                            <h3 class=\"sidebar-section__title\">结果</h3>\r\n" + 
				"                            <a class=\"btn btn-primary\" href='javascript:showCase(0)'>概要 100.00% </a><br/><br/>\r\n" + 
				"<a class=\"btn btn-danger\" href='javascript:showCase(1)'>失败 0 </a><br/><br/>\r\n" + 
				"<a class=\"btn btn-success\" href='javascript:showCase(2)'>通过 9 </a><br/><br/>\r\n" + 
				"<a class=\"btn btn-info\" href='javascript:showCase(3)'>所有 9 </a>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"                        </div>\r\n" + 
				"                        <div class=\"sidebar-section\">\r\n" + 
				"                            <h3 class=\"sidebar-section__title\">其他</h3>\r\n" + 
				"                                    <p>本报告站点服务由服务中心提供,用于支持由<a href=\"http:\">XXXX</a>生产的测试报告预览，管理员为<a href=\"http:\">XXXX</a></p>\r\n" + 
				"                                </div>\r\n" + 
				"\r\n" + 
				"                    </div>\r\n" + 
				"\r\n" + 
				"                    <div class = \"vertical-tabs__panel\">\r\n" + 
				"\r\n" + 
				"                        \r\n" + 
				"<table id='result_table' class=\"table table-condensed table-bordered table-hover\">\r\n" + 
				"<colgroup>\r\n" + 
				"<col align='left' />\r\n" + 
				"<col align='right' />\r\n" + 
				"<col align='right' />\r\n" + 
				"<col align='right' />\r\n" + 
				"<col align='right' />\r\n" + 
				"<col align='right' />\r\n" + 
				"</colgroup>\r\n" + 
				"<tr id='header_row' class=\"text-center success\" style=\"font-weight: bold;font-size: 14px;\">\r\n" + 
				"    <td>用例集/测试用例</td>\r\n" + 
				"    <td>总计</td>\r\n" + 
				"    <td>通过</td>\r\n" + 
				"    <td>失败</td>\r\n" + 
				"    <td>错误</td>\r\n" + 
				"    <td>详细</td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				/*
				"<tr class='passClass warning'>\r\n" + 
				"    <td>设班单流程校验</td>\r\n" + 
				"    <td class=\"text-center\">9</td>\r\n" + 
				"    <td class=\"text-center\">9</td>\r\n" + 
				"    <td class=\"text-center\">0</td>\r\n" + 
				"    <td class=\"text-center\">0</td>\r\n" + 
				"    <td class=\"text-center\"><a href=\"javascript:showClassDetail('c1',9)\" class=\"detail\" id='c1'>详细</a></td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr id='pt1_1' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>设班单模块: 用例8：创建学生并开通客户端</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_pt1_1' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_pt1_1'>通过</button>\r\n" + 
				"    <div id='div_pt1_1' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_pt1_1' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_pt1_1'>通过</button>\r\n" + 
				"    <div id='div_pt1_1' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				"pt1_1: 创建学生:结果True\r\n" + 
				"全局搜索:结果True\r\n" + 
				"回收名单:结果True\r\n" + 
				"转销售个人池:结果True\r\n" + 
				"销售分配名单:结果True\r\n" + 
				"开通账号:结果True\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr id='pt1_2' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>设班单模块: 用例9：创建设班单试听课时间不能早于当前时间</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_pt1_2' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_pt1_2'>通过</button>\r\n" + 
				"    <div id='div_pt1_2' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_pt1_2' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_pt1_2'>通过</button>\r\n" + 
				"    <div id='div_pt1_2' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				"pt1_2: 创建设班单（试听时间不能早于当前时间）:结果False\r\n" + 
				"\r\n" + 
				
				
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr id='pt1_3' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>设班单模块: 用例10：创建设班单试听课时间段不能小于1小时</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_pt1_3' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_pt1_3'>通过</button>\r\n" + 
				"    <div id='div_pt1_3' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_pt1_3' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_pt1_3'>通过</button>\r\n" + 
				"    <div id='div_pt1_3' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				"pt1_3: 创建设班单（试听时间段不能小于1小时）:结果False\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr id='pt1_4' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>设班单模块: 用例11：创建设班单试听课时间不足8小时，需要经历审核</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_pt1_4' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_pt1_4'>通过</button>\r\n" + 
				"    <div id='div_pt1_4' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_pt1_4' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_pt1_4'>通过</button>\r\n" + 
				"    <div id='div_pt1_4' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				"pt1_4: 创建设班单(试听时间不足8小时）:结果True\r\n" + 
				"验证设班单状态为待审核:结果True\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr id='pt1_5' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>设班单模块: 用例12：待审核状态下不允许再申请设班单</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_pt1_5' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_pt1_5'>通过</button>\r\n" + 
				"    <div id='div_pt1_5' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_pt1_5' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_pt1_5'>通过</button>\r\n" + 
				"    <div id='div_pt1_5' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				"pt1_5: 待审核状态下不允许再申请设班单:结果False\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr id='pt1_6' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>设班单模块: 用例13：销售撤回设班单</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_pt1_6' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_pt1_6'>通过</button>\r\n" + 
				"    <div id='div_pt1_6' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_pt1_6' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_pt1_6'>通过</button>\r\n" + 
				"    <div id='div_pt1_6' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				"pt1_6: 销售撤回设班单:结果True\r\n" + 
				"验证设班单状态为已撤回:结果True\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr id='pt1_7' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>设班单模块: 用例14：经理驳回设班单</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_pt1_7' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_pt1_7'>通过</button>\r\n" + 
				"    <div id='div_pt1_7' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_pt1_7' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_pt1_7'>通过</button>\r\n" + 
				"    <div id='div_pt1_7' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				"pt1_7: 撤回设班单编辑重新提交-待审核:结果True\r\n" + 
				"经理审核设班单-驳回:结果True\r\n" + 
				"验证设班单状态为已驳回:结果True\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr id='pt1_8' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>设班单模块: 用例15：驳回状态下不允许再申请设班单</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_pt1_8' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_pt1_8'>通过</button>\r\n" + 
				"    <div id='div_pt1_8' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_pt1_8' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_pt1_8'>通过</button>\r\n" + 
				"    <div id='div_pt1_8' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				"pt1_8: 驳回状态下不允许再申请设班单:结果False\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr id='pt1_9' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>设班单模块: 用例16：自动驳回设班单（老师不能教授该学科）</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_pt1_9' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_pt1_9'>通过</button>\r\n" + 
				"    <div id='div_pt1_9' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_pt1_9' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_pt1_9'>通过</button>\r\n" + 
				"    <div id='div_pt1_9' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				"pt1_9: 设班单编辑重新提交（课程选择老师不能教授的学科）:结果True\r\n" + 
				"检验：设班单状态为已驳回:结果True\r\n" + 
				"检验：新增驳回记录:结果True\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr class='passClass warning'>\r\n" + 
				"    <td>设班单流程校验2</td>\r\n" + 
				"    <td class=\"text-center\">9</td>\r\n" + 
				"    <td class=\"text-center\">9</td>\r\n" + 
				"    <td class=\"text-center\">0</td>\r\n" + 
				"    <td class=\"text-center\">0</td>\r\n" + 
				"    <td class=\"text-center\"><a href=\"javascript:showClassDetail('c2',2)\" class=\"detail\" id='c2'>详细</a></td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr id='pt2_1' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>设班单模块: 用例8：创建学生并开通客户端</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_pt1_1' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_pt1_1'>通过</button>\r\n" + 
				"    <div id='div_pt1_1' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_pt1_1' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_pt1_1'>通过</button>\r\n" + 
				"    <div id='div_pt1_1' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				"pt1_1: 创建学生:结果True\r\n" + 
				"全局搜索:结果True\r\n" + 
				"回收名单:结果True\r\n" + 
				"转销售个人池:结果True\r\n" + 
				"销售分配名单:结果True\r\n" + 
				"开通账号:结果True\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>\r\n" + 
				"\r\n" + 
				"<tr id='pt2_2' class='hiddenRow'>\r\n" + 
				"    <td class='passCase'><div class='testcase'>设班单模块: 用例9：创建设班单试听课时间不能早于当前时间</div></td>\r\n" + 
				"    <td colspan='5' align='center'>\r\n" + 
				"    <!--默认展开错误信息 -Findyou\r\n" + 
				"    <button id='btn_pt1_2' type=\"button\"  class=\"btn btn-success btn-xs \" data-toggle=\"collapse\" data-target='#div_pt1_2'>通过</button>\r\n" + 
				"    <div id='div_pt1_2' class=\"collapse\">  -->\r\n" + 
				"\r\n" + 
				"    <!-- 默认收起错误信息 -Findyou -->\r\n" + 
				"    <button id='btn_pt1_2' type=\"button\"  class=\"btn btn-success btn-xs collapsed\" data-toggle=\"collapse\" data-target='#div_pt1_2'>通过</button>\r\n" + 
				"    <div id='div_pt1_2' class=\"collapse\">\r\n" + 
				"    <pre>\r\n" + 
				"    \r\n" + 
				"pt1_2: 创建设班单（试听时间不能早于当前时间）:结果False\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    </pre>\r\n" + 
				"    </div>\r\n" + 
				"    </td>\r\n" + 
				"</tr>\r\n" + 
				
				*/
				"\r\n" + 
				
				genHtmlBody() + 
				
				"\r\n" + 
				"\r\n" + 
				"<tr id='total_row' class=\"text-center active\">\r\n" + 
				"    <td>总计</td>\r\n" + 
				"    <td>9</td>\r\n" + 
				"    <td>9</td>\r\n" + 
				"    <td>0</td>\r\n" + 
				"    <td>0</td>\r\n" + 
				"    <td>通过率：100.00%</td>\r\n" + 
				"</tr>\r\n" + 
				"</table>\r\n" + 
				"\r\n" + 
				"                        <div id='ending'>&nbsp;</div>\r\n" + 
				"    <div style=\" position:fixed;right:50px; bottom:30px; width:20px; height:20px;cursor:pointer\">\r\n" + 
				"    <a href=\"#\"><span class=\"glyphicon glyphicon-eject\" style = \"font-size:30px;\" aria-hidden=\"true\">\r\n" + 
				"    </span></a></div>\r\n" + 
				"    \r\n" + 
				"                    </div>\r\n" + 
				"</body>\r\n" + 
				"</html>\r\n" + 
				"";
		
		
		return htmlReport;
	}
	
	
	
	
}
