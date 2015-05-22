package de.fau.cs.mad.fablab.rest.server;

import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.core.News;
import io.dropwizard.hibernate.HibernateBundle;
import org.hibernate.Session;

import java.util.Date;

public class dummyData {
    private static Session session = null;

    public static void createDummyData(HibernateBundle<ServerConfiguration> hibernateBundle){
        session = hibernateBundle.getSessionFactory().openSession();
        if(session.get(News.class, (long)0) == null)
            createNews();
        if(session.get(ICal.class, (long)0) == null)
            createICals();
    }



    private static void createNews(){
        System.out.println("Creating dummy News");
        News n = new News();
        n.setTitle("Kassenterminal nimmt jetzt auch Bargeld an");
        n.setDescription("<p><a href=\"/sites/fablab.fau.de/files/images/terminal.jpg\"><img alt=\"\" class=\"lightbox\" src=\"/sites/fablab.fau.de/files/images/terminal.jpg\" style=\"width: 200px; height: 309px; float: left;\" /></a>Wie die meisten von euch vermutlich wissen, zahlt man bei uns an einem automatischen Kassenterminal. Kürzlich haben wir diese um eine Bargeldannahme erweitert.</p>\n" +
                "<p>Das heißt, du kannst jetzt mit Scheinen oder Münzen zahlen und dein Wechselgeld wird dir automatisch ausgezahlt. In Zukunft wird dieses Kassenterminal auch um eine Smartphone-App und die Bezahlung per FAUCard erweitert. Dazu berichten wir mehr, wenn es soweit ist.</p>\n" +
                "<p>Das Kassenterminal mit seinen Bestandteilen ist eine Eigenentwicklung, welche sehr viel Zeit hinter den Kulissen benötigt hat und benötigen wird. Es steht unter offenen Lizenzen und wird beziehungsweise wurde bereits <a href=\"https://github.com/fau-fablab/kassenautomat.CAD\">in</a> <a href=\"https://github.com/fau-fablab/kassenautomat.mdb-interface\">Teilen</a> veröffentlicht. Falls du zum Beispiel Mitglied eines anderen FabLabs bist und Interesse daran hast, soetwas selbst zu bauen, dann melde dich.</p>\n");
        n.setLink("https://fablab.fau.de/2015/05/12/kassenterminal-nimmt-jetzt-auch-bargeld");
        n.setCreator("patkan");
        n.setPubDate(new Date());
        n.setLinkToPreviewImage("https://fablab.fau.de/sites/fablab.fau.de/files/images/terminal.jpg");
        session.save(n);

        News n2 = new News();
        n2.setTitle("Neue OpenLab-Termine");
        n2.setDescription(" <p><a class=\"lightbox-processed\" rel=\"lightbox[][]\" href=\"/sites/fablab.fau.de/files/images/2015_aktionstag_flyer-a6_01.png\"><img title=\"\" alt=\"\" class=\"lightbox\" src=\"/sites/fablab.fau.de/files/images/2015_aktionstag_flyer-a6_01.png\" style=\"width: 40%; max-width:500px; float: left;\"></a></p>\n" +
                "<p>Das Semester läuft wieder und damit sind auch unsere OpenLab-Termine unter der Woche wieder da. Wir haben für dich an fast jedem Tag der Woche einen OpenLab-Termin an dem du vorbei kommen und an deinen Projekten arbeiten kannst. Während diesen Terminen erhältst du auch Einweisungen zu den Maschinen. Für mehr Informationen sieh dir unsere <a href=\"/termine\">Terminseite</a> an.</p>\n" +
                "<p>Für alle, die vormittags und nachmittags keine Zeit haben, gibt es auch Abend-Termine. Der nächste findet diesen Freitag, den 8.5., um 18 Uhr statt. Außerdem bieten wir im Rahmen des <a href=\"http://www.offene-werkstaetten.org/seite/aktionstage-2015\">Aktionswochenende</a> des <a href=\"http://www.offene-werkstaetten.org/\">Verbund offener Werkstätten</a> ein OpenLab an diesem Samstag, den 9.5., ab 14 Uhr an. Auch in vielen anderen deutschen Städten finden an diesem Wochenende verschiedene Aktionen statt.</p>\n" +
                "<p>Wenn dir das noch zu wenig Öffnungszeiten sind und natürlich auch wenn du einfach nur interessiert bist, bist du herzlich eingeladen bei uns mitzuhelfen! Sprich einen Betreuer beim OpenLab an oder schreib eine Mail an <a href=\"mailto:kontakt@fablab.fau.de\">kontakt@fablab.fau.de</a>. Wir freuen uns auf dich!</p>\n" +
                "\n");
        n2.setLink("https://fablab.fau.de/2015/05/04/neue-openlab-termine");
        n2.setCreator("patkan");
        n2.setPubDate(new Date());
        n2.setLinkToPreviewImage("https://fablab.fau.de/sites/fablab.fau.de/files/images/2015_aktionstag_flyer-a6_01.png");
        session.save(n2);

        News n3 = new News();
        n3.setTitle("Interview beim StabiLab");
        n3.setDescription("Während des <a href=\"/2015/02/07/das-fablab-besucht-die-stadtbibliothek\">StabiLabs</a> entstand auch ein Interview, das wir gerne mit euch teilen möchten. Den vollen Artikel dazu gibt es im <a href=\"https://www.fau.de/2015/03/news/studium/die-werkstatt-des-fau-fablabs-steht-jedem-offen/\">FAU-Blog</a>.");
        n3.setLink("https://fablab.fau.de/2015/05/04/neue-openlab-termine");
        n3.setCreator("patkan");
        n3.setPubDate(new Date());
        session.save(n3);

        News n4 = new News();
        n4.setTitle("Jugendkunstschule");
        n4.setDescription("<a href=\"/sites/fablab.fau.de/files/images/dsc7890-1.jpg\"><img alt=\"\" class=\"lightbox\" src=\"/sites/fablab.fau.de/files/images/dsc7890-1ca-halbbreit.jpg\" style=\"width: 300px; float: right; height: 300px;\" /></a>Letztes Wochenende besuchten uns im Rahmen der Jugendkunstschule wieder bastelfreudige, kreative Jugendliche. Dabei ist auch dieser schöne Kristall entstanden, vielen Dank für das Bild!");
        n4.setLink("https://fablab.fau.de/2015/04/29/jugendkunstschule");
        n4.setCreator("patkan");
        n4.setPubDate(new Date());
        n4.setLinkToPreviewImage("https://fablab.fau.de/sites/fablab.fau.de/files/images/dsc7890-1ca-halbbreit.jpg");
        session.save(n4);
    }

