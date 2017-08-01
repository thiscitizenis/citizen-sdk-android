package is.citizen.citizenapi.exception;

public class UserNotFoundException extends Exception
{
    public UserNotFoundException(String detailMessage) {
        super(detailMessage);
    }
}
