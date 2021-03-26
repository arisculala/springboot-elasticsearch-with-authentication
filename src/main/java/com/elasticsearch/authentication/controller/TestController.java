package com.elasticsearch.authentication.controller;

import com.elasticsearch.authentication.service.ElasticsearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  public static final Logger log = LoggerFactory.getLogger(TestController.class);

  @Autowired
  private ElasticsearchService elasticsearchService;

  @GetMapping("/test/elasticsearch-details")
  public ResponseEntity<?> setUserSuggestedIntent() {
    return elasticsearchService.getElasticsearchClustersHealth();
  }
}
