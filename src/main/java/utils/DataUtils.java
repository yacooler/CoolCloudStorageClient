package utils;


public class DataUtils {
    /**
     * Получение int из массива байт
     *
     */
    public static int byteArrayToInt(byte[] array, int startPosition){
        return array[startPosition] << 24 | array[startPosition+1] << 16 | array[startPosition+2] << 8 | array[startPosition+3];
    }

    public static byte[] intToByteArray(int value){
        byte[] result = {(byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) (value)};
        return result;
    }

}
