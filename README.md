# jmeter_html_report


#导出方式 Eclipse
Export -> Runnable JAR file -> 选择主程序清单

#执行方式 cmd
java -Dcsv=demo.jtl -Dhtml=demo.html -Dtester=张三 -Dreport=某项目测试报告 -jar report.jar
