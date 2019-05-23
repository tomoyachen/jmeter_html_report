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
//java -Dcsv=����Banner�ӿ�.jtl -Dreport=����Banner�ӿ�_Report.html -jar jmeter.html.report.jar

public class TestReport {
	
	//csv�ļ���ַ
	public static String CSV_PATH = System.getProperty("csv");
	//HTML�ļ���ַ
	public static String HTML_PATH = System.getProperty("report"); 
	
	private static void initFilePath(){
		//��ʼ��csv�ļ���ַ
		if (CSV_PATH == null || CSV_PATH.equals("")) {
			 System.out.println("WARN: δ��ȡ��CSV�ļ�·������!");
			 CSV_PATH = "E:\\Desktop\\����Banner�ӿ�.jtl";
		 }
		 System.out.println("INFO: CSV�ļ�·���� " + CSV_PATH);
		 
		if (HTML_PATH == null || HTML_PATH.equals("")) {
			 System.out.println("WARN: δ��ȡ��HTML�ļ�·������!");
			 HTML_PATH = "E:\\Desktop\\����Banner�ӿ�.html";
		 }
		 System.out.println("INFO: HTML�ļ�·���� " + HTML_PATH);
		
	}
	
	private static boolean startTimeFlag  = false;
	

	public static void main(String[] args) throws IOException {
		
		//��ʼ��csv�ļ���ַ
		initFilePath();
		
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
			        Date date = new Date(lt);
			        startTime = simpleDateFormat.format(date);
			        
			        

				}
				
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
		
		
		//��ӡ����
		HtmlReport htmlReport = new HtmlReport(reportMap);
		htmlReport.setReportInfo("���Ա���", "����", startTime, "29.5��"); 
		htmlWrite.write(htmlReport.genHtmlReport());
		
		csvReader.close();
		htmlWrite.close();

		
//		System.out.println(htmlReport.genHtmlBody());
		
//		System.out.println(reportMap); //��������
	}
}