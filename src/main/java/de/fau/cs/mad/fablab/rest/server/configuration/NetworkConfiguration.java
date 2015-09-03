package de.fau.cs.mad.fablab.rest.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class NetworkConfiguration {

        @NotEmpty
        @JsonProperty
        private String hostname;

        @NotNull
        @JsonProperty
        private int adminPort;

        public boolean validate()
        {
            if (hostname == null)
                return false;

            return true;
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public int getAdminPort() {
            return adminPort;
        }

        public void setAdminPort(int adminPort) {
            this.adminPort = adminPort;
        }
}
