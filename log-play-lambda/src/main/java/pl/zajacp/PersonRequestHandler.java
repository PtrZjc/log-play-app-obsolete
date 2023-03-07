package pl.zajacp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class PersonRequestHandler
        implements RequestHandler<PersonRequest, PersonRequest> {

    @Override
    public PersonRequest handleRequest(PersonRequest personRequest, Context context) {
        return new PersonRequest(
                personRequest.getFirstName().toUpperCase(),
                personRequest.getLastName().toUpperCase()
        );
    }
}
