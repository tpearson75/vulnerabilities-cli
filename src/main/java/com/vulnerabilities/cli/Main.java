package com.vulnerabilities.cli;

import com.google.gson.*;
import com.vulnerabilities.cli.config.Configuration;
import com.vulnerabilities.cli.models.*;
import org.apache.commons.cli.*;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class Main {

    private final static String API_BASE_URL = "https://ce.contrastsecurity.com/Contrast/api/ng/";

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
        String baseApiUrl = API_BASE_URL;
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
                
                if(config.getBaseApiUrl() != null && config.getBaseApiUrl().length() > 0) {
                    baseApiUrl = config.getBaseApiUrl();
                }
            }
        }
        
        // Validation of required options
        if(key == null || key.length() == 0) {
            System.out.println("Please supply an API Key using the -key option or through the configuration file.");
            return;
        }
        if(auth == null || auth.length() == 0) {
            System.out.println("Please supply an Authorization Token using the -auth option or through the configuration file.");
            return;
        }
        if(org == null || org.length() == 0) {
            System.out.println("Please supply an organization ID using the -org option or through the configuration file.");
            return;
        }
        
        String aid = options.getApplicationUuid();
        String tid = options.getTraceUuid();

        StringBuilder url;
        Class<? extends ApiResponse> responseClass; // Use generics to support several different API Responses

        if (aid != null) {
            if (tid != null) {
                // Get a single trace
                System.out.println("Getting trace details...\n");
                url = new StringBuilder(baseApiUrl).append(org).append("/traces/").append(aid).append("/trace/").append(tid);
                responseClass = TraceResponse.class;
            }
            else {
                // Get a list of traces
                System.out.println("No trace ID was supplied, getting application traces...\n");
                url = new StringBuilder(baseApiUrl).append(org).append("/traces/").append(aid).append("/ids/");
                responseClass = TraceUUIDsResponse.class;
            }
        }
        else if (tid != null) {
            // Get a single trace from the organization endpoint
            System.out.println("Getting trace summary...\n");
            url = new StringBuilder(baseApiUrl).append(org).append("/orgtraces/").append(tid).append("/short");
            responseClass = TraceShortResponse.class;
        }
        else {
            // Get a list of applications
            System.out.println("No trace ID was supplied, getting organization traces...\n");
            url = new StringBuilder(baseApiUrl).append(org).append("/orgtraces/ids");
            responseClass = TraceUUIDsResponse.class;
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

                ApiResponse apiResponse = getGson().fromJson(content.toString(), responseClass);
                
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

    private static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        return builder.create();
    }
}
