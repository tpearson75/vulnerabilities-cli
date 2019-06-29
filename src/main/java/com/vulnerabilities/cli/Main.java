package com.vulnerabilities.cli;

import com.google.gson.Gson;
import com.vulnerabilities.cli.config.Configuration;
import com.vulnerabilities.cli.models.ApiResponse;
import com.vulnerabilities.cli.models.ApplicationsResponse;
import com.vulnerabilities.cli.models.TraceResponse;
import com.vulnerabilities.cli.models.TraceUUIDsResponse;
import org.apache.commons.cli.*;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;

public class Main {

    private final static String API_BASE_URL = "https://ce.contrastsecurity.com/Contrast/api/";

    public static void main(String[] args) {
        // Initialize and parse command line options
        CliOptions options;
        try {
            options = new CliOptions(args);
        } catch (ParseException exp) {
            System.out.println("Unexpected exception: " + exp.getMessage());
            return;
        }

        // Show help
        if (options.isHelpEnabled()) {
            options.showHelp();
            return;
        }
        
        // Get options
        boolean debug = options.isDebugEnabled();
        String configPath = options.getConfigPath();
        String key = options.getApiKey();
        String auth = options.getAuthToken();
        String org = options.getOrganizationUuid();

        if (configPath != null && configPath.length() > 0) {
            Configuration config = Configuration.initializeConfiguration(configPath);
            if (config != null) {
                // The manually set command line options take priority over the config file so only populate these if they are not overridden
                if (key == null || key.length() == 0) {
                    key = config.getApiKey();
                }
                if (auth == null || auth.length() == 0) {
                    auth = config.getAuthorization();
                }
                if (org == null || org.length() == 0) {
                    org = config.getOrganizationId();
                }
            }
        }

        String aid = options.getApplicationUuid();
        String tid = options.getTraceUuid();

        StringBuilder url;
        Class<? extends ApiResponse> responseClass; // Use generics to support several different API Responses

        if (aid != null) {
            if (tid != null) {
                // Get a single trace
                url = new StringBuilder(API_BASE_URL).append("ng/").append(org).append("/traces/").append(aid).append("/trace/").append(tid);
                responseClass = TraceResponse.class;
            }
            else {
                // Get a list of traces
                url = new StringBuilder(API_BASE_URL).append("ng/").append(org).append("/traces/").append(aid).append("/ids/");
                responseClass = TraceUUIDsResponse.class;
            }
        }
        else {
            // Get a list of applications
            url = new StringBuilder(API_BASE_URL).append("ng/").append(org).append("/applications/");
            responseClass = ApplicationsResponse.class;
        }

        try {
            if (debug) {
                System.out.println("Requesting resource: " + url.toString());
            }

            Response response = Request.Get(url.toString())
                    .addHeader("Authorization", auth)
                    .addHeader("API-Key", key)
                    .addHeader("Accept", "application/json")
                    .execute();

            Content content = response.returnContent(); // TODO: Switch this to use "responseHandler" to avoid putting buffer content in memory

            if (content != null) {
                if (debug) {
                    System.out.println(content.toString());
                }

                ApiResponse apiResponse = new Gson().fromJson(content.toString(), responseClass);
                
                if (apiResponse == null || !apiResponse.success) {
                    // Handle API Errors
                    System.out.println("--- An API error occurred ---");
                    if (apiResponse != null && apiResponse.messages != null) {
                        for (String message : apiResponse.messages) {
                            System.out.println(message);
                        }
                    }
                }
                else {
                    System.out.println(apiResponse.getOutput());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
