
package com.tnpfc.protean.API.controller;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnpfc.protean.API.DTO.AccessToken;

@RestController
@RequestMapping("Generate")
public class AccessTokenController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccessTokenController.class);
	
	
	@PostMapping("/UpdateToken")
	public String AccessToken(@RequestBody AccessToken configreq) {
		StringBuilder response = new StringBuilder();
		 try {
			 System.out.println("InsideAPI");

	            String tokenUrl =
	                "https://uat.risewithprotean.io/v1/oauth/token";

	            // ---- FROM POSTMAN ----
	            String clientId     = "4egiI7JvsBWIPPbGqYkACmrNR1Tq52CMLshvdbivUcGbRUk1";
	            String clientSecret = "qyYBzyFy10n4buZI3w0AtGsYlpApYbFWVlpmo1xk6VUgSGK1GXuxWxpKYIvXh1I5"; // same as Postman password

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
	            String auth = clientId + ":" + clientSecret;
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
	          

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		  return response.toString();
		 
	}
}
