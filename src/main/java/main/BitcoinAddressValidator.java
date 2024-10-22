package main;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class BitcoinAddressValidator {

    // Base58 字符表
    private static final String BASE58_ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";

    // Bech32 字符表
    private static final String BECH32_ALPHABET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";

    // SHA-256 哈希
    public static byte[] sha256(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(data);
    }

    // Base58 解码
    public static byte[] base58Decode(String input) throws Exception {
        BigInteger num = BigInteger.ZERO;
        for (char ch : input.toCharArray()) {
            int digit = BASE58_ALPHABET.indexOf(ch);
            if (digit == -1) {
                throw new IllegalArgumentException("Invalid character found: " + ch);
            }
            num = num.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(digit));
        }

        byte[] decoded = num.toByteArray();
        if (decoded[0] == 0) {
            decoded = Arrays.copyOfRange(decoded, 1, decoded.length);
        }

        // 补齐前导的0字节
        int leadingZeros = 0;
        for (char ch : input.toCharArray()) {
            if (ch != '1') break;
            leadingZeros++;
        }

        byte[] result = new byte[leadingZeros + decoded.length];
        System.arraycopy(decoded, 0, result, leadingZeros, decoded.length);
        return result;
    }

    // 双重 SHA-256 哈希
    public static byte[] doubleSha256(byte[] data) throws Exception {
        return sha256(sha256(data));
    }

    // 校验 Base58 地址 (P2PKH / P2SH)
    public static boolean validateBase58Address(String address) throws Exception {
        byte[] decoded = base58Decode(address);
        if (decoded.length < 4) {
            return false;
        }

        byte[] checksum = Arrays.copyOfRange(decoded, decoded.length - 4, decoded.length);
        byte[] versionAndData = Arrays.copyOfRange(decoded, 0, decoded.length - 4);

        byte[] calculatedChecksum = Arrays.copyOfRange(doubleSha256(versionAndData), 0, 4);
        return Arrays.equals(checksum, calculatedChecksum);
    }

    // 校验 Bech32 地址 (SegWit)
    public static boolean validateBech32Address(String address) {
        if (!address.startsWith("bc1")) {
            return false;
        }

        try {
            bech32Decode(address);
            // Bech32地址需要至少包含一个分隔符'1'后跟至少6个字符
            int separatorPosition = address.lastIndexOf('1');
            if (separatorPosition == -1 || address.length() < 10) {
                throw new IllegalArgumentException("Invalid Bech32 address length");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Bech32 解码
    public static void bech32Decode(String bech32) throws Exception {
        int separatorPosition = bech32.lastIndexOf('1');
        if (separatorPosition == -1 || bech32.length() < 10) {
            throw new IllegalArgumentException("Invalid Bech32 address length");
        }

        String hrp = bech32.substring(0, separatorPosition);
        String dataPart = bech32.substring(separatorPosition + 1);

        if (hrp.length() == 0 || dataPart.length() == 0) {
            throw new IllegalArgumentException("Invalid Bech32 address format");
        }

        byte[] data = new byte[dataPart.length()];
        for (int i = 0; i < dataPart.length(); i++) {
            char ch = dataPart.charAt(i);
            int digit = BECH32_ALPHABET.indexOf(ch);
            if (digit == -1) {
                throw new IllegalArgumentException("Invalid Bech32 character: " + ch);
            }
            data[i] = (byte) digit;
        }

        // 校验前缀
        if (!hrp.equals("bc")) {
            throw new IllegalArgumentException("Invalid Bech32 HRP: " + hrp);
        }
    }

    // 校验比特币地址
    public static boolean validateBitcoinAddress(String address) throws Exception {
        if (address.startsWith("1") || address.startsWith("3")) {
            return validateBase58Address(address);
        } else if (address.startsWith("bc1")) {
            return validateBech32Address(address);
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        String[] addresses = {
                "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa",       // P2PKH
                "3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy",       // P2SH
                "bc1qvurplu59haxqpmyqgza02ygq4p6rqrc8j54t5q", // Bech32 (SegWit)
                "bc1panax73tg6jr0vep4gjfrm6sja9pzqtvvvx8dx2dqctgxvg65fv9q8r98wk"  // Bech32 (SegWit)
        };

        for (String address : addresses) {
            try {
                boolean isValid = validateBitcoinAddress(address);
                System.out.printf("Address %s is valid: %b\n", address, isValid);
            } catch (Exception e) {
                System.out.printf("Address %s validation error: %s\n", address, e.getMessage());
            }
        }
    }
}