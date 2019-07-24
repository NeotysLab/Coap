package com.neotys.coap.common;

import com.google.common.base.Optional;

public class CoapContext {
    private String uRl;
    private Optional<String> accept;
    private String method;
    private Optional<String> formatOfpayLoad;
    private Optional<String> payload;
    private boolean asynchronous;
    private Optional<String> trustedkeyLocation;
    private Optional<String> trustedkeyPassword;
    private Optional<String> keyStoreLocation;
    private Optional<String> keyStorePassword;
    private Optional<String> keyStoreAlias;
    private Optional<String> keyStoreAliasPassword;

    public CoapContext(String URl, Optional<String> accept, String method, Optional<String> formatOfpayLoad, Optional<String> payload, boolean asynchronous, Optional<String> trustedkeyLocation, Optional<String> trustedkeyPassword, Optional<String> keyStoreLocation, Optional<String> keyStorePassword, Optional<String> keyStoreAlias, Optional<String> keyStoreAliasPassword) {
        this.uRl = URl;
        this.accept = accept;
        this.method = method;
        this.formatOfpayLoad = formatOfpayLoad;
        this.payload = payload;
        this.asynchronous = asynchronous;
        this.trustedkeyLocation = trustedkeyLocation;
        this.trustedkeyPassword = trustedkeyPassword;
        this.keyStoreLocation = keyStoreLocation;
        this.keyStorePassword = keyStorePassword;
        this.keyStoreAlias = keyStoreAlias;
        this.keyStoreAliasPassword = keyStoreAliasPassword;
    }

    public boolean isAsynchronous() {
        return asynchronous;
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    public String getuRl() {
        return uRl;
    }

    public void setuRl(String uRl) {
        this.uRl = uRl;
    }

    public Optional<String> getAccept() {
        return accept;
    }

    public void setAccept(Optional<String> accept) {
        this.accept = accept;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Optional<String> getFormatOfpayLoad() {
        return formatOfpayLoad;
    }

    public void setFormatOfpayLoad(Optional<String> formatOfpayLoad) {
        this.formatOfpayLoad = formatOfpayLoad;
    }

    public Optional<String> getPayload() {
        return payload;
    }

    public void setPayload(Optional<String> payload) {
        this.payload = payload;
    }



    public Optional<String> getTrustedkeyLocation() {
        return trustedkeyLocation;
    }

    public void setTrustedkeyLocation(Optional<String> trustedkeyLocation) {
        this.trustedkeyLocation = trustedkeyLocation;
    }

    public Optional<String> getTrustedkeyPassword() {
        return trustedkeyPassword;
    }

    public void setTrustedkeyPassword(Optional<String> trustedkeyPassword) {
        this.trustedkeyPassword = trustedkeyPassword;
    }

    public Optional<String> getKeyStoreLocation() {
        return keyStoreLocation;
    }

    public void setKeyStoreLocation(Optional<String> keyStoreLocation) {
        this.keyStoreLocation = keyStoreLocation;
    }

    public Optional<String> getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(Optional<String> keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public Optional<String> getKeyStoreAlias() {
        return keyStoreAlias;
    }

    public void setKeyStoreAlias(Optional<String> keyStoreAlias) {
        this.keyStoreAlias = keyStoreAlias;
    }

    public Optional<String> getKeyStoreAliasPassword() {
        return keyStoreAliasPassword;
    }

    public void setKeyStoreAliasPassword(Optional<String> keyStoreAliasPassword) {
        this.keyStoreAliasPassword = keyStoreAliasPassword;
    }
}
