package io.specto.hoverfly.ruletest;

import static io.specto.hoverfly.junit.core.HoverflyConfig.localConfigs;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.HttpBodyConverter.json;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static org.assertj.core.api.Assertions.assertThat;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class HoverflyRuleMutualTlsAuthenticationTest {

    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inCaptureOrSimulationMode(
        "mutual-tls.json",
        localConfigs().clientAuth("ssl/client-auth.pem", "ssl/client-auth.key"));

    @Test
    public void shouldWorkWithTestServerRequiredClientCertificate() {

        // Given
        RestTemplate restTemplate = new RestTemplate();

        // When
        ResponseEntity<Void> response = restTemplate.getForEntity(UriComponentsBuilder.fromHttpUrl("https://client.badssl.com")
                .toUriString(), Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
