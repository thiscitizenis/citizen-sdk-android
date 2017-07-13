package is.citizen.citizenapi.resource;

import java.io.Serializable;
import java.util.Map;

public class BaseEncryptedAsset  implements Serializable
    {
        private static final long serialVersionUID = 8529598365947124732L;
        protected String id;

    protected Map metaData;

    protected byte[] data;
    protected String encryptedKey; //store the key ENCRYPTED to the owner - AES 256
    protected String iv; //initialisation vector for the AES key


    public String getId() {
    return id;
    }
    public void setId(String id) {
    this.id = id;
    }


    public byte[] getData()
        {
        return data;
        }

    public void setData(byte[] data)
        {
        this.data = data;
        }

    // putting this back in the DTOs so we can do local crypto
    public String getEncryptedKey() { return encryptedKey; }

    public void setEncryptedKey(String encryptedKey) { this.encryptedKey = encryptedKey; }

    public String getIv() { return iv; }

    public void setIv(String iv) { this.iv = iv; }

    public Map getMetaData() { return metaData; }

    public void setMetaData(Map metaData) { this.metaData = metaData; }
    }

