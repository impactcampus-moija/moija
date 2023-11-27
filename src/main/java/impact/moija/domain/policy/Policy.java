package impact.moija.domain.policy;

import impact.moija.domain.common.BaseTimeEntity;
import impact.moija.domain.user.Location;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "policies")
public class Policy extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String number;

    @Enumerated(EnumType.STRING)
    PolicyType type;

    @Enumerated(EnumType.STRING)
    Location location;

    String name;

    String introduction;

    String content;

    String age;

    String major;

    String employment;

    String special;

    String period;

    String url;
}