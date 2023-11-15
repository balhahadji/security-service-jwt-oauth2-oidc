package tn.mycompany.securityservice.sec;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RsaKeysConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {

}
