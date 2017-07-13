package is.citizen.citizenapi.exception;


public class PersonNotFoundException extends Exception {
    public PersonNotFoundException(String detailMessage) {
        super(detailMessage);
    }
}
