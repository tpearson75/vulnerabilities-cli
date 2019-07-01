package com.vulnerabilities.cli.models;

public class TraceShortResponse extends ApiResponse {
    public TraceShortResource trace;

    @Override
    public String getOutput() {
        StringBuilder output = new StringBuilder();

        if(trace != null) {
            output.append("-- Trace Details --\n");
            output.append(trace.ruleName).append("\n");
            output.append("Trace UUID: ").append(trace.uuid).append("\n");
            output.append("Application UUID: ").append(trace.appId).append("\n");
            output.append("First Time Seen: ").append(trace.firstTimeSeen.toString()).append("\n");
            output.append("Likelihood: ").append(trace.likelihood).append("\n");
            output.append("Status: ").append(trace.status).append("\n");
            output.append("Impact: ").append(trace.impact).append("\n");
            output.append("Severity: ").append(trace.severity).append("\n");
            output.append("Visible: ").append(trace.visible ? "Yes" : "No").append("\n");
            output.append("(For more details, please also supply the application ID with your request)");
        }
        else {
            output.append("No trace was found.");
        }

        return output.toString();
    }
}
