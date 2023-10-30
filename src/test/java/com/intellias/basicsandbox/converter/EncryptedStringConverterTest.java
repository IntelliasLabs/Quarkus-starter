package com.intellias.basicsandbox.converter;

import com.intellias.basicsandbox.persistence.converter.EncryptedStringConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncryptedStringConverterTest {

    @Test
    void encryptionAndDecryption() throws NoSuchPaddingException, NoSuchAlgorithmException {
        EncryptedStringConverter convertor = new EncryptedStringConverter();
        String sensitiveData = "sensitive data";
        String encryptedData = convertor.convertToDatabaseColumn(sensitiveData);
        String decryptedData = convertor.convertToEntityAttribute(encryptedData);
        assertEquals(sensitiveData, decryptedData);
    }

}