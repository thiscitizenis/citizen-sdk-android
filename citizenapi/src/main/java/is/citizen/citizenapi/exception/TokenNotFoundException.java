package is.citizen.citizenapi.exception;


public class TokenNotFoundException extends Exception
{
    public TokenNotFoundException(String detailMessage) {
        super(detailMessage);
    }
}
