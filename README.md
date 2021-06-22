# Nagarik App Tester

> How we generated Challenge Hash
```java
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
```