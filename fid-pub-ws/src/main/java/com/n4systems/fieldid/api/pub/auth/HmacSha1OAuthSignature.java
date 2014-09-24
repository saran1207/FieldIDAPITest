package com.n4systems.fieldid.api.pub.auth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by kirillternovsky on 2014-09-23.
 */
public class HmacSha1OAuthSignature implements OAuthSignature {
    private Mac calculator;
    private Base64.Encoder encoder;
    public HmacSha1OAuthSignature() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        calculator = Mac.getInstance("HmacSHA1");
        encoder = Base64.getEncoder();
    }

    @Override
    public String sign(OAuthRequestParams requestParams, OAuthSecrets secrets) {
        String signatureString = secrets.getSigningString();
        try
        {
            SecretKeySpec keySpec = new SecretKeySpec(signatureString.getBytes("UTF-8"), "HmacSHA1");
            calculator.init(keySpec);
            String sigString = requestParams.getSignatureString();

            return OAuthEncoder.encode(new String(encoder.encode(calculator.doFinal(sigString.getBytes("UTF-8")))));
        }
        catch(UnsupportedEncodingException | InvalidKeyException ignored) {} // there's no way UTF-8 is unsupported

        return "";

    }

    @Override
    public boolean verify(OAuthRequestParams requestParams, OAuthSecrets secrets) {
        return sign(requestParams, secrets).equals(OAuthEncoder.encode(requestParams.getOAuthParams().signature));
    }
}
