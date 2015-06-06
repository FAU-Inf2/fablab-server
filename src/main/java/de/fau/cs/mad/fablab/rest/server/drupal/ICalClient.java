package de.fau.cs.mad.fablab.rest.server.drupal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
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

    private List<ICal> events;

    private static final String BEGIN_CAL = "BEGIN:VCALENDAR";
    private static final String END_CAL = "END:VCALENDAR";
    private static final String BEGIN_EV = "BEGIN:VEVENT";
    private static final String END_EV = "END:VEVENT";

    /***
     * Singleton getInstance()
     * @return instance
     */
    public static ICalInterface getInstance() {
        if (instance == null) {
            instance = new ICalClient();
        }
        return instance;
    }

    public static void setConfiguration(ICalConfiguration c) {
        config = c;
    }

    /***
     * Checks for valid configuration and processes the iCal-Feed for the first time
     * If any environment variable is missing, it will shutdown the whole application with exit code 1
     *
     * @throws IOException if the endpoint + iCalUrl is not a valid url
     */
    private ICalClient() {
        if (config == null || !config.validate()) {
            System.err.println("ERROR while initializing ICalClient. Configuration vars missing.\n" +
                    "The configuration (endpoint and iCalUrl) has to be set \n " +
                    "using the class ICalConfiguration.\n");
            System.exit(1);
        }

        updateEvents();
    }

    @Override
    public ICal findById(Long id) {
        return events.get(id.intValue());
    }

    /***
     * Reads the iCal-Feed and updates the Event-List
     * Returns a list with all iCal-Events
     *
     * @return a List of {@link ICal}
     */
    @Override
    public List<ICal> findAll() {
        updateEvents();
        return events;
    }

    /***
     * Retrieves the iCal-Feed from the given Endpoint + iCalUrl and updates the Event-List
     *
     */
    private void updateEvents() {
        try {
            iCalUrl = new URL(config.getEndpoint() + config.getIcalUrl());
        } catch (MalformedURLException e) {
            System.err.println("ERROR - MalformedURLException while updating Events. \n" +
                    "The Reason is : " + e.getMessage() + "\n" +
                    "ICalUrl is : " + config.getEndpoint() + config.getIcalUrl());
        }

        BufferedReader reader;
        List<ICal> events = null;
        try {
            reader = new BufferedReader(new InputStreamReader(iCalUrl.openStream()));
            events = parseEvents(reader);
        } catch (IOException e) {
            System.err.println("ERROR - IOException while updating Events. \n" +
                    "The Reason is : "+e.getMessage()+"\n"+
                    "ICalUrl is : " + config.getEndpoint() + config.getIcalUrl());
        } catch (ParserException e) {
            System.err.println("ERROR - ParserException while updating Events. \n" +
                    "The Reason is : "+e.getMessage());
        }

        if (events != null) this.events = events;
    }

    /***
     * Parses events from the given reader into a list
     * @param reader    the reader
     * @return a List of {@link ICal}
     */
    private List<ICal> parseEvents(BufferedReader reader) throws IOException, ParserException {
        List<ICal> res = new LinkedList<>();

        CalendarBuilder builder = new CalendarBuilder();

        Calendar calEvent;
        String eventString;
        int nextIndex = 0;
        while ((eventString = parseEventString(reader)) != null) {
            StringReader sr = new StringReader(eventString);
            calEvent = builder.build(sr);

            ICal event = getICalFromCalendar(calEvent);
            event.setId(nextIndex);
            res.add(nextIndex, event);

            nextIndex++;
        }

        return res;
    }

    /***
     * Parses one event into a String (and corrects wrong Date-Formats)
     * @param reader    the reader
     * @return a String with all the event-data
     */
    private String parseEventString(BufferedReader reader) throws IOException {
        StringBuilder event = new StringBuilder();
        event.append(BEGIN_CAL);
        event.append("\r\n");

        String line;
        // go to first event
        while (!(line = reader.readLine()).equals(BEGIN_EV)) {
            if (line.equals(END_CAL)) return null;
        }

        event.append(BEGIN_EV);
        event.append("\r\n");
        while ((line = reader.readLine()) != null) {
            // check for wrong formatted ical and fix it
            if (line.matches("(.*):\\d{8}Z{1}(.*)")) {
                line = line.substring(0, line.lastIndexOf('Z'));
                line += "T000000Z";
            }

            event.append(line);
            event.append("\r\n");
            if (line.equals(END_EV)) break;
        }
        event.append(END_CAL);
        event.append("\r\n");

        return event.toString();
    }

    private ICal getICalFromCalendar(Calendar calendar) {
        Component component = calendar.getComponent(Component.VEVENT);

        ICal event = new ICal();
        event.setUid(component.getProperty(Property.UID).getValue());
        event.setSummery(component.getProperty(Property.SUMMARY).getValue());
        event.setDtstamp(component.getProperty(Property.DTSTAMP).getValue());
        event.setDtstart(component.getProperty(Property.DTSTART).getValue());
        event.setDtend(component.getProperty(Property.DTEND).getValue());

        Property p;

        p = component.getProperty(Property.RRULE);
        if (p != null) event.setRrule(p.getValue());
        p = component.getProperty(Property.EXDATE);
        if (p != null) event.setExdate(p.getValue().split(","));
        p = component.getProperty(Property.URL);
        if (p != null) event.setUrl(p.getValue());
        p = component.getProperty(Property.LOCATION);
        if (p != null) event.setLocation(p.getValue());
        p = component.getProperty(Property.DESCRIPTION);
        if (p != null) event.setDescription(p.getValue());

        return event;
    }
}
