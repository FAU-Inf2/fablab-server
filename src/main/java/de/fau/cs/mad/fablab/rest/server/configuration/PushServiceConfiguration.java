package de.fau.cs.mad.fablab.rest.server.configuration;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class PushServiceConfiguration {

    @NotEmpty
    @JsonProperty
    private String pushAPIRegistrationId;

    @NotEmpty
    @JsonProperty
    private String googlePushServiceURL;

    public void setGooglePushServiceURL(String aGooglePushServiceURL) {
        googlePushServiceURL = aGooglePushServiceURL;
    }

    public String getGooglePushServiceURL() {
        return googlePushServiceURL;
    }

    public void setPushAPIRegistrationId(String aPushAPIRegistrationId) {
        pushAPIRegistrationId = aPushAPIRegistrationId;
    }

    public String getPushAPIRegistrationId() {
        return pushAPIRegistrationId;
    }

}
