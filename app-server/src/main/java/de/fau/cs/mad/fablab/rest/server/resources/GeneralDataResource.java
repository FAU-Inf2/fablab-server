package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.DataApi;
import de.fau.cs.mad.fablab.rest.core.MailAddresses;
import de.fau.cs.mad.fablab.rest.server.configuration.GeneralDataConfiguration;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http404Exception;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http500Exception;

public class GeneralDataResource implements DataApi {

    private GeneralDataConfiguration config;

    public GeneralDataResource(GeneralDataConfiguration config) {
        this.config = config;
    }

    @Override
    public String getFabLabMailAddress() {
        String mail = config.getFabMail();
        if (mail == null){
            throw new Http500Exception("An error occurred while retrieving the FabMail-Adress");
        }
        if (mail.length() == 0) throw new Http404Exception("Result is empty");
        return mail;
    }

    @Override
    public String getFeedbackMailAddress() {
        String mail = config.getFabMail();
        if (mail == null){
            throw new Http500Exception("An error occurred while retrieving the FabMail-Adress");
        }
        if (mail.length() == 0) throw new Http404Exception("Result is empty");
        return mail;
    }

    @Override
    public MailAddresses getMailAddresses() {
        MailAddresses ret = new MailAddresses(config.getFeedbackMail(), config.getFabMail());
        if (ret == null || ret.getFeedbackMail() == null || ret.getFabLabMail() == null) {
            throw new Http500Exception("An error occurred while retrieving the Mail-Adresses");
        }
        return ret;
    }
}
