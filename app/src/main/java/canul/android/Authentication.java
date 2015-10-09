package canul.android;

/**
 * Created by Chazz on 09/10/15.
 */
public class Authentication {

    private static Authentication instance;

    private static String token = null;

    private Authentication(String t){
        token = t;
    }

    public static void init(String token){
        if (instance == null) {
            instance = new Authentication(token);
        }
    }
    public static String getToken(){
        if (instance ==  null) {
            return null;
        }
        return token;
    }




}
