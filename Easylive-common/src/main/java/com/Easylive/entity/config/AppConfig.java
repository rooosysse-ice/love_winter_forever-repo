package com.Easylive.entity.config;

import com.Easylive.utils.StringTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${project.folder:}")
    private String projectFolder;

    @Value("${admin.account:admin}")
    private String adminAccount;

    @Value("${admin.password:admin123456}")
    private String adminPassword;

    public String getAdminAccount() {
        return adminAccount;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getProjectFolder() { return projectFolder; }

}
