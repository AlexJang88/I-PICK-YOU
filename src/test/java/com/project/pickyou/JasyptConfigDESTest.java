package com.project.pickyou;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

    public class JasyptConfigDESTest {
        public String key = "070good";

        @Test
        void stringEncryptor() {
            String url = "jdbc:mariadb://3.34.35.73:3306/pickyou";
            String userName = "manageus";
            String password = "1234";
            String redisPassword = null;

            System.out.println("En_url : " + jasyptEncoding(url));
            System.out.println("En_username : " + jasyptEncoding(userName));
            System.out.println("En_password : " + jasyptEncoding(password));
            System.out.println("En_redisPassword : " + jasyptEncoding(redisPassword));
        }

        @Test
        void stringDecryptor() {
            String url = "dHiTlDTsWnl2KiKbIVIksv6yLmDekOw8cEvq0yLITSqUYnn1Fa/v89wMWMa9h12w";
            String userName = null;
            String password = null;
            String redisPassword = null;

            System.out.println("De_url : " + jasyptDecoding(url));
            System.out.println("De_username : " + jasyptDecoding(userName));
            System.out.println("De_password : " + jasyptDecoding(password));
            System.out.println("De_redisPassword : " + jasyptDecoding(redisPassword));
        }

        public String jasyptEncoding(String value) {
            StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
            pbeEnc.setAlgorithm("PBEWithMD5AndDES");
            pbeEnc.setPassword(key);
            return pbeEnc.encrypt(value);
        }

        public String jasyptDecoding(String value) {
            StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
            pbeEnc.setAlgorithm("PBEWithMD5AndDES");
            pbeEnc.setPassword(key);
            return pbeEnc.decrypt(value);
        }
    }
