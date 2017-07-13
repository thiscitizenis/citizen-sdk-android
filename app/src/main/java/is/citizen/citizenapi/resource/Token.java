package is.citizen.citizenapi.resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;


import java.util.Objects;

import is.citizen.citizenapi.enums.AccessType;
import is.citizen.citizenapi.enums.TokenDurationType;
import is.citizen.citizenapi.enums.TokenStatus;

public class Token extends BaseEncryptedAsset {

    private static final long serialVersionUID = 4781750110446530509L;

    @JsonView({CitizenView.User.Login.class})
    private String id;

    private TokenStatus tokenStatus;

    private String hashedUserEmail;

    private String userEmail;

    private int duration;

    private int access = 0;

    private byte[] message; // encrypted message

    @Deprecated
    private byte[] image; // unencrypted image for the token, often the profile pic

    private String imageId;

    private TokenDurationType durationType;

    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime expiryDate;

    private String hashedRequesterEmail;

    private String requesterEmail;

    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime creationDate;

    // TODO  avoid of using these constants
    public final static int
            REQUESTED_AMOUNT_1 = 1,
            REQUESTED_AMOUNT_2 = 2,
            REQUESTED_AMOUNT_3 = 3,
            REQUESTED_AMOUNT_4 = 4,
            REQUESTED_AMOUNT_5 = 5;

    public static int getMaxRequestedAmountOfDocuments() {
        return REQUESTED_AMOUNT_5;
    }

    /**
     * made it static, so we will able to reuse this logic for is.citizen.ums.domain.Token if needed
     */
    public static int getRequestedAmountOfPhotoIDs(int access) {
        if (AccessType.contains(access, AccessType.PHOTO_ID_1)) {
            return REQUESTED_AMOUNT_1;
        }
        if (AccessType.contains(access, AccessType.PHOTO_ID_2)) {
            return REQUESTED_AMOUNT_2;
        }
        if (AccessType.contains(access, AccessType.PHOTO_ID_3)) {
            return REQUESTED_AMOUNT_3;
        }
        if (AccessType.contains(access, AccessType.PHOTO_ID_4)) {
            return REQUESTED_AMOUNT_4;
        }
        if (AccessType.contains(access, AccessType.PHOTO_ID_5)) {
            return REQUESTED_AMOUNT_5;
        }
        return 0;
    }

    public static int getRequestedAmountOfAddresses(int access) {
        if (AccessType.contains(access, AccessType.ADDRESS_VALID1)) {
            return REQUESTED_AMOUNT_1;
        }
        if (AccessType.contains(access, AccessType.ADDRESS_VALID2)) {
            return REQUESTED_AMOUNT_2;
        }
        if (AccessType.contains(access, AccessType.ADDRESS_VALID3)) {
            return REQUESTED_AMOUNT_3;
        }
        if (AccessType.contains(access, AccessType.ADDRESS_VALID4)) {
            return REQUESTED_AMOUNT_4;
        }
        if (AccessType.contains(access, AccessType.ADDRESS_VALID5)) {
            return REQUESTED_AMOUNT_5;
        }
        return 0;
    }

    public static boolean hasAccessToAddressValidation(int access) {
        return
                AccessType.contains(access, AccessType.ADDRESS_VALID1) ||
                        AccessType.contains(access, AccessType.ADDRESS_VALID2) ||
                        AccessType.contains(access, AccessType.ADDRESS_VALID3) ||
                        AccessType.contains(access, AccessType.ADDRESS_VALID4) ||
                        AccessType.contains(access, AccessType.ADDRESS_VALID5);
    }

    public static boolean hasAccessToPhotoID(int access) {
        return
                AccessType.contains(access, AccessType.PHOTO_ID_1) ||
                        AccessType.contains(access, AccessType.PHOTO_ID_2) ||
                        AccessType.contains(access, AccessType.PHOTO_ID_3) ||
                        AccessType.contains(access, AccessType.PHOTO_ID_4) ||
                        AccessType.contains(access, AccessType.PHOTO_ID_5);
    }

    public int amountOfRequestedAddresses() {
        return getRequestedAmountOfAddresses(getAccess());
    }

    public int amountOfRequestedPhotoIDs() {
        return getRequestedAmountOfPhotoIDs(getAccess());
    }

    public boolean hasAccessToAddressValidation() {
        return hasAccessToAddressValidation(getAccess());
    }

    public boolean hasAccessToPhotoID() {
        return hasAccessToPhotoID(getAccess());
    }

    public String getHashedUserEmail() {
        return hashedUserEmail;
    }

    public void setHashedUserEmail(String hashedUserEmail) {
        this.hashedUserEmail = hashedUserEmail;
    }

    public String getHashedRequesterEmail() {
        return hashedRequesterEmail;
    }

    public void setHashedRequesterEmail(String hashedRequesterEmail) {
        this.hashedRequesterEmail = hashedRequesterEmail;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public DateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(DateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public void addAccess(AccessType accessType) {
        access = AccessType.add(access, accessType);
    }

    public void removeAccess(AccessType accessType) {
        access = AccessType.remove(access, accessType);
    }

    public void fullAccess() {
        access = AccessType.all();
    }

    public void clearAccess() {
        access = AccessType.none();
    }

    public TokenDurationType getDurationType() {
        return durationType;
    }

    public void setDurationType(TokenDurationType durationType) {
        this.durationType = durationType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public TokenStatus getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(TokenStatus tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Deprecated
    public byte[] getImage() {
        return image;
    }

    @Deprecated
    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(id, tokenStatus, userEmail, duration, durationType, expiryDate, creationDate,
                        access, requesterEmail, hashedRequesterEmail);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Token other = (Token) obj;
        return Objects.equals(this.id, other.id) &&
                Objects.equals(this.userEmail, other.userEmail) &&
                Objects.equals(this.tokenStatus, other.tokenStatus) &&
                Objects.equals(this.duration, other.duration) &&
                Objects.equals(this.access, other.access) &&
                Objects.equals(this.metaData, other.metaData) &&
                Objects.equals(this.expiryDate, other.expiryDate) &&
                Objects.equals(this.creationDate, other.creationDate) &&
                Objects.equals(this.requesterEmail, other.requesterEmail) &&
                Objects.equals(this.hashedRequesterEmail, other.hashedRequesterEmail) &&
                Objects.equals(this.hashedUserEmail, other.hashedUserEmail) &&
                Objects.equals(this.durationType, other.durationType);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
