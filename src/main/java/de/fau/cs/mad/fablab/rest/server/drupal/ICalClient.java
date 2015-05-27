package de.fau.cs.mad.fablab.rest.server.drupal;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.server.configuration.ICalConfiguration;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

public class ICalClient implements ICalInterface {

    private static ICalInterface instance;
    private static ICalConfiguration config = null;

    private URL iCalUrl;
    private Calendar calendar;

    /***
     * Singleton getInstance()
     * @return instance
     */
    public static ICalInterface getInstance() {
        if (instance == null) {
            try {
                instance = new ICalClient();
            } catch (IOException e) {
                System.err.println("ERROR - IOException while initializing ICalClient. \n" +
                        "The Reason is : "+e.getMessage()+"\n"+
                        "ICalUrl is : " + config.getEndpoint() + config.getIcalUrl());
                System.exit(1);
            } catch (ParserException e) {
                System.err.println("ERROR - ParserException while initializing ICalClient. \n" +
                        "The Reason is : "+e.getMessage());
                System.exit(1);
            }
        }

        return instance;
    }

    public static void setConfiguration(ICalConfiguration config) {
        config = config;
    }

    /***
     * asfjasfij
     * If any environment variable is missing, it will shutdown the whole application with exit code 1
     *
     * @throws IOException if the endpoint + iCalUrl is not a valid url
     */
    private ICalClient() throws IOException, ParserException {
        if (config == null || !config.validate()) {
            System.err.println("ERROR while initializing ICalClient. Configuration vars missing.\n" +
                    "The configuration (endpoint and iCalUrl) has to be set \n " +
                    "using the class ICalConfiguration.\n");
            //System.exit(1); TODO
        }

        //iCalUrl = new URL(config.getEndpoint() + config.getIcalUrl()); TODO
        iCalUrl = new URL("https://fablab.fau.de/termine/ical");

        Reader reader = new InputStreamReader(iCalUrl.openStream());

        CalendarBuilder builder = new CalendarBuilder();

        calendar = builder.build(reader);
    }

    @Override
    public ICal findById(Long id) {
        return new ICal();
    }

    @Override
    public List<ICal> findAll() {
        List<ICal> calendarEvents = new LinkedList<ICal>();

        //Iterator ic = calendar.getComponents().iterator();
        Iterator ic = calendar.getComponents(Component.VEVENT).iterator();
        while (ic.hasNext()) {
            Component component = (Component) ic.next();
            System.out.println("Component [" + component.getName() + "]");

            ICal event = new ICal();
            event.setUid(component.getProperty(Property.UID).getValue());
            event.setSummery(component.getProperty(Property.SUMMARY).getValue());
            event.setDtstamp(component.getProperty(Property.DTSTAMP).getValue());
            event.setDtstart(component.getProperty(Property.DTSTART).getValue());
            event.setDtend(component.getProperty(Property.DTEND).getValue());
            event.setRrule(component.getProperty(Property.RRULE).getValue());
            event.setExdate(component.getProperty(Property.EXDATE).getValue().split(","));
            event.setUrl(component.getProperty(Property.URL).getValue());
            event.setLocation(component.getProperty(Property.LOCATION).getValue());
            event.setDescription(component.getProperty(Property.DESCRIPTION).getValue());

            calendarEvents.add(event);
        }
        return calendarEvents;
    }
}
