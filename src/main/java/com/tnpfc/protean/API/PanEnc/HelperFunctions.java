package com.tnpfc.protean.API.PanEnc;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HelperFunctions {
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}
	
	public static PublicKey getPublicKey(String base64PublicKey) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec (Base64.getDecoder().decode(base64PublicKey.getBytes())); 
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic (keySpec); 
		return publicKey;
	}
	
	public static PrivateKey getPrivateKey(String base64PrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException { 
		PrivateKey privateKey = null;
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec (Base64.getDecoder().decode(base64PrivateKey.getBytes())); KeyFactory keyFactory = null;
		keyFactory = KeyFactory.getInstance("RSA");
		privateKey = keyFactory.generatePrivate (keySpec);
		return privateKey;
	}
	
	public static String getRandomSymmetricKey() {
		String symmetrickey = randomString(16);
		return symmetrickey;
	}
	
	public static String payloadSignatureGenerator(String data, String plainSymmetrickey) throws InvalidKeyException, NoSuchAlgorithmException{
		String plainSymmetrickeyReceived = plainSymmetrickey;
		Mac hasher = null;
		hasher = Mac.getInstance("HmacSHA256");
		hasher.init(new SecretKeySpec(plainSymmetrickeyReceived.getBytes(), "HmacSHA256"));
		return Base64.getEncoder().encodeToString(hasher.doFinal(data.getBytes())); 
	}
}