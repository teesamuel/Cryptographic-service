package com.amplus.crypto.service;

import com.amplus.crypto.Utils.components.AppSettings;
import com.amplus.crypto.Utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CryptoService {

private static final Logger logger = Logger.getLogger(CryptoService.class.getName());

@Autowired
AppSettings appSettings;

@Autowired
ResponseUtility responseUtility;

String RSA_ALGO = "RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING";
String SIG_ALGO = "SHA256WithRSA";
int IV_LENGTH = 16;
int AES_LENGTH = 256;
String AES_ALGO = "AES/CBC/PKCS5Padding";


int RSA_LENGTH = 2048;

   public ResponseEntity<Object> encrypt(String data) {
      try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(AES_LENGTH);
            SecretKey key = generator.generateKey();
            byte[] aesKey = key.getEncoded();
//            IV generation
            byte[] iv = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
//            Encrypt message with AES key
            SecretKey secKey = new SecretKeySpec(aesKey, "AES");
//            SecretKey secKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(AES_ALGO);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secKey, ivSpec);

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            byte[] ret = new byte[IV_LENGTH + encrypted.length];
            System.arraycopy(iv, 0, ret, 0, IV_LENGTH);
            System.arraycopy(encrypted, 0, ret, IV_LENGTH, encrypted.length);
            byte[] emBytes = ret;

//            Encrypt key with RSA
            byte[] pubKeyBytes = Helper.getKeyFileContentFromNameAsByteArray(appSettings.getPublicKeyFile()); // service public key here
            PublicKey apiUserPublicKey = KeyFactory.getInstance("RSA").generatePublic(
                    new X509EncodedKeySpec(pubKeyBytes));
            Cipher cipher2 = Cipher.getInstance(RSA_ALGO);
            cipher2.init(Cipher.ENCRYPT_MODE, apiUserPublicKey);
            byte[] ekBytes = cipher2.doFinal(aesKey);
//            Sign
            Signature signature = Signature.getInstance(SIG_ALGO);
            byte[] privKeyBytes = Helper.getKeyFileContentFromNameAsByteArray(appSettings.getPrivateKeyFile());; // your private key here
            PrivateKey apiUserPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(
                    new PKCS8EncodedKeySpec(privKeyBytes));
            signature.initSign(apiUserPrivateKey);
            signature.update(emBytes);
            byte[] signatureBytes = signature.sign();
//            Now you only need to convert bytes to base64 strings

            String ek = Base64.getEncoder().encodeToString(ekBytes);
            String em = Base64.getEncoder().encodeToString(emBytes);
            String sig = Base64.getEncoder().encodeToString(signatureBytes);

            Map<String, String> res = new HashMap<>();
            res.put("ek", ek);
            res.put("em", em);
            res.put("sig", sig);

            return new ResponseEntity<Object>(responseUtility.response(0, res), HttpStatus.OK);
        } catch (Exception e) {
            logger.info("config begin with error");
            e.printStackTrace();
            logger.info(e.getMessage());
            Map<String, String> res = new HashMap<>();
            res.put("logMessage", e.getMessage());
           return new ResponseEntity<Object>(responseUtility.response(3, res), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> decrypt(String em, String sig , String ek ){
        try {
            //            Check signature
//            byte[] publicKeyBytes = ...; //service public key here
            byte[] publicKeyBytes = Helper.getKeyFileContentFromNameAsByteArray(appSettings.getPublicKeyFile()); // service public key here
            byte[] emBytes = Base64.getDecoder().decode(em);
            byte[] sigBytes = Base64.getDecoder().decode(sig);
            Signature s = Signature.getInstance(SIG_ALGO);
            PublicKey pk = KeyFactory.getInstance("RSA").generatePublic(
                    new X509EncodedKeySpec(publicKeyBytes));
            s.initVerify(pk);
            s.update(emBytes);
            boolean valid = s.verify(sigBytes);
//            Decrypt AES key
            byte[] privateKeyBytes = Helper.getKeyFileContentFromNameAsByteArray(appSettings.getPrivateKeyFile());; //your private key here
            byte[] ekBytes = Base64.getDecoder().decode(ek);
            Cipher dipher = Cipher.getInstance(RSA_ALGO);
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(
                    new PKCS8EncodedKeySpec(privateKeyBytes));
            dipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] dKey = dipher.doFinal(ekBytes);
//            Decrypt message
            SecretKey secKey = new SecretKeySpec(dKey, "AES");
            Cipher cipher = Cipher.getInstance(AES_ALGO);

            byte[] iv = new byte[IV_LENGTH];
            byte[] encBytes = new byte[emBytes.length - IV_LENGTH];
            System.arraycopy(emBytes, 0, iv, 0, IV_LENGTH);
            System.arraycopy(emBytes, IV_LENGTH, encBytes, 0, emBytes.length - IV_LENGTH);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secKey, ivSpec);

            byte[] dec = cipher.doFinal(encBytes);
            String decryptedMessage = new String(dec, StandardCharsets.UTF_8);

            Map<String, String> res = new HashMap<>();
            res.put("decryptedMessage", decryptedMessage);

            return new ResponseEntity<Object>(responseUtility.response(0, res), HttpStatus.OK);
        }catch (Exception e) {
            logger.info("config begin with error");
            e.printStackTrace();
            logger.info(e.getMessage());
            Map<String, String> res = new HashMap<>();
            res.put("logMessage", e.getMessage());
            return new ResponseEntity<Object>(responseUtility.response(3, res), HttpStatus.OK);
        }
    }

}

