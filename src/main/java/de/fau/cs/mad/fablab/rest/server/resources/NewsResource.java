package de.fau.cs.mad.fablab.rest.server.resources;


import de.fau.cs.mad.fablab.rest.api.NewsApi;
import de.fau.cs.mad.fablab.rest.entities.News;

import java.util.ArrayList;
import java.util.List;

/***
 * Resource class for our /news uri
 */
public class NewsResource implements NewsApi
{
    private static List<News> dummyNews;

    public NewsResource() {
        dummyNews = new ArrayList<News>();

        dummyNews.add(new News("Lorem Ipsum", "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.   \n" +
                "\n" +
                "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.   \n" +
                "\n" +
                "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.   \n" +
                "\n" +
                "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.   \n" +
                "\n" +
                "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.   \n" +
                "\n" +
                "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, At accusam aliquyam diam diam dolore dolores duo eirmod eos erat, et nonumy sed tempor et et invidunt justo labore Stet clita ea et gubergren, kasd magna no rebum. sanctus sea sed takimata ut vero voluptua. est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur", "Mr. DeLorem von Ipsum", "http://www.loremipsum.de/", "http://www.loremipsum.de/images/lorem_ipsum_sit_dolor.jpg"));
        dummyNews.add(new News("Jugendkunstschule", "Letztes Wochenende besuchten uns im Rahmen der Jugendkunstschule wieder bastelfreudige, kreative Jugendliche. Dabei ist auch dieser schöne Kristall entstanden, vielen Dank für das Bild!", "patkan", "https://fablab.fau.de/2015/04/29/jugendkunstschule", "https://fablab.fau.de/sites/fablab.fau.de/files/images/dsc7890-1ca-halbbreit.jpg"));
        dummyNews.add(new News("Interview beim StabiLab", "Während des StabiLabs entstand auch ein Interview, das wir gerne mit euch teilen möchten. Den vollen Artikel dazu gibt es im FAU-Blog.", "patkan", "https://fablab.fau.de/2015/05/01/interview-beim-stabilab", "http://cdn.video.uni-erlangen.de//symlinks/e2d4648c-e73c-b52b-7ad5-7ed461872aae.m4v"));
        dummyNews.add(new News("Neue OpenLab-Termine", "Das Semester läuft wieder und damit sind auch unsere OpenLab-Termine unter der Woche wieder da. Wir haben für dich an fast jedem Tag der Woche einen OpenLab-Termin an dem du vorbei kommen und an deinen Projekten arbeiten kannst. Während diesen Terminen erhältst du auch Einweisungen zu den Maschinen. Für mehr Informationen sieh dir unsere Terminseite an.\n" +
                "\n" +
                "Für alle, die vormittags und nachmittags keine Zeit haben, gibt es auch Abend-Termine. Der nächste findet diesen Freitag, den 8.5., um 18 Uhr statt. Außerdem bieten wir im Rahmen des Aktionswochenende des Verbund offener Werkstätten ein OpenLab an diesem Samstag, den 9.5., ab 14 Uhr an. Auch in vielen anderen deutschen Städten finden an diesem Wochenende verschiedene Aktionen statt.\n" +
                "\n" +
                "Wenn dir das noch zu wenig Öffnungszeiten sind und natürlich auch wenn du einfach nur interessiert bist, bist du herzlich eingeladen bei uns mitzuhelfen! Sprich einen Betreuer beim OpenLab an oder schreib eine Mail an kontakt@fablab.fau.de. Wir freuen uns auf dich!", "patkan", "https://fablab.fau.de/2015/05/04/neue-openlab-termine", "https://fablab.fau.de/sites/fablab.fau.de/files/images/2015_aktionstag_flyer-a6_01.png"));
    }

    @Override
    public List<News> getNews() {
        return dummyNews;
    }

}