    private static void createICals(){
        System.out.println("Creating dummy iCals");

        String[] exdate1 = {"20140528T220000Z" ," 20140618T220000Z"};
        ICal c1 = new ICal("calendar.493.field_datetime.2.0",
                "StudentLab",
                "20150522T005944Z",
                "20140522T080000Z",
                "20140522T100000Z",
                "FREQ=WEEKLY;INTERVAL=1;BYDAY=TH;UNTIL=20140710T215959Z",
                exdate1,
                "https://fablab.fau.de/termine/2014-05-08-studentlab",
                "FabLab",
                "Betreuer: Christopher S\\, Christopher R");
        session.save(c1);

        String[] exdate2 = {"20140528T220000Z" ," 20140618T220000Z"};
        ICal c2 = new ICal("calendar.494.field_datetime.2.1",
                "StudentLab",
                "20150522T005944Z",
                "20140522T100000Z",
                "20140522T120000Z",
                "FREQ=WEEKLY;INTERVAL=1;BYDAY=TH;UNTIL=20140710T215959Z;WKST=MO",
                exdate2,
                "https://fablab.fau.de/termine/2014-05-08-studentlab-0",
                "FabLab",
                "Betreuer: Lucas\\, Max B");
        session.save(c2);

        String[] exdate3 = new String[0];
        ICal c3 = new ICal("calendar.472.field_datetime.0.2",
                "OpenLab",
                "20150522T005944Z",
                "20140523T160000Z",
                "20140523T200000Z",
                "",
                exdate3,
                "https://fablab.fau.de/termine/2014-05-23-openlab",
                "FabLab, siehe Seite Kontakt und Ort",
                "Das FabLab ist geöffnet, du kannst einfach so vorbeikommen. Wenn es nicht nur um Kleinigkeiten geht, sollte man spätestens zwei Stunden vor Ende vorbeikommen.");
        session.save(c3);

        String[] exdate4 = {"20140607T220000Z"};
        ICal c4 = new ICal("calendar.495.field_datetime.2.3",
                "StudentLab + Fräsenberatung",
                "20150522T005944Z",
                "20140526T120000Z",
                "20140526T140000Z",
                "WEEKLY;INTERVAL=1;BYDAY=MO;UNTIL=20140707T215959Z;WKST=MO",
                exdate4,
                "https://fablab.fau.de/termine/2014-05-12-studentlab",
                "FabLab",
                "Betreuer: Lucas\\, Michael J\\, Sonja");
        session.save(c4);
    }
}
