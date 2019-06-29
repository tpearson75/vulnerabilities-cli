package com.vulnerabilities.cli.models;

import java.util.List;

public class ApplicationsResponse extends ApiResponse {
    public List<ApplicationResource> applications;

    public String getOutput() {
        StringBuilder output = new StringBuilder();

        if(applications != null && applications.size() > 0) {
            output.append("-- Applications --\n");
            for(ApplicationResource app : applications) {
                output.append(app.name).append(": ").append(app.app_id).append("\n");
            }
        }
        else {
            output.append("No applications found.");
        }
        
        return output.toString();
    }
}
