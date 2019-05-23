package jmeter.html.report;

import com.csvreader.CsvReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

//cmd执行命令
//java -Dcsv=demo.jtl -Dhtml=demo.html -Dtester=张三 -Dreport=某项目测试报告 -jar report.jar

public class TestReport {

	
	//csv文件地址
	public static String CSV_PATH = System.getProperty("csv");
	//HTML文件地址
	public static String HTML_PATH = System.getProperty("html"); 

	//测试人员
	public static String TESTER_NAME = System.getProperty("tester"); 
	
	//测试人员
	public static String REPORT_NAME = System.getProperty("report"); 
	
	private static boolean ifDebug = false;
	
	private static void init(){
		
		System.out.println("--欢迎使用------------------------------------------------------");
		System.out.println("执行命令请参考以下示例: ");
		System.out.println("java -Dcsv=D:\\demo.jtl -Dhtml=D:\\demo.html -Dtester=张三 -Dreport=某项目测试报告 -jar report.jar");
		
		System.out.println("--开始生成------------------------------------------------------");
		
		if(ifDebug) {
			CSV_PATH = "E:\\Desktop\\新增Banner接口.jtl";
			HTML_PATH = "E:\\Desktop\\新增Banner接口.html";
			TESTER_NAME = "测试人员";
			REPORT_NAME = "测试报告";
		}
		if (CSV_PATH == null || CSV_PATH.equals("")) {
			 System.out.println("WARN: 未获取到CSV文件路径参数!");
			 System.out.println("ERROR: 程序无法正常执行!");
			 System.exit(0);
		 }
		 System.out.println("INFO: CSV文件路径是 " + CSV_PATH);
		 
		if (HTML_PATH == null || HTML_PATH.equals("")) {
			 System.out.println("WARN: 未获取到HTML文件路径参数!");
			 HTML_PATH = "测试报告.html";
		 }
		 System.out.println("INFO: HTML文件路径是 " + HTML_PATH);
		 
		if (TESTER_NAME == null || TESTER_NAME.equals("")) {
			 System.out.println("WARN: 未获取到测试人员姓名!");
			 TESTER_NAME = "测试人员";
		 }
		 System.out.println("INFO: 测试人员是 " + TESTER_NAME);
		 
		if (REPORT_NAME == null || REPORT_NAME.equals("")) {
			 System.out.println("WARN: 未获取到测试报告名称!");
			 REPORT_NAME = "测试报告";
		 }
		 System.out.println("INFO: 测试报告名称是 " + REPORT_NAME);
		
		
		//初始化csv文件地址
	

		
	}
	
	private static boolean startTimeFlag  = false;
	

	public static void main(String[] args) throws IOException {
		
		//初始化csv文件地址
		init();
		
		//创建CSV文件对象
		CsvReader csvReader = new CsvReader(CSV_PATH, ',', Charset.forName("UTF-8"));
		
		//创建HTML报告文件对象
//		BufferedWriter htmlWrite = new BufferedWriter(new FileWriter(HTML_PATH)); //中文会有乱码
		BufferedWriter htmlWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(HTML_PATH)),"utf-8"));
		
		//读取所有列
		csvReader.readHeaders();

		//总Map 结构为 LinkedHashMap(测试集, ArrayList(HashMap(列1, 列2, 列3), HashMap2(列1, 列2, 列3), HashMap3(列1, 列2, 列3)))
		LinkedHashMap<String, ArrayList> reportMap = new LinkedHashMap<>();
		
//		有用的列名数组
		String [] csvTitle = {"elapsed","label","responseCode","threadName","success","failureMessage","URL"};
		String groupTitle = "threadName";
		
		String startTime = "";
		String endTime = "";

		long startTimeAslong = 0;
		long endTimeAslong = 0;

//		循环每一行，写入数据结构。以线程组分组
		while (csvReader.readRecord()) {
			
			//每一行的Map
			HashMap<String, String> testResult = new HashMap<>();

//			往hashmap中写入指定列
			for(int i = 0;i < csvTitle.length; i++){
				testResult.put(csvTitle[i], csvReader.get(csvTitle[i]));
				
				
				//第一次记录第一条记录时间
				if(!startTimeFlag) {
					
					startTimeFlag = true;
					startTime = csvReader.get("timeStamp");
			        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        long lt = new Long(startTime);
			        Date startTimeAsDate = new Date(lt);
			        startTime = simpleDateFormat.format(startTimeAsDate);
			        
			        startTimeAslong = lt;
			        
			        
			        
			        

				}
				endTime = csvReader.get("timeStamp");
				
            }
			
			//根据组名分组
			String group = csvReader.get(groupTitle);
		
			if(reportMap.containsKey(group)) {
				//存在这个key
				reportMap.get(group).add(testResult);
			}else {
				//不存在这个key
				reportMap.put(group, new ArrayList<>());
				reportMap.get(group).add(testResult);
				
			}
			
			
		}
		
        endTimeAslong = new Long(endTime);
        double sec = (endTimeAslong - startTimeAslong)/(1000.0);
        sec = Math.round(sec*10)/10.0;
        String secAsString =  sec + "秒";
        
        
        
		
		//打印报告
		HtmlReport htmlReport = new HtmlReport(reportMap);
		htmlReport.setReportInfo(REPORT_NAME, TESTER_NAME, startTime, secAsString); 
		htmlWrite.write(htmlReport.getHtmlReport());
		
		csvReader.close();
		htmlWrite.close();

		
//		System.out.println(htmlReport.genHtmlBody());
		
//		System.out.println(reportMap); //最终数据
		
		System.out.println("--生成完毕------------------------------------------------------");
	}
}