package com.vfde.reports;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.commons.io.IOUtils;
import com.vfde.reports.AuthToken;
import com.google.gson.Gson;
import java.io.BufferedReader;

import java.io.BufferedWriter;

import java.io.FileWriter;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.OutputStreamWriter;

import java.io.PrintWriter;

import java.io.StringReader;

import java.net.Socket;

import java.net.URL;

import java.net.URLConnection;

import java.net.URLEncoder;
import java.time.LocalTime;


public class GetVfdeEntityCount {
	
	
	public static void main(String[] args) {
		String endConnectUrl = "vodafone-tools01.cenx.localnet";
		
		String psUrl = "ps02.cenx.localnet";
		//getUserSessionToken(endConnectUrl);
		GetVfdeEntityCount entityCount = new GetVfdeEntityCount();
		//entityCount.getAtmPVCCountTest(psUrl);
		//entityCount.getPDHCount(endConnectUrl);
		//entityCount.getIPCount(endConnectUrl);
		//entityCount.getTotalCount(psUrl);
		//entityCount.getConfidenceCountForATM(url,serverUser,serverPass);
		//String validUrl = "http://vodafone-tools01.cenx.localnet:8080/cenx/api-docs/topology/entity/ATM_PVC?token=0f18e09a648b2b8eb97e98209e041524";
		
	}

	public  String getUserSessionToken(String url,String user,String pass) {
		try {
			String sessionEndpoint = "http://" + url  + ":8080/cenx/api-docs/session/login?username=" + user + "&password=" + pass +"&domain=ldap";
			URL sessionUrl = new URL(sessionEndpoint);
			HttpURLConnection conn = (HttpURLConnection) sessionUrl.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
	
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			
			List<String> sessionTokenResponse = IOUtils.readLines(conn.getInputStream(), StandardCharsets.UTF_8.name());

			
			Gson gson = new Gson();

			AuthToken authToken = gson.fromJson(sessionTokenResponse.get(0),AuthToken.class );
			 
			conn.disconnect();
			return authToken.token;
		
		
		}catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		return null;
	}
	
