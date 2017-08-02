package is.citizen.citizenapi.service;

import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Years;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import is.citizen.citizenapi.enums.AccessType;
import is.citizen.citizenapi.enums.PropertyType;
import is.citizen.citizenapi.enums.TokenStatus;
import is.citizen.citizenapi.exception.CryptoException;
import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.TokenNotFoundException;
import is.citizen.citizenapi.exception.UserNotFoundException;
import is.citizen.citizenapi.resource.CanIssueToken;
import is.citizen.citizenapi.resource.Token;
import is.citizen.citizenapi.resource.WebLoginPhoneParameters;
import is.citizen.citizenapi.util.Constant;
import is.citizen.citizenapi.util.RestClient;
import is.citizen.citizenapi.enums.TokenDurationType;
import is.citizen.citizenapi.enums.TokenSignatureVerificationResult;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.resource.TokenWrapper;


public class TokenService
{
    private static final String TAG = TokenService.class.getSimpleName();
    private static final RestClient restClient = new RestClient();
    private CryptoService cryptoService;

    List<Token> tokens = null;
    List<Token> tokensIssued = null;


    public TokenService(CryptoService cryptoService) {
		this.cryptoService = cryptoService;
	}


    public List<Token> getTokensRequested(String tokenSort, String apiKey, String mnemonic)
            throws TokenNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);
        restClient.setXcode(mnemonic);

        try {
            Map map = new HashMap();
            map.put(Constant.CITIZEN_SORT, tokenSort);
            TokenWrapper wrapper = restClient.get(Constant.CITIZEN_TOKEN_RESOURCE + "/user", map, TokenWrapper.class);
            tokens = new ArrayList<>(wrapper.getTokens());

            tokens = sort(tokens, tokenSort);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new TokenNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return tokens;
    }


    public List<Token> getTokensIssued(String tokenSort, String apiKey, String mnemonic)
            throws TokenNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);
        restClient.setXcode(mnemonic);

        try {
            Map map = new HashMap();
            map.put(Constant.CITIZEN_SORT, tokenSort);

            String uri = Constant.CITIZEN_TOKEN_RESOURCE + "/requester";

            TokenWrapper wrapper = restClient.get(uri, null, TokenWrapper.class);
            tokensIssued = new ArrayList<>(wrapper.getTokens());

            tokensIssued = sort(tokensIssued, tokenSort);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new TokenNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return tokensIssued;
    }


    public List<Token> getTokensIssuedByStatus(TokenStatus tokenStatus, String apiKey, String mnemonic)
            throws TokenNotFoundException, UnauthorisedException, HttpException
    {
        List tokensIssuedByType;

        restClient.setServiceKey(apiKey);
        restClient.setXcode(mnemonic);

        try {
            String uri = Constant.CITIZEN_TOKEN_RESOURCE + "/sent";
            if (tokenStatus == null)
                uri = uri.concat("/all");
            else
                uri = uri.concat("/" + tokenStatus.name());

            TokenWrapper wrapper = restClient.get(uri, null, TokenWrapper.class);
            tokensIssuedByType = new ArrayList<>(wrapper.getTokens());

            tokensIssuedByType = sort(tokensIssuedByType, Constant.CITIZEN_SORT_DATE);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new TokenNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }


        return tokensIssuedByType;
    }


    public Token getToken(String tokenId, String apiKey, String mnemonic)
            throws TokenNotFoundException, UnauthorisedException, HttpException
    {
        Token token;

        restClient.setServiceKey(apiKey);
        restClient.setXcode(mnemonic);

        try {
            token = restClient.get(Constant.CITIZEN_TOKEN_RESOURCE + "/" + tokenId, null, Token.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new TokenNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return token;
    }


    public boolean deleteToken(String tokenId, String apiKey)
            throws TokenNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);

        try {
            restClient.delete(Constant.CITIZEN_TOKEN_RESOURCE + "/" + tokenId, null, Void.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new TokenNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return true;
    }


    public Token grantToken(Token token, String apiKey, String mnemonic)
            throws TokenNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);
        restClient.setXcode(mnemonic);

        try {
            String resourcePath = Constant.CITIZEN_TOKEN_RESOURCE + "/" + token.getId() + "/GRANTED";
            addPropertiesForWebLoginRequest(token, mnemonic);
            token = restClient.put(resourcePath, token.getMetaData(), Token.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new TokenNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return token;
    }


    public CanIssueToken canIssue(Token token, String apiKey)
            throws TokenNotFoundException, UnauthorisedException, HttpException
    {
        CanIssueToken canIssueToken;

        restClient.setServiceKey(apiKey);

        try {
            String resourcePath = Constant.CITIZEN_TOKEN_RESOURCE + "/" + token.getId() + "/canissue";
            canIssueToken = restClient.get(resourcePath, null, CanIssueToken.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new TokenNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return canIssueToken;
    }


    public Token declineToken(Token token, String apiKey, String mnemonic)
            throws TokenNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);
        restClient.setXcode(mnemonic);

        try {
            String resourcePath = Constant.CITIZEN_TOKEN_RESOURCE + "/" + token.getId() + "/DECLINED";
            token = restClient.put(resourcePath, null, Token.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new TokenNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return token;
    }


    public Token issueToken(Token token, String apiKey, String mnemonic)
            throws TokenNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);
        restClient.setXcode(mnemonic);

        try {
            if (token.getDurationType() == null) {
                token.setDurationType(TokenDurationType.PERPETUAL);
            }

            token = restClient.post(Constant.CITIZEN_TOKEN_RESOURCE, token, Token.class);

        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new TokenNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return token;
    }


    public Token signToken(Token token, Signature signature)
            throws CryptoException
    {
        try {
            signature.update(token.getId().getBytes());
            byte[] signedTokenId = signature.sign();
            if (token.getMetaData() == null) {
                token.setMetaData(new HashMap());
            }
            token.getMetaData().put(PropertyType.SIGNED_TOKEN_ID, Base64.encodeToString(signedTokenId, Base64.NO_WRAP));
        } catch (SignatureException | NullPointerException e) {
            throw new CryptoException(e.getMessage());
        }

        return token;
    }


    public Token verifyTokenSignature(Token token)
            throws UserNotFoundException, UnauthorisedException, HttpException
    {
        String tokenSignature = (String) token.getMetaData().get(PropertyType.SIGNED_TOKEN_ID.name());

        if (tokenSignature == null || tokenSignature.length() == 0) {
            token.getMetaData().put(PropertyType.TOKEN_SIGNATURE_VERIFICATION_RESULT.toString(),
                    TokenSignatureVerificationResult.UNABLE_TO_RUN_VERIFICATION.toString());
        }

        try {
            String resourcePath = Constant.CITIZEN_USER_RESOURCE + "/" + token.getHashedUserEmail() + "/devicePublicKey";
            String devicePublicKey = restClient.get(resourcePath, null, String.class);

            boolean result = cryptoService.verifyECSignature(Base64.encodeToString(token.getId().getBytes(), Base64.NO_WRAP), tokenSignature, devicePublicKey);

            if (result) {
                token.getMetaData().put(PropertyType.TOKEN_SIGNATURE_VERIFICATION_RESULT.toString(),
                        TokenSignatureVerificationResult.VERIFICATION_SUCCEEDED.toString());
                token.getMetaData().put(PropertyType.TOKEN_SIGNATURE_PUBLIC_KEY.toString(),
                        devicePublicKey);
            } else {
                token.getMetaData().put(PropertyType.TOKEN_SIGNATURE_VERIFICATION_RESULT.toString(),
                        TokenSignatureVerificationResult.VERIFICATION_FAILED.toString());
            }

        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }

        } catch (CryptoException e) {
            token.getMetaData().put(PropertyType.TOKEN_SIGNATURE_VERIFICATION_RESULT.toString(),
                    TokenSignatureVerificationResult.UNABLE_TO_RUN_VERIFICATION.toString());
            Log.e(TAG, "Unable to verify token signature: " + e.getMessage());

        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return token;
    }


    public boolean addPropertiesForWebLoginRequest(Token token, String mnemonic) {
        if (AccessType.contains(token.getAccess(), AccessType.WEB_ACCESS)) {
            Log.d(TAG, "Granting Web Token");
            try {
                String browserECDHPublicKey = null;
                String serviceECDHPublicKey = null;

                if (token.getMetaData() != null) {
                    browserECDHPublicKey = (String) token.getMetaData().get(PropertyType.WEB_ACCESS_BROWSER_ECDH_PUB_KEY.toString());
                    serviceECDHPublicKey = (String) token.getMetaData().get(PropertyType.WEB_ACCESS_SERVICE_ECDH_PUB_KEY.toString());
                }

                if (browserECDHPublicKey != null && serviceECDHPublicKey != null) {
                    Log.d(TAG, "Generating web login parameters");

                    WebLoginPhoneParameters phoneLoginParameters = cryptoService.encryptPassphraseForWebLogin(mnemonic, browserECDHPublicKey, serviceECDHPublicKey);

                    token.getMetaData().put(PropertyType.WEB_ACCESS_BROWSER_PASSPHRASE_CIPHER_IV.toString(), phoneLoginParameters.getBrowserCipherIv());
                    token.getMetaData().put(PropertyType.WEB_ACCESS_SERVICE_PASSPHRASE_CIPHER_IV.toString(), phoneLoginParameters.getServiceCipherIv());
                    token.getMetaData().put(PropertyType.WEB_ACCESS_SERVICE_PASSPHRASE_CIPHER.toString(), phoneLoginParameters.getServiceCipher());
                    token.getMetaData().put(PropertyType.WEB_ACCESS_PHONE_ECDH_PUB_KEY.toString(), phoneLoginParameters.getPhoneECDHPublicKey());

                    Log.d(TAG, "Web login parameters set.");
                } else {
                    Log.d(TAG, "OAuth token login.");
                }
                return true;
            } catch (CryptoException e) {
                Log.e(TAG, "Unable to grant web token: " + e.getMessage());
            }
        }
        return false;
    }


    public List<Token> updateList(List<Token> tokens, Token tokenn) {
        ArrayList<Token> tokensNew = new ArrayList<>();
        ListIterator<Token> i = tokens.listIterator();
        boolean found = false;
        while (i.hasNext()) {
            Token token = i.next();
            if (token.getId().equals(tokenn.getId())) // replace
            {
                tokensNew.add(tokenn);
                found = true;
            } else
                tokensNew.add(token); // add existing
        }

        if (!found) // not found in list so doesn't exist
            tokensNew.add(tokenn);

        return tokensNew;
    }


    public List<Token> sort(List<Token> list, String sortMode) {
        if ((list != null) && (sortMode.equals(Constant.CITIZEN_SORT_DATE))) {
            Collections.sort(list, new Comparator<Token>() {
                @Override
                public int compare(Token lhs, Token rhs) {
                    DateTime leftDate = lhs.getCreationDate();
                    DateTime rightDate = rhs.getCreationDate();

                    if ((leftDate != null) && (rightDate != null)) {
                        return
                                leftDate.isBefore(rightDate) ? 1 :
                                        leftDate.isAfter(rightDate) ? -1 : 0;
                    } else return 0;
                }
            });
        } else if ((list != null) && (sortMode.equals(Constant.CITIZEN_SORT_STATUS))) {
            // sort by date
            Collections.sort(list, new Comparator<Token>() {
                @Override
                public int compare(Token lhs, Token rhs) {
                    TokenStatus leftStatus = lhs.getTokenStatus();
                    TokenStatus rightStatus = rhs.getTokenStatus();
                    return leftStatus.compareTo(rightStatus);
                }
            });
        }
        return list;
    }


    @NonNull
    public static List getTokenDurationAsStringList(LocalDate expiry, String year, String month, String day) {// Get months
        LocalDate start = new LocalDate(DateTime.now());
        int years = Years.yearsBetween(start, expiry).getYears();
        // Subtract this number of years from the end date so we can calculate days
        expiry = expiry.minusYears(years);
        int months = Months.monthsBetween(start, expiry).getMonths();
        // Subtract this number of months from the end date so we can calculate days
        expiry = expiry.minusMonths(months);
        // Get days
        int days = Days.daysBetween(start, expiry).getDays();

        String y = null, m = null, d = null;

        List list = new ArrayList();

        if (years > 0) {
            y = years + year;
            list.add(y);
        }
        if (months > 0) {
            m = months + month;
            list.add(m);
        }
        if (days > 0) {
            d = days + day;
            list.add(d);
        }
        return list;
    }
}
