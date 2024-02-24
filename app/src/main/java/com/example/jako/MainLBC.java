package com.example.jako;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class MainLBC {

    public static String mainLbcEncryption(String inputText) {
        Scanner scanner = new Scanner(System.in);

        // Ввод текста для шифрования


        // Преобразование введенного текста в массив байтов
        byte[] plaintext = inputText.getBytes(StandardCharsets.UTF_8);

        // Инициализация мастер-ключа (пример)
        byte[] masterKey = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

        // Инициализация экземпляра генератора ключей
        LBCKeyGenerator keyGenerator = new LBCKeyGenerator(masterKey);

        // Генерация раундовых ключей
        byte[][] roundKeys = keyGenerator.generateRoundKeys();

        // Инициализация экземпляра алгоритма шифрования
        LBCAlgorithm lbcAlgorithm = new LBCAlgorithm();

        // Шифрование данных
        byte[] ciphertext = lbcAlgorithm.encrypt(plaintext, roundKeys);
        String ciphertextOuput = bytesToHex(ciphertext);

        return ciphertextOuput;
    }

    public static String mainLbcDecryption(String inputText) {
        Scanner scanner = new Scanner(System.in);

        // Ввод текста для шифрования


        // Преобразование введенного текста в массив байтов
        byte[] plaintext = inputText.getBytes(StandardCharsets.UTF_8);

        // Инициализация мастер-ключа (пример)
        byte[] masterKey = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

        // Инициализация экземпляра генератора ключей
        LBCKeyGenerator keyGenerator = new LBCKeyGenerator(masterKey);

        // Генерация раундовых ключей
        byte[][] roundKeys = keyGenerator.generateRoundKeys();

        // Инициализация экземпляра алгоритма шифрования
        LBCAlgorithm lbcAlgorithm = new LBCAlgorithm();





        // Расшифрование данных
        byte[] decrypted = lbcAlgorithm.decrypt(plaintext, roundKeys);
        String decryptedText;
        // Проверка, совпадает ли расшифрованный текст с исходным
        boolean isSuccessful = Arrays.equals(plaintext, decrypted);
        if (isSuccessful) {
            System.out.println("Шифрование и расшифрование прошли успешно.");
            System.out.println("Расшифрованный текст: " + new String(decrypted, StandardCharsets.UTF_8));

        } else {
            System.out.println("Ошибка в процессе шифрования/расшифрования.");
        }
        decryptedText = decrypted.toString();

        return decryptedText;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
