package com.mavenforge.Crypto;

import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import com.password4j.SaltGenerator;

import java.util.Base64;

import com.password4j.BcryptFunction;

public class Hashing {

    static BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);

    public static String hash(String password) {

        byte[] saltBytes = SaltGenerator.generate(12);
        String salt = Base64.getEncoder().encodeToString(saltBytes);

        Hash hash = Password.hash(password).addSalt(saltBytes).with(bcrypt);

        return salt + "$" + hash.getResult();
    }

    public static boolean verify(String password, String hash) {
        String[] parts = hash.split("\\$");

        String hashPart = parts[1];
        byte[] saltBytes = Base64.getDecoder().decode(parts[0]);

        return Password.check(password, hashPart).addSalt(saltBytes).with(bcrypt);

    }

    public static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new java.math.BigInteger(1, data));
    }

    public static byte[] randomBytes(int length) {
        byte[] bytes = new byte[length];
        new java.security.SecureRandom().nextBytes(bytes);
        return bytes;
    }

}
