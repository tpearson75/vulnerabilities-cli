# vulnerabilities-cli
[![Build Status](https://travis-ci.org/tpearson75/vulnerabilities-cli.svg?branch=master)](https://travis-ci.org/tpearson75/vulnerabilities-cli)

### Introduction
This is a Command Line application that is designed to make API calls to view your web application vulnerabilities.

### Setup
To execute this application you must first compile it using maven (from the root directory):

```
mvn clean install
```

This will generate the executable JAR in the `target` directory.

### Executing
The application can be executed using the `java` command, for example:

```
java -jar target\cli-1.0.jar <options>
```

#### Command line options

See the `--help` for a list of all options available:

```
java -jar target\cli-1.0.jar --help
```
```
-aid,--applicationid <arg>    Application UUID
-auth,--authorization <arg>   Authorization Header (overrides config
                               file)
-con,--config <arg>           Configuration file path
-h,--help                     This help text
-key,--apikey <arg>           API Key (overrides config file)
-org,--organization <arg>     Organization ID (overrides config file)
-tid,--traceid <arg>          Trace UUID
```