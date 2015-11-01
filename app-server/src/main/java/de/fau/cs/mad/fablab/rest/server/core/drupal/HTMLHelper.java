package de.fau.cs.mad.fablab.rest.server.core.drupal;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class HTMLHelper {

    private static final String ZUM_VIDEO = "Zum Video";

    /***
     * Extracts the first image of a given {@link Document} and returns it
     *
     * @param doc the input {@link Document}
     * @return the link to the image, if no image is found return link to FabLab-Logo
     */
    protected static String extractImageLink(Document doc) {
        Element image = doc.select("img").first();

        // no image found, return null
        if (image == null) return null;

        return image.attr("abs:src");
    }

    /***
     * Removes the first image, fixes relative links and replaces <li> and </li>-tags
     *
     * @param doc the input document
     * @return the parsed body
     */
    protected static String parseBody(Document doc) {
        return parseBody(doc, true);
    }

    protected static String parseBody(Document doc, boolean removeFirstImage) {
        if (removeFirstImage) doc = removeFirstImg(doc);
        doc = fixRelativeLinks(doc);
        doc = fixListTags(doc);
        doc = fixVideoLinks(doc);
        doc = removeStyleAttributes(doc);
        return doc.body().toString();
    }

    /***
     * Removes (unwanted) style-attributes
     *
     * @param doc the input Document
     * @return the 'improved' Document
     */
    protected static Document removeStyleAttributes(Document doc) {
        for (Element div : doc.select("div")) {
            div.removeAttr("style");
        }
        for (Element p : doc.select("p")) {
            p.removeAttr("style");
        }
        for (Element span : doc.select("span")) {
            span.removeAttr("style");
        }
        return doc;
    }

    /***
     * Removes the first image
     *
     * @param doc the input Document
     * @return the parsed body
     */
    protected static Document removeFirstImg(Document doc) {
        Element image = doc.select("img").first();

        if (image != null) {
            // remove first image and link
            image.remove();
            //doc.select("a").first().remove();
            String link = image.attr("abs:src");

            for (Element a : doc.select("a")) {
                if (link.equals(a.attr("abs:href"))) {
                    a.remove();
                    break;
                }
            }
        }

        return doc;
    }

    /***
     * Fixes relative-links and relative-links to images and adds "- " at the beginning and "<br>" at the end
     * of <li></li>-tags
     *
     * @param doc the input text
     * @return the parsed text
     */
    protected static Document fixRelativeLinks(Document doc) {
        for (Element a : doc.select("a")) {
            String link = a.attr("abs:href");
            a.attr("href", link);
        }

        for (Element img : doc.select("img")) {
            String link = img.attr("abs:src");
            img.attr("src", link);
        }
        return doc;
    }

    /***
     * Adds "- " at the beginning and "<br>" at the end of <li></li>-tags
     *
     * @param doc the input text
     * @return the parsed text
     */
    protected static Document fixListTags(Document doc) {
        for (Element li : doc.select("li")) {
            li.prepend("- ");
            li.append("<br />");
        }
        return doc;
    }

    /***
     * Replaces embedded videos by a link to the video
     *
     * @param doc the input text
     * @return the parsed text
     */
    protected static Document fixVideoLinks(Document doc) {
        for (Element video : doc.select("video")) {
            String link = video.attr("src");
            Attributes attr = new Attributes();
            attr.put("href", link);

            Element a = new Element(Tag.valueOf("a"), "", attr);
            a.text(ZUM_VIDEO + ": " + link);

            video.replaceWith(a);
        }

        for (Element iframe : doc.select("iframe")) {
            String link = iframe.attr("src");
            if (link.charAt(0) == '/' && link.charAt(1) == '/') link = "http:" + link;

            Attributes attr = new Attributes();
            attr.put("href", link);

            Element a = new Element(Tag.valueOf("a"), "", attr);
            a.text(ZUM_VIDEO + ": " + link);

            iframe.replaceWith(a);
        }

        return doc;
    }

    /***
     * Removes all HTML-Tags in the given Document
     *
     * @param doc the input text
     * @return the parsed text
     */
    protected static String removeHTML(Document doc) {
        return doc.body().text();
    }

    /***
     * Removes all \n and nbsp; from the given text
     *
     * @param text the input text
     * @return the parsed text
     */
    protected String removeNewlines(String text) {
        text = text.replaceAll("&nbsp;", "");
        return text.replaceAll("\n", "");
    }
}
