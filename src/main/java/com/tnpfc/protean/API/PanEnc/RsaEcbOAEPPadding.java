package com.tnpfc.protean.API.PanEnc;

import java.security.InvalidAlgorithmParameterException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

public class RsaEcbOAEPPadding {
	// encrypt symmetric key using RSA/ECB/OAEPPadding
	public static String encrypt(String data,String rsaPublicKey) throws BadPaddingException, IllegalBlockSizeException,
	InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException { 
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
		OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT); 
		cipher.init(Cipher. ENCRYPT_MODE, HelperFunctions.getPublicKey(rsaPublicKey), oaepParams);
		return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
	}
	
	// decrypt symmetric key using RSA/ECB/OAEPPadding
	public static String decrypt(String data,String rsaPrivateKey)
		throws NoSuchPaddingException, NoSuchAlgorithmException,InvalidKeyException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IllegalBlockSizeException { 
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding"); 
		OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT); 
		cipher.init(Cipher.DECRYPT_MODE, HelperFunctions.getPrivateKey(rsaPrivateKey), oaepParams);
		return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes())));
	}
}
