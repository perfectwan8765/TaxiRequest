package com.jsw.app;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class TaxiRequestApplicationTests {

    @Test
    void jasypt() {
        String url = "jdbc:postgresql://localhost:5432/taxiapp?currentSchema=taxiapp";
        String username = "taxiapp";

        System.out.format("url: %s", jasyptEncoding(url));
        System.out.println();
        System.out.format("username: %s", jasyptEncoding(username));
    }
    
    public String jasyptEncoding (String message) {
        String key = "taxiapp";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();

        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);

        return pbeEnc.encrypt(message);
    }

}
