package com.vulnerabilities.cli.models;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TraceShortResource {
    @SerializedName("app_id")
    public String appId;
    @SerializedName("first_time_seen")
    public Date firstTimeSeen;
    public String impact;
    public String likelihood;
    public String ruleName;
    public String severity;
    public String status;
    public String uuid;
    public boolean visible;
}
