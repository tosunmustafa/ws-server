package com.mustafatosun.webservice.server;

import com.mustafatosun.webservice.server.generated.GetCountryRequest;
import com.mustafatosun.webservice.server.generated.GetCountryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

@Endpoint
public class CountryEndpoint {
    private static final String NAMESPACE_URI = "http://mustafatosun.com/webservice/server/generated";
    private static final Logger logger = LoggerFactory.getLogger(CountryEndpoint.class);

    private CountryRepository countryRepository;

    @Autowired
    public CountryEndpoint(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request,
                                         @SoapHeader(value = "{http://mustafatosun.com/webservice/server/generated/header}credentials") SoapHeaderElement header) {
//        Credentials h = getHeader(header);
//        logger.info("username: {}, password: {}", h.getUsername(), h.getPassword());
        logger.info("Request arrived, asking for country: {}", request.getName());
        GetCountryResponse response = new GetCountryResponse();
        response.setCountry(countryRepository.findCountry(request.getName()));
        return response;
    }


    private Credentials getHeader(SoapHeaderElement header){
        Credentials h = null;
        try {

            JAXBContext context = JAXBContext.newInstance(Credentials.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            h = (Credentials) unmarshaller.unmarshal(header.getSource());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return h;
    }
}
