package com.f1soft.nagarik.tester.webservice;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NagarikWebService extends RestTemplate {

    private static final String BASE_URL = "https://dev-webservice.nagarikapp.gov.np/api";

    public String fetchAccessToken() {

        final MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("client-code", "RESTBNK");
        headers.add("client-secret", "hNcKFwvlm9pa00oWunKNRmLeh84MSI2s");

        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        final URI uri = URI.create(BASE_URL + "/clientauth/token");

        ResponseEntity<JsonNode> response = exchange(uri, HttpMethod.GET, httpEntity, JsonNode.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody()).get("token").asText();
        }
        throw new RuntimeException("Fetching Access Token Failed");
    }

    public String fetchRedirectionCode(String token) {
        final MultiValueMap<String, String> headers = new HttpHeaders();

        headers.add("client-code", "RESTBNK");
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        final HttpEntity<List<Integer>> httpEntity = new HttpEntity<>(Arrays.asList(8078500, 8078650, 8076950),
                headers);

        final URI uri = URI.create(BASE_URL + "/clients/native/redirection-consent");

        ResponseEntity<JsonNode> response = exchange(uri, HttpMethod.POST, httpEntity, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody()).get("redirection_code").asText();
        }
        throw new RuntimeException("Fetching Redirection Code Failed");
    }

    public JsonNode challengeHashToGenerateQRCode(String redirectionCode, String challengeHash) {
        final MultiValueMap<String, String> headers = new HttpHeaders();

        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();

        formData.add("challenge_hash", challengeHash);
        formData.add("redirection_code", redirectionCode);

        final HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(formData, headers);

        final URI uri = URI.create(BASE_URL + "/clients/native/redirection-consent");

        ResponseEntity<JsonNode> response = exchange(uri, HttpMethod.POST, httpEntity, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody());
        }

        throw new RuntimeException("Challenge Hash To Generate QR Code Failed");
    }

    public JsonNode fetchCitizenShipDetails(String token, String authorizationCode) {
        final MultiValueMap<String, String> headers = new HttpHeaders();

        headers.add("client-code", "RESTBNK");
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.add("authorization-code", authorizationCode);

        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        final URI uri = URI.create(BASE_URL + "/clients/cims/details-consent");

        ResponseEntity<JsonNode> response = exchange(uri, HttpMethod.GET, httpEntity, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody());
        }
        throw new RuntimeException("Fetching Citizenship details Failed");
    }
}
