package org.pujolitoo.CModpackDownloader;


public class Project {
    private String projectName;
    private int projectId;
    private String projectSlug;
    private String contentDownloadURL;
    private int latestFileID;
    private String fileName;

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectId() {
        return this.projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectSlug() {
        return this.projectSlug;
    }

    public void setProjectSlug(String projectSlug) {
        this.projectSlug = projectSlug;
    }

    public String getContentDownloadURL() {
        return this.contentDownloadURL;
    }

    public void setContentDownloadURL(String contentDownloadURL) {
        this.contentDownloadURL = contentDownloadURL;
    }

    public int getLatestFileID() {
        return this.latestFileID;
    }

    public void setLatestFileID(int latestFileID) {
        this.latestFileID = latestFileID;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
