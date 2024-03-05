package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.user.Location;
import impact.moija.dto.common.PageResponse;
import impact.moija.dto.policy.PolicyRequestListDto;
import impact.moija.dto.policy.PolicyRequestListDto.PolicyRequestDto;
import impact.moija.dto.policy.PolicyResponse;
import impact.moija.repository.PolicyRepository;

import java.io.StringReader;
import java.net.URI;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class PolicyService {
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void createPolicies() {
        boolean continueScraping = true;
        int displayCount = 20;
        int currentPage = 1;

        while (continueScraping) {
            PolicyRequestListDto policies = callPolicyApi(String.valueOf(displayCount), String.valueOf(currentPage));

            if (policies != null) {
                for (PolicyRequestDto policy : policies.getPolicies()) {
                    if (!policyRepository.existsByNumber(policy.getNumber())) {
                        log.info("saved{}", policy.getName());
                        policyRepository.save(policy.toEntity());
                    } else {
                        continueScraping = false;
                        break;
                    }
                }
            } else {
                break;
            }
            currentPage++;
        }
    }

    @Value("${moija.apiKey}")
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
            PolicyRequestListDto policyRequestListDto = (PolicyRequestListDto) xmlToObject(response.getBody(), PolicyRequestListDto.class);
            if (!policyRequestListDto.getTotalCount().equals("0")) {
                return policyRequestListDto;
            }
        }

        return null;
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

    public PageResponse<PolicyResponse> searchPolicies(Integer age, String locationName, String searchText, Pageable pageable) {
        Location location;
        if (locationName != null && !locationName.isEmpty()) {
            location = Location.findByName(locationName);
            locationName = location.getName();
        }

        return PageResponse.of(policyRepository.findByFilter(age, locationName, searchText, pageable)
                .map(PolicyResponse::of));
    }
}
