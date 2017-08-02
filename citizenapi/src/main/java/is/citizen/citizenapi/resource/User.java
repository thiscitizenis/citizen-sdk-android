package is.citizen.citizenapi.resource;

import com.fasterxml.jackson.annotation.JsonView;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

//TODO: Write tests for different views
public final class User implements Serializable {

    private static final long serialVersionUID = -2462246955420639790L;

    @JsonView({CitizenView.User.Register.class, CitizenView.User.Login.class})
    private String username;

    @JsonView({CitizenView.User.Register.class, CitizenView.User.Login.class})
    private String namespace;

    @JsonView({CitizenView.User.Register.class, CitizenView.User.Login.class})
    private String id;

    @JsonView({CitizenView.User.FixMe.class})
    private String password;

    @JsonView({CitizenView.User.FixMe.class})
    private String passPhrase;

    @JsonView({CitizenView.User.Register.class, CitizenView.User.Login.class})
    private boolean passwordTemporary;

    @JsonView({CitizenView.User.Register.class, CitizenView.User.Login.class, CitizenView.User.Verify.class})
    private String authPublicKey;

    @JsonView(CitizenView.User.Register.class)
    private String mnemonicCode;

    @JsonView({CitizenView.User.Register.class, CitizenView.User.Login.class})
    private String publicKey;

    @JsonView({CitizenView.User.Register.class, CitizenView.User.Login.class})
    private String apiKey;

    @JsonView({CitizenView.User.Login.class, CitizenView.User.Register.class})
    private String personId;

    @JsonView({CitizenView.User.Login.class, CitizenView.User.Register.class})
    private String notificationsToken;

    @JsonView({CitizenView.User.Login.class, CitizenView.User.Register.class})
    private List<Email> emails;

    @JsonView({CitizenView.User.Login.class, CitizenView.User.Register.class})
    private String entityEmail;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNamespace() { return namespace; }

    public void setNamespace(String namespace) { this.namespace = namespace; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassPhrase() {
        return passPhrase;
    }

    public void setPassPhrase(String passPhrase) {
        this.passPhrase = passPhrase;
    }

    public boolean getPasswordTemporary() { return passwordTemporary; }

    public void setPasswordTemporary(boolean passwordTemporary) { this.passwordTemporary = passwordTemporary; }

    public String getPublicKey() { return publicKey; }

    public String getAuthPublicKey() { return authPublicKey; }

    public void setAuthPublicKey(String authPublicKey) { this.authPublicKey = authPublicKey; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMnemonicCode() {
        return mnemonicCode;
    }

    public void setMnemonicCode(String mnemonicCode) {
        this.mnemonicCode = mnemonicCode;
    }

    public String getNotificationsToken() {
        return notificationsToken;
    }

    public void setNotificationsToken(String notificationsToken) {
        this.notificationsToken = notificationsToken;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public String getEntityEmail() {
        return entityEmail;
    }

    public void setEntityEmail(String entityEmail) {
        this.entityEmail = entityEmail;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, id, apiKey, personId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.username, other.username) &&
               Objects.equals(this.password, other.password) &&
               Objects.equals(this.apiKey, other.apiKey) &&
               Objects.equals(this.personId, other.personId) &&
               Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "password", "mnemonicCode");
    }
}
