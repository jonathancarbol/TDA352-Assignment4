import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

public class FiatShamir {

    public static class ProtocolRun {
        public final BigInteger R;
        public final int c;
        public final BigInteger s;

        public ProtocolRun(BigInteger R, int c, BigInteger s) {
            this.R = R;
            this.c = c;
            this.s = s;
        }
    }

    public static void main(String[] args) {
        String filename = "FiatShamir/input.txt";
        BigInteger N = BigInteger.ZERO;
        BigInteger X = BigInteger.ZERO;
        ProtocolRun[] runs = new ProtocolRun[10];
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            N = new BigInteger(br.readLine().split("=")[1]);
            X = new BigInteger(br.readLine().split("=")[1]);
            for (int i = 0; i < 10; i++) {
                String line = br.readLine();
                String[] elem = line.split(",");
                runs[i] = new ProtocolRun(
                        new BigInteger(elem[0].split("=")[1]),
                        Integer.parseInt(elem[1].split("=")[1]),
                        new BigInteger(elem[2].split("=")[1]));
            }
            br.close();
        } catch (Exception err) {
            System.err.println("Error handling file.");
            err.printStackTrace();
            System.exit(1);
        }
        BigInteger m = recoverSecret(N, X, runs);
        System.out.println("Recovered message: " + m);
        System.out.println("Decoded text: " + decodeMessage(m));
    }

    public static String decodeMessage(BigInteger m) {
        return new String(m.toByteArray());
    }

    /**
     * Recovers the secret used in this collection of Fiat-Shamir protocol runs.
     *
     * @param N
     *            The modulus
     * @param X
     *            The public component
     * @param runs
     *            Ten runs of the protocol.
     * @return
     */
    private static BigInteger recoverSecret(BigInteger N, BigInteger X, ProtocolRun[] runs) {

        int a = 0;
        int b = 0;
        for(int i = 0; i < runs.length; i++) {
            for (int j = i+1; j < runs.length; j++){
                if(runs[i].R.equals(runs[j].R)){
                    if (runs[i].c == 0){
                        a = i;
                        b = j;
                    }else {
                        a = j;
                        b = i;
                    }
                }
            }
        }

        // From run with e = 0, we would know that s = r*a^0  mod n => r^-1 can be found.
        // From second run e = 1, we would know that s = r*a^1 mod n
        // Therefore a can be found using a = s*r*r^-1.

        BigInteger x;

        BigInteger s1 = runs[a].s;
        BigInteger s2 = runs[b].s;

        s1 = s1.modInverse(N);

        X = X.mod(N);
        x = s1.multiply(s2).mod(N);
        // Check to see that X = x^2 mod N
        BigInteger key = x.modPow(BigInteger.valueOf(2),N);
        if(key.equals(X)){
            return x;
        }
        return BigInteger.ZERO;
    }
}