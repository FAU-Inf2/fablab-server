package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.core.PlatformType;
import de.fau.cs.mad.fablab.rest.core.PushToken;
import de.fau.cs.mad.fablab.rest.core.TriggerPushType;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class PushDAO extends AbstractDAO<PushToken> {

    public PushDAO(SessionFactory factory) {
        super(factory);
    }

    public Boolean subscribeDoorOpensNextTime(PushToken token){
        Query query = super.currentSession().createQuery("FROM PushToken WHERE token = :token AND triggerPushType = :trigger");
        query.setParameter("token", token.getToken());
        query.setParameter("trigger", token.getTriggerPushType());
        if(query.list().size() == 0) {
            token.setTriggerPushType(TriggerPushType.DOOR_OPENS_NEXT_TIME);
            persist(token);
        }
        return true;
    }


    public Boolean unsubscribeDoorOpensNextTime(PushToken token) {
        Query query = super.currentSession().createQuery("DELETE PushToken WHERE token = :token AND triggerPushType = :trigger");
        query.setParameter("token", token.getToken());
        query.setParameter("trigger", token.getTriggerPushType());
        query.executeUpdate();
        return doorOpensNextTimeIsSetForToken(token);
    }

    public Boolean doorOpensNextTimeIsSetForToken(PushToken token){
        Query query = super.currentSession().createQuery("FROM PushToken WHERE token = :token AND triggerPushType = :trigger");
        query.setParameter("token", token.getToken());
        query.setParameter("trigger", TriggerPushType.DOOR_OPENS_NEXT_TIME);
        if(query.list().size() == 0)
            return false;
        return true;
    }

    public List<String> findAllTokensWith(PlatformType platformType, TriggerPushType trigger){

        Query query = super.currentSession().createQuery("FROM PushToken WHERE platformType = :platformType AND triggerPushType = :trigger");
        query.setParameter("platformType", platformType);
        query.setParameter("trigger", trigger);
        List<PushToken> list = query.list();

        List<String> tokens = new ArrayList<>();
        for(PushToken pushToken : list)
            tokens.add(pushToken.getToken());
        return tokens;
    }

    public void removeTokensForTrigger(TriggerPushType trigger){
        Query query = super.currentSession().createQuery("DELETE PushToken WHERE triggerPushType = :trigger");
        query.setParameter("trigger", trigger);
        query.executeUpdate();
    }
}
