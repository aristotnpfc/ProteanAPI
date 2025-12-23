package com.tnpfc.protean.API.PanEnc;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.stereotype.Service;

@Service
public class ProteanDecryption {
	
	private static String BANK_RSA_PRIVATE_KEY;
	private static String RBI_RSA_PUBLIC_KEY;
	private String plainSymmetricKey;
	
					
		//String plainData = mainDecryption(data,symmetricKey,hash,PrivKey);	
		
		
		public  String mainDecryption(String data,
				 String symmetricKey,
				 String hash,
				 String bankPrivKey
				) throws Exception {

				
				
					String dataReceived = data;
					String symmetricKeyReceived = symmetricKey;
					String hashReceived = hash;
					BANK_RSA_PRIVATE_KEY = bankPrivKey;
					System.out.println("Private 2nd Length=>"+BANK_RSA_PRIVATE_KEY.length());
				//	System.out.println(data);
				//	System.out.println(symmetricKey);
				//	System.out.println(hash);
				//	System.out.println(BANK_RSA_PRIVATE_KEY);
					
					String encryptedSymmetricKey = symmetricKeyReceived;
					ProteanDecryption encDec = new ProteanDecryption();
					
					
					encDec.plainSymmetricKey = encDec.decryptedSymmetricKey(encryptedSymmetricKey, BANK_RSA_PRIVATE_KEY);
					
					String plainData;		
					plainData = encDec.decryptedData(dataReceived, encDec.plainSymmetricKey);
					
					
					String hashGenerated = HelperFunctions.payloadSignatureGenerator(plainData, encDec.plainSymmetricKey);
						if (!hashGenerated.equals(hashReceived)) {
							throw new Exception("hash not matching");
						}
					
					
					return plainData;
			}	

		private String decryptedSymmetricKey(String encryptedKey, String PrivateKey)
		throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException,
		NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException {
			String encryptedKeyReceived = encryptedKey;
			String PrivateKeyReceived = PrivateKey;
			String plainSymmetricKey = RsaEcbOAEPPadding.decrypt(encryptedKeyReceived, PrivateKeyReceived);
			return plainSymmetricKey;
		}

		private String decryptedData(String data, String plainSymmetricKey) throws Exception {
			String encryptedData = data;
			String plainSymmetricKeyReceived = plainSymmetricKey;
			String decryptedData = AesGcmNoPadding.decrypt(encryptedData, plainSymmetricKeyReceived);
			return decryptedData;
		}

}
