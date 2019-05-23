package jmeter.html.report;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class HtmlReport {

	HtmlReport(LinkedHashMap<String, ArrayList> reportMap) {
		HtmlReport.reportMap = reportMap;

	}

	private static String title = "";

	private static String tester = "";

	private static String startTime = "";

	private static String totalTime = "";

	// 是否加序号
	private static boolean ifAddIndex = false;

	public static LinkedHashMap<String, ArrayList> reportMap = null;

	public static void setReportInfo(String title, String tester, String startTime, String totalTime) {

		HtmlReport.title = title;
		HtmlReport.tester = tester;
		HtmlReport.startTime = startTime;
		HtmlReport.totalTime = totalTime;

	}

	// 通过数 失败数 错误数
	private static int testCaseCount = 0;
	private static int passCount = 0;
	private static int failCount = 0;
	private static int errorCount = 0;

	private static int totalTestCaseCount = 0;
	private static int totalPassCount = 0;
	private static int totalFailCount = 0;
	private static int totalErrorCount = 0;

	private static void setTestSuiteResultCount(ArrayList testSuite) {

		HtmlReport.testCaseCount = 0;
		HtmlReport.passCount = 0;
		HtmlReport.failCount = 0;
		HtmlReport.errorCount = 0;

		// 循环testcases
		Iterator testCases = testSuite.iterator();
		int testCaseIndex = 1;
		while (testCases.hasNext()) {
			HashMap testCase = (HashMap) testCases.next();
			String successAsString = (String) testCase.get("success");
			if (successAsString.equals("true")) {
				HtmlReport.passCount++;
			} else {
				
				// 异常用例数
				String responseCode = (String) testCase.get("responseCode");
				try {
					Integer.parseInt(responseCode);
					HtmlReport.failCount++;

				} catch (NumberFormatException e) {
					HtmlReport.errorCount++;
				}

			}



			HtmlReport.totalTestCaseCount++;

		}

		HtmlReport.totalTestCaseCount += HtmlReport.testCaseCount;
		HtmlReport.totalPassCount += HtmlReport.passCount;
		HtmlReport.totalFailCount += HtmlReport.failCount;
		HtmlReport.totalErrorCount += HtmlReport.errorCount;

	}

	private static String getPercent(int num1, int num2) {

		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();

		// 设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(2);

		String result = numberFormat.format((float) num1 / (float) num2 * 100);

		return result + "%";

	}

	private static String addTestSuite(int TestSuiteIndex, String TestStuteName, String TestCaseCount, String PassCount,
			String FailCount, String ErrorCount) {
		String TestSuite = "";
		String id = "c" + TestSuiteIndex;
		TestSuite = "" + "<tr class='passClass warning'>\r\n" + "    <td>" + TestStuteName + "</td>\r\n"
				+ "    <td class=\"text-center\">" + TestCaseCount + "</td>\r\n" + "    <td class=\"text-center\">"
				+ PassCount + "</td>\r\n" + "    <td class=\"text-center\">" + FailCount + "</td>\r\n"
				+ "    <td class=\"text-center\">" + ErrorCount + "</td>\r\n"
				+ "    <td class=\"text-center\"><a href=\"javascript:showClassDetail('" + id + "', " + TestCaseCount
				+ ")\" class=\"detail\" id='" + id + "'>详细</a></td>\r\n" + "</tr>";

		return TestSuite;
	}

	private static String addTestCase(int TestSuiteIndex, int TestCaseIndex, String TestCaseName, String pre,
			boolean success, boolean error) {
		String TestCase = "";
		String id = "pt" + TestSuiteIndex + "_" + TestCaseIndex;
		String hiddenRowClass = "none";
		String passClass = "failCase";
		String successClass = "danger";
		String hiddenPreClass = "in";
		String text = "失败";

		if (success) {
			hiddenRowClass = "hiddenRow";
			passClass = "passCase";
			successClass = "success";
			hiddenPreClass = "";
			text = "通过";
		}
		
		if(error) {
			text = "错误";
		}

//		默认收起错误信息
		boolean ifCollapsed = false;
		String collapsed = "";
//		如果展开错误信息
		if (ifCollapsed) {
			collapsed = "collapsed";
		}

		TestCase = "" + "<tr id='" + id + "' class='" + hiddenRowClass + "'>\r\n" + "    <td class='" + passClass
				+ "'><div class='testcase'>" + TestCaseName + "</div></td>\r\n"
				+ "    <td colspan='5' align='center'>\r\n" + "\r\n" + "    <!-- 默认收起错误信息 -Findyou -->\r\n"
				+ "    <button id='btn_" + id + "' type=\"button\"  class=\"btn btn-" + successClass + " btn-xs "
				+ collapsed + "\" data-toggle=\"collapse\" data-target='#div_" + id + "'>" + text + "</button>\r\n"
				+ "    <div id='div_" + id + "' class=\"collapse " + hiddenPreClass + "\">\r\n" + "    <pre>\r\n"
				+ "    \r\n" + pre + "\r\n" + "</pre>\r\n" + "    </div>\r\n" + "    </td>\r\n" + "</tr>";

		return TestCase;
	}

	private static String htmlBody = "";

	public static void genHtmlBody() {
		HtmlReport.htmlBody = getHtmlBody();
	}

	public static String getHtmlBody() {

		String htmlBody = "";
		Map map = HtmlReport.reportMap;
		Iterator testSuites = reportMap.entrySet().iterator();

		// 循环testsuites
		int testSuiteIndex = 1;
		while (testSuites.hasNext()) {
			Map.Entry testSuite = (Map.Entry) testSuites.next();
			String key = (String) testSuite.getKey(); // 测试集名称
			String testSuiteName = key.split(" ")[0];

			// 是否加序号
			if (ifAddIndex) {
				testSuiteName = String.valueOf(testSuiteIndex) + ". " + testSuiteName;
			}
			ArrayList val = (ArrayList) testSuite.getValue();
//			System.out.println(val);
			String TestCaseCount = String.valueOf(val.size());

			// 初始化胜出呢个测试集数字
			setTestSuiteResultCount(val);
			htmlBody += addTestSuite(testSuiteIndex, testSuiteName, TestCaseCount, String.valueOf(HtmlReport.passCount),
					String.valueOf(HtmlReport.failCount), String.valueOf(HtmlReport.errorCount));

//			System.out.println(addTestSuite(key, "9", "9", "9", "9"));

			// 循环testcases
			Iterator testCases = val.iterator();
			int testCaseIndex = 1;
			while (testCases.hasNext()) {
				HashMap testCase = (HashMap) testCases.next();
				String testCaseName = (String) testCase.get("label");

				// 用例执行结果
				String pre = "";
				
				if (!(testCase.get("URL") == null) && !(boolean) testCase.get("URL").equals("")) {
					pre += "请求地址: " + (String) testCase.get("URL") + "\r\n";
				}
				pre += "HTTP码: " + (String) testCase.get("responseCode") + "\r\n";

				
				boolean ifSuccess = false;
				boolean ifError = false;
				
				if ((boolean) testCase.get("success").equals("true")) {
					ifSuccess = true;
					pre += "执行结果: 成功" + "\r\n ";
				} else if ((boolean) testCase.get("responseMessage").equals("OK")){
					ifSuccess = false;
					pre += "执行结果: 失败" + "\r\n ";
					pre += "断言结果: " + (String) testCase.get("failureMessage") + "\r\n";
				}else {
					ifError = true;
					pre += "执行结果: 错误" + "\r\n ";
					pre += "错误结果: " + (String) testCase.get("responseMessage") + "\r\n";
				}

				String successAsString = (String) testCase.get("success");


				// 是否加序号
				if (ifAddIndex) {
					testCaseName = String.valueOf(testSuiteIndex) + ". " + String.valueOf(testCaseIndex) + testCaseName;
				}

				htmlBody += addTestCase(testSuiteIndex, testCaseIndex, testCaseName, pre, ifSuccess, ifError);

				testCaseIndex++;

			}

			testSuiteIndex++;

		}

		return htmlBody;
	}

	public static String getHtmlReport() {
		genHtmlBody();

		String htmlReport = "" + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + "<head>\r\n" + "    <title>" + HtmlReport.title
				+ "</title>\r\n" + "\r\n"
				+ "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\r\n"
				+ "    <link href=\"https://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.css\" rel=\"stylesheet\">\r\n"
				+ "    <link rel=\"stylesheet\" href=\"https://pypi.org/static/css/warehouse.d8da1ae4.css\">\r\n"
				+ "    <link rel=\"stylesheet\" href=\"https://pypi.org/static/css/fontawesome.3173d2f0.css\">\r\n"
				+ "    <link rel=\"stylesheet\" href=\"https://pypi.org/static/css/regular.19624371.css\">\r\n"
				+ "    <link rel=\"stylesheet\" href=\"https://pypi.org/static/css/solid.f478cfb1.css\">\r\n"
				+ "    <link rel=\"stylesheet\" href=\"https://pypi.org/static/css/brands.1ea560bf.css\">\r\n"
				+ "    <script src=\"https://cdn.bootcss.com/jquery/2.1.4/jquery.js\"></script>\r\n"
				+ "    <script src=\"https://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js\"></script>\r\n"
				+ "\r\n" + "    \r\n" + "<style type=\"text/css\" media=\"screen\">\r\n"
				+ "body        { font-family: Microsoft YaHei,Tahoma,arial,helvetica,sans-serif;padding: 20px; font-size: 80%; }\r\n"
				+ "table       { font-size: 100%; }\r\n" + "\r\n"
				+ "/* -- heading ---------------------------------------------------------------------- */\r\n"
				+ ".heading {\r\n" + "    margin-top: 0ex;\r\n" + "    margin-bottom: 1ex;\r\n" + "}\r\n" + "\r\n"
				+ ".heading .description {\r\n" + "    margin-top: 4ex;\r\n" + "    margin-bottom: 6ex;\r\n" + "}\r\n"
				+ "\r\n"
				+ "/* -- report ------------------------------------------------------------------------ */\r\n"
				+ "#total_row  { font-weight: bold; }\r\n" + ".passCase   { color: #5cb85c; }\r\n"
				+ ".failCase   { color: #d9534f; font-weight: bold; }\r\n"
				+ ".errorCase  { color: #f0ad4e; font-weight: bold; }\r\n" + ".hiddenRow  { display: none; }\r\n"
				+ ".testcase   { margin-left: 2em; }\r\n" + "</style>\r\n" + "\r\n" + "\r\n"
				+ "    <script language=\"javascript\" type=\"text/javascript\">\r\n" + "output_list = Array();\r\n"
				+ "\r\n" + "/*level 调整增加只显示通过用例的分类 --Findyou\r\n" + "0:Summary //all hiddenRow\r\n"
				+ "1:Failed  //pt hiddenRow, ft none\r\n" + "2:Pass    //pt none, ft hiddenRow\r\n"
				+ "3:All     //pt none, ft none\r\n" + "*/\r\n" + "function showCase(level) {\r\n"
				+ "    trs = document.getElementsByTagName(\"tr\");\r\n"
				+ "    for (var i = 0; i < trs.length; i++) {\r\n" + "        tr = trs[i];\r\n"
				+ "        id = tr.id;\r\n" + "        if (id.substr(0,2) == 'ft') {\r\n"
				+ "            if (level == 2 || level == 0 ) {\r\n" + "                tr.className = 'hiddenRow';\r\n"
				+ "            }\r\n" + "            else {\r\n" + "                tr.className = '';\r\n"
				+ "            }\r\n" + "        }\r\n" + "        if (id.substr(0,2) == 'pt') {\r\n"
				+ "            if (level < 2) {\r\n" + "                tr.className = 'hiddenRow';\r\n"
				+ "            }\r\n" + "            else {\r\n" + "                tr.className = '';\r\n"
				+ "            }\r\n" + "        }\r\n" + "    }\r\n" + "\r\n" + "    //加入【详细】切换文字变化 --Findyou\r\n"
				+ "    detail_class=document.getElementsByClassName('detail');\r\n"
				+ "	//console.log(detail_class.length)\r\n" + "	if (level == 3) {\r\n"
				+ "		for (var i = 0; i < detail_class.length; i++){\r\n"
				+ "			detail_class[i].innerHTML=\"收起\"\r\n" + "		}\r\n" + "	}\r\n" + "	else{\r\n"
				+ "			for (var i = 0; i < detail_class.length; i++){\r\n"
				+ "			detail_class[i].innerHTML=\"详细\"\r\n" + "		}\r\n" + "	}\r\n" + "}\r\n" + "\r\n"
				+ "function showClassDetail(cid, count) {\r\n" + "    var id_list = Array(count);\r\n"
				+ "    var toHide = 1;\r\n" + "    for (var i = 0; i < count; i++) {\r\n"
				+ "        //ID修改 点 为 下划线 -Findyou\r\n" + "        tid0 = 't' + cid.substr(1) + '_' + (i+1);\r\n"
				+ "        tid = 'f' + tid0;\r\n" + "        tr = document.getElementById(tid);\r\n"
				+ "        if (!tr) {\r\n" + "            tid = 'p' + tid0;\r\n"
				+ "            tr = document.getElementById(tid);\r\n" + "        }\r\n"
				+ "        id_list[i] = tid;\r\n" + "        if (tr.className) {\r\n" + "            toHide = 0;\r\n"
				+ "        }\r\n" + "    }\r\n" + "    for (var i = 0; i < count; i++) {\r\n"
				+ "        tid = id_list[i];\r\n" + "        //修改点击无法收起的BUG，加入【详细】切换文字变化 --Findyou\r\n"
				+ "        if (toHide) {\r\n" + "            document.getElementById(tid).className = 'hiddenRow';\r\n"
				+ "            document.getElementById(cid).innerText = \"详细\"\r\n" + "        }\r\n"
				+ "        else {\r\n" + "            document.getElementById(tid).className = '';\r\n"
				+ "            document.getElementById(cid).innerText = \"收起\"\r\n" + "        }\r\n" + "    }\r\n"
				+ "}\r\n" + "\r\n" + "function html_escape(s) {\r\n" + "    s = s.replace(/&/g,'&amp;');\r\n"
				+ "    s = s.replace(/</g,'&lt;');\r\n" + "    s = s.replace(/>/g,'&gt;');\r\n" + "    return s;\r\n"
				+ "}\r\n" + "</script>\r\n" + "</head>\r\n"
				+ "<body data-controller=\"viewport-toggle\" style=\"padding-top: 0px;\">\r\n"
				+ "    <header class=\"site-header \">\r\n" + "        <div class=\"site-container\">\r\n"
				+ "            <div class=\"split-layout\">\r\n"
				+ "                <div data-html-include=\"/_includes/current-user-indicator/\">\r\n"
				+ "                    <nav id=\"user-indicator\" class=\"horizontal-menu horizontal-menu--light horizontal-menu--tall\" aria-label=\"Main navigation\">\r\n"
				+ "                    </nav>\r\n" + "                </div>\r\n" + "            </div>\r\n"
				+ "          </div>\r\n" + "    </header>\r\n" + "    <main id = \"content\">\r\n"
				+ "        <section data-controller=\"project-tabs\" data-project-tabs-content=\"description\">\r\n"
				+ "            <div class=\"tabs-container\">\r\n" + "                <div class=\"vertical-tabs\">\r\n"
				+ "                    <div class = \"vertical-tabs__tabs\">\r\n"
				+ "                        <div class=\"sidebar-section\">\r\n"
				+ "                            <h3 class=\"sidebar-section__title\">常规</h3>\r\n"
				+ "                            <nav role=\"tablist\">\r\n"
				+ "                              <a id=\"description-tab\" class=\"vertical-tabs__tab \"  role=\"tab\">\r\n"
				+ "                                测试人员 :  " + HtmlReport.tester + "\r\n"
				+ "                              </a>\r\n"
				+ "                              <a id=\"description-tab\" class=\"vertical-tabs__tab \"  role=\"tab\">\r\n"
				+ "                                开始时间 :  " + HtmlReport.startTime + "\r\n"
				+ "                              </a>\r\n"
				+ "                              <a id=\"description-tab\" class=\"vertical-tabs__tab \"  role=\"tab\">\r\n"
				+ "                                合计耗时 :  " + HtmlReport.totalTime + "\r\n"
				+ "                              </a>\r\n" + "                            </nav>\r\n"
				+ "                        </div>\r\n" + "\r\n"
				+ "                        <div class=\"sidebar-section\">\r\n"
				+ "                            <h3 class=\"sidebar-section__title\">结果</h3>\r\n"
				+ "                            <a class=\"btn btn-primary\" href='javascript:showCase(0)'>概要 "
				+ getPercent(HtmlReport.totalPassCount, HtmlReport.totalTestCaseCount) + " </a><br/><br/>\r\n"
				+ "<a class=\"btn btn-danger\" href='javascript:showCase(1)'>失败 " + HtmlReport.totalFailCount
				+ " </a><br/><br/>\r\n" + "<a class=\"btn btn-success\" href='javascript:showCase(2)'>通过 "
				+ HtmlReport.totalPassCount + " </a><br/><br/>\r\n"
				+ "<a class=\"btn btn-info\" href='javascript:showCase(3)'>所有 " + HtmlReport.totalTestCaseCount
				+ " </a>\r\n" + "\r\n" + "\r\n" + "                        </div>\r\n"
				+ "                        <div class=\"sidebar-section\">\r\n"
				+ "                            <h3 class=\"sidebar-section__title\">其他</h3>\r\n"
				+ "                                    <p>本报告站点服务由服务中心提供,用于支持由<a href=\"http:\">XXXX</a>生产的测试报告预览，管理员为<a href=\"http:\">XXXX</a></p>\r\n"
				+ "                                </div>\r\n" + "\r\n" + "                    </div>\r\n" + "\r\n"
				+ "                    <div class = \"vertical-tabs__panel\">\r\n" + "\r\n"
				+ "                        \r\n"
				+ "<table id='result_table' class=\"table table-condensed table-bordered table-hover\">\r\n"
				+ "<colgroup>\r\n" + "<col align='left' />\r\n" + "<col align='right' />\r\n"
				+ "<col align='right' />\r\n" + "<col align='right' />\r\n" + "<col align='right' />\r\n"
				+ "<col align='right' />\r\n" + "</colgroup>\r\n"
				+ "<tr id='header_row' class=\"text-center success\" style=\"font-weight: bold;font-size: 14px;\">\r\n"
				+ "    <td>用例集/测试用例</td>\r\n" + "    <td>总计</td>\r\n" + "    <td>通过</td>\r\n" + "    <td>失败</td>\r\n"
				+ "    <td>错误</td>\r\n" + "    <td>详细</td>\r\n" + "</tr>\r\n" + "\r\n" +

				HtmlReport.htmlBody +

				"\r\n" + "\r\n" + "<tr id='total_row' class=\"text-center active\">\r\n" + "    <td>总计</td>\r\n"
				+ "    <td>" + HtmlReport.totalTestCaseCount + "</td>\r\n" + "    <td>" + HtmlReport.totalPassCount
				+ "</td>\r\n" + "    <td>" + HtmlReport.totalFailCount + "</td>\r\n" + "    <td>"
				+ HtmlReport.totalErrorCount + "</td>\r\n" + "    <td>通过率："
				+ getPercent(HtmlReport.totalPassCount, HtmlReport.totalTestCaseCount) + "</td>\r\n" + "</tr>\r\n"
				+ "</table>\r\n" + "\r\n" + "                        <div id='ending'>&nbsp;</div>\r\n"
				+ "    <div style=\" position:fixed;right:50px; bottom:30px; width:20px; height:20px;cursor:pointer\">\r\n"
				+ "    <a href=\"#\"><span class=\"glyphicon glyphicon-eject\" style = \"font-size:30px;\" aria-hidden=\"true\">\r\n"
				+ "    </span></a></div>\r\n" + "    \r\n" + "                    </div>\r\n" + "</body>\r\n"
				+ "</html>\r\n" + "";

		return htmlReport;
	}

}
