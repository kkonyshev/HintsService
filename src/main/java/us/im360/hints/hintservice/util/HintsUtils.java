package us.im360.hints.hintservice.util;

import us.im360.hints.hintservice.exception.SystemErrorException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * @author 	Yen
 * @version v1.0
 * @since  	v1.0
 * 
 */
public class HintsUtils {
	
	
    public static Date createDateFromString(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date rdate = null;
        try {
            rdate = dateFormat.parse(date);
        } catch (Exception ex) {
            throw new SystemErrorException(ex);
        }
        return rdate;
    }
    
    public static String createStringFromDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        return df.format(date);
    }

    public static String md5Gen() {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(UUID.randomUUID().toString().getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // тут можно обработать ошибку
            // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while (md5Hex.length() < 32) {
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }
}
