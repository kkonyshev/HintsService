package us.im360.hints.hintservice.util;

import us.im360.hints.hintservice.exception.SystemErrorException;

import java.text.SimpleDateFormat;
import java.util.Date;


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
    
}
