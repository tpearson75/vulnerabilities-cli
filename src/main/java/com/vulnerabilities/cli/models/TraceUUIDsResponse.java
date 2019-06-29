package com.vulnerabilities.cli.models;

import java.util.List;

public class TraceUUIDsResponse extends ApiResponse {
    public List<String> traces;

    public String getOutput() {
        StringBuilder output = new StringBuilder();
        
        if(traces != null && traces.size() > 0) {
            output.append("-- Trace UUIDs --\n");
            for(String trace : traces) {
                output.append(trace).append("\n");
            }
        }
        else {
            output.append("No traces found.");
        }
        
        return output.toString();
    }
}
