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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private static String privateKey, publicKey, entityId;

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
        privateKey = bundle.getString("privateKey");
        publicKey = bundle.getString("publicKey");
        entityId = bundle.getString("BankEntity");
    }

    @PostMapping("/VerifyBank")
    public ResponseEntity<?> VerifyBank(@RequestBody BankAccount bank) {

        HttpURLConnection conn = null;

        try {

            protean_config config =apirep.findByConfigtype("TnpfcBanK");
            String accessToken ="Bearer " + config.getAccess_token();
            String reqId = MAIN.generateUniqueNumber();
            logger.info("Request Id : {}", reqId);

            // REQUEST JSON
            JsonObject json = new JsonObject();

            json.addProperty("entityId", entityId);
            json.addProperty("programId", bank.getProgramid());
            json.addProperty("requestId", reqId);
            json.addProperty("custIfsc", bank.getIfsccode());
            json.addProperty("custAcctNo", bank.getAccountno());
            json.addProperty("txnType", bank.getTxntype());
            String requestData = json.toString();

            logger.info("Request Data : {}", requestData);

            // ENCRYPT
            String encryptedRequest =ProTeanEncr.mainEncryption(requestData,publicKey,privateKey,reqId);

            URL url = new URL(BankUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("apikey",config.getApikey());
            conn.setRequestProperty("Authorization",accessToken);
            conn.setDoOutput(true);

            // TIMEOUT SETTINGS
            conn.setConnectTimeout(15000); // 15 seconds
            conn.setReadTimeout(30000);    // 30 seconds

            // SEND REQUEST
            try (OutputStream os = conn.getOutputStream()) {
                os.write(encryptedRequest.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            logger.info("Response Code : {}", responseCode);

            InputStream is = (responseCode >= 200 && responseCode < 300)? conn.getInputStream(): conn.getErrorStream();
            StringBuilder response = new StringBuilder();

            try (BufferedReader br =new BufferedReader(new InputStreamReader(is))) {

                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            logger.info("Encrypted Response : {}",response.toString());

            // JSON MAPPING
            ObjectMapper mapper = new ObjectMapper();
            EmailPayLoad payload =mapper.readValue( response.toString(),EmailPayLoad.class);

            // DECRYPT
            String plainData =proTeanDecry.mainDecryption(payload.getData(),payload.getSymmetricKey(),payload.getHash(),privateKey);

            logger.info("Final Response : {}", plainData);

            // SAVE TO DB HERE

            // RETURN CUSTOM RESPONSE
            return ResponseEntity.ok(plainData);

        }
        catch (java.net.SocketTimeoutException e) {

            logger.error("API Timeout Error", e);

            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout");

        }
        catch (Exception e) {

            logger.error("Bank Verification Error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to process request");
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}