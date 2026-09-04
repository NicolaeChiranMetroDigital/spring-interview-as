package com.interview.as.api;

import com.interview.as.model.CountryCode;
import com.interview.as.service.QueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/queue")
public class QueueController {
  private final QueueService queueService;

  public QueueController(QueueService queueService) {
    this.queueService = queueService;
  }

  @GetMapping({ "/country"})
  public ResponseEntity<Map<String, Object>> processCountry(@RequestParam CountryCode countryCode) {
    return ResponseEntity.ok(queueService.processCountry(countryCode));
  }
}
