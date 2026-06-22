package uk.gov.hmcts.cp.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.cp.openapi.api.HearingsApi;
import uk.gov.hmcts.cp.openapi.model.DefendantView;
import uk.gov.hmcts.cp.openapi.model.ErrorResponse;
import uk.gov.hmcts.cp.openapi.model.HearingSummaryView;
import uk.gov.hmcts.cp.openapi.model.OffenceView;
import java.lang.reflect.Field;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class OpenApiObjectsTest {
    @Test
    void generated_error_response_should_have_expected_fields() {
        assertThat(ErrorResponse.class).hasDeclaredMethods("error", "message", "details", "traceId");
    }

    @Test
    void generated_hearing_summary_should_have_expected_fields() {
        assertThat(HearingSummaryView.class)
                .hasDeclaredFields("id", "date", "type", "courtHouse", "courtRoom", "time", "startTime", "outcome");
    }

    @Test
    void generated_offence_view_should_have_expected_fields() {
        assertThat(OffenceView.class).hasDeclaredFields("id", "code", "title", "status");
    }

    @Test
    void generated_defendant_view_should_have_expected_fields() {
        assertThat(DefendantView.class).hasDeclaredFields("id", "name", "dateOfBirth", "offences");
    }

    @Test
    void generated_hearings_api_should_have_expected_methods() {
        assertThat(HearingsApi.class)
                .hasDeclaredMethods("getCaseTimeline", "getDefendantAttendance", "getDefendants");
    }

    @Test
    void generated_error_response_timestamp_should_be_instant() throws Exception {
        Field timestampField = ErrorResponse.class.getDeclaredField("timestamp");

        assertThat(timestampField.getType())
                .as("timestamp field type")
                .isEqualTo(Instant.class);
    }
}
