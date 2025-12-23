package com.tnpfc.protean.API.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
import com.google.gson.JsonObject;
import com.tnpfc.protean.API.DTO.BankAccount;
import com.tnpfc.protean.API.DTO.EmailPayLoad;
import com.tnpfc.protean.API.DTO.protean_config;
import com.tnpfc.protean.API.PanEnc.ProteanDecryption;
import com.tnpfc.protean.API.PanEnc.ProteanEncryption;
import com.tnpfc.protean.API.Repository.ProteanConfigRepository;
import com.tnpfc.protean.API.Util.UtilityMAIN;

@RestController
@RequestMapping("/Bank")
public class BankAccountController {
	private static final Logger logger = LoggerFactory.getLogger(BankAccountController.class);
	private static final String CONFIG = "config";
	private static String BankUrl;
	private static String privateKey,publicKey,entityId;
	
	
	@Autowired
	private UtilityMAIN MAIN;
	
	@Autowired	
	private ProteanConfigRepository apirep;
	
	@Autowired
	private ProteanEncryption ProTeanEncr;
	
	@Autowired
	private ProteanDecryption proTeanDecry;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle(CONFIG);
		BankUrl = bundle.getString("BankUrl");
		privateKey 	= bundle.getString("privateKey");
		publicKey 	= bundle.getString("publicKey");
		entityId	= bundle.getString("BankEntity");
	}
	
	@PostMapping("/VerifyBank")
	public String VerifyBank(@RequestBody BankAccount bank) {
		String plain_data="";
		
		try {
		protean_config config = apirep.findByConfigtype("TnpfcAPI");
		String AccessToken = "Bearer "+config.getAccess_token();
		logger.info("AccessToken=>"+AccessToken);
		String ReqId=MAIN.generateUniqueNumber();
		
		
		JsonObject json = new JsonObject(); 
		json.addProperty("entityId", entityId);
		json.addProperty("programId", bank.getProgramid());
		json.addProperty("requestId", ReqId);
		json.addProperty("custIfsc", bank.getIfsccode());
		json.addProperty("custAcctNo", bank.getAccountno());
		json.addProperty("txnType",bank.getTxntype());
		
		String data= json.toString();
		logger.info("Data:"+data);
		
		String JsonValue = ProTeanEncr.mainEncryption(data,publicKey,privateKey,ReqId);
		logger.info(BankUrl);
		logger.info(JsonValue);
		URL url = new URL(BankUrl);
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
			
		}catch(Exception e) {
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
