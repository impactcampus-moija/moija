package impact.moija.dto.policy;

import impact.moija.domain.policy.Policy;
import impact.moija.domain.policy.PolicyType;
import impact.moija.domain.user.Location;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "youthPolicyList")
@XmlAccessorType(XmlAccessType.FIELD)
public class PolicyRequestListDto {

    @XmlElement(name = "pageIndex")
    private String page;

    @XmlElement(name = "totalCnt")
    private String totalCount;

    @XmlElement(name = "youthPolicy")
    private List<PolicyRequestDto> policies;

    @Data
    @XmlRootElement(name = "youthPolicy")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PolicyRequestDto {
        @XmlElement(name = "bizId")
        private String number;

        @XmlElement(name = "polyBizSecd")
        private String locationCode;

        @XmlElement(name = "polyBizSjnm")
        private String name;

        @XmlElement(name = "polyItcnCn")
        private String introduction;

        @XmlElement(name = "sporCn")
        private String content;

        @XmlElement(name = "ageInfo")
        private String age;

        @XmlElement(name = "majrRqisCn")
        private String major;

        @XmlElement(name = "empmSttsCn")
        private String employment;

        @XmlElement(name = "splzRlmRqisCn")
        private String special;

        @XmlElement(name = "bizPrdCn")
        private String period;

        @XmlElement(name = "rqutUrla")
        private String url;

        @XmlElement(name = "polyRlmCd")
        private String typeCode;

        public Policy toEntity() {
            return Policy.builder()
                    .number(this.number)
                    .name(this.name)
                    .introduction(this.introduction)
                    .content(this.content)
                    .age(this.age)
                    .major(this.major)
                    .employment(this.employment)
                    .special(this.special)
                    .period(this.period)
                    .url(this.url)
                    .type(PolicyType.findByCode(this.typeCode))
                    .location(Location.findByCode(this.locationCode))
                    .build();
        }
    }
}
