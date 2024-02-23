package impact.moija.domain.policy;

import impact.moija.domain.common.BaseTimeEntity;
import impact.moija.domain.user.Location;
import javax.persistence.Column;
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

    @Column(length = 1000)
    String name;

    @Column(length = 1000)
    String introduction;

    @Column(columnDefinition = "TEXT")
    String content;

    Integer minAge;
    Integer maxAge;

    String major;

    @Column(length = 1000)
    String employment;

    String special;

    @Column(length = 1000)
    String period;

    String url;
}