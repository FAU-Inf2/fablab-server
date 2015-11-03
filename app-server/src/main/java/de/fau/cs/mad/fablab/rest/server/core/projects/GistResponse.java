package de.fau.cs.mad.fablab.rest.server.core.projects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GistResponse  implements Serializable {

    @JsonIgnore
    private String url;
    @JsonIgnore
    private String forks_url;
    @JsonIgnore
    private String commits_url;

    private String id;

    @JsonIgnore
    private String git_pull_url;
    @JsonIgnore
    private String git_push_url;

    private String html_url;

    @JsonIgnore
    private String files;
    @JsonIgnore
    private boolean isPublic;
    @JsonIgnore
    private String created_at;
    @JsonIgnore
    private String updated_at;
    @JsonIgnore
    private String description;
    @JsonIgnore
    private String comments;
    @JsonIgnore
    private String user;
    @JsonIgnore
    private String comments_url;
    @JsonIgnore
    private String owner;
    @JsonIgnore
    private String forks;
    @JsonIgnore
    private String history;

    public GistResponse() {
    }

    public GistResponse(String url, String forks_url, String commits_url, String id, String git_pull_url, String git_push_url, String html_url, String files, boolean isPublic, String created_at, String updated_at, String description, String comments, String user, String comments_url, String owner, String forks, String history) {
        this.url = url;
        this.forks_url = forks_url;
        this.commits_url = commits_url;
        this.id = id;
        this.git_pull_url = git_pull_url;
        this.git_push_url = git_push_url;
        this.html_url = html_url;
        this.files = files;
        this.isPublic = isPublic;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.description = description;
        this.comments = comments;
        this.user = user;
        this.comments_url = comments_url;
        this.owner = owner;
        this.forks = forks;
        this.history = history;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getForks_url() {
        return forks_url;
    }

    public void setForks_url(String forks_url) {
        this.forks_url = forks_url;
    }

    public String getCommits_url() {
        return commits_url;
    }

    public void setCommits_url(String commits_url) {
        this.commits_url = commits_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGit_pull_url() {
        return git_pull_url;
    }

    public void setGit_pull_url(String git_pull_url) {
        this.git_pull_url = git_pull_url;
    }

    public String getGit_push_url() {
        return git_push_url;
    }

    public void setGit_push_url(String git_push_url) {
        this.git_push_url = git_push_url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public boolean isPublic() {
        return isPublic;
    }

    @JsonProperty("public")
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComments_url() {
        return comments_url;
    }

    public void setComments_url(String comments_url) {
        this.comments_url = comments_url;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getForks() {
        return forks;
    }

    public void setForks(String forks) {
        this.forks = forks;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
