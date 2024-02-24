package com.example.jako;

public class LBCUtils {

    // Метод для выполнения циклического сдвига влево на заданное количество битов
    public static byte[] rotateLeft(byte[] input, int length, int shift) {
        byte[] result = new byte[length];
        int byteShift = shift / 8;
        int bitShift = shift % 8;

        for (int i = 0; i < length; i++) {
            int sourceIndex = (i + byteShift) % length;
            int bitIndex = (i * 8 + bitShift) % (length * 8);

            int targetByte = bitIndex / 8;
            int targetBit = bitIndex % 8;

            byte sourceByte = input[sourceIndex];
            byte sourceBit = (byte)((sourceByte >> (8 - (bitShift + 1))) & 1);

            result[targetByte] |= (sourceBit << (7 - targetBit));
        }

        return result;
    }

    // Метод для применения S-бокс преобразования к байту
    public static byte sBoxTransform(byte input, byte[] sBox) {
        int index = input & 0xFF; // Преобразование в unsigned
        return sBox[index];
    }

    // Метод для объединения четырех байтов в одно целое
    public static int bytesToInt(byte b1, byte b2, byte b3, byte b4) {
        return ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
    }

    // Метод для разделения целого числа на четыре байта
    public static byte[] intToBytes(int value) {
        return new byte[] {
                (byte) ((value >> 24) & 0xFF),
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) (value & 0xFF)
        };
    }

    // Другие вспомогательные методы по необходимости
}