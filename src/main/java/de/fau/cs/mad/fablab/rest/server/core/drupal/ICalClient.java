package de.fau.cs.mad.fablab.rest.server.core.drupal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.server.configuration.ICalConfiguration;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.*;

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
        if (events == null) updateEvents();
        for (ICal event : events) {
            if (event.getId() == id) return event;
        }
        return null;
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
     * Reads the iCal-Feed and updates the Event-List
     * Returns a list with 'limit' iCal-Events starting at 'offset'
     *
     * @param offset the offset
     * @param limit the maximum number of ICals to return
     * @return a List of {@link ICal}
     */
    public List<ICal> find(int offset, int limit) {
        updateEvents();

        List<ICal> events = new LinkedList<>();

        ListIterator<ICal> iterator = null;
        try {
            iterator = this.events.listIterator(offset);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

        int numElements = 0;
        while (iterator.hasNext()) {
            if (numElements == limit) break;
            events.add(iterator.next());
            numElements++;
        }
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
        List<Component> components = null;
        try {
            reader = new BufferedReader(new InputStreamReader(iCalUrl.openStream()));
            components = parseEventsToComponent(reader);
        } catch (IOException e) {
            System.err.println("ERROR - IOException while updating Events. \n" +
                    "The Reason is : "+e.getMessage()+"\n"+
                    "ICalUrl is : " + config.getEndpoint() + config.getIcalUrl());
        } catch (ParserException e) {
            System.err.println("ERROR - ParserException while updating Events. \n" +
                    "The Reason is : "+e.getMessage());
        }

        List<ICal> events = parseEvents(components);

        Collections.sort(events, new ICalComparator());

        if (events != null) this.events = events;
    }

    /***
     * Converts events from the List of {@link Component} into a List of {@link ICal}
     * @param components    the list of components
     * @return a List of {@link ICal}-Events
     */
    private List<ICal> parseEvents(List<Component> components) {
        List<ICal> res = new LinkedList<>();

        Period period = getPeriod(52);

        int nextIndex = 0;
        for (Component component : components) {
            PeriodList pl = component.calculateRecurrenceSet(period);

            for (Object po : pl) {
                Period p = (Period) po;
                String start = p.getStart().toString();
                String end = p.getEnd().toString();

                ICal event = getICalFromComponent(component, start, end);
                event.setId(nextIndex++);
                res.add(event);
            }
        }

        return res;
    }

    /***
     * Parses events from the given reader into a list of {@link Component}
     * @param reader    the reader
     * @return a List of {@link Component}
     */
    private List<Component> parseEventsToComponent(BufferedReader reader) throws IOException, ParserException {
        List<Component> res = new LinkedList<>();
        Filter filter = getFilter();

        CalendarBuilder builder = new CalendarBuilder();

        Calendar calEvent;
        String eventString;
        while ((eventString = parseEventString(reader)) != null) {
            StringReader sr = new StringReader(eventString);
            calEvent = builder.build(sr);

            Component component = filterComponent(calEvent, filter);
            if (component != null) {
                res.add(component);
            }
        }

        return res;
    }

    /***
     * Creates and returns a new Filter (to get events during the next 52 weeks)
     * @return the created {@link Filter}
     */
    private Filter getFilter() {
        Period period = getPeriod(52); // get events during the next 52 weeks
        PeriodRule[] pr = {new PeriodRule(period)};
        return new Filter(pr, Filter.MATCH_ALL);
    }

    /***
     * Creates and returns a new Period starting today, ending in 'weeks' weeks
     * @param weeks the number of weeks
     * @return the created {@link Period}
     */
    private Period getPeriod(int weeks) {
        java.util.Calendar today = java.util.Calendar.getInstance();
        today.set(java.util.Calendar.HOUR_OF_DAY, 0);
        today.clear(java.util.Calendar.MINUTE);
        today.clear(java.util.Calendar.SECOND);

        return new Period(new DateTime(today.getTime()), new Dur(weeks));
    }

    /***
     * Applys the given Filter to the given Calendar-Event
     * @param calEvent the {@link Calendar}-Event to be filtered
     * @param filter the {@link Filter}
     * @return the filtered {@link Component}
     */
    private Component filterComponent(Calendar calEvent, Filter filter) {
        List<Component> upcomingEvents = (List) filter.filter(calEvent.getComponents(Component.VEVENT));
        for (Component c : upcomingEvents) {
            if (c != null) return c;
        }
        return null;
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

    /***
     * Creates a new ICal-Object from the given Component and the given 'start'- and 'end'-dates
     * @param component the {@link Component}
     * @param start the start-string
     * @param end the end-string
     * @return a new ICal-Object
     */
    private ICal getICalFromComponent(Component component, String start, String end) {
        if (!component.getName().equals(Component.VEVENT)) return null;

        ICal event = new ICal();
        event.setUid(component.getProperty(Property.UID).getValue());
        event.setSummery(component.getProperty(Property.SUMMARY).getValue());
        event.setStart(start);
        event.setEnd(end);

        event.setAllday(isAlldayEvent(start, end));

        Property p;
        p = component.getProperty(Property.URL);
        if (p != null) event.setUrl(p.getValue());
        p = component.getProperty(Property.LOCATION);
        if (p != null) event.setLocation(p.getValue());
        p = component.getProperty(Property.DESCRIPTION);
        if (p != null) event.setDescription(p.getValue());

        return event;
    }

    /***
     * Checks if the given event is an all-day event
     * @param start the start-string
     * @param end the end-string
     * @return true or false
     */
    private boolean isAlldayEvent(String start, String end) {
        String s = start.substring(9, 15);
        String e = end.substring(9, 15);
        String target = "000000";

        if (s.equals(target) && e.equals(target)) return true;
        return false;
    }
}
