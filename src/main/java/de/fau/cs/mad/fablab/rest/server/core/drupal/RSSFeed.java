package de.fau.cs.mad.fablab.rest.server.core.drupal;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "rss")
public class RSSFeed {

    @JacksonXmlProperty(isAttribute = true)
    private String version;

    @JacksonXmlProperty(isAttribute = true, localName = "base")
    private String base;

    @JacksonXmlProperty(isAttribute = true, localName = "dc")
    private String dc;

    private RSSFeedChannel channel;

    public RSSFeed() {}

    public RSSFeed(String version, String base, String dc, RSSFeedChannel channel) {
        this.version = version;
        this.base = base;
        this.dc = dc;
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public RSSFeedChannel getChannel() {
        return channel;
    }

    public void setChannel(RSSFeedChannel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return channel.toString();
    }
}
