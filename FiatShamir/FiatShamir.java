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
            for (int j = 0; i < runs.length; i++){
                if(i != j && runs[i].s.equals(runs[j].s)){
                    if (runs[i].c == 0){
                        a = i;
                        b = j;
                    }else {
                        a = j;
                        b = i;
                    }
                    break;
                }
            }
        }

        // From run with e = 0, we would know that a = r mod n-> r
        // From second run e = 1, we would know that a = r*s^e mod n
        // Therefore s should be given by s = r^-1 * a

        BigInteger x;
        BigInteger a1;
        BigInteger a2;

        a1 = runs[b].R;
        a2 = runs[b].s.pow(runs[b].c).divide(runs[a].R);
        x = a1.multiply(a2);


        // s = rx^c
        // r1x^c1 = r2x^c2
        // x = e^((-log(r1) + log(r2))/(c1 - c2))



        // TODO. Recover the secret value x such that x^2 = X (mod N).
        return x;
    }


/*1.    A trusted center chooses n=pq, and publishes n but keeps p and q secret.
2.    Each prover A chooses a secret s with gcd(s,n)=1, and publishes v=s2 mod n.
3.    A proves knowledge of s to B by repeating:
            (a)    A chooses random r and sends r^2 = R mod n to B.
            (b)    B chooses random c in {0,1}, and sends it to A.
            (c)    A responds with s=rx^c mod n.
            (d)    B checks if s^2 = v^e r^2 mod n.*/

    public static BigInteger sqrt(BigInteger x) {
        BigInteger div = BigInteger.ZERO.setBit(x.bitLength()/2);
        BigInteger div2 = div;
        // Loop until we hit the same value twice in a row, or wind
        // up alternating.
        for(;;) {
            BigInteger y = div.add(x.divide(div)).shiftRight(1);
            if (y.equals(div) || y.equals(div2))
                return y;
            div2 = div;
            div = y;
        }
    }
}