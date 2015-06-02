package de.fau.cs.mad.fablab.rest.server.drupal;


public class DrupalNode {

    private Integer nid;
    private String type;
    private String language;
    private Integer uid;
    private Integer status;
    private Long created;
    private Long changed;
    private Integer comment;
    private Integer promote;
    private Integer moderate;
    private Integer sticky;
    private Integer tnid;
    private Integer translate;
    private Integer vid;
    private Integer revision_uid;
    private String title;
    private String body;
    private String teaser;
    private String log;
    private Long revision_timestamp;
    private Integer format;
    private String name;
    private String picture;
    private String data;
    private String path;
    private Integer old_status;
    private Long last_comment_timestamp;
    private String last_comment_name;
    private Integer comment_count;
    private String[] taxonomy;
    private String signature;
    private Integer spaminess;
    private String uri;

    public DrupalNode() {
    }

    public DrupalNode(Integer nid, String type, String language, Integer uid, Integer status, Long created, Long changed, Integer comment, Integer promote, Integer moderate, Integer sticky, Integer tnid, Integer translate, Integer vid, Integer revision_uid, String title, String body, String teaser, String log, Long revision_timestamp, Integer format, String name, String picture, String data, String path, Integer old_status, Long last_comment_timestamp, String last_comment_name, Integer comment_count, String[] taxonomy, String signature, Integer spaminess, String uri) {
        this.nid = nid;
        this.type = type;
        this.language = language;
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
        this.vid = vid;
        this.revision_uid = revision_uid;
        this.title = title;
        this.body = body;
        this.teaser = teaser;
        this.log = log;
        this.revision_timestamp = revision_timestamp;
        this.format = format;
        this.name = name;
        this.picture = picture;
        this.data = data;
        this.path = path;
        this.old_status = old_status;
        this.last_comment_timestamp = last_comment_timestamp;
        this.last_comment_name = last_comment_name;
        this.comment_count = comment_count;
        this.taxonomy = taxonomy;
        this.signature = signature;
        this.spaminess = spaminess;
        this.uri = uri;
    }

    public Integer getNid() {
        return nid;
    }

    public void setNid(Integer nid) {
        this.nid = nid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getChanged() {
        return changed;
    }

    public void setChanged(Long changed) {
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

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public Integer getRevision_uid() {
        return revision_uid;
    }

    public void setRevision_uid(Integer revision_uid) {
        this.revision_uid = revision_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Long getRevision_timestamp() {
        return revision_timestamp;
    }

    public void setRevision_timestamp(Long revision_timestamp) {
        this.revision_timestamp = revision_timestamp;
    }

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getOld_status() {
        return old_status;
    }

    public void setOld_status(Integer old_status) {
        this.old_status = old_status;
    }

    public Long getLast_comment_timestamp() {
        return last_comment_timestamp;
    }

    public void setLast_comment_timestamp(Long last_comment_timestamp) {
        this.last_comment_timestamp = last_comment_timestamp;
    }

    public String getLast_comment_name() {
        return last_comment_name;
    }

    public void setLast_comment_name(String last_comment_name) {
        this.last_comment_name = last_comment_name;
    }

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    public String[] getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String[] taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getSpaminess() {
        return spaminess;
    }

    public void setSpaminess(Integer spaminess) {
        this.spaminess = spaminess;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String toString() {
        return "Node: id: " + nid + ", url: " + uri + ", title: " + title;
    }
}
