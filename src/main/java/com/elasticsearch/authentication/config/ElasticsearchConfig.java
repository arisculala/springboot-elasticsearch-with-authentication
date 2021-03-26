package com.elasticsearch.authentication.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for elasticsearch client
 */
@Configuration
public class ElasticsearchConfig {
  public static final Logger log = LoggerFactory.getLogger(ElasticsearchConfig.class);

  @Value("${elasticsearch.authentication.mode:0}")
  private int elasticsearchAuthenticationMode;

  @Value("${elasticsearch.host:localhost}")
  private String elasticsearchHost;

  @Value("${elasticsearch.port:9200}")
  private int elasticsearchPort;

  @Value("${elasticsearch.authentication.api-key.id}")
  private String apiKeyId;

  @Value("${elasticsearch.authentication.api-key.name}")
  private String apiKeyName;

  @Value("${elasticsearch.authentication.api-key.key}")
  private String apiKeyUniqueKey;

  @Bean
  public RestHighLevelClient restHighLevelClient() {
    switch (elasticsearchAuthenticationMode) {
      case 1:
        return createApiKeyAuthenticatedRestHighLevelClient();
      default:
        return createDefaultRestHighLevelClient();
    }
  }

  @Bean
  public RestClient restClient() {
    switch (elasticsearchAuthenticationMode) {
      case 1:
        return createApiKeyAuthenticatedRestClient();
      default:
        return createDefaultRestClient();
    }
  }

  /**
   * Default instance of RestHighLevelClient object without authentication
   * @return
   */
  private RestHighLevelClient createDefaultRestHighLevelClient() {
    return new RestHighLevelClient(
        RestClient.builder(
            new HttpHost(elasticsearchHost, elasticsearchPort, HttpHost.DEFAULT_SCHEME_NAME)));
  }

  /**
   * ApiKey authenticated instance of RestHighLevelClient object
   * @return
   */
  private RestHighLevelClient createApiKeyAuthenticatedRestHighLevelClient() {
    RestClientBuilder builder = RestClient.builder(
        new HttpHost(elasticsearchHost, elasticsearchPort, HttpHost.DEFAULT_SCHEME_NAME));
    Header[] defaultHeaders = new Header[]{
        new BasicHeader("Authorization", "ApiKey " + generateApiKey())};
    builder.setDefaultHeaders(defaultHeaders);
    return new RestHighLevelClient(builder);
  }

  /**
   * Default instance of RestClient object without authentication
   * @return
   */
  private RestClient createDefaultRestClient() {
    return RestClient.builder(
        new HttpHost(elasticsearchHost, elasticsearchPort, HttpHost.DEFAULT_SCHEME_NAME)).build();
  }

  /**
   * ApiKey authenticated instance of RestClient object
   * @return
   */
  private RestClient createApiKeyAuthenticatedRestClient() {
    return RestClient.builder(
        new HttpHost(elasticsearchHost, elasticsearchPort, HttpHost.DEFAULT_SCHEME_NAME))
          .setDefaultHeaders(getApiKeyDefaultHeaders()).build();
  }

  /**
   * Return apiKey headers
   * @return
   */
  private Header[] getApiKeyDefaultHeaders() {
    return new Header[]{new BasicHeader("Authorization", "ApiKey " + generateApiKey())};
  }

  /**
   * Generate the actual apiKey to be used for authentication
   *  - The apiKey value credentials are the base64 encoding of
   *    the API key ID and the API key joined by a colon
   * @return
   */
  private String generateApiKey() {
    try {
      return Base64.getEncoder().encodeToString(
          String.valueOf(apiKeyId + ":" + apiKeyUniqueKey).getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      log.error("Error encountered generateApiKey", e);
    }
    return null;
  }
}