package is.citizen.citizenapi.util;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.DeserializationFeature;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import is.citizen.citizenapi.exception.UnauthorisedException;

/**
 * Simple Rest client
 */
public class RestClient {

    private static final String AUTH_HEADER_NAME = "AuthorizationCitizen";
    private static final String XCODE_HEADER_NAME = "X-code";
    private static final String XSIGNATURE_HEADER_NAME = "X-signature";

    public static final String BASE_URL = Constant.CITIZEN_API_URL;

    private static RestClient INSTANCE;

    private String serviceKey;
    private String xCode;
    private String xSignature;

    private RestTemplate mRestTemplate;

    public RestClient() {
        mRestTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.getObjectMapper().disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        mRestTemplate.getMessageConverters().add(messageConverter);
    }

    public RestTemplate getRestTemplate() {
        return mRestTemplate;
    }

    public static RestClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RestClient();
        }

        return INSTANCE;
    }

    // API key
    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    // Mnemonic
    public void setXcode(String xCode) {
        this.xCode = xCode;
    }

    // Login transaction signature
    public void setSignature(String xSignature) {
        this.xSignature = xSignature;
    }


    /**
     * Simple get on url address
     *
     * @param url
     * @param responseType
     * @return
     */
    public <T> T getUrl(String url, final Class<T> responseType) {
        HttpHeaders httpHeaders = createHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
        T body = getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
        return body;
    }


    /**
     * Get request with Citizen API headers.
     *
     * @param resourcePath the location of the resource e.g. /users/123
     * @param params query string parameters
     * @param responseType the type of the return value
     * @return the response entity
     */
    public <T> T get(String resourcePath, Map params, final Class<T> responseType) {
        String requestParams = buildRequestParams(params);
        HttpHeaders httpHeaders = createHeaders();
        addApiHeaders(httpHeaders);

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        T body = getRestTemplate().exchange(BASE_URL + resourcePath + "?" + requestParams, HttpMethod.GET, httpEntity, responseType).getBody();

        return body;
    }

    /**
     * Get request without Citizen API headers
     *
     * @param resourcePath the location of the resource e.g. /users/123
     * @param request      the Object which needs to be serialized and sent as GET payload, may be null
     * @param responseType the type of the return value
     * @return the response entity
     */
    public <T> T getWithoutAuthorization(String resourcePath, Object request, final Class<T> responseType) {
        HttpHeaders httpHeaders = createHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(request, httpHeaders);
        T body = getRestTemplate().exchange(BASE_URL + resourcePath, HttpMethod.GET, httpEntity, responseType).getBody();
        return body;
    }


    /**
     * Post a request with Citizen API headers
     *
     * @param resourcePath the location of the resource e.g. /users/123
     * @param request      the Object which needs to be serialized and sent as POST payload, may be null
     * @param responseType the type of the return value
     * @return the response entity
     */
    public <T> T post(String resourcePath, Object request, final Class<T> responseType)
            throws UnauthorisedException {
        HttpHeaders httpHeaders = createHeaders();
        addApiHeaders(httpHeaders);

        HttpEntity<Object> httpEntity = new HttpEntity<>(request, httpHeaders);

        T body = getRestTemplate().exchange(BASE_URL + resourcePath, HttpMethod.POST, httpEntity, responseType).getBody();

        return body;
    }


     /**
     * Post a request without Citizen API headers
     *
     * @param resourcePath the location of the resource e.g. /users/123
     * @param request      the Object which needs to be serialized and sent as POST payload, may be null
     * @param responseType the type of the return value
     * @return the response entity
     */
    public <T> T postWithoutAuthorization(String resourcePath, Object request, final Class<T> responseType) {
        HttpHeaders httpHeaders = createHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(request, httpHeaders);
        T body = getRestTemplate().exchange(BASE_URL + resourcePath, HttpMethod.POST, httpEntity, responseType).getBody();
        return body;
    }


    /**
     * Put a request with Citizen API headers.
     *
     * @param resourcePath the location of the resource e.g. /users/123
     * @param request      the Object which needs to be serialized and sent as PUT payload, may be null
     * @param responseType the type of the return value
     * @return the response entity
     */
    public <T> T put(String resourcePath, Object request, final Class<T> responseType) {
        HttpHeaders httpHeaders = createHeaders();
        addApiHeaders(httpHeaders);

        HttpEntity<Object> httpEntity = new HttpEntity<>(request, httpHeaders);

        T body = getRestTemplate().exchange(BASE_URL + resourcePath, HttpMethod.PUT, httpEntity, responseType).getBody();

        return body;
    }


    /**
     * Put a request without Citizen API headers
     *
     * @param resourcePath the location of the resource e.g. /users/123
     * @param request      the Object which needs to be serialized and sent as PUT payload, may be null
     * @param responseType the type of the return value
     * @return the response entity
     */
    public <T> T putWithoutAuthorization(String resourcePath, Object request, final Class<T> responseType) {
        HttpHeaders httpHeaders = createHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(request, httpHeaders);
        T body = getRestTemplate().exchange(BASE_URL + resourcePath, HttpMethod.PUT, httpEntity, responseType).getBody();
        return body;
    }


    /**
     * Delete request with Citizen API headers
     *
     * @param resourcePath the location of the resource e.g. /users/123
     * @param params query string parameters
     * @param responseType the type of the return value
     * @return the response entity
     */
    public <T> T delete(String resourcePath, Map params, final Class<T> responseType) {

        String requestParams = buildRequestParams(params);
        HttpHeaders httpHeaders = createHeaders();
        addApiHeaders(httpHeaders);

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        T body = getRestTemplate().exchange(BASE_URL + resourcePath + "?" + requestParams, HttpMethod.DELETE, httpEntity, responseType).getBody();

        return body;
    }


    private HttpHeaders createHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add((MediaType.APPLICATION_JSON));
        httpHeaders.setAccept(mediaTypeList);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(HttpHeaders.CONNECTION, "Close");

        return httpHeaders;
    }


    private void addApiHeaders(HttpHeaders httpHeaders) {
        if (serviceKey != null) {
            httpHeaders.add(AUTH_HEADER_NAME, serviceKey);
        }

        if (xCode != null) {
            httpHeaders.add(XCODE_HEADER_NAME, xCode);
        }

        if (xSignature != null) {
            httpHeaders.add(XSIGNATURE_HEADER_NAME, xSignature);
        }
    }

    @NonNull
    private String buildRequestParams(Map params) {
        String requestParams = new String();
        if (params != null) {
            for (Object key : params.keySet()) {
                Object value = params.get(key);
                requestParams = requestParams + "&" + key.toString() + "=" + value.toString();
            }
            StringUtils.trimLeadingCharacter(requestParams, requestParams.charAt(0));
        }
        return requestParams;
    }
}