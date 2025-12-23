package com.tnpfc.protean.API.PanEnc;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ProteanEncryption {
	
	// 2048 test keys
		private static String BANK_RSA_PRIVATE_KEY;
		private static String RBI_RSA_PUBLIC_KEY;
		private String plainSymmetricKey;
		
			
	public  String mainEncryption(String data, String PubKey, String PrivKey,String Random) throws Exception {
		
		String dataReceived = data;
		RBI_RSA_PUBLIC_KEY = PubKey;
		BANK_RSA_PRIVATE_KEY = PrivKey;
		
		ProteanEncryption encDec = new ProteanEncryption();
		encDec.setPlainSymmetricKey();
		JSONObject json = new JSONObject();		
		json.put("requestId", Random);
		json.put("version", "1.0.0");
		json.put("timestamp", encDec.timesStampCreation());
		json.put("symmetricKey", encDec.encryptedSymmetricKey(RBI_RSA_PUBLIC_KEY));
		json.put("data", encDec.encryptedData(dataReceived));
		json.put("hash", HelperFunctions.payloadSignatureGenerator(dataReceived,encDec.getPlainSymmetricKey()));
		return json.toString();
	}


private String timesStampCreation() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'T'HH:mm:ss.SSS");
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		Date currentDatePlusOne = c.getTime();
		return dateFormat.format(currentDatePlusOne);
	}


private String encryptedSymmetricKey(String privKey) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		String plainSymmKey = getPlainSymmetricKey();
		String encryptedSymmetricKey = RsaEcbOAEPPadding.encrypt(plainSymmKey,privKey);
		return encryptedSymmetricKey;
	}
	
	private String encryptedData(String data) throws Exception {
		String plainSymmKey = getPlainSymmetricKey();
		String encryptedData = AesGcmNoPadding.encrypt(data,plainSymmKey);
		return encryptedData;
	}

private void setPlainSymmetricKey() {
		String plainSymmKey = HelperFunctions.getRandomSymmetricKey();
		plainSymmetricKey = plainSymmKey;
	}
	
	private String getPlainSymmetricKey() {
		return this.plainSymmetricKey;
	}
	
}