	public  int getAtmPVCCount(String url,String serverUser,String serverPass) {
		int result = 0 ;
		try {
			
			String token = getUserSessionToken(url,serverUser,serverPass);
			String sessionEndpoint = "http://" + url  + ":8080/cenx/api-docs/topology/entity/ATM_PVC";
			String param = "token="+token;
		    URL atmurl = new URL(sessionEndpoint + "?" + param);
			
          	HttpURLConnection atmconn = (HttpURLConnection) atmurl.openConnection();
          	atmconn.setRequestMethod("GET");
          	atmconn.setRequestProperty("bufferedWriterSize", "200000");
        	System.out.println("ATM Response Code " + atmconn.getResponseCode());
          	if (atmconn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + atmconn.getResponseCode());
			}
			
              
             List<String> atmConnectionResult = IOUtils.readLines(atmconn.getInputStream(), StandardCharsets.UTF_8.name());
            
             result = atmConnectionResult.size();
             atmconn.disconnect();
		
		}catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		return result;
	}
	
	
	public  int getAtmPVCCountTest(String url,String serverUser,String serverPass) {
		int result = 0 ;
		try {
			 LocalTime myObj = LocalTime.now();
			    System.out.println(myObj);
			String token = getUserSessionToken(url,serverUser,serverPass);
			String sessionEndpoint = "http://" + url  + ":8080/cenx/api-docs/topology/entity/ATM_PVC";
			String param = "token="+token;
		    URL atmurl = new URL(sessionEndpoint + "?" + param);
			
          	HttpURLConnection atmconn = (HttpURLConnection) atmurl.openConnection();
          	atmconn.setRequestMethod("GET");
          	atmconn.setRequestProperty("bufferedWriterSize", "20000000");
        	System.out.println("ATM Response Code Test " + atmconn.getResponseCode());
          	if (atmconn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + atmconn.getResponseCode());
			}
			
          	BufferedReader reader = new BufferedReader(new InputStreamReader(atmconn.getInputStream()));
          	int atmCount = 0;

    		String line;

    		while((line = reader.readLine()) != null) {

    			atmCount++;

    		}

    		reader.close();
    		System.out.println("AtmCount From test " + atmCount);
    		 LocalTime myObj1 = LocalTime.now();
			    System.out.println(myObj1);
            // List<String> atmConnectionResult = IOUtils.readLines(atmconn.getInputStream(), StandardCharsets.UTF_8.name());
            
            // result = atmConnectionResult.size();
             atmconn.disconnect();
		
		}catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		return result;
	}
	
	public  int getPDHCount(String url,String serverUser,String serverPass) {
		int result = 0 ;
		try {
			
			String token = getUserSessionToken(url,serverUser,serverPass);
			String sessionEndpoint = "http://" + url  + ":8080/cenx/api-docs/topology/entity/PDH_Circuit";
			String param = "token="+token;
		    URL atmurl = new URL(sessionEndpoint + "?" + param);
			
          	HttpURLConnection pdhconn = (HttpURLConnection) atmurl.openConnection();
          	pdhconn.setRequestMethod("GET");
          	pdhconn.setRequestProperty("bufferedWriterSize", "200000");
        	//System.out.println("PDH Response Code " + pdhconn.getResponseCode());
          	if (pdhconn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + pdhconn.getResponseCode());
			}
			
              
             List<String> pdhConnectionResult = IOUtils.readLines(pdhconn.getInputStream(), StandardCharsets.UTF_8.name());
             
             result = pdhConnectionResult.size();
             pdhconn.disconnect();
		
		}catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		return result;
	}
	
	public  int getIPCount(String url,String serverUser,String serverPass) {
		int result = 0 ;
		try {
			
			String token = getUserSessionToken(url,serverUser,serverPass);
			String sessionEndpoint = "http://" + url  + ":8080/cenx/api-docs/topology/entity/IP_Consumer";
			String param = "token="+token;
		    URL atmurl = new URL(sessionEndpoint + "?" + param);
			
          	HttpURLConnection ipconn = (HttpURLConnection) atmurl.openConnection();
          	ipconn.setRequestMethod("GET");
          	ipconn.setRequestProperty("bufferedWriterSize", "200000");
        	//System.out.println("IP Response Code " + ipconn.getResponseCode());
          	if (ipconn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + ipconn.getResponseCode());
			}
			
              
             List<String> IPConnectionResult = IOUtils.readLines(ipconn.getInputStream(), StandardCharsets.UTF_8.name());
            
             result = IPConnectionResult.size();
             ipconn.disconnect();
		
		}catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		return result;
	}
	
	public  HashMap getTotalCount(String url,String serverUser,String serverPass) {
		HashMap<String, Integer> resultMap = null ;
		try {
			
			String token = getUserSessionToken(url,serverUser,serverPass);
			String sessionEndpoint = "http://" + url  + ":8080/api/inventory-dashboard/total-entities-by-type";
			String param = "token="+token;
			String q = "&q=&field=data._type&filter=true" ;
		    URL atmurl = new URL(sessionEndpoint + "?" + param + q	);
		   
			
          	HttpURLConnection atmconn = (HttpURLConnection) atmurl.openConnection();
          	atmconn.setRequestMethod("GET");
        	System.out.println("API Response Code " + atmconn.getResponseCode());
          	if (atmconn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + atmconn.getResponseCode());
			}
			
              
            List<String> atmConnectionResult = IOUtils.readLines(atmconn.getInputStream(), StandardCharsets.UTF_8.name());
 			Gson gson = new Gson();
 			ApiResponse[] apiResponse = gson.fromJson(atmConnectionResult.get(0),ApiResponse[].class );
 			
 			 resultMap = prepareResultMap(apiResponse);
             atmconn.disconnect();
		
		}catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		return resultMap;
	}
	
	private static HashMap prepareResultMap(ApiResponse[] apiResponse) {
		HashMap<String,Integer > resultMap = new HashMap<String, Integer>();
		
		for(ApiResponse res : apiResponse) {
			resultMap.put(res._base, Integer.parseInt(res.total));
		}
		
		return resultMap;
	}
	
	public  HashMap getConfidenceCountForATM(String url,String serverUser,String serverPass) {
		
		HashMap<String, Integer> resultATMConfMap  = new HashMap<String, Integer>();  ;
		String token = getUserSessionToken(url,serverUser,serverPass);
		String sessionEndpoint = "http://" + url  + ":8080/api/inventory-dashboard/total-entities-by-type";
		String confidence1 = "&q=ATM_PVC.confidence%3A\"1\"&field=data._type&filter=true";
		String confidence2 = "&q=ATM_PVC.confidence%3A\"2\"&field=data._type&filter=true";
		String confidence3 = "&q=ATM_PVC.confidence%3A\"3\"&field=data._type&filter=true";
		String param = "token="+token;
		String filter = "ATM_PVC";
		
		//Conf Level 1
		int conf1 = getConfidenceLevel(sessionEndpoint,param,confidence1,filter);
		System.out.println("ATM Conf1 " + conf1 );
			resultATMConfMap.put("1", conf1)	;
		
		//Conf Level 2
		int conf2 = getConfidenceLevel(sessionEndpoint,param,confidence2,filter);
		System.out.println("ATM Conf2 " + conf2 );
			resultATMConfMap.put("2", conf2)	;
			
		//Conf Level 3
		int conf3 = getConfidenceLevel(sessionEndpoint,param,confidence3,filter);
		System.out.println("ATM Conf3 " + conf3 );
			resultATMConfMap.put("3", conf3)	;
		
		return resultATMConfMap;
	}
	
