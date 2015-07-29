package de.fau.cs.mad.fablab.rest.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class GeneralDataConfiguration {

    @NotEmpty
    @JsonProperty
    private String fabUrl;

    @NotEmpty
    @JsonProperty
    private String fabMail;

    public boolean validate() {
        if (fabUrl == null || fabUrl.isEmpty() || fabMail == null || fabMail.isEmpty()) return false;
        return true;
    }

    public String getFabUrl() {
        return fabUrl;
    }

    public void setFabUrl(String fabUrl) {
        this.fabUrl = fabUrl;
    }

    public String getFabMail() {
        return fabMail;
    }

    public void setFabMail(String fabMail) {
        this.fabMail = fabMail;
    }
}
