package com.example.demo.utils;

import com.example.demo.config.secret.Secret;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AES128 {
    private static AES128 instance;
    private final String ips;
    private final Key keySpec;

    private AES128() {
            String key = Secret.USER_INFO_PASSWORD_KEY;
            byte[] keyBytes = new byte[16];
            byte[] b = key.getBytes(UTF_8);
            System.arraycopy(b, 0, keyBytes, 0, keyBytes.length);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            this.ips = key.substring(0, 16);
            this.keySpec = keySpec;
    }
    public static AES128 getInstance(){
        if(instance==null){
           instance = new AES128();
        }
        return instance;
    }

    public String encrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(ips.getBytes()));
        byte[] encrypted = cipher.doFinal(value.getBytes(UTF_8));
        return new String(Base64.getEncoder().encode(encrypted));
    }

    public String decrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ips.getBytes(UTF_8)));
        byte[] decrypted = Base64.getDecoder().decode(value.getBytes());
        return new String(cipher.doFinal(decrypted), UTF_8);
    }
}

