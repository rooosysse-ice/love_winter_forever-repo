package com.Easylive.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${project.folder:}")
    private String projectFolder;

    @Value("${admin.account:admin}")
    private String adminAccount;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    @Value("${showFFmegLog:true}")
    private Boolean showFFmpegLog;

    @Value("${es.host.port:127.0.0.1:9200}")
    private String esHostPort;

    @Value("${es.index.video.name:easylive_video}")
    private String esIndexVideoName;

    public String getEsIndexVideoName() {
        return esIndexVideoName;
    }

    public String getEsHostPort() {
        return esHostPort;
    }

    public String getAdminAccount() {
        return adminAccount;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getProjectFolder() { return projectFolder; }

    public Boolean getShowFFmpegLog() {
        return showFFmpegLog;
    }


}
