package py.com.fi.prototipo.utils;


import org.apache.commons.codec.binary.Hex;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wtorales
 * @fecha 29/11/2021
 */

public class CommonUtils {

    /**
     * Funcion que valida si el dato ingresado es null
     * @param data (Object)
     * @return true o false
     */
    public static boolean isEmpty(Object data) {
        if (data == null) {
            return true;
        } else if (data instanceof String) {
            return ((String) data).trim().isEmpty();
        } else if (data instanceof List) {
            return ((List) data).isEmpty();
        } else if (data instanceof Map) {
            return ((Map) data).isEmpty();
        } else if (data instanceof HashMap) {
            return ((HashMap) data).isEmpty();
        }
        return false;
    }

    /**
     * Funcion que valida si el dato ingresado es distinto de null
     * @param data (Object)
     * @return true o false
     */
    public static boolean nonEmpty(Object data) {
        return !isEmpty(data);
    }

    /**
     * Funcion que dado un String genera una cadena con el hash MD5
     * @param str (String)
     * @return String
     * @throws NoSuchAlgorithmException
     * @see MessageDigest
     * @see Hex
     */
    public static String generarMD5(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(Constants.MD5);
        messageDigest.reset();
        messageDigest.update(str.getBytes(Charset.forName(Constants.UTF8)));
        byte[] resultByte = messageDigest.digest();
        return new String(Hex.encodeHex(resultByte));
    }

}
