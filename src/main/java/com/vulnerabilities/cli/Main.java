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
        Options options = new Options();
        options.addOption("h", "help", false, "This help text");

        options.addOption("con", "config", true, "Configuration file path");

        options.addOption("key", "apikey", true, "API Key (overrides config file)");
        options.addOption("auth", "authorization", true, "Authorization Header (overrides config file)");
        options.addOption("org", "organization", true, "Organization ID (overrides config file)");

        options.addOption("tid", "traceid", true, "Trace UUID");
        options.addOption("aid", "applicationid", true, "Application UUID");

        try {
            CommandLine line = new DefaultParser().parse(options, args);

            if (line.hasOption("h")) {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Vulnerabilities CLI - Used see your vulnerability details", options);
                return;
            }

            String configPath = line.getOptionValue("con");

            String key = line.getOptionValue("key");
            String auth = line.getOptionValue("auth");
            String org = line.getOptionValue("org");

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

            String aid = line.getOptionValue("aid");
            String tid = line.getOptionValue("tid");

            StringBuilder url;
            Class<? extends ApiResponse> responseClass; // Use generics to support several different API Responses

            if (aid != null) {
                if (tid != null) {
                    // Get a single trace
                    url = new StringBuilder(API_BASE_URL).append("ng/").append(org).append("/traces/").append(aid).append("/trace/").append(tid);
                    responseClass = TraceResponse.class;
                } else {
                    // Get a list of traces
                    url = new StringBuilder(API_BASE_URL).append("ng/").append(org).append("/traces/").append(aid).append("/ids/");
                    responseClass = TraceUUIDsResponse.class;
                }
            } else {
                // Get a list of applications
                url = new StringBuilder(API_BASE_URL).append("ng/").append(org).append("/applications/");
                responseClass = ApplicationsResponse.class;
            }

            try {
                System.out.println("Requesting resource: " + url.toString());

                Response response = Request.Get(url.toString())
                        .addHeader("Authorization", auth)
                        .addHeader("API-Key", key)
                        .addHeader("Accept", "application/json")
                        .execute();

                Content content = response.returnContent(); // TODO: Switch this to use "responseHandler" to avoid putting buffer content in memory

                if (content != null) {
                    System.out.println(content.toString());

                    ApiResponse apiResponse = new Gson().fromJson(content.toString(), responseClass);
                    System.out.println(apiResponse.getOutput());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }
}
