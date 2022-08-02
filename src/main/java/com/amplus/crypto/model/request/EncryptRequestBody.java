package com.amplus.crypto.model.request;

import javax.validation.constraints.NotNull;

public class EncryptRequestBody {
    @NotNull
    private String requestData;

    public EncryptRequestBody() {
    }

    public EncryptRequestBody(String requestData) {
        this.requestData = requestData;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }
}
