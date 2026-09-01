package com.interview.as;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import com.interview.as.model.CountryCode;
import com.interview.as.model.QueueItemStatus;
import com.interview.as.service.Scheduler;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SpringInterviewAsTests {
  @Autowired private MockMvc mockMvc;
  @Autowired private Scheduler scheduler;

  @Test
  void contextLoads() {}

  @Test
  void shouldProcessOnlyRequestedCountryItems() throws Exception {
    mockMvc
        .perform(get("/api/queue/next").param("countryCode", "RO"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.countryCode").value("RO"))
        .andExpect(jsonPath("$.finishedWithError").value(false))
        .andExpect(jsonPath("$.status").value("PROCESSED"));
  }

  @Test
  void shouldRetryAndFinishWithErrorWhenDummyApiFails() throws Exception {
    mockMvc
        .perform(get("/api/queue/next").param("countryCode", "DE"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.countryCode").value("DE"))
        .andExpect(jsonPath("$.finishedWithError").value(true))
        .andExpect(jsonPath("$.attempts").value(3))
        .andExpect(jsonPath("$.status").value("FAILED"));
  }

  @Test
  void schedulerShouldRunCountrySpecificExport() {
    Map<String, Object> result = scheduler.runExportForTest(CountryCode.FR);

    assertThat(result.get("countryCode")).isEqualTo(CountryCode.FR);
    assertThat(result.get("status")).isEqualTo(QueueItemStatus.PROCESSED);
    assertThat(result.get("finishedWithError")).isEqualTo(false);
  }
}
