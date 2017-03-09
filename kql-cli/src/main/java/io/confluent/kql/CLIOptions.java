package io.confluent.kql;

import com.github.rvesse.airline.HelpOption;
import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.MutuallyExclusiveWith;
import com.github.rvesse.airline.annotations.restrictions.Required;
import com.github.rvesse.airline.help.Help;
import com.github.rvesse.airline.parser.errors.ParseException;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Command(name = "KQL", description = "Kafka Query Language")
public class CLIOptions {

  private static final String PROPERTIES_FILE_OPTION_NAME = "--properties-file";
  private static final String CATALOG_FILE_OPTION_NAME = "--catalog-file";
  private static final String QUERY_FILE_OPTION_NAME = "--query-file";
  private static final String QUERIES_OPTION_NAME = "--queries";
  private static final String QUERY_TIME_OPTION_NAME = "--query-time";

  private static final String NON_INTERACTIVE_QUERIES_TAG = "non-interactive queries";

  // Only here so that the help message generated by Help.help() is accurate
  @Inject
  public HelpOption help;

  // Uncomment if you ever want to take in non-flag arguments
//  @Arguments
//  public ArrayList<String> arguments;
//  public List<String> getArguments() {
//    return arguments;
//  }

  @Required
  @Option(
      name = PROPERTIES_FILE_OPTION_NAME,
      description = "A file specifying properties for KQL and its underlying Kafka Streams instance(s)"
  )
  private String propertiesFile;
  public String getPropertiesFile() {
    return propertiesFile;
  }

  @Option(
      name = CATALOG_FILE_OPTION_NAME,
      description = "A file to import metastore data from before execution"
  )
  private String catalogFile;
  public String getCatalogFile() {
    return catalogFile;
  }

  @Option(
      name = QUERY_FILE_OPTION_NAME,
      description = "A file to run non-interactive queries from"
  )
  @MutuallyExclusiveWith(tag = NON_INTERACTIVE_QUERIES_TAG)
  private String queryFile;
  public String getQueryFile() {
    return queryFile;
  }

  @Option(
      name = QUERIES_OPTION_NAME,
      description = "One or more non-interactive queries to run"
  )
  @MutuallyExclusiveWith(tag = NON_INTERACTIVE_QUERIES_TAG)
  private String queries;
  public String getQueries() {
    return queries;
  }

  @Option(
      name = QUERY_TIME_OPTION_NAME,
      description = "How long to run non-interactive queries for (ms)"
  )
  private Long queryTime;
  public Long getQueryTime() {
    return queryTime;
  }

  public static CLIOptions parse(String[] ass) throws IOException {
    SingleCommand<CLIOptions> optionsParser = SingleCommand.singleCommand(CLIOptions.class);

    // If just a help flag is given, an exception will be thrown due to missing required options; hence, this workaround
    for (String arg : ass) {
      if ("--help".equals(arg) || "-h".equals(arg)) {
        Help.help(optionsParser.getCommandMetadata());
        return null;
      }
    }

    try {
      return optionsParser.parse(ass);
    } catch (ParseException exception) {
      if (exception.getMessage() != null) {
        System.err.print(exception.getMessage());
      } else {
        System.err.println("Options parsing failed for an unknown reason");
      }
      System.err.println("See the -h or --help flags for usage information");
      return null;
    }
  }

  public static Map<String, Object> getPropertiesMap(String propertiesFile) throws IOException {
    Map<String, Object> result = new HashMap<>();

    Properties properties = new Properties();
    properties.load(new FileInputStream(propertiesFile));

    for (Map.Entry<Object, Object> propertyEntry : properties.entrySet()) {
      result.put((String) propertyEntry.getKey(), propertyEntry.getValue());
    }

    return result;
  }

  /*
    QUERY_FILE_PATH_CONFIG = "kql.query.file";
    CATALOG_FILE_PATH_CONFIG = "kql.catalog.file";
    PROP_FILE_PATH_CONFIG = "kql.properties.file";

    QUERY_CONTENT_CONFIG = "query";
    QUERY_EXECUTION_TIME_CONFIG = "terminate.in";

    DEFAULT_QUERY_FILE_PATH_CONFIG = "cli";
    DEFAULT_SCHEMA_FILE_PATH_CONFIG = "NULL";
    DEFAULT_PROP_FILE_PATH_CONFIG = "";

    DEFAULT_BOOTSTRAP_SERVERS_CONFIG = "localhost:9092";
    DEFAULT_AUTO_OFFSET_RESET_CONFIG = "earliest";

    AVRO_SERDE_SCHEMA_DIRECTORY_CONFIG = "avro.serde.schema";

    AVRO_SCHEMA_FOLDER_PATH_CONFIG = "/tmp/";
    DEFAULT_AVRO_SCHEMA_FOLDER_PATH_CONFIG = "/tmp/";
   */
}
