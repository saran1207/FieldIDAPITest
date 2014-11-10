package com.n4systems.util.math;

import java.math.BigInteger;

/*
See: http://ericlippert.com/2013/11/14/a-practical-use-of-multiplicative-inverses/
 */
public class MultiplicativeInverseEncoder {
	//XXX - Note that all calculations are done in BigInteger.  For large coprimes and encode numbers, long calculations will overflow.
	private final BigInteger coprimeA;
	private final BigInteger coprimeB;
	private final BigInteger mInverse;

	public MultiplicativeInverseEncoder(long coprimeA, long coprimeB) {
		this(BigInteger.valueOf(coprimeA), BigInteger.valueOf(coprimeB));
	}

	/*
	Initializes the encoder with 2 coprime longs.  Both coprime numbers should be > 0 and significantly larger than
	 the largest number to encode/decode.  Finding large coprimes is trivial by using independently prime numbers.
	 Wolfram Alpha has a function for generating numbers in the prime sequence (Prime[n]).
	 See: http://www.wolframalpha.com/input/?i=Prime%5B5000000%5D
	 */
	public MultiplicativeInverseEncoder(BigInteger coprimeA, BigInteger coprimeB) {
		if (coprimeA.compareTo(BigInteger.ZERO) != 1 || coprimeB.compareTo(BigInteger.ZERO) != 1) {
			throw new IllegalArgumentException(coprimeA + " and " + coprimeB + " must be > 0");
		}

		if (!isCoprime(coprimeA, coprimeB)) {
			throw new IllegalArgumentException(coprimeA + " and " + coprimeB + " are not coprime");
		}

		this.coprimeA = coprimeA;
		this.coprimeB = coprimeB;
		this.mInverse = multiplicativeInverse(coprimeA, coprimeB);
	}

	/*
	Tests if 2 numbers are coprime
	 */
	public static boolean isCoprime(BigInteger a, BigInteger b) {
		return a.gcd(b).compareTo(BigInteger.ONE) == 0;
	}

	/*
	Used to compute Bézout’s Identity for use in multiplicative inverses
	See: http://ericlippert.com/2013/11/04/math-from-scratch-part-twelve-euclid-and-bezout/
	 */
	private static BigInteger[] extendedEuclideanDivision(BigInteger a, BigInteger b) {
		BigInteger[] result;
		if (a.compareTo(BigInteger.ZERO) == -1) {
			BigInteger[] eed = extendedEuclideanDivision(a.negate(), b);
			result = new BigInteger[] {eed[0].negate(), eed[1]};
		} else if (b.compareTo(BigInteger.ZERO) == -1) {
			BigInteger[] eed = extendedEuclideanDivision(a, b.negate());
			result = new BigInteger[] {eed[0], eed[1].negate()};
		} else if (b.compareTo(BigInteger.ZERO) == 0) {
			result = new BigInteger[] {BigInteger.ONE, BigInteger.ZERO};
		} else {
			BigInteger[] qr = a.divideAndRemainder(b);
			BigInteger[] eed = extendedEuclideanDivision(b, qr[1]);
			result = new BigInteger[] {eed[1], eed[0].subtract(qr[0].multiply(eed[1]))};
		}
		return result;
	}

	/*
	Computes the multiplicative inverse of 2 coprime numbers for use in publicId calculations.
	See: http://ericlippert.com/2013/11/12/math-from-scratch-part-thirteen-multiplicative-inverses/
	 */
	public static BigInteger multiplicativeInverse(BigInteger x, BigInteger m) {
		return extendedEuclideanDivision(x, m)[0].mod(m);
	}

	/*
	Encodes a number using coprime A and B
	see: http://ericlippert.com/2013/11/14/a-practical-use-of-multiplicative-inverses/
	 */
	public long encode(long x) {
		BigInteger X = BigInteger.valueOf(x);
		if (X.compareTo(coprimeB) != -1) {
			throw new IllegalArgumentException(x + " not less than " + coprimeB);
		}
		return X.multiply(coprimeA).remainder(coprimeB).longValue();
	}

	/*
	Encodes a number using the multiplicative inverse and coprime B
	see: http://ericlippert.com/2013/11/14/a-practical-use-of-multiplicative-inverses/
	 */
	public long decode(long y) {
		BigInteger Y = BigInteger.valueOf(y);
		if (Y.compareTo(coprimeB) != -1) {
			throw new IllegalArgumentException(y + " not less than " + coprimeB);
		}
		return Y.multiply(mInverse).remainder(coprimeB).longValue();
	}

}
