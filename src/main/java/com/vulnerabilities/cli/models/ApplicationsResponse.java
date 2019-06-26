package com.vulnerabilities.cli.models;

import java.util.List;

public class ApplicationsResponse extends ApiResponse {
    public List<ApplicationResource> applications;

    public String getOutput() {
        StringBuilder output = new StringBuilder();
        for(ApplicationResource app : applications) {
            output.append(app.name).append(": ").append(app.app_id).append("\n");
        }
        return output.toString();
    }
}
