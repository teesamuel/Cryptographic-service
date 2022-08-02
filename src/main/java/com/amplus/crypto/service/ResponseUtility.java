package com.amplus.crypto.service;

import com.amplus.crypto.Utils.components.ResponseCode;
import com.amplus.crypto.model.response.EncryptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ResponseUtility {

    private EncryptResponse encryptResponse;
    private StringBuilder stringBuilder;
    @Autowired
    ResponseCode responseCode;

    public EncryptResponse response(int code, Map<String,String> details) {
        stringBuilder = new StringBuilder();
        encryptResponse = new EncryptResponse();
        encryptResponse.setDetails(details);
        encryptResponse.setResponseCode(String.format("%03d", code));
        encryptResponse.setResponseMessage(
                stringBuilder.append(responseCode.getResponse_message()[Integer.valueOf(code)]).toString());
        return encryptResponse;
    }

}
