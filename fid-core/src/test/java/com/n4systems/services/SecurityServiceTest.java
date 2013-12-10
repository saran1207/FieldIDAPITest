package com.n4systems.services;

import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import org.easymock.Capture;
import org.easymock.IAnswer;
import org.junit.Test;

import java.security.MessageDigest;
import java.util.Random;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SecurityServiceTest extends FieldIdServiceTest {

	@TestTarget private SecurityService securityService;
	@TestMock 	private Random secureRandom;
	@TestMock 	private MessageDigest sha512Digest;

	@Test
	public void test_generateSale() {
		final int saltLen = 8;
		final Capture<byte[]> randomBytes = new Capture<byte[]>();

		secureRandom.nextBytes(capture(randomBytes));
		expectLastCall().andAnswer(new IAnswer() {
			@Override
			public Object answer() throws Throwable {
				byte[] buff = randomBytes.getValue();

				assertEquals(saltLen, buff.length);
				for (int i = 1; i < saltLen; i++) {
					buff[i] = (byte) i;
				}
				return null;
			}
		});
		replay(sha512Digest, secureRandom);

		assertArrayEquals("0001020304050607".toCharArray(), securityService.generateSalt(saltLen));
		verify(sha512Digest, secureRandom);
	}

	@Test
	public void test_hashSaltedPassword() {
		final String pass = "hat";
		final String salt = "cat";

		final Capture<byte[]> saltPass = new Capture<byte[]>();
		expect(sha512Digest.digest(capture(saltPass))).andAnswer(new IAnswer<byte[]>() {
			@Override
			public byte[] answer() throws Throwable {
				assertArrayEquals((pass + salt).getBytes(), saltPass.getValue());
				return new byte[] {0x0, 0x1, 0x2, 0x3};
			}
		});

		replay(sha512Digest, secureRandom);

		assertArrayEquals("00010203".toCharArray(), securityService.hashSaltedPassword(pass, salt.toCharArray()));
		verify(sha512Digest, secureRandom);
	}

}
