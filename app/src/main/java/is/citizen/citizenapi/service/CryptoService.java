package is.citizen.citizenapi.service;


import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jce.spec.ECNamedCurveParameterSpec;
import org.spongycastle.jce.spec.ECNamedCurveSpec;

import is.citizen.citizenapi.exception.CryptoException;
import is.citizen.citizenapi.resource.WebLoginPhoneParameters;

public class CryptoService {

	private static final String REST_AUTH_KEYSTORE_NAME = "AndroidKeyStore";
	private static final String REST_AUTH_SIGNATURE_ALGORITHM = "SHA256withECDSA";
    private static final String REST_AUTH_EC_CURVE = "secp256r1";

	private static final String BROWSER_AUTH_SECURITY_PROVIDER = "SC";
	private static final String BROWSER_AUTH_EC_CURVE = "P-384";
    private static final ECNamedCurveParameterSpec BROWSER_AUTH_EC_NC_SPEC = ECNamedCurveTable.getParameterSpec(BROWSER_AUTH_EC_CURVE);

    private static final ECParameterSpec BROWSER_AUTH_EC_SPEC = new ECNamedCurveSpec(BROWSER_AUTH_EC_CURVE,
                                                                                     BROWSER_AUTH_EC_NC_SPEC.getCurve(),
                                                                                     BROWSER_AUTH_EC_NC_SPEC.getG(),
                                                                                     BROWSER_AUTH_EC_NC_SPEC.getN(),
                                                                                     BROWSER_AUTH_EC_NC_SPEC.getH(),
                                                                                     BROWSER_AUTH_EC_NC_SPEC.getSeed());


    @TargetApi(23)
    public KeyPair createKeyPair(String keyName, boolean invalidatedByBiometricEnrollment) throws CryptoException
	{
		KeyPair keyPair = null;

		try {
			KeyStore mKeyStore = KeyStore.getInstance(REST_AUTH_KEYSTORE_NAME);
			mKeyStore.load(null);

			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
					KeyProperties.KEY_ALGORITHM_EC, REST_AUTH_KEYSTORE_NAME);


			KeyGenParameterSpec.Builder builder
					= new KeyGenParameterSpec.Builder(keyName,
					                                  KeyProperties.PURPOSE_ENCRYPT |
							                          KeyProperties.PURPOSE_DECRYPT |
							                          KeyProperties.PURPOSE_SIGN)
					.setDigests(KeyProperties.DIGEST_SHA256)
					.setAlgorithmParameterSpec(new ECGenParameterSpec(REST_AUTH_EC_CURVE))
					.setUserAuthenticationRequired(true);

            // This call is only available on API level >= 24.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
			}

