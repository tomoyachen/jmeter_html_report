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

//cmdִ������
//java -Dcsv=demo.jtl -Dhtml=demo.html -Dtester=���� -Dreport=ĳ��Ŀ���Ա��� -jar report.jar

public class TestReport {

	
	//csv�ļ���ַ
	public static String CSV_PATH = System.getProperty("csv");
	//HTML�ļ���ַ
	public static String HTML_PATH = System.getProperty("html"); 

	//������Ա
	public static String TESTER_NAME = System.getProperty("tester"); 
	
	//������Ա
	public static String REPORT_NAME = System.getProperty("report"); 
	
	private static boolean ifDebug = false;
	
	private static void init(){
		
		System.out.println("--��ӭʹ��------------------------------------------------------");
		System.out.println("ִ��������ο�����ʾ��: ");
		System.out.println("java -Dcsv=D:\\demo.jtl -Dhtml=D:\\demo.html -Dtester=���� -Dreport=ĳ��Ŀ���Ա��� -jar report.jar");
		
		System.out.println("--��ʼ����------------------------------------------------------");
		
		if(ifDebug) {
			CSV_PATH = "E:\\Desktop\\����Banner�ӿ�.jtl";
			HTML_PATH = "E:\\Desktop\\����Banner�ӿ�.html";
			TESTER_NAME = "������Ա";
			REPORT_NAME = "���Ա���";
		}
		if (CSV_PATH == null || CSV_PATH.equals("")) {
			 System.out.println("WARN: δ��ȡ��CSV�ļ�·������!");
			 System.out.println("ERROR: �����޷�����ִ��!");
			 System.exit(0);
		 }
		 System.out.println("INFO: CSV�ļ�·���� " + CSV_PATH);
		 
		if (HTML_PATH == null || HTML_PATH.equals("")) {
			 System.out.println("WARN: δ��ȡ��HTML�ļ�·������!");
			 HTML_PATH = "���Ա���.html";
		 }
		 System.out.println("INFO: HTML�ļ�·���� " + HTML_PATH);
		 
		if (TESTER_NAME == null || TESTER_NAME.equals("")) {
			 System.out.println("WARN: δ��ȡ��������Ա����!");
			 TESTER_NAME = "������Ա";
		 }
		 System.out.println("INFO: ������Ա�� " + TESTER_NAME);
		 
		if (REPORT_NAME == null || REPORT_NAME.equals("")) {
			 System.out.println("WARN: δ��ȡ�����Ա�������!");
			 REPORT_NAME = "���Ա���";
		 }
		 System.out.println("INFO: ���Ա��������� " + REPORT_NAME);
		
		
		//��ʼ��csv�ļ���ַ
	

		
	}
	
	private static boolean startTimeFlag  = false;
	

	public static void main(String[] args) throws IOException {
		
		//��ʼ��csv�ļ���ַ
		init();
		
		//����CSV�ļ�����
		CsvReader csvReader = new CsvReader(CSV_PATH, ',', Charset.forName("UTF-8"));
		
		//����HTML�����ļ�����
//		BufferedWriter htmlWrite = new BufferedWriter(new FileWriter(HTML_PATH)); //���Ļ�������
		BufferedWriter htmlWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(HTML_PATH)),"utf-8"));
		
		//��ȡ������
		csvReader.readHeaders();

		//��Map �ṹΪ LinkedHashMap(���Լ�, ArrayList(HashMap(��1, ��2, ��3), HashMap2(��1, ��2, ��3), HashMap3(��1, ��2, ��3)))
		LinkedHashMap<String, ArrayList> reportMap = new LinkedHashMap<>();
		
//		���õ���������
		String [] csvTitle = {"elapsed","label","responseCode","threadName","success","failureMessage","URL"};
		String groupTitle = "threadName";
		
		String startTime = "";
		String endTime = "";

		long startTimeAslong = 0;
		long endTimeAslong = 0;

//		ѭ��ÿһ�У�д�����ݽṹ�����߳������
		while (csvReader.readRecord()) {
			
			//ÿһ�е�Map
			HashMap<String, String> testResult = new HashMap<>();

//			��hashmap��д��ָ����
			for(int i = 0;i < csvTitle.length; i++){
				testResult.put(csvTitle[i], csvReader.get(csvTitle[i]));
				
				
				//��һ�μ�¼��һ����¼ʱ��
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
			
			//������������
			String group = csvReader.get(groupTitle);
		
			if(reportMap.containsKey(group)) {
				//�������key
				reportMap.get(group).add(testResult);
			}else {
				//���������key
				reportMap.put(group, new ArrayList<>());
				reportMap.get(group).add(testResult);
				
			}
			
			
		}
		
        endTimeAslong = new Long(endTime);
        double sec = (endTimeAslong - startTimeAslong)/(1000.0);
        sec = Math.round(sec*10)/10.0;
        String secAsString =  sec + "��";
        
        
        
		
		//��ӡ����
		HtmlReport htmlReport = new HtmlReport(reportMap);
		htmlReport.setReportInfo(REPORT_NAME, TESTER_NAME, startTime, secAsString); 
		htmlWrite.write(htmlReport.getHtmlReport());
		
		csvReader.close();
		htmlWrite.close();

		
//		System.out.println(htmlReport.genHtmlBody());
		
//		System.out.println(reportMap); //��������
		
		System.out.println("--�������------------------------------------------------------");
	}
}