package de.fau.cs.mad.fablab.rest.server.drupal;


public class DrupalNode {

    private Integer nid;
    private Integer vid;
    private String type;
    private String language;
    private String title;
    private Integer uid;
    private Integer status;
    private String created;
    private String changed;
    private Integer comment;
    private Integer promote;
    private Integer moderate;
    private Integer sticky;
    private Integer tnid;
    private Integer translate;
    private String uri;

    public DrupalNode() {
    }

    public DrupalNode(Integer nid, Integer vid, String type, String language, String title, Integer uid, Integer status, String created, String changed,
                      Integer comment, Integer promote, Integer moderate, Integer sticky, Integer tnid, Integer translate, String uri) {
        this.nid = nid;
        this.vid = vid;
        this.type = type;
        this.language = language;
        this.title = title;
        this.uid = uid;
        this.status = status;
        this.created = created;
        this.changed = changed;
        this.comment = comment;
        this.promote = promote;
        this.moderate = moderate;
        this.sticky = sticky;
        this.tnid = tnid;
        this.translate = translate;
        this.uri = uri;
    }

    public Integer getNid() {
        return nid;
    }

    public void setNid(Integer nid) {
        this.nid = nid;
    }

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getChanged() {
        return changed;
    }

    public void setChanged(String changed) {
        this.changed = changed;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public Integer getPromote() {
        return promote;
    }

    public void setPromote(Integer promote) {
        this.promote = promote;
    }

    public Integer getModerate() {
        return moderate;
    }

    public void setModerate(Integer moderate) {
        this.moderate = moderate;
    }

    public Integer getSticky() {
        return sticky;
    }

    public void setSticky(Integer sticky) {
        this.sticky = sticky;
    }

    public Integer getTnid() {
        return tnid;
    }

    public void setTnid(Integer tnid) {
        this.tnid = tnid;
    }

    public Integer getTranslate() {
        return translate;
    }

    public void setTranslate(Integer translate) {
        this.translate = translate;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "Node: id: " + nid + ", url: " + uri;
    }
}