			keyPairGenerator.initialize(builder.build());
			keyPair = keyPairGenerator.generateKeyPair();

		} catch (NoSuchAlgorithmException | NoSuchProviderException | KeyStoreException |
			   InvalidAlgorithmParameterException | CertificateException | IOException e)
		{
			throw new CryptoException(e.getMessage());
		}

		return keyPair;
	}


	public PublicKey getPublicKey(String keyName) throws CryptoException
	{
		PublicKey publicKey = null;

		try {
			KeyStore mKeyStore = KeyStore.getInstance(REST_AUTH_KEYSTORE_NAME);
			mKeyStore.load(null);

			Certificate certificate = mKeyStore.getCertificate(keyName);
			if (certificate != null)
				publicKey = certificate.getPublicKey();
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new CryptoException(e.getMessage());
		}

		return publicKey;
	}


	public String getEncodedPublicKey(String keyName) throws CryptoException
	{
		PublicKey publicKey = null;

		try {
			KeyStore mKeyStore = KeyStore.getInstance(REST_AUTH_KEYSTORE_NAME);
			mKeyStore.load(null);

			Certificate certificate = mKeyStore.getCertificate(keyName);
			if (certificate != null)
				publicKey = certificate.getPublicKey();
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new CryptoException(e.getMessage());
		}

		if (publicKey != null) {
			return Base64.encodeToString(publicKey.getEncoded(), Base64.NO_WRAP);
		}

		return null;
	}


	public PrivateKey getPrivateKey(String keyName) throws CryptoException
	{
		PrivateKey key = null;

		try {
			KeyStore mKeyStore = KeyStore.getInstance(REST_AUTH_KEYSTORE_NAME);
			mKeyStore.load(null);

			key = (PrivateKey) mKeyStore.getKey(keyName, null);

		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException |
		    	UnrecoverableKeyException | IOException e)
		{
			throw new CryptoException(e.getMessage());
		}

		return key;
	}


	public Signature initSignature(PrivateKey privateKey) throws CryptoException
	{
		Signature signature = null;

		try {
			signature = Signature.getInstance(REST_AUTH_SIGNATURE_ALGORITHM);
			signature.initSign(privateKey);
		} catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new CryptoException(e.getMessage());
		}

		return signature;
	}


	public String sign(byte[] input, Signature signature) throws CryptoException
	{
		String result = null;

		try {
			signature.update(input);
			byte[] signedText = signature.sign();
            result = Base64.encodeToString(signedText, Base64.NO_WRAP);
		} catch (IllegalArgumentException | SignatureException e) {
            throw new CryptoException(e.getMessage());
		}

		return result;
	}


	public KeyStore getKeyStore() throws CryptoException
	{
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance(REST_AUTH_KEYSTORE_NAME);
		} catch (KeyStoreException e) {
			throw new CryptoException(e.getMessage());
		}

		return keyStore;
	}


	public static boolean verifyECSignature(String encodededData, String encodedSignedData, String encodedPublicKey) throws CryptoException
    {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decode(encodedPublicKey, Base64.DEFAULT));
            ECPublicKey publicKey = (ECPublicKey) keyFactory.generatePublic(publicKeySpec);

            Signature verificationFunction = Signature.getInstance(REST_AUTH_SIGNATURE_ALGORITHM);
            verificationFunction.initVerify(publicKey);
            verificationFunction.update(Base64.decode(encodededData, Base64.DEFAULT));

             if (verificationFunction.verify(Base64.decode(encodedSignedData, Base64.DEFAULT))) {
                return true;
             }

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException |
                 SignatureException e)
        {
            throw new CryptoException("Unable to verify signature: " + e.getMessage());
        }

        return false;
    }



    public WebLoginPhoneParameters encryptPassphraseForWebLogin(String passphrase, String browserECDHPhonePublicKey, String serviceECDHPublicKey)
            throws CryptoException
    {
        WebLoginPhoneParameters webLoginPhoneParameters = new WebLoginPhoneParameters();

        try {
            // Add SC security provider.
			Security.addProvider(new BouncyCastleProvider());

            // Initialise a key pair generator.
			KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDH", BROWSER_AUTH_SECURITY_PROVIDER);
			generator.initialize(BROWSER_AUTH_EC_SPEC, new SecureRandom());

            // Generate an ECDH pair for the phone.

            KeyPair phoneKeyPair = generator.generateKeyPair();
            ECPrivateKey phonePrivateKey = (ECPrivateKey) phoneKeyPair.getPrivate();
            ECPublicKey phonePublicKey = (ECPublicKey) phoneKeyPair.getPublic();

            // Import the browser and service ECDH public keys.

            ECPublicKey browserPublicKey = importBrowserAuthPublicKey(browserECDHPhonePublicKey);
            ECPublicKey servicePublicKey = importBrowserAuthPublicKey(serviceECDHPublicKey);

            // Determine the ECDH shared secret between the browser and the phone.

            KeyAgreement browserECDH = KeyAgreement.getInstance("ECDH");
            browserECDH.init(phonePrivateKey);
            browserECDH.doPhase(browserPublicKey, true);

            BigInteger browserSharedSecret = new BigInteger(1, browserECDH.generateSecret());

            // Generate an AES key from the browser-phone shared secret.

            MessageDigest browserDigest = MessageDigest.getInstance("SHA-256");
            byte[] browserAESKeyBytes = browserDigest.digest(browserSharedSecret.toString(16).getBytes());
            SecretKeySpec browserAESKey = new SecretKeySpec(browserAESKeyBytes, "AES");

            // Generate an IV for the browser-phone AES key.

            byte[] browserIVBytes = new byte[16];
            new SecureRandom().nextBytes(browserIVBytes);
            IvParameterSpec browserIV = new IvParameterSpec(browserIVBytes);

            // Encrypt the passphrase with the browser-phone AES key and IV.

            Cipher browserCipherGenerator = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            browserCipherGenerator.init(Cipher.ENCRYPT_MODE, browserAESKey, browserIV);

            byte[] browserCipher = browserCipherGenerator.doFinal(passphrase.getBytes());

            // Determine the ECDH shared secret between the Citizen service and the phone.

            KeyAgreement serviceECDH = KeyAgreement.getInstance("ECDH");
            serviceECDH.init(phonePrivateKey);
            serviceECDH.doPhase(servicePublicKey, true);

            BigInteger serviceSharedSecret = new BigInteger(1, serviceECDH.generateSecret());

            // Generate an AES key from the service-phone shared secret.

            MessageDigest serviceDigest = MessageDigest.getInstance("SHA-256");
            byte[] serviceAESKeyBytes = browserDigest.digest(serviceSharedSecret.toString(16).getBytes());
            SecretKeySpec serviceAESKey = new SecretKeySpec(serviceAESKeyBytes, "AES");

            // Generate an IV for the service-phone AES key.

            byte[] serviceIVBytes = new byte[16];
            new SecureRandom().nextBytes(serviceIVBytes);
            IvParameterSpec serviceIV = new IvParameterSpec(serviceIVBytes);

            // Encrypt the browser cipher with the service-phone AES key and IV.

            Cipher serviceCipherGenerator = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            serviceCipherGenerator.init(Cipher.ENCRYPT_MODE, serviceAESKey, serviceIV);

            byte[] serviceCipher = serviceCipherGenerator.doFinal(browserCipher);

            // Encode the service-phone cipher and the IVs of both ciphers as strings.

            String browserIVString = Base64.encodeToString(browserIVBytes, Base64.NO_WRAP);
            String serviceIVString = Base64.encodeToString(serviceIVBytes, Base64.NO_WRAP);
            String serviceCipherString = Base64.encodeToString(serviceCipher, Base64.NO_WRAP);

            // Export the phone ECDH public key as hex X, Y coordinates.

            String phonePublicKeyString = exportBrowserAuthPublicKey(phonePublicKey);

            // Set the parameters to return.

            webLoginPhoneParameters.setBrowserCipherIv(browserIVString);
            webLoginPhoneParameters.setServiceCipherIv(serviceIVString);
            webLoginPhoneParameters.setServiceCipher(serviceCipherString);
            webLoginPhoneParameters.setPhoneECDHPublicKey(phonePublicKeyString);

        } catch (NoSuchProviderException | InvalidAlgorithmParameterException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException |
                 IllegalBlockSizeException | BadPaddingException |
				 NoSuchPaddingException e)
        {
            throw new CryptoException("Error encrypting passphrase: " + e.getMessage());
        }

        return webLoginPhoneParameters;
    }


    private static ECPublicKey importBrowserAuthPublicKey(String publicKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException
    {
        int xPos = publicKey.indexOf('x');
        int yPos = publicKey.indexOf('y');

        if (xPos != 0 || yPos < 0 || xPos >= yPos - 1 || publicKey.length() <= yPos + 2) {
           throw new InvalidKeyException("Incorrectly formatted ECDH key.");
        }

        String x = publicKey.substring(publicKey.indexOf('x') + 1, publicKey.indexOf('y'));
        String y = publicKey.substring(publicKey.indexOf('y') + 1);

        KeyFactory kf = KeyFactory.getInstance("EC");

        ECPoint point = new ECPoint(new BigInteger(x, 16), new BigInteger(y, 16));

        ECPublicKey convertedPubKey = (ECPublicKey) kf.generatePublic(new ECPublicKeySpec(point, BROWSER_AUTH_EC_SPEC));

        return convertedPubKey;
    }


    private static String exportBrowserAuthPublicKey(ECPublicKey publicKey) {
        return "x" + publicKey.getW().getAffineX().toString(16) + "y" + publicKey.getW().getAffineY().toString(16);
    }
}
