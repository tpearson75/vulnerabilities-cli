package com.vulnerabilities.cli.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TraceResource {
    public String category;
    public String confidence;
    @SerializedName("first_time_seen")
    public Date firstTimeSeen;
    public String impact;
    public String likelihood;
    @SerializedName("last_time_seen")
    public Date lastTimeSeen;
    public String severity;
    public String status;
    public String title;
    public String uuid;
    public boolean visible;
}
