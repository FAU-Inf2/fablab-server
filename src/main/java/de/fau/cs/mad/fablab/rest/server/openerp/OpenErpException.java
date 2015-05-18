package de.fau.cs.mad.fablab.rest.server.openerp;

public class OpenErpException extends Exception {

    private String message;
    private String jsonErrorMessage;

    public OpenErpException(String message, String jsonErrorMessage) {
        this.message = message;
        this.jsonErrorMessage = jsonErrorMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getJsonErrorMessage() {
        return jsonErrorMessage;
    }
}
