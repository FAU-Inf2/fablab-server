package de.fau.cs.mad.fablab.rest.server.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by EE on 11.05.15.
 */

@Entity
@Table(name="news")
public class News implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "link")
    private String link;

    @Column(name = "description")
    private String description;

    @Column(name = "descriptionShort")
    private String descriptionShort;

    @Column(name = "category")
    private String category;

    @Column(name = "pubDate")
    private Date pubDate;

    @Column(name = "creator")
    private String creator;

    @Column(name = "isPermaLink")
    private boolean isPermaLink;

    @Column(name = "linkToPreviewImage")
    private String linkToPreviewImage;


    public News(){    }
    public News(String title, String link, String description, String descriptionShort, String category, Date pubDate, String creator, boolean isPermaLink, String linkToPreviewImage){
        this.title = title;
        this.link = link;
        this.description = description;
        this.descriptionShort = descriptionShort;
        this.category = category;
        this.pubDate = pubDate;
        this.creator = creator;
        this.isPermaLink = isPermaLink;
        this.linkToPreviewImage = linkToPreviewImage;
    }

    @JsonProperty
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty
    public String getDescriptionShort() {
        return descriptionShort;
    }
    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    @JsonProperty
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty
    public Date getPubDate() {
        return pubDate;
    }
    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @JsonProperty
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @JsonProperty
    public boolean getIsPermaLink() {
        return isPermaLink;
    }
    public void setIsPermaLink(Boolean isPermaLink) {
        this.isPermaLink = isPermaLink;
    }

    @JsonProperty
    public String getLinkToPreviewImage() {
        return linkToPreviewImage;
    }
    public void setLinkToPreviewImage(String linkToPreviewImage) {
        this.linkToPreviewImage = linkToPreviewImage;
    }

    public String asHTMLString(){
        return "<strong>Title: " + title + "</strong>"
                + "<br><a href=\"" + link + "\">LINK</a>"
                + "<br>description: " + description
                + "<br>category: " + category
                + "<br>pubDate: " + pubDate.toString()
                + "<br>creator: " + creator
                + "<br>isPermaLink: " + String.valueOf(isPermaLink)
                + "<img src=\"" + linkToPreviewImage + "\" />";
    }

    @Override
    public String toString(){
        return "Title: " + title + " Link: " + link + " description: " + description + " category: " + category + " pubDate: " + pubDate.toString() + " creator: " + creator + "isPermaLink: " + String.valueOf(isPermaLink);
    }

}
