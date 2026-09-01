package com.interview.as.api;

import com.interview.as.model.CountryCode;
import com.interview.as.service.QueueService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/queue")
public class QueueController {
  private final QueueService queueService;

  public QueueController(QueueService queueService) {
    this.queueService = queueService;
  }

  @GetMapping("/next")
  public ResponseEntity<Map<String, Object>> next(@RequestParam CountryCode countryCode) {
    return ResponseEntity.ok(queueService.processNext(countryCode));
  }
}
