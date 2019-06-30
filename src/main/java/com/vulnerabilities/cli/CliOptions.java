package com.vulnerabilities.cli;

import org.apache.commons.cli.*;

class CliOptions {
    private static final String DEBUG = "d";
    private static final String HELP = "h";
    private static final String CONFIG = "con";
    private static final String API_KEY = "key";
    private static final String AUTH_TOKEN = "auth";
    private static final String ORGANIZATION_UUID = "org";
    private static final String TRACE_UUID = "tid";
    private static final String APPLICATION_UUID = "aid";
    
    private Options options;
    private CommandLine cmdLine;

    CliOptions(String[] args) throws ParseException {
        options = new Options();
        options.addOption(HELP, "help", false, "This help text");
        options.addOption(CONFIG, "config", true, "Configuration file path");
        options.addOption(API_KEY, "apikey", true, "API Key (overrides config file)");
        options.addOption(AUTH_TOKEN, "authorization", true, "Authorization Header (overrides config file)");
        options.addOption(ORGANIZATION_UUID, "organization", true, "Organization ID (overrides config file)");
        options.addOption(TRACE_UUID, "traceid", true, "Trace UUID");
        options.addOption(APPLICATION_UUID, "applicationid", true, "Application UUID");
        options.addOption(DEBUG, "debug", false, "Show additional debug information");

        this.parseArgs(args);
    }
    
    private void parseArgs(String[] args) throws ParseException {
        cmdLine = new DefaultParser().parse(options, args);
    }
    
    void showHelp() {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar cli.jar [-options]\nwhere options include:", options);
    }
    
    boolean isHelpEnabled() {
        return cmdLine != null && cmdLine.hasOption(HELP);
    }

    boolean isDebugEnabled() {
        return cmdLine != null && cmdLine.hasOption(DEBUG);
    }

    String getConfigPath() {
        if(cmdLine == null)
            return null;
        return cmdLine.getOptionValue(CONFIG);
    }

    String getApiKey() {
        if(cmdLine == null)
            return null;
        return cmdLine.getOptionValue(API_KEY);
    }

    String getAuthToken() {
        if(cmdLine == null)
            return null;
        return cmdLine.getOptionValue(AUTH_TOKEN);
    }

    String getOrganizationUuid() {
        if(cmdLine == null)
            return null;
        return cmdLine.getOptionValue(ORGANIZATION_UUID);
    }

    String getTraceUuid() {
        if(cmdLine == null)
            return null;
        return cmdLine.getOptionValue(TRACE_UUID);
    }

    String getApplicationUuid() {
        if(cmdLine == null)
            return null;
        return cmdLine.getOptionValue(APPLICATION_UUID);
    }
}
