package is.citizen.citizenapi.exception;


public class DuplicateUserException extends Exception
{
    public DuplicateUserException(String detailMessage) {
        super(detailMessage);
    }
}
