package com.amplus.crypto.Utils.components;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import javax.persistence.Column;

@Component
@ConfigurationProperties
@PropertySource("classpath:responseCode.properties")
public class ResponseCode implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 821828713866586526L;
    @Column(name = "response_message", nullable = false)
    private String[] response_message;

    /**
     *
     */
    public ResponseCode() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param response_message
     */
    public ResponseCode(String[] response_message) {
        super();
        this.response_message = response_message;
    }

    /**
     * @return the response_message
     */
    public String[] getResponse_message() {
        return response_message;
    }

    /**
     * @param response_message the response_message to set
     */
    public void setResponse_message(String[] response_message) {
        this.response_message = response_message;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
