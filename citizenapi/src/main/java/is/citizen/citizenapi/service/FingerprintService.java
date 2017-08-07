package is.citizen.citizenapi.service;


import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.CertificateException;

import is.citizen.citizenapi.exception.CryptoException;
import is.citizen.citizenapi.exception.FingerprintException;
import is.citizen.citizenapi.util.Constant;

public class FingerprintService {
    private static final String TAG = FingerprintService.class.getSimpleName();

    private Context mContext;
    private CryptoService cryptoService;

    private FingerprintManagerCompat mFingerprintManager;
    private KeyguardManager mKeyguardManager;

    public FingerprintService(Context mContext, CryptoService cryptoService) {
		this.mContext = mContext;
		this.cryptoService = cryptoService;

        mFingerprintManager = FingerprintManagerCompat.from(mContext);
        mKeyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
	}

	public FingerprintManagerCompat getFingerprintManager() {
        return mFingerprintManager;
    }

	public boolean hasKeyguardSecure() {
        if (mKeyguardManager.isKeyguardSecure()) {
            return true;
        }

        return false;
    }

    public boolean hasFingerprintHardware() {
        if (mFingerprintManager != null && mFingerprintManager.isHardwareDetected()) {
            return true;
        }

        return false;
    }

    public boolean hasEnrolledFingerprints() {
        if (mFingerprintManager != null && mFingerprintManager.isHardwareDetected() && mFingerprintManager.hasEnrolledFingerprints()) {
            return true;
        }

        return false;
    }

    public Signature getFingerprintSignature()
        throws FingerprintException
    {
        return getFingerprintSignature(Constant.CITIZEN_FINGERPRINT_AUTH_KEY);
    }

    public Signature getFingerprintSignature(String keyName)
        throws FingerprintException
    {
        KeyStore mKeyStore;

        Signature signature = null;
        PrivateKey key = null;

        try {
            mKeyStore = cryptoService.getKeyStore();
            mKeyStore.load(null);
            key = cryptoService.getPrivateKey(keyName);
        } catch (CryptoException | CertificateException |
                NoSuchAlgorithmException | IOException e)
        {
            throw new FingerprintException(e.getMessage());
        }

        if (key != null) {
            try {
                signature = cryptoService.initSignature(key);
            } catch (CryptoException e) {
                throw new FingerprintException(e.getMessage());
            }
        }

        return signature;
    }

    public String createFingerprintKeyPair()
        throws FingerprintException
    {
        return createFingerprintKeyPair(Constant.CITIZEN_FINGERPRINT_AUTH_KEY);
    }

    public String createFingerprintKeyPair(String keyName)
        throws FingerprintException
    {
        try {
            cryptoService.createKeyPair(keyName, true);
            return cryptoService.getEncodedPublicKey(keyName);
        } catch (CryptoException e) {
            throw new FingerprintException(e.getMessage());
        }
    }
}
