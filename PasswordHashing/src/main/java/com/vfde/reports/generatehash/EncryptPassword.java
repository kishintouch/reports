package com.vfde.reports.generatehash;

import java.security.NoSuchAlgorithmException;
import java.util.Base64; 
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;

public class EncryptPassword {
	//AES – Advanced Encryption Standard
	private static String salt = "cenxvfde";
	public static void main(String[] args) throws NoSuchAlgorithmException 
	{
	    
	    String stringToHash = args[0];
	    String secretKey = args[1];
	     
	    String encryptedString = EncryptPassword.encrypt(stringToHash, secretKey,salt) ;
	    System.out.println(encryptedString);
	
	}
	
	
	public static String encrypt(String strToEncrypt, String secret,String salt) 
	{
	    try
	    {
	        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	         
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	         
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
	        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	    } 
	    catch (Exception e) 
	    {
	        System.out.println("Error while encrypting: " + e.toString());
	    }
	    return null;
	}
	
	
}
