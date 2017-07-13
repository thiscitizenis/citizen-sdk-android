package is.citizen.citizenapi.resource;

import java.io.Serializable;

public class WebLoginPhoneParameters implements Serializable {
    private static final long serialVersionUID = -2193198474233515021L;

    private String serviceCipher;
    private String serviceCipherIv;
    private String browserCipherIv;
    private String phoneECDHPublicKey;

    public String getServiceCipher() {
        return serviceCipher;
    }

    public void setServiceCipher(String serviceCipher) {
        this.serviceCipher = serviceCipher;
    }

    public String getServiceCipherIv() {
        return serviceCipherIv;
    }

    public void setServiceCipherIv(String serviceCipherIv) {
        this.serviceCipherIv = serviceCipherIv;
    }

    public String getBrowserCipherIv() {
        return browserCipherIv;
    }

    public void setBrowserCipherIv(String browserCipherIv) {
        this.browserCipherIv = browserCipherIv;
    }

    public String getPhoneECDHPublicKey() {
        return phoneECDHPublicKey;
    }

    public void setPhoneECDHPublicKey(String phoneECDHPublicKey) {
        this.phoneECDHPublicKey = phoneECDHPublicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebLoginPhoneParameters that = (WebLoginPhoneParameters) o;

        if (serviceCipher != null ? !serviceCipher.equals(that.serviceCipher) : that.serviceCipher != null)
            return false;
        if (serviceCipherIv != null ? !serviceCipherIv.equals(that.serviceCipherIv) : that.serviceCipherIv != null)
            return false;
        if (browserCipherIv != null ? !browserCipherIv.equals(that.browserCipherIv) : that.browserCipherIv != null)
            return false;
        return phoneECDHPublicKey != null ? phoneECDHPublicKey.equals(that.phoneECDHPublicKey) : that.phoneECDHPublicKey == null;
    }

    @Override
    public int hashCode() {
        int result = serviceCipher != null ? serviceCipher.hashCode() : 0;
        result = 31 * result + (serviceCipherIv != null ? serviceCipherIv.hashCode() : 0);
        result = 31 * result + (browserCipherIv != null ? browserCipherIv.hashCode() : 0);
        result = 31 * result + (phoneECDHPublicKey != null ? phoneECDHPublicKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WebLoginPhoneParameters{" +
                "serviceCipher='" + serviceCipher + '\'' +
                ", serviceCipherIv='" + serviceCipherIv + '\'' +
                ", browserCipherIv='" + browserCipherIv + '\'' +
                ", phoneECDHPublicKey='" + phoneECDHPublicKey + '\'' +
                '}';
    }
}
