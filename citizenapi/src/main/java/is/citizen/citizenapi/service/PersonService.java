package is.citizen.citizenapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import is.citizen.citizenapi.exception.AddressNotFoundException;
import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.MnemonicCodeNotFoundException;
import is.citizen.citizenapi.exception.PhoneNotFoundException;
import is.citizen.citizenapi.resource.Address;
import is.citizen.citizenapi.resource.Person;
import is.citizen.citizenapi.resource.Phone;
import is.citizen.citizenapi.resource.User;
import is.citizen.citizenapi.util.Constant;
import is.citizen.citizenapi.util.RestClient;
import is.citizen.citizenapi.exception.PersonNotFoundException;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.resource.Name;

public class PersonService
{
    private static final String TAG = PersonService.class.getSimpleName();
    private static final RestClient restClient = new RestClient();


    public Person getPerson(String apiKey, String mnemonic)
            throws PersonNotFoundException, MnemonicCodeNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);
        restClient.setXcode(mnemonic);

        Person person = null;

        try {
            person = restClient.get(Constant.CITIZEN_PERSON_RESOURCE, null, Person.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.UNPROCESSABLE_ENTITY))
            {
                throw new MnemonicCodeNotFoundException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new PersonNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return person;
    }


    public Address getAddress(String personId, String apiKey, String mnemonic)
            throws AddressNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);
        restClient.setXcode(mnemonic);

        Address address = null;

        try {
            address = restClient.get(Constant.CITIZEN_PERSON_RESOURCE + "/" + personId + "/address", null, Address.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new AddressNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return address;
    }


    public Phone getPhone(String personId, String apiKey, String mnemonic)
            throws PhoneNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);
        restClient.setXcode(mnemonic);

        Phone phone = null;

        try {
            phone = restClient.get(Constant.CITIZEN_PERSON_RESOURCE + "/" + personId + "/phone", null, Phone.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new PhoneNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return phone;
    }


    public Person updateName(String personId, Name name, String apiKey)
            throws PersonNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);

        Person person = new Person();

        try {
            person = restClient.post(Constant.CITIZEN_PERSON_RESOURCE + "/" + personId + "/name", name, Person.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new PersonNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return person;
    }


    public Person updateOrigin(Person person, String apiKey)
            throws PersonNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);

        try {
            person = restClient.put(Constant.CITIZEN_PERSON_RESOURCE + "/" + person.getId() + "/origin", person, Person.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new PersonNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return person;
    }


    public Address addAddress(String personId, Address address, String apiKey)
            throws PersonNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);

        try {
            address = restClient.post(Constant.CITIZEN_PERSON_RESOURCE + "/" + personId + "/address", address, Address.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new PersonNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return address;
    }


    public Phone addPhone(String personId, Phone phone, String apiKey)
            throws PersonNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);

        try {
            phone = restClient.post(Constant.CITIZEN_PERSON_RESOURCE + "/" + personId + "/phones", phone, Phone.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new PersonNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return phone;
    }


    public Phone confirmPhone(Phone phone, String apiKey)
            throws PhoneNotFoundException, UnauthorisedException, HttpException
    {
        restClient.setServiceKey(apiKey);

        try {
            phone = restClient.post(Constant.CITIZEN_PHONE_RESOURCE + "/" + phone.getId() + "/confirm", phone, Phone.class);
        } catch (HttpClientErrorException e) {
            HttpStatus code = e.getStatusCode();

            if (code.equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorisedException(e.getMessage());
            } else if (code.equals(HttpStatus.NOT_FOUND)) {
                throw new PhoneNotFoundException(e.getMessage());
            } else {
                throw new HttpException(e.getMessage());
            }
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return phone;
    }


    public String retrieveMnemonic(User user)
            throws HttpException
    {
        String mnemonic = null;

        try {
            mnemonic = restClient.postWithoutAuthorization(Constant.CITIZEN_SESSION_RESOURCE + "/mnemonic", user, String.class);
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        return mnemonic;
    }
}
