package com.vulnerabilities.cli.models;

import java.util.List;

public abstract class ApiResponse {
    public boolean success;
    public List<String> messages;

    public abstract String getOutput();
}
