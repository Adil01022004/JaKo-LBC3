package com.example.jako;

public class LBCKeyGenerator {

    private static final int KEY_SIZE = 80; // Размер ключа в битах
    private static final int ROUNDS = 20; // Количество раундов
    private static final int SUBBLOCK_SIZE = 16; // Размер подблока в битах

    private byte[] masterKey; // Мастер-ключ

    public LBCKeyGenerator(byte[] masterKey) {
        if (masterKey.length * 8 != KEY_SIZE) {
            throw new IllegalArgumentException("Неверный размер ключа: " + masterKey.length * 8 + " бит. Требуется: " + KEY_SIZE + " бит.");
        }
        this.masterKey = masterKey;

        int i = 0;

    }

    public byte[][] generateRoundKeys() {
        byte[][] roundKeys = new byte[ROUNDS][KEY_SIZE / 8];
        byte[] currentKey = masterKey.clone();

        for (int round = 0; round < ROUNDS; round++) {
            // Разделение текущего ключа на подблоки
            byte[][] subBlocks = new byte[5][SUBBLOCK_SIZE / 8];
            for (int i = 0; i < 5; i++) {
                System.arraycopy(currentKey, i * (SUBBLOCK_SIZE / 8), subBlocks[i], 0, SUBBLOCK_SIZE / 8);
            }

            // Преобразование подблоков
            for (int i = 0; i < 5; i++) {
                if (i == 0) {
                    subBlocks[i] = sBlockTransformation(subBlocks[i]);
                }
                subBlocks[i] = rotateLeft(subBlocks[i], 6 + i % 2);
                if (i < 4) {
                    subBlocks[i] = xor(subBlocks[i], subBlocks[i + 1]);
                }
            }

            // Сдвиг и сборка раундового ключа
            byte[] roundKey = new byte[KEY_SIZE / 8];
            for (int i = 0; i < 4; i++) {
                System.arraycopy(subBlocks[(i + 1) % 5], 0, roundKey, i * (SUBBLOCK_SIZE / 8), SUBBLOCK_SIZE / 8);
            }

            roundKeys[round] = roundKey;

            // Обновление текущего ключа для следующего раунда
            currentKey = roundKey.clone();
        }

        return roundKeys;
    }

    private byte[] sBlockTransformation(byte[] block) {
        // Реализация преобразования S-блока
        // Заменить на соответствующую логику с использованием S-блока
        return block;
    }

    private byte[] rotateLeft(byte[] block, int positions) {
        // Реализация циклического сдвига влево
        // Заменить на соответствующую логику
        return block;
    }

    private byte[] xor(byte[] a, byte[] b) {
        // Реализация операции XOR
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }
}
