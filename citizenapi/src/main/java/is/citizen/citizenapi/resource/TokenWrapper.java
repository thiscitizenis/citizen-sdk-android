package is.citizen.citizenapi.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TokenWrapper implements Serializable {

    private static final long serialVersionUID = -1543052717770132125L;

    List<Token> tokens = new ArrayList<>();

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}
