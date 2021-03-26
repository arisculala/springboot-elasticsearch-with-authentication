package com.elasticsearch.authentication.service;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchService {
  public static final Logger log = LoggerFactory.getLogger(ElasticsearchService.class);

  @Autowired
  private RestHighLevelClient client; // Get instance of elasticsearch RestHighLevelClient

  /**
   * Sample call to elesticsearch using apiKey authentication
   * @return
   */
  public ResponseEntity<?> getElasticsearchClustersHealth() {
    try {
      ClusterHealthResponse response = client.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Error encountered getElasticsearchDetails", e);
      return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
