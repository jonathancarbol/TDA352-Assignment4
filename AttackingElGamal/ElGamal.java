import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;


public class ElGamal {

  public static String decodeMessage(BigInteger m) {
    return new String(m.toByteArray());
  }

  public static void main(String[] arg) {
    String filename = "AttackingElGamal/input.txt";
    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      BigInteger p = new BigInteger(br.readLine().split("=")[1]);
      BigInteger g = new BigInteger(br.readLine().split("=")[1]);
      BigInteger y = new BigInteger(br.readLine().split("=")[1]);
      String line = br.readLine().split("=")[1];
      String date = line.split(" ")[0];
      String time = line.split(" ")[1];
      int year  = Integer.parseInt(date.split("-")[0]);
      int month = Integer.parseInt(date.split("-")[1]);
      int day   = Integer.parseInt(date.split("-")[2]);
      int hour   = Integer.parseInt(time.split(":")[0]);
      int minute = Integer.parseInt(time.split(":")[1]);
      int second = Integer.parseInt(time.split(":")[2]);
      BigInteger c1 = new BigInteger(br.readLine().split("=")[1]);
      BigInteger c2 = new BigInteger(br.readLine().split("=")[1]);
      br.close();
      BigInteger m = recoverSecret(p, g, y, year, month, day, hour, minute,
          second, c1, c2);
      System.out.println("Recovered message: " + m);
      System.out.println("Decoded text: " + decodeMessage(m));
    } catch (Exception err) {
      System.err.println("Error handling file.");
      err.printStackTrace();
      System.exit(1);
    }
  }

  public static BigInteger recoverSecret(BigInteger p, BigInteger g,
      BigInteger y, int year, int month, int day, int hour, int minute,
      int second, BigInteger c1, BigInteger c2) {

    BigInteger r = new BigInteger(String.valueOf(createRandomNumber(year,month,day,hour,minute,second)));


    // CryptoLib.ModInv();

    return c1;
  }

  public static BigInteger createRandomNumber(int year, int month, int day, int hour, int minute, int second){
    // int x = year*(10^10) + month*(10^8) + day*(10^6) + hour*(10^4) + minute*(10^2) + second;

    BigInteger ye = new BigInteger("10000000000");
    BigInteger me = new BigInteger("100000000");
    BigInteger de = new BigInteger("1000000");
    BigInteger he = new BigInteger("10000");
    BigInteger mine = new BigInteger("100");

    BigInteger yB = new BigInteger(String.valueOf(year)).multiply(ye);
    BigInteger mB = new BigInteger(String.valueOf(month)).multiply(me);
    BigInteger dB = new BigInteger(String.valueOf(day)).multiply(de);
    BigInteger hB = new BigInteger(String.valueOf(hour)).multiply(he);
    BigInteger minB = new BigInteger(String.valueOf(minute)).multiply(mine);
    BigInteger sB = new BigInteger(String.valueOf(second));

    return yB.add(mB).add(dB).add(hB).add(minB).add(sB);
  }


}