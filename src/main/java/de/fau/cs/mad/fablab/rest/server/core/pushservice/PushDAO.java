package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.core.PlatformType;
import de.fau.cs.mad.fablab.rest.core.PushToken;
import de.fau.cs.mad.fablab.rest.core.TriggerPushType;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class PushDAO extends AbstractDAO<PushToken> {

    public PushDAO(SessionFactory factory) {
        super(factory);
    }

    public PushToken subscribeDoorOpensNextTime(PushToken token){
        token.setTriggerPushType(TriggerPushType.DOOR_OPENS_NEXT_TIME);
        return persist(token);
    }


    public boolean unsubscribeDoorOpensNextTime(PushToken token) {
        //TODO never TESTED!
        if (get(token) == null)
            return false;
        currentSession().delete(get(token));
        return true;
    }

    public List<String> findAllTokensWith(PlatformType platformType, TriggerPushType trigger){
        /*
            Should be changed to WHERE... dont wanna dance on to much parties -> TODO: FixMe
               (if... works for now.)
         */
         List<PushToken> all = super.currentSession().createQuery("FROM push_token").list();

        List<String> tokens = new ArrayList<>();
        for(PushToken pushToken : all)
            if(pushToken.getPlatformType().equals(platformType) && pushToken.getTriggerPushType().equals(trigger))
                tokens.add(pushToken.getToken());
        return tokens;
    }

}
