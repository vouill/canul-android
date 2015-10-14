package canul.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Chazz on 14/10/15.
 */
public class  DateConverter {

    public static String INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static String OUTPUT_FORMAT = "dd/MM/yy";

    public static String convert(String input) {
        String output = "xx/xx/xx";
        SimpleDateFormat df = new SimpleDateFormat( INPUT_FORMAT);
        TimeZone tz = TimeZone.getTimeZone("GMT");
        df.setTimeZone(tz);
        try {
            Date date = df.parse(input);
            output = new SimpleDateFormat(OUTPUT_FORMAT).format(date);
        } catch( ParseException e) {
            e.printStackTrace();
        }

        return output;
    }
}
