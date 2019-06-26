package com.vulnerabilities.cli.models;

import java.util.List;

public class TraceUUIDsResponse extends ApiResponse {
    public List<String> traces;

    public String getOutput() {
        StringBuilder output = new StringBuilder();
        for(String trace : traces) {
            output.append(trace).append("\n");
        }
        return output.toString();
    }
}
