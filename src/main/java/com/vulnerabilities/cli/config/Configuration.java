package com.vulnerabilities.cli.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Configuration {
    private String apiKey;
    private String authorization;
    private String organizationId;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public static Configuration initializeConfiguration(String path) {
        Configuration config = null;
        Yaml yaml = new Yaml();
        // Note that this requires Java 7
        try (InputStream in = Files.newInputStream(Paths.get(path))) {
            config = yaml.loadAs(in, Configuration.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return config;
    }
}
