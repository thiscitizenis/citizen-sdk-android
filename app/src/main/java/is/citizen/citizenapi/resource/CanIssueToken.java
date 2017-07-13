package is.citizen.citizenapi.resource;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import is.citizen.citizenapi.enums.AccessType;

public class CanIssueToken implements Serializable {
    private static final long serialVersionUID = -4374908863024005454L;

    private int numberOfNotConsideredOrMissedPhotoID = 0;
    private int numberOfNotConsideredOrMissedAddressValidations = 0;

    private boolean
            canIssueAddressValidation = false,
            canIssuePhotoID = false;

    private List<AccessType> unsatisfiedAccesses = new ArrayList<>();

    public CanIssueToken() {

    }

    public List<AccessType> getUnsatisfiedAccesses() {
        return unsatisfiedAccesses;
    }

    public CanIssueToken addUnsatisfiedAccess(AccessType accessType) {
        getUnsatisfiedAccesses().add(accessType);
        return this;
    }

    public CanIssueToken setCanIssueAddressValidation(boolean canIssueAddressValidation) {
        this.canIssueAddressValidation = canIssueAddressValidation;
        return this;
    }

    public CanIssueToken setCanIssuePhotoID(boolean canIssuePhotoID) {
        this.canIssuePhotoID = canIssuePhotoID;
        return this;
    }

    public CanIssueToken setNumberOfNotConsideredOrMissedPhotoID(int numberOfNotConsideredOrMissedPhotoID) {
        this.numberOfNotConsideredOrMissedPhotoID = numberOfNotConsideredOrMissedPhotoID;
        return this;
    }

    public CanIssueToken setNumberOfNotConsideredOrMissedAddressValidations(int numberOfNotConsideredOrMissedAddressValidations) {
        this.numberOfNotConsideredOrMissedAddressValidations = numberOfNotConsideredOrMissedAddressValidations;
        return this;
    }

    /**
     * @returns 0 if there are enough documents, 1+ is amount of documents if we need to select, -1 and less amount of documents we need to upload
     */
    public int getNumberOfNotConsideredOrMissedPhotoID() {
        return numberOfNotConsideredOrMissedPhotoID;
    }

    /**
     * @returns 0 if there are enough documents, 1+ is amount of documents if we need to select, -1 and less amount of documents we need to upload
     */
    public int getNumberOfNotConsideredOrMissedAddressValidations() {
        return numberOfNotConsideredOrMissedAddressValidations;
    }

    public boolean isCanIssueAddressValidation() {
        return canIssueAddressValidation;
    }

    public boolean isCanIssuePhotoID() {
        return canIssuePhotoID;
    }

    @Override
    public String toString() {
        return "CanIssueToken{" +
                "canIssueAddressValidation=" + canIssueAddressValidation +
                ", canIssuePhotoID=" + canIssuePhotoID +
                ", numberOfNotConsideredOrMissedPhotoID=" + numberOfNotConsideredOrMissedPhotoID +
                ", numberOfNotConsideredOrMissedAddressValidations=" + numberOfNotConsideredOrMissedAddressValidations +
                '}';
    }
}
