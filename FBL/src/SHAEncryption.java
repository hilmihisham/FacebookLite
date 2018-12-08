import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * calculating SHA hash value
 */
public class SHAEncryption {

    public String getSHA(String input) {

        try {
            // get instance of SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // calculating message digest of input and return byte[]
            byte[] messageDigest = md.digest(input.getBytes());

            // convert byte array into signum representation
            BigInteger number = new BigInteger(1, messageDigest);

            // convert message digest into hex value
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;

        } catch (NoSuchAlgorithmException e) { // exception for MessageDigest
            System.out.println("Exception: incorrect algorithm");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SHAEncryption sha = new SHAEncryption();

        System.out.println("password1 = " + sha.getSHA("password1"));
        System.out.println("password2 = " + sha.getSHA("password2"));
        System.out.println("password1 = " + sha.getSHA("password1"));

    }

}
