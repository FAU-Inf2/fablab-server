package de.fau.cs.mad.fablab.rest.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class CheckOutApiKeyConfiguration {

    @NotEmpty
    @JsonProperty
    private String key;

    public String getCheckoutApiKey() {
        return key;
    }

    public void setCheckoutApiKey(String checkoutApiKey) {
        this.key = checkoutApiKey;
    }
}
