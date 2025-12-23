package com.tnpfc.protean.API.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnpfc.protean.API.DTO.EmailPayLoad;
import com.tnpfc.protean.API.DTO.EmailVerify;
import com.tnpfc.protean.API.DTO.protean_config;
import com.tnpfc.protean.API.PanEnc.ProteanDecryption;
import com.tnpfc.protean.API.PanEnc.ProteanEncryption;
import com.tnpfc.protean.API.Repository.ProteanConfigRepository;
import com.tnpfc.protean.API.Repository.SequenceDao;

@RestController
@RequestMapping("/Email")
public class EmailController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
	private static final String CONFIG = "config";
	private static String EmailUrl;
	private static Long seqId;
	private static String privateKey,publicKey;
	
	@Autowired	
	private ProteanConfigRepository apirep;
	
	@Autowired
	private SequenceDao sequenceDao;
	
	@Autowired
	private ProteanEncryption ProTeanEncr;
	
	@Autowired
	private ProteanDecryption proTeanDecry;
	
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle(CONFIG);
		EmailUrl 	= bundle.getString("EmailUrl");
		privateKey 	= bundle.getString("privateKey");
		publicKey 	= bundle.getString("publicKey");
		
	}
	
	@PostMapping("/EmailVerify")
	public String VerifyEmail(@RequestBody EmailVerify email) {
		seqId = sequenceDao.getNextVal();
		String plain_data="";
		try {
			protean_config config = apirep.findByConfigtype("TnpfcAPI");
			String AccessToken = "Bearer "+config.getAccess_token();
			logger.info("AccessToken=>"+AccessToken);
		
			
			String data =
				    "{\r\n" +
				    "  \"email\": \"" + email.getEmailid() + "\",\r\n" +
				    "  \"version\": \"2.1\",\r\n" +
				    "  \"clientData\": {\r\n" +
				    "    \"caseId\": \"" + seqId +"\" \r\n" +
				    "  }\r\n" +
				    "}";

			
			logger.info("data=>"+data);
		
		
			String JsonValue = ProTeanEncr.mainEncryption(data,publicKey,privateKey,getRandomString());
			logger.info(JsonValue);
			
			URL url = new URL(EmailUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("apikey", config.getApikey()); // Api Key
	        conn.setRequestProperty("Authorization", AccessToken); //Security Key
	        conn.setDoOutput(true);       

	        
	        try (OutputStream os = conn.getOutputStream()) {
	            os.write(JsonValue.getBytes(StandardCharsets.UTF_8));
	        }
	        
	        InputStream is = (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300)
	                ? conn.getInputStream()
	                : conn.getErrorStream();

	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        StringBuilder response = new StringBuilder();
	        String line;

	        while ((line = br.readLine()) != null) {
	            response.append(line);
	        }       
	        
	        ObjectMapper mapper = new ObjectMapper();
	        
	        System.out.println("Response Code: " + response.toString());    
	      
	       
	        EmailPayLoad payload = mapper.readValue(response.toString(), EmailPayLoad.class);
	       
	        plain_data = proTeanDecry.mainDecryption(payload.getData(), payload.getSymmetricKey(), payload.getHash(), privateKey);
	        
	        System.out.println("Final Data: " + plain_data);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return plain_data;
	}
	
	public String getRandomString()
	{
	 
	 String randomStr = UUID.randomUUID().toString();
	    while(randomStr.length() < 5) {
	        randomStr += UUID.randomUUID().toString();
	    }		    
	    logger.info("Process_id for Email Push->"+randomStr.substring(0, 5).toUpperCase());		    
	    return randomStr.substring(0, 5);
	    
	}
	
}
