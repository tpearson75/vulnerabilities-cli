package com.vulnerabilities.cli.models;

public class TraceResponse extends ApiResponse {
    public TraceResource trace;

    public String getOutput() {
        StringBuilder output = new StringBuilder();
        
        if(trace != null) {
            output.append("-- Trace Details --\n");
            output.append(trace.title).append("\n");
            output.append("Trace UUID: ").append(trace.uuid).append("\n");
            output.append("First Time Seen: ").append(trace.firstTimeSeen.toString()).append("\n");
            output.append("Last Time Seen: ").append(trace.lastTimeSeen.toString()).append("\n");
            output.append("Category: ").append(trace.category).append("\n");
            output.append("Confidence: ").append(trace.confidence).append("\n");
            output.append("Likelihood: ").append(trace.likelihood).append("\n");
            output.append("Status: ").append(trace.status).append("\n");
            output.append("Impact: ").append(trace.impact).append("\n");
            output.append("Severity: ").append(trace.severity).append("\n");
            output.append("Visible: ").append(trace.visible ? "Yes" : "No").append("\n");
        }
        else {
            output.append("No trace was found.");
        }
        
        return output.toString();
    }
}
