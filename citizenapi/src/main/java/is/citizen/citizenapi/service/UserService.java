package is.citizen.citizenapi.service;

import android.util.Base64;

import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Signature;
import java.security.SignatureException;
import java.util.HashMap;

import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.UserNotFoundException;
import is.citizen.citizenapi.exception.CryptoException;
import is.citizen.citizenapi.exception.DuplicateUserException;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.resource.LoginTransaction;
import is.citizen.citizenapi.resource.User;
import is.citizen.citizenapi.util.Constant;
import is.citizen.citizenapi.util.RestClient;

public class UserService {
    private static final String TAG = UserService.class.getSimpleName();
    private static final RestClient restClient = new RestClient();


    public User createUser(String username, String password, String passPhrase, String authPublicKey)
            throws HttpException, UnauthorisedException, DuplicateUserException
    {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPassPhrase(passPhrase);

        if (authPublicKey != null) {
            user.setAuthPublicKey(authPublicKey);
        }

        try {
            user = restClient.postWithoutAuthorization(Constant.CITIZEN_USER_RESOURCE, user, User.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.CONFLICT)) {
                throw new DuplicateUserException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return user;
    }


    public User loginUserPass(String username, String password)
            throws HttpException, UnauthorisedException, UserNotFoundException
    {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        try {
            user = restClient.postWithoutAuthorization(Constant.CITIZEN_SESSION_RESOURCE, user, User.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return user;
    }


    public String getMnemonic(User user, String apiKey)
            throws HttpException, UnauthorisedException, UserNotFoundException
    {
        restClient.setServiceKey(apiKey);

        String mnemonic = null;

        try {
            mnemonic = restClient.post(Constant.CITIZEN_SESSION_RESOURCE + "mnemonic", user, String.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return mnemonic;
    }


    public User enrollAuthPublicKey(String userId, String authPublicKey, String apiKey)
            throws HttpException, UnauthorisedException, UserNotFoundException
    {
        restClient.setServiceKey(apiKey);

        User user = null;

        try {
            user = restClient.post(Constant.CITIZEN_USER_RESOURCE + "/" + userId + "/publicKey", new TextNode(authPublicKey), User.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return user;
    }


    public User loginWithSignedTransaction(String username, Signature signature)
        throws HttpException, UnauthorisedException, UserNotFoundException, CryptoException
    {
        User user = null;
        String nonce = null;

        try {
            nonce = restClient.getWithoutAuthorization(Constant.CITIZEN_SESSION_RESOURCE + "/getLoginNonce", new HashMap(), String.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        LoginTransaction loginTransaction = new LoginTransaction(username, nonce);

        String encodedTransactionSignature = null;

        try {
            signature.update(loginTransaction.toByteArray());
            byte[] transactionSignature = signature.sign();
            encodedTransactionSignature = Base64.encodeToString(transactionSignature, Base64.NO_WRAP);
        } catch (SignatureException e) {
            throw new CryptoException(e.getMessage());
        }

        restClient.setSignature(encodedTransactionSignature);

        try {
            user = restClient.post(Constant.CITIZEN_SESSION_RESOURCE + "/auth", loginTransaction, User.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(e.getMessage());
            } else if (code.equals(HttpStatus.UNPROCESSABLE_ENTITY)) {
                throw new CryptoException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return user;
    }


    public User updateNotificationsToken(User user, String notificationsToken, String apiKey)
            throws UserNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);

        try {
            user = restClient.post(Constant.CITIZEN_USER_RESOURCE + "/" + user.getId() + "/notificationsToken", new TextNode(notificationsToken), User.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return user;
    }
}
