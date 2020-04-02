package com.vfde.reports;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import com.datastax.driver.core.Cluster;  
import com.datastax.driver.core.Host;  
import com.datastax.driver.core.Metadata;  
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class CassandraConnect {

	public static void main(String[] args) {
		
		
		CassandraConnect cassandraTestConnect = new CassandraConnect();
		cassandraTestConnect.connectToPS02Cassandra();
		

	}
	
	
	public void connectToDevCassandra(){
		
		String serverIP = "skynet01.cenx.localnet";
		String keyspace = "vodafone_dev_envcenx";
		String cqlStatement = "select count(*) from entity where type = 'ATM_PVC' ALLOW FILTERING ;";
		Cluster cluster = Cluster.builder()
		  .addContactPoints(serverIP)
		  .build();

		Session session = cluster.connect(keyspace);
		ResultSet resultSet = session.execute(cqlStatement);
		System.out.println(resultSet.all().get(0).getLong(0));		
	}
	
	public void connectToPS02Cassandra(){
		try {
		String serverIP = "ps09.cenx.localnet";
		String keyspace = "ps01cenx";
		String atmCqlStatement = "select count(*) from entity where type = 'ATM_PVC' ALLOW FILTERING ;";
		String ipCqlStatement = "select count(*) from entity where type = 'IP_Consumer' ALLOW FILTERING  ;";
		String pdhCqlStatement = "select count(*) from entity where type = 'PDH_Circuit' ALLOW FILTERING  ;";
		
		 LocalTime myObj = LocalTime.now();
		    System.out.println(myObj);
		Cluster cluster = Cluster.builder()
		  .addContactPoints(serverIP)
		  .build();

		Session session = cluster.connect(keyspace);
		//ResultSet resultSet = session.execute(atmCqlStatement);
		
		ResultSet resultSet = session.execute(
		        new SimpleStatement(atmCqlStatement).setReadTimeoutMillis(120000));
		System.out.println("ATM " + resultSet.all().get(0).getLong(0));	
		
		resultSet = session.execute(
		        new SimpleStatement(ipCqlStatement).setReadTimeoutMillis(120000));
		System.out.println("IP " + resultSet.all().get(0).getLong(0));	
		
		resultSet = session.execute(
		        new SimpleStatement(pdhCqlStatement).setReadTimeoutMillis(120000));
		System.out.println("PDH " + resultSet.all().get(0).getLong(0));	
		
		
		 LocalTime myObj1 = LocalTime.now();
		    System.out.println(myObj1);
		    
		    session.close();
		    cluster.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Long getAtmPVCCount(String serverIP,String keyspace){
		Long result = 0L;
		try {
		//String serverIP = "ps09.cenx.localnet";
		//String keyspace = "ps01cenx";
		String atmCqlStatement = "select count(*) from entity where type = 'ATM_PVC' ALLOW FILTERING ;";

		Cluster cluster = Cluster.builder()
		  .addContactPoints(serverIP)
		  .build();

		Session session = cluster.connect(keyspace);
		
		ResultSet resultSet = session.execute(
		        new SimpleStatement(atmCqlStatement).setReadTimeoutMillis(120000));
		result= resultSet.all().get(0).getLong(0);	
		
		    session.close();
		    cluster.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return result ;
	}
	
	public Long getPDHCount(String serverIP,String keyspace){
		Long result = 0L ;
		try {
		//String serverIP = "ps09.cenx.localnet";
		//String keyspace = "ps01cenx";
		
		String pdhCqlStatement = "select count(*) from entity where type = 'PDH_Circuit' ALLOW FILTERING  ;";
		
		Cluster cluster = Cluster.builder()
		  .addContactPoints(serverIP)
		  .build();

		Session session = cluster.connect(keyspace);
		
		ResultSet 
		resultSet = session.execute(
		        new SimpleStatement(pdhCqlStatement).setReadTimeoutMillis(120000));
		result = resultSet.all().get(0).getLong(0);
		
		    session.close();
		    cluster.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public Long getIPCount(String serverIP,String keyspace){
		Long result = 0L ;
		try {
		//String serverIP = "ps09.cenx.localnet";
		//String keyspace = "ps01cenx";

		String ipCqlStatement = "select count(*) from entity where type = 'IP_Consumer' ALLOW FILTERING  ;";
		
		Cluster cluster = Cluster.builder()
		  .addContactPoints(serverIP)
		  .build();

		Session session = cluster.connect(keyspace);
		
		ResultSet resultSet = session.execute(
		        new SimpleStatement(ipCqlStatement).setReadTimeoutMillis(120000));
		result = resultSet.all().get(0).getLong(0);
		
		    session.close();
		    cluster.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

}
