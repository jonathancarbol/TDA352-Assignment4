// Compilation (CryptoLibTest contains the main-method):
//   javac CryptoLibTest.java
// Running:
//   java CryptoLibTest

import java.math.BigDecimal;
import java.util.Random;

public class CryptoLib {

	/**
	 * Returns an array "result" with the values "result[0] = gcd",
	 * "result[1] = s" and "result[2] = t" such that "gcd" is the greatest
	 * common divisor of "a" and "b", and "gcd = a * s + b * t".
	 **/
	public static int[] EEA(int a, int b) {
		// Note: as you can see in the test suite,
		// your function should work for any (positive) value of a and b.

		int gcd = gcd(a,b);

		int[] coeffs = bezoutCoefficients(a,b,gcd);

		int s = coeffs[0];
		int t = coeffs[1];
		int[] result = new int[3];
		result[0] = gcd;
		result[1] = s;
		result[2] = t;

		return result;

	}

	public static int[] bezoutCoefficients(int a, int b, int gcd){

		int[] coefficients = new int[2];
		int r1, r2;

		if(a >= b){
			r1 = a;
			r2 = b;
		} else {
			r1 = b;
			r2 = a;
		}

		int s1 = 1;
		int s2 = 0;

		int t1 = 0;
		int t2 = 1;

		while (r2 != 0){
			int q = r1 / r2;

			int temp = s2;
			s2 = s1 - (s2 * q);
			s1 = temp;

			temp = t2;
			t2 = t1 - (t2 * q);
			t1 = temp;

			temp = r2;
			r2 = r1 - (r2*q);
			r1 = temp;
		}

		if(a > b){
			coefficients[1] = t1;
			coefficients[0] = s1;
		} else {
			coefficients[1] = s1;
			coefficients[0] = t1;
		}

		return coefficients;
	}

	/**
	 * Returns Euler's Totient for value "n".
	 **/
	public static int EulerPhi(int n) {
		if (n < 0){
			return 0;
		}
		int num = 1; //since 1 is always a relative prime
		for (int i = 2; i < n; i++){
			if (gcd(i,n) == 1){
				num++;
			}
		}
		return num;
	}

	/**
	 * Returns the greatest common divisor recursively.
	 */
	public static int gcd(int x, int y){
		int num;
		if (x == 0){
			num = y;
			return num;
		} else{
			num = y % x;
			return gcd(num, x);
		}
	}

	/**
	 * Returns the value "v" such that "n*v = 1 (mod m)". Returns 0 if the
	 * modular inverse does not exist.
	 **/
	public static int ModInv(int n, int m) {
		if (n == 0 || m == 0){
			return 0;
		}
		for (int i = 2; i <= m; i++){
			//Specific implementation of modulus for java to include negative numbers: (n*i % m + m) % m
			//Tests if values n*i mod m = 1
			if ((n*i % m + m) % m == 1 ){
				return i;
			}
		}
		return 0;
	}

	/**
	 * Returns 0 if "n" is a Fermat Prime, otherwise it returns the lowest
	 * Fermat Witness. Tests values from 2 (inclusive) to "n/3" (exclusive).
	 **/
	public static int FermatPT(int n) {
		int a = 2;
		int k = n/3;
		int res = 0;

		if (n > 3){
			while (a < k){

				int y = powerModulo(a,n);

				if(y != 1){
					res = a;
					return res;
				}
				a = a + 1;
			}
		}

		return res;
	}

	public static int powerModulo(int a, int n){
		int x = (a*a) % n;

		for(int i = 3; i < n; i++){
			x = (x*a) % n;
		}

		return x;
	}

	/**
	 * Returns the probability that calling a perfect hash function with
	 * "n_samples" (uniformly distributed) will give one collision (i.e. that
	 * two samples result in the same hash) -- where "size" is the number of
	 * different output values the hash function can produce.
	 **/
	public static double HashCP(double n_samples, double size) {
		int i = 1;
		double sum = 1;
		// Calculates probability that generated samples are unique.
		while(i < n_samples){
			sum = sum*(size-i)/size;
			i++;
		}
		// Probability that collision occurred.
		return 1 - sum;
	}

}
