package canul.android;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Chazz on 20/10/15.
 */
public class Hash {

    private static final String TAG = Hash.class.getName();

    public static String digest(String password) {
        MessageDigest mdSHA1;
        String hash;

        StringBuffer buffer = new StringBuffer();
        try {
            mdSHA1 = MessageDigest.getInstance("SHA-1");
            mdSHA1.update(password.getBytes("UTF-8"));
            byte[] data = mdSHA1.digest();
            hash = convertToHex(data);
            buffer.append(hash);

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error while initializing SHA1 message digest");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static String convertToHex(byte[] data) throws IOException {

        StringBuffer buffer = new StringBuffer();
        for(byte b : data){
            buffer.append(String.format("%02x", b));
        }

        return buffer.toString();
    }

}
