package com.amplus.crypto.model.request;

import javax.validation.constraints.NotNull;

public class DecryptRequestBody {

    @NotNull
    private String em;
    @NotNull
    private String sig;
    @NotNull
    private String ek;

    public DecryptRequestBody() {
    }

    public DecryptRequestBody(String em, String sig, String ek) {
        this.em = em;
        this.sig = sig;
        this.ek = ek;
    }

    public String getEm() {
        return em;
    }

    public void setEm(String em) {
        this.em = em;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getEk() {
        return ek;
    }

    public void setEk(String ek) {
        this.ek = ek;
    }
}
