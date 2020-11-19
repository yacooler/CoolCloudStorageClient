package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthUtils {
    //Переедет в автотирацию
    public static String md5(String string){
        //Результат совпадает с postgresql select md5('...')
        StringBuilder stringBuilder = new StringBuilder();

        //Получаем массив байт в md5 и переводим его в строку, т.к. в бд он лежит в строке
        byte[] bytes = new byte[0];

        try {
            bytes = MessageDigest.getInstance("MD5").digest(string.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка получения кода MD5", e);
        }

        for (int i = 0; i < bytes.length; i += 2) {
            stringBuilder.append(Integer.toHexString((bytes[i] & 0xff) << 8 | (bytes[i+1] & 0xff)));
        }

        return stringBuilder.toString();
    }
}
