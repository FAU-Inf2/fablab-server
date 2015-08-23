package de.fau.cs.mad.fablab.rest.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class APNConfiguration {

    @NotEmpty
    @JsonProperty
    private String certificate;

    @NotEmpty
    @JsonProperty
    private String password;

    public boolean validate() {
        if (certificate == null || password.isEmpty()) return false;
        return true;
    }

    public String getCertificate() {
        return this.certificate;
    }

    public String getPassword() {
        return this.password;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
