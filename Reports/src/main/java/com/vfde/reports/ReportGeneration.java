package com.vfde.reports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Statement;
import java.sql.ResultSet;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.*;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.poi.xssf.usermodel.XSSFCell;
import java.nio.charset.StandardCharsets;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;

public class ReportGeneration {

	private static String salt = "cenxvfde";
	public static void main(String[] args) {
		try {
			ReportGeneration generation = new ReportGeneration(); 
			String configFileName = args[0];
			String secret = args[1];
			String configFilePath = generation.getOutputStreamPath()+"/" + configFileName;
			
			FileInputStream inputStream = new FileInputStream(new File(configFilePath));
			//String config = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name()); 
			
			//String configArray [] = config.split("#");
			//generation.generateReport(configArray[0],configArray[1],configArray[2]);
			 List<String> configList = IOUtils.readLines(inputStream, StandardCharsets.UTF_8.name());
			 HashMap configMap = getConfigMap(configList);
			 generation.generateReport(configMap,secret);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}
	
	private static HashMap getConfigMap(List<String> configList) {
		HashMap<String, String> configMapper = new HashMap<String, String>();
		
		// ATM Circuits
		
		for(String cnfig : configList) {
			String cnfigArray[] = cnfig.split("#");
			configMapper.put(cnfigArray[0],cnfigArray[1]);
		}
		return configMapper;
	}

	private  void generateReport(Map configMap,String secret) {
		try {
			ReportGeneration generation = new ReportGeneration(); 
			String fileName = "/report/Report.xlsx";
			 
			InputStream reportInputStream = this.getClass().getResourceAsStream(fileName);
			XSSFWorkbook workbook = new XSSFWorkbook(reportInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			generateSqlReports(sheet,configMap,secret);
			generateBashReport(sheet,configMap,secret);
			generateUIReportsFromAPI(sheet,configMap,secret);
			generateReportsForConfLevel(sheet,configMap,secret);
			
			FileOutputStream outputStream = new FileOutputStream(new File(getOutputStreamPath()+"/Report.xlsx"));
			XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

	private  void generateSqlReports(XSSFSheet sheet,Map configMap,String secret) {
		try {
			System.out.println("===============================");
			System.out.println("SQL Report Generation Started");
			
			List<String> sqlFileList = getSqlFileList();
			//String connectionArray[] = sqlConfig.split(",");
			String DB_URL = configMap.get("POSTGRES_URL").toString();
			String DB_UN = configMap.get("POSTGRES_USERNAME").toString();
			String DB_PW = configMap.get("POSTGRES_PASSWORD").toString();
			DecryptPassword decry = DecryptPassword.getInstance();
			String pgDBpass = decry.decrypt(DB_PW, secret, salt);
			//System.out.println(" DB_URL  " + DB_URL +  " DB_UN  " +  DB_UN + " DB_PW " + DB_PW);
			Connection conn = connect(DB_URL,DB_UN,pgDBpass); 
			Statement stmt = null;
			for (String fName  : sqlFileList) {
				InputStream sqlinputStream = this.getClass().getResourceAsStream("/sql/" + fName);
				String text = IOUtils.toString(sqlinputStream, StandardCharsets.UTF_8.name()); 
				
				stmt = conn.createStatement();
				HashMap rowNum = ReportGeneration.getRowMapper();
				
				ResultSet rs = stmt.executeQuery(new String(text));
				while (rs.next()) {
					Integer columnValue = (Integer) rowNum.get(fName);
					XSSFCell cell2Update = sheet.getRow(columnValue.intValue()).getCell(1);
					cell2Update.setCellValue(rs.getInt(1));
				}
			}
			
			updateFormula(sheet);
			System.out.println("SQL Report Generation Completed");
			System.out.println("================================");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private  void generateUIReports(XSSFSheet sheet,Map configMap,String secret) {
		try {
			System.out.println("===============================");
			System.out.println("UI Report Generation Started");
		
			CassandraConnect cassandraConnect = new CassandraConnect();
			String serverIP = configMap.get("CASS_IP").toString();
			String keyspace = configMap.get("CASS_KEYSPACE").toString();
			Long atmCount = cassandraConnect.getAtmPVCCount(serverIP,keyspace);
				XSSFCell cellPDHUpdate = sheet.getRow(8).getCell(2);
				cellPDHUpdate.setCellValue(atmCount);
				System.out.println("ATM UI Count " +  atmCount);
			Long pdhCount = cassandraConnect.getPDHCount(serverIP,keyspace);	
				XSSFCell cellATMUpdate = sheet.getRow(3).getCell(2);
				cellATMUpdate.setCellValue(pdhCount);
				System.out.println("PDH UI Count " + pdhCount );
			Long ipCount = cassandraConnect.getIPCount(serverIP,keyspace);	
				XSSFCell cellIPUpdate = sheet.getRow(11).getCell(2);
				cellIPUpdate.setCellValue(ipCount);
				System.out.println("IP Count " + ipCount);
			
			System.out.println("UI Report Generation Completed");
			System.out.println("=================================");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private  void generateUIReportsFromAPI(XSSFSheet sheet,Map configMap,String secret) {
		try {
			System.out.println("===============================");
			System.out.println("UI Report Generation Started");
		
			GetVfdeEntityCount entityCount = new GetVfdeEntityCount();
			
			String serverIP = configMap.get("API_IP").toString();
			String serverUser = configMap.get("API_USER").toString();
			String serverPass= configMap.get("API_PASSWORD").toString();
			DecryptPassword decry = DecryptPassword.getInstance();
			String apiPass = decry.decrypt(serverPass, secret, salt);
			
			HashMap<String, Integer> resultMap = entityCount.getTotalCount(serverIP,serverUser,apiPass);
			
			int atmCount = resultMap.get("ATM_PVC");
				XSSFCell cellPDHUpdate = sheet.getRow(8).getCell(2);
				cellPDHUpdate.setCellValue(atmCount);
				System.out.println("ATM UI Count " +  atmCount);
			int pdhCount = resultMap.get("PDH_Circuit");
				XSSFCell cellATMUpdate = sheet.getRow(3).getCell(2);
				cellATMUpdate.setCellValue(pdhCount);
				System.out.println("PDH UI Count " + pdhCount );
			int ipCount = resultMap.get("IP_Consumer");
				XSSFCell cellIPUpdate = sheet.getRow(11).getCell(2);
				cellIPUpdate.setCellValue(ipCount);
				System.out.println("IP Count " + ipCount);
			
			System.out.println("UI Report Generation Completed");
			System.out.println("=================================");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private  void generateReportsForConfLevel(XSSFSheet sheet,Map configMap,String secret) {
		try {
			System.out.println("==========================================");
			System.out.println("Confidence Level Report Generation Started");
		
			GetVfdeEntityCount entityCount = new GetVfdeEntityCount();
			
			String serverIP = configMap.get("API_IP").toString();
			String serverUser = configMap.get("API_USER").toString();
			String serverPass= configMap.get("API_PASSWORD").toString();
			DecryptPassword decry = DecryptPassword.getInstance();
			String apiPass = decry.decrypt(serverPass, secret, salt);
			
			
			//PDH Confidence Level
			HashMap<String, Integer> resultMap = entityCount.getConfidenceCountForPDH(serverIP,serverUser,apiPass);
			
			int pdhconf1 = resultMap.get("1");
				XSSFCell cellconf1Update = sheet.getRow(21).getCell(2);
				cellconf1Update.setCellValue(pdhconf1);
				
			int pdhconf2 = resultMap.get("2");
				XSSFCell cellConf2Update = sheet.getRow(22).getCell(2);
				cellConf2Update.setCellValue(pdhconf2);
				
			int pdhconf3 = resultMap.get("3");
				XSSFCell cellconf3Update = sheet.getRow(23).getCell(2);
				cellconf3Update.setCellValue(pdhconf3);
			//ATM Confidence Level
				HashMap<String, Integer> atmresultMap = entityCount.getConfidenceCountForATM(serverIP,serverUser,apiPass);
				
				int atmconf1 = atmresultMap.get("1");
					XSSFCell cellatmconf1Update = sheet.getRow(24).getCell(2);
					cellatmconf1Update.setCellValue(atmconf1);
					
				int atmconf2 = atmresultMap.get("2");
					XSSFCell cellatmConf2Update = sheet.getRow(25).getCell(2);
					cellatmConf2Update.setCellValue(atmconf2);
					
				int atmconf3 = atmresultMap.get("3");
					XSSFCell cellatmconf3Update = sheet.getRow(26).getCell(2);
					cellatmconf3Update.setCellValue(atmconf3);
			//IP Confidence Level
				HashMap<String, Integer> ipresultMap = entityCount.getConfidenceCountForIP(serverIP,serverUser,apiPass);
				
				int ipconf1 = ipresultMap.get("1");
					XSSFCell cellipconf1Update = sheet.getRow(27).getCell(2);
					cellipconf1Update.setCellValue(ipconf1);
					
				int ipconf2 = ipresultMap.get("2");
					XSSFCell cellipConf2Update = sheet.getRow(28).getCell(2);
					cellipConf2Update.setCellValue(ipconf2);
					
				int ipconf3 = ipresultMap.get("3");
					XSSFCell cellipconf3Update = sheet.getRow(29).getCell(2);
					cellipconf3Update.setCellValue(ipconf3);			
			
			System.out.println("Confidence Level Report Generation Completed");
			System.out.println("=============================================");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public  void updateFormula(XSSFSheet sheet) {
		XSSFCell cellPDHUpdate = sheet.getRow(3).getCell(1);
		cellPDHUpdate.setCellFormula("SUM(B2:B3)");
		
		XSSFCell cellATMUpdate = sheet.getRow(8).getCell(1);
		cellATMUpdate.setCellFormula("SUM(B7:B8)");
		
		XSSFCell cellIPUpdate = sheet.getRow(13).getCell(1);
		cellIPUpdate.setCellFormula("SUM(B12:B13)");
	}
	
	public  String getOutputStreamPath() {
		String absolutePath =   this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		 absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
		 absolutePath = absolutePath.replaceAll("%20"," ");
		 //return absolutePath+"/Report.xlsx";
		 return absolutePath;
	}
	
	private List getSqlFileList() {
		List<String> sqlList = new ArrayList<String>();
		// ATM Circuits
		sqlList.add("MAATMC.sql"); //3
		sqlList.add("MLATMC.sql"); //0
		sqlList.add("TATMC.sql");  //3
		//IP Circuits
		sqlList.add("MAIPC.sql"); //30
		sqlList.add("MLIPC.sql"); //15
		sqlList.add("TIPC.sql");  //45
		//PDH Circuits
		sqlList.add("MAPDHC.sql"); //4
		sqlList.add("MLPDHC.sql"); //12
		sqlList.add("TPDHC.sql");  //16
		
		return sqlList;
	}
	
	private  void generateBashReport(XSSFSheet sheet,Map configMap,String secret) {
		try {
			System.out.println("==============================");
			System.out.println("Bash Script Generation Started");
			
			//String connectionArray[] = bashConfig.split(",");
			HashMap rowNum = ReportGeneration.getBashCommandRowMapper();
			String[] listOfNe = new String[] {"pdh","atm","ip"};
			String bPass = (String) configMap.get("BASH_PASSWORD");
			DecryptPassword decry = DecryptPassword.getInstance();
			String pgDBpass = decry.decrypt(bPass, secret, salt);
			
			Session session = getSession(configMap.get("BASH_IP").toString(),configMap.get("BASH_USERNAME").toString(),pgDBpass);
			for(String ne : listOfNe) {
				String networkElements = (String) rowNum.get(ne);
				int cols = Integer.parseInt(networkElements.split(",")[0]);
				String command =networkElements.split(",")[1] ;
				XSSFCell cellUpdate = sheet.getRow(cols).getCell(4);
				String res = executeCommand(session,command);
				System.out.println("Result  " +  res);
				cellUpdate.setCellValue(res);		
			}
			session.disconnect();
			System.out.println("Bash Script Execution Completed ");
			System.out.println("================================");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	private Session getSession(String host,String user,String password) {
		Session session = null;
		try {
		    
		    java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
	    	JSch jsch = new JSch();
	    	 session=jsch.getSession(user, host, 22);
	    	session.setPassword(password);
	    	session.setConfig(config);
	    	session.connect();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
    	return session;
		
	}
	/**
	 * Connect to the PostgreSQL database
	 *
	 * @return a Connection object
	 */
	public static Connection connect(String url,String user,String password) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return conn;
	}
	
	private String executeCommand(Session session,String command) {
		String result =null;
		
	    try{
	    	
	    	 Channel channel=session.openChannel("exec");
	    
	        ((ChannelExec)channel).setCommand("cd DIILog && "+ command);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        channel.connect();
	        result = IOUtils.toString(in, "UTF-8");
	        channel.disconnect();
	        return result;
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	   
		return result;
		
	}

	private static HashMap getRowMapper() {
		HashMap<String, Integer> excelRowMapper = new HashMap<String, Integer>();
		
		// ATM Circuits
		excelRowMapper.put("MAATMC.sql",6);
		excelRowMapper.put("MLATMC.sql",7);
		excelRowMapper.put("TATMC.sql",9);
		//IP Circuits
		excelRowMapper.put("MAIPC.sql",11);
		excelRowMapper.put("MLIPC.sql",12);
		excelRowMapper.put("TIPC.sql",14);
		//PDH Circuits
		excelRowMapper.put("MAPDHC.sql",1);
		excelRowMapper.put("MLPDHC.sql",2);
		excelRowMapper.put("TPDHC.sql",4);
		return excelRowMapper;
	}
	
	private static HashMap getBashCommandRowMapper() {
		HashMap<String, String> excelRowMapper = new HashMap<String, String>();
		excelRowMapper.put("pdh", "3,grep 'VFDEBuildLog\\[9' *.csv | grep 'PDH_Circuit' | wc -l");
		excelRowMapper.put("atm", "8,grep 'VFDEBuildLog\\[9' *.csv | grep 'ATM_PVC' | wc -l");
		excelRowMapper.put("ip", "11,grep 'VFDEBuildLog\\[9' *.csv | grep 'IP_Consumer' | wc -l");
		return excelRowMapper;
	}
	
	private static HashMap getBashCommandRowMapperDummy() {
		HashMap<String, String> excelRowMapper = new HashMap<String, String>();
		excelRowMapper.put("pdh", "3,grep config SRT3LI.xml |wc -l");
		excelRowMapper.put("atm", "8,grep Count SRT3LI.xml |wc -l");
		excelRowMapper.put("ip", "11,grep robust SRT3LI.xml |wc -l");
		return excelRowMapper;
	}

	private static File[] getResourceFolderFiles(String folder) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource(folder);
		String path = url.getPath();
		try {
			List<String> filesList = IOUtils.readLines(loader.getResourceAsStream(folder));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new File(path).listFiles();
	}

}