public  HashMap getConfidenceCountForPDH(String url,String serverUser,String serverPass) {
		
		HashMap<String, Integer> resultPDHConfMap  = new HashMap<String, Integer>();  ;
		String token = getUserSessionToken(url,serverUser,serverPass);
		String sessionEndpoint = "http://" + url  + ":8080/api/inventory-dashboard/total-entities-by-type";
		String confidence1 = "&q=PDH_Circuit.confidence%3A\"1\"&field=data._type&filter=true";
		String confidence2 = "&q=PDH_Circuit.confidence%3A\"2\"&field=data._type&filter=true";
		String confidence3 = "&q=PDH_Circuit.confidence%3A\"3\"&field=data._type&filter=true";
		String param = "token="+token;
		String filter = "PDH_Circuit";
		
		//Conf Level 1
		int conf1 = getConfidenceLevel(sessionEndpoint,param,confidence1,filter);
		System.out.println("PDH Conf1 " + conf1 );
			resultPDHConfMap.put("1", conf1)	;
		
		//Conf Level 2
		int conf2 = getConfidenceLevel(sessionEndpoint,param,confidence2,filter);
		System.out.println("PDH Conf2 " + conf2 );
			resultPDHConfMap.put("2", conf2)	;
			
		//Conf Level 3
		int conf3 = getConfidenceLevel(sessionEndpoint,param,confidence3,filter);
		System.out.println("PDH Conf3 " + conf3 );
			resultPDHConfMap.put("3", conf3)	;
		
		return resultPDHConfMap;
	}
	
public  HashMap getConfidenceCountForIP(String url,String serverUser,String serverPass) {
	
	HashMap<String, Integer> resultIPConfMap  = new HashMap<String, Integer>();  ;
	String token = getUserSessionToken(url,serverUser,serverPass);
	String sessionEndpoint = "http://" + url  + ":8080/api/inventory-dashboard/total-entities-by-type";
	String confidence1 = "&q=IP_Consumer.confidence%3A\"1\"&field=data._type&filter=true";
	String confidence2 = "&q=IP_Consumer.confidence%3A\"2\"&field=data._type&filter=true";
	String confidence3 = "&q=IP_Consumer.confidence%3A\"3\"&field=data._type&filter=true";
	String param = "token="+token;
	String filter = "IP_Consumer";
	
	//Conf Level 1
	int conf1 = getConfidenceLevel(sessionEndpoint,param,confidence1,filter);
	System.out.println("IP Conf1 " + conf1 );
		resultIPConfMap.put("1", conf1)	;
	
	//Conf Level 2
	int conf2 = getConfidenceLevel(sessionEndpoint,param,confidence2,filter);
	System.out.println("IP Conf2 " + conf2 );
		resultIPConfMap.put("2", conf2)	;
		
	//Conf Level 3
	int conf3 = getConfidenceLevel(sessionEndpoint,param,confidence3,filter);
	System.out.println("IP Conf3 " + conf3 );
		resultIPConfMap.put("3", conf3)	;
	
	return resultIPConfMap;
}
	
	public int getConfidenceLevel(String sessionEndpoint,String param,String level,String filter) {
		int confCount= 0 ;
		try {
			URL url = new URL(sessionEndpoint + "?" + param + level	);
			   
	      	HttpURLConnection connect = (HttpURLConnection) url.openConnection();
	      	connect.setRequestMethod("GET");
	    	
	      	if (connect.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + connect.getResponseCode());
			}
			  
	        List<String> connectionResult = IOUtils.readLines(connect.getInputStream(), StandardCharsets.UTF_8.name());
				Gson gson = new Gson();
				ApiResponse[] apiResponse1 = gson.fromJson(connectionResult.get(0),ApiResponse[].class );
				 connect.disconnect();
				for(ApiResponse res : apiResponse1) {
					if(res._base.equals(filter)) {
						confCount=  Integer.parseInt(res.value);
						return confCount;
					}
				}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return confCount;
	}
	

}
