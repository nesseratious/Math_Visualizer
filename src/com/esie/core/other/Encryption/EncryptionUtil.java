package com.esie.core.other.Encryption;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionUtil {

    private static byte[] keyValue;

    public static void initKey(byte [] b){
        keyValue = b;
    }

    private static Key generateKey() {
        return new SecretKeySpec(keyValue,0,16, "AES");
    }


    public static String getMD5(String text){
        MessageDigest messageDigest;
        StringBuilder stringMD5  = new StringBuilder();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(text.getBytes());
            byte[] messageDigestMD5 = messageDigest.digest();
            for (byte bytes : messageDigestMD5)
                stringMD5.append(String.format("%02x", bytes & 0xff));
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return stringMD5.toString();
    }


    public static String encrypt(String data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }


    public static byte [] encryptToBytes(byte [] bytes) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        return c.doFinal(bytes);
    }


    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }


    public static byte [] decryptToBytes(byte [] encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        return c.doFinal(encryptedData);
    }

}
