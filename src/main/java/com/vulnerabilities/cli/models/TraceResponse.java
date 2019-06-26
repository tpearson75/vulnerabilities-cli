package com.vulnerabilities.cli.models;

public class TraceResponse extends ApiResponse {
    public TraceResource trace;

    public String getOutput() {
        StringBuilder output = new StringBuilder();
        if(trace != null) {
            output.append("UUID: ").append(trace.uuid).append("\n");
            output.append("Status: ").append(trace.status).append("\n");
            output.append("Severity: ").append(trace.severity).append("\n");
        }
        return output.toString();
    }
}
