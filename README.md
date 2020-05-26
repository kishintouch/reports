# reports
This project will connect to cassandra , posgres db , stand alone server and execute commands .Generate reports based on it .
READ ME :  REPORT AUTOMATION MODULE - RAM

Password Encryption : 
To Encrypt password execute the below command and copy paste the encrypted password in the config.txt.

Command : java -jar PasswordHashing-v1-jar-with-dependencies.jar textToBeEncrypted secretKey
Example : java -jar PasswordHashing-v1-jar-with-dependencies.jar test cenx


Report Generation : 
As part of RAM we will be generating below mentioned reports .It has to be ensured we have setup required Prerequisite for corresponding Modules .
For RAM module we need to configure config.txt with required details , password are to be encrypted using Password Encryption module .
Reports module jar , config.txt should be placed in the same folder and below mentioned command to be executed .secretKey is used by module to 
decrypt password present in the config.txt and it has to be ensured we use same key for enrypt and decrypt.Final Report.xlsx will be generated in the same Folder.


Command : java -jar Reports-v1-jar-with-dependencies.jar "config.txt" "secretKey"

1.Prerequisite for SQL Reports is 
	a.POSTGRES_URL,POSTGRES_USERNAME,POSTGRES_DB,POSTGRES_PASSWORD should be configured in config.txt.POSTGRES_PASSWORD should be enrypted as mentioned in 
	  Password Encryption Module .
	b.Sql required for Reports Module is Preconfigured in the RAM module, which will connect to required DB as per configuration and Reports are generated.


2.Prerequisite for Bash Reports from Log File  is
    a.BASH_IP,BASH_USERNAME,BASH_PASSWORD should be configured in config.txt.BASH_PASSWORD should be enrypted as mentioned in 
	  Password Encryption Module .
	b.VFDEBuildLog*.csv should be downloaded manually and placed in the remote server .
	c.Proper Connectivity between Remote Server and from where Report generation module is executed should be ensured.
Report Auomation Module will Connect  to Remote Server Using Java JSch and Navigate to DIILog ,Execute GREP Command.
Outcome of GREP command results are captured and Reports is generated.


3.Prerequisite for Count From API  and Conf Level From API .
	a.API_IP,API_USER and API_PASSWORD should be configured in config.txt.API_PASSWORD should be enrypted as mentioned in 
	  Password Encryption Module .

