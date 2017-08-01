package is.citizen.citizenapi.exception;


public class UnauthorisedException extends Exception
{
    public UnauthorisedException(String detailMessage) {
        super(detailMessage);
    }
}
