package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class PushContent {

    private List<String> registration_ids;
    private Map<String, Object> data;

    public PushContent()
    {
        data = new HashMap<String, Object>();
    }

    public void addRegId(String regId) {
        if (registration_ids == null)
            registration_ids = new LinkedList<String>();

        registration_ids.add(regId);
    }

    public void addData(String title, Object message) {
        data.put(title, message);
    }

    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
