import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.FileReader;

public class CBCXor {

    public static void main(String[] args) {
        String filename = "DecryptingCBC/input.txt";
        byte[] first_block = null;
        byte[] encrypted = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            first_block = br.readLine().getBytes();
            encrypted = DatatypeConverter.parseHexBinary(br.readLine());
            br.close();
        } catch (Exception err) {
            System.err.println("Error handling file.");
            err.printStackTrace();
            System.exit(1);
        }
        String m = recoverMessage(first_block, encrypted);
        System.out.println("Recovered message: " + m);
    }

    /**
     * Recover the encrypted message (CBC encrypted with XOR, block size = 12).
     *
     * @param first_block
     *            We know that this is the value of the first block of plain
     *            text.
     * @param encrypted
     *            The encrypted text, of the form IV | C0 | C1 | ... where each
     *            block is 12 bytes long.
     */
    private static String recoverMessage(byte[] first_block, byte[] encrypted) {


        //K = C0 (+) M0 (+) IV
        byte[] key = new byte[12];
        for (int i = 0; i < 12; i++) {
            key[i] =(byte) (encrypted[i] ^ encrypted[i+12] ^ first_block[i]);
        }

        //Mi = (K (+) Ci) (+) Ci-1
        String message = "199402195417";
        for (int i = 12; i < encrypted.length; i++){
            char temp = (char) (key[i%12] ^ encrypted[i] ^ encrypted[i-12]);
            message = message + temp;
        }
        //return new String(key);

        return  message;
    }
}