
package com.tnpfc.protean.API.controller;


import java.io.BufferedReader;
import java.util.Date;
import java.util.List;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tnpfc.protean.API.DTO.AccessToken;
import com.tnpfc.protean.API.DTO.protean_config;
import com.tnpfc.protean.API.Repository.ProteanConfigRepository;

@RestController
@RequestMapping("Generate")
public class AccessTokenController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccessTokenController.class);
	
	@Autowired
	private ProteanConfigRepository apirep;
	
	private static final String CONFIG = "config";
	private static String tokenUrl;
	
	static {		
		ResourceBundle bundle = ResourceBundle.getBundle(CONFIG);
        tokenUrl = bundle.getString("TokenUrl");
	}
	
	
	@PostMapping("/UpdateToken")
	public String AccessToken(@RequestBody AccessToken configreq) {
		StringBuilder response = new StringBuilder();
		 try {
			 	logger.info("InsideAPI");			 
			 	logger.info(tokenUrl);
			 	
			 	protean_config config = apirep.findByConfigtype(configreq.getConfig_type());
			 	
	            String ApiKey     = config.getApikey();	            
	            String clientSecret = config.getSecuritykey();
	            
	            logger.info("clientId=>"+ApiKey);
	            logger.info("clientSecret=>"+clientSecret);

	            // ---- BODY ----
	            String body = "grant_type=client_credentials";

	           
	           URL url = new URL(tokenUrl);
	            HttpURLConnection conn =
	                (HttpURLConnection) url.openConnection();

	            conn.setRequestMethod("POST");
	            conn.setDoOutput(true);

	            // ---- HEADERS ----
	            conn.setRequestProperty(
	                "Content-Type",
	                "application/x-www-form-urlencoded"
	            );

	            // ---- BASIC AUTH HEADER ----
	            String auth = ApiKey + ":" + clientSecret;
	            String encodedAuth =
	                Base64.getEncoder()
	                      .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

	            conn.setRequestProperty(
	                "Authorization",
	                "Basic " + encodedAuth
	            );

	            // ---- WRITE BODY ----
	            try (OutputStream os = conn.getOutputStream()) {
	                os.write(body.getBytes(StandardCharsets.UTF_8));
	            }

	            // ---- READ RESPONSE ----
	            InputStream is = 
	                (conn.getResponseCode() >= 200 &&
	                 conn.getResponseCode() < 300)
	                ? conn.getInputStream()
	                : conn.getErrorStream();

	            BufferedReader br =
	                new BufferedReader(new InputStreamReader(is));

	            
	            String line;

	            while ((line = br.readLine()) != null) {
	                response.append(line);
	            }

	            System.out.println("HTTP CODE : " + conn.getResponseCode());
	            System.out.println("RESPONSE  : " + response);
	            
	            JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();

	            String clientId     = json.get("client_id").getAsString();
	            String accessToken  = json.get("access_token").getAsString();
	            String applicationName  = json.get("application_name").getAsString();
	            String status  = json.get("status").getAsString();
	            
	            config.setClient_id(clientId);
	            config.setAccess_token(accessToken);
	            config.setApplication_name(applicationName);
	            config.setStatus(status);
	            config.setStatus_dt(new Date());
	            apirep.save(config);
	        

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		  return response.toString();
		 
	}
}
