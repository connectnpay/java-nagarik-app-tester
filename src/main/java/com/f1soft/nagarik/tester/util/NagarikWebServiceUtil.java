package com.f1soft.nagarik.tester.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
@Slf4j
public class NagarikWebServiceUtil {

    public static String createChallengeHash() {
        try {
            String challengeHash = RandomStringUtils.random(40, 0, 0, true, true, null, new SecureRandom());
            log.debug("Challenge Hash Plain: {}", challengeHash);

            MessageDigest digester = MessageDigest.getInstance("SHA-256");
            digester.update(challengeHash.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(digester.digest());
        } catch (Exception e) {
            throw new RuntimeException("Failture generating challenge hash");
        }
    }
}
