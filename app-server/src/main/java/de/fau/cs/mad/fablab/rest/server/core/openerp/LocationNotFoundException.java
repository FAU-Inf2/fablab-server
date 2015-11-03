package de.fau.cs.mad.fablab.rest.server.core.openerp;


public class LocationNotFoundException extends Exception{

    public LocationNotFoundException(final String aMessage){
        super(aMessage);
    }

    public LocationNotFoundException(final String aMessage, final Throwable aCause){
        super(aMessage,aCause);
    }
}
