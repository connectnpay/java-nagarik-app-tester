package com.f1soft.nagarik.tester;

import com.f1soft.nagarik.tester.util.NagarikWebServiceUtil;
import com.f1soft.nagarik.tester.webservice.NagarikWebService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class NagarikTesterApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NagarikTesterApplication.class, args);
    }

    @Bean
    public NagarikWebService nagarikWebService() {
        return new NagarikWebService();
    }

    @Autowired
    private NagarikWebService nagarikWebService;

    @Override public void run(String... args) throws Exception {
        // GET: Fetch Access Token
        String token = nagarikWebService.fetchAccessToken();
        log.debug("Access Token : {}", token);

        // POST: Fetch Redirection Code
        String redirectionCode = nagarikWebService.fetchRedirectionCode(token);
        log.debug("Redirection Code : {}", redirectionCode);

        // POST: Challenge Hash to generate QR Code
        String challengeHash = NagarikWebServiceUtil.createChallengeHash();
        log.debug("Challenge Hash SHA-256 (Base64 Encoded) : {}", challengeHash);
        JsonNode challengeHashResponse = nagarikWebService.challengeHashToGenerateQRCode(redirectionCode,
                challengeHash);
        log.debug("Challenge Hash To Generate QR Code : {}", challengeHashResponse);

        /*
        // GET: Fetch Citizenship Details
        JsonNode citizenShipDetailsResponse = nagarikWebService.fetchCitizenShipDetails(token, "");
        log.debug("Citizenship Details : {}", citizenShipDetailsResponse);
        */
    }
}
