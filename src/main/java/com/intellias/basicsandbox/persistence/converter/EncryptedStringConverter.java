package com.intellias.basicsandbox.persistence.converter;

import com.intellias.basicsandbox.service.exception.CypherException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Converter
public final class EncryptedStringConverter implements AttributeConverter<String, String> {

    private static final String AES = "AES";
    private static final String ALGORITHM = "AES/GCM/NoPadding";

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final String SECRET = "secret-key-12345";

    private final Key key;
    private final Cipher cipher;



    @SuppressWarnings("squid:S6437") // use secrets stored in safe environment configs, and not in source code
    public EncryptedStringConverter() throws NoSuchPaddingException, NoSuchAlgorithmException {
        key = new SecretKeySpec(SECRET.getBytes(), AES);
        cipher = Cipher.getInstance(ALGORITHM);
    }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) {
            return null;
        }

        try {
            byte[] iv = generateIV();
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            byte[] encrypted = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new CypherException("Failed to encrypt attribute", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        try {
            byte[] combined = Base64.getDecoder().decode(dbData);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encrypted = new byte[combined.length - GCM_IV_LENGTH];

            System.arraycopy(combined, 0, iv, 0, iv.length);
            System.arraycopy(combined, iv.length, encrypted, 0, encrypted.length);

            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CypherException("Failed to decrypt attribute", e);
        }
    }

    private byte[] generateIV() {
        // Generate a random IV (Initialization Vector) using a secure random number generator
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }


}
