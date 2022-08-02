package com.amplus.crypto.controller;

import com.amplus.crypto.model.request.DecryptRequestBody;
import com.amplus.crypto.model.request.EncryptRequestBody;
import com.amplus.crypto.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/crypto")
public class CryptoController {

    @Autowired
    CryptoService cryptoService;

    @PostMapping(value = "/encrypt", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> paymentInitiate(@Valid @RequestBody EncryptRequestBody encryptRequestBody){
        return  cryptoService.encrypt(encryptRequestBody.getRequestData());
    }

    @PostMapping(value = "/decrypt", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> paymentConfirm(@Validated @RequestBody DecryptRequestBody decryptRequestBody){
        return  cryptoService.decrypt(decryptRequestBody.getEm(), decryptRequestBody.getSig(), decryptRequestBody.getSig());
    }

}
