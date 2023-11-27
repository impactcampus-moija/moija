package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.dto.policy.PolicyRequestListDto;
import impact.moija.dto.policy.PolicyRequestListDto.PolicyRequestDto;
import impact.moija.repository.PolicyRepository;
import java.io.StringReader;
import java.net.URI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class PolicyService {

    @Value("${API-KEY}")
    private String APIKEY;
    private final RestTemplate template;
    private final PolicyRepository policyRepository;

    public PolicyRequestListDto callPolicyApi(String display, String page) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://www.youthcenter.go.kr/opi/youthPlcyList.do")
                .queryParam("openApiVlak", APIKEY)
                .queryParam("display", display)
                .queryParam("pageIndex", page)
                .encode()
                .build()
                .toUri();

        ResponseEntity<String> response = template.getForEntity(uri, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return (PolicyRequestListDto) xmlToObject(response.getBody(), PolicyRequestListDto.class);
        }

        return null;
    }
    public void createPolicies(String display, String page) {
        PolicyRequestListDto policies = callPolicyApi(display, page);

        if (policies != null) {
            for (PolicyRequestDto policy : policies.getPolicies()) {
                policyRepository.save(policy.toEntity());
            }
        }
    }


    private static <T> Object xmlToObject(String xml, Class<T> classType) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classType);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new ApiException(MoijaHttpStatus.FAIL_CONVERT_XML);
        }
    }
}
