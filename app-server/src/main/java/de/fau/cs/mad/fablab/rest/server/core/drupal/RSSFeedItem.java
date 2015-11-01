package de.fau.cs.mad.fablab.rest.server.core.drupal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RSSFeedItem {

    private String title;
    private String link;
    private String description;
    private String comments;
    private String pubDate;
    private String category;

    @JacksonXmlProperty(localName = "creator", namespace = "http://purl.org/dc/elements/1.1/")
    private String creator;

    private String guid;

    public RSSFeedItem() {}

    public RSSFeedItem(String title, String link, String description, String comments, String pubDate, String creator, String guid, String category) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.comments = comments;
        this.pubDate = pubDate;
        this.creator = creator;
        this.guid = guid;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString() {
        return getTitle() + ", " + getLink();
    }
}
