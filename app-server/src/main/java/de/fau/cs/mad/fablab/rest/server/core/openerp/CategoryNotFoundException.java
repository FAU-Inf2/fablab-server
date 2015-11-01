package de.fau.cs.mad.fablab.rest.server.core.openerp;


public class CategoryNotFoundException extends Exception{

    public CategoryNotFoundException(final String aMessage){
        super(aMessage);
    }

    public CategoryNotFoundException(final String aMessage, final Throwable aCause){
        super(aMessage,aCause);
    }
}
