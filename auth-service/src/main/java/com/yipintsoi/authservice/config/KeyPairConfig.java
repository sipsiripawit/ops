package com.yipintsoi.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;

@Configuration
public class KeyPairConfig {
    
    @Value("${app.jwt.keystore:keystore.jks}")
    private String keyStorePath;
    
    @Value("${app.jwt.keystore-password:password}")
    private String keyStorePassword;
    
    @Value("${app.jwt.key-alias:jwtsigning}")
    private String keyAlias;
    
    @Value("${app.jwt.private-key-passphrase:password}")
    private String privateKeyPassphrase;
    
    @Bean
    public KeyPair keyPair() throws Exception {
        // ใน production ควรใช้ KeyStore ที่ถูกต้อง
        // สำหรับการพัฒนาเบื้องต้น เราจะสร้าง KeyPair ใหม่
        
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new ClassPathResource(keyStorePath).getInputStream(), keyStorePassword.toCharArray());
            
            RSAPrivateCrtKey key = (RSAPrivateCrtKey) keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());
            RSAPublicKeySpec spec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);
            
            return new KeyPair(publicKey, key);
        } catch (Exception e) {
            // ถ้าไม่สามารถโหลด KeyStore ได้ ให้ใช้ KeyPair ที่สร้างจาก secret ใน application.yml
            return new java.security.KeyPairGenerator().getInstance("RSA").generateKeyPair();
        }
    }
}