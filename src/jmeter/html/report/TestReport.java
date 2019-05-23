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
//java -Dcsv=新增Banner接口.jtl -Dreport=新增Banner接口_Report.html -jar jmeter.html.report.jar

public class TestReport {
	
	//csv文件地址
	public static String CSV_PATH = System.getProperty("csv");
	//HTML文件地址
	public static String HTML_PATH = System.getProperty("report"); 
	
	private static void initFilePath(){
		//初始化csv文件地址
		if (CSV_PATH == null || CSV_PATH.equals("")) {
			 System.out.println("WARN: 未获取到CSV文件路径参数!");
			 CSV_PATH = "E:\\Desktop\\新增Banner接口.jtl";
		 }
		 System.out.println("INFO: CSV文件路径是 " + CSV_PATH);
		 
		if (HTML_PATH == null || HTML_PATH.equals("")) {
			 System.out.println("WARN: 未获取到HTML文件路径参数!");
			 HTML_PATH = "E:\\Desktop\\新增Banner接口.html";
		 }
		 System.out.println("INFO: HTML文件路径是 " + HTML_PATH);
		
	}
	
	private static boolean startTimeFlag  = false;
	

	public static void main(String[] args) throws IOException {
		
		//初始化csv文件地址
		initFilePath();
		
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
			        Date date = new Date(lt);
			        startTime = simpleDateFormat.format(date);
			        
			        

				}
				
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
		
		
		//打印报告
		HtmlReport htmlReport = new HtmlReport(reportMap);
		htmlReport.setReportInfo("测试报告", "张三", startTime, "29.5秒"); 
		htmlWrite.write(htmlReport.genHtmlReport());
		
		csvReader.close();
		htmlWrite.close();

		
//		System.out.println(htmlReport.genHtmlBody());
		
//		System.out.println(reportMap); //最终数据
	}
}