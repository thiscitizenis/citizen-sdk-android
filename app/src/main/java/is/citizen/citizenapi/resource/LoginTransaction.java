

package is.citizen.citizenapi.resource;

import com.fasterxml.jackson.annotation.JsonView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * An entity that represents a single transaction (purchase) of an item.
 */
public class LoginTransaction implements Serializable
    {
    private static final long serialVersionUID = -5857991618456373513L;

    @JsonView({CitizenView.User.Register.class, CitizenView.User.Login.class})
    private String id;

    /**
     * The unique user ID who made the transaction
     */
    private String username;

    /**
     * The random long value that will be also signed by the private key and verified in the server
     * that the same nonce can't be reused to prevent replay attacks.
     */
    private String token;


    public LoginTransaction()
        {
        }

    public LoginTransaction(String username, String token)
        {
        this.username = username;
        this.token = token;
        }


    public String getId() {
    return id;
    }

    public void setId(String id) {
    this.id = id;
    }

    public String getUsername()
        {
        return this.username;
        }

    public String getToken() { return this.token; }


    public byte[] toByteArray()
        {
            String combined = this.username + this.token;
            return combined.getBytes(Charset.forName("UTF-8"));
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o)
            {
            return true;
            }
        if (o == null || getClass() != o.getClass())
            {
            return false;
            }

        LoginTransaction that = (LoginTransaction) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(token, that.token);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(username, token);
        }
    }
