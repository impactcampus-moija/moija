package impact.moija.domain.user;

import impact.moija.domain.common.BaseTimeEntity;
import impact.moija.domain.mentoring.Mentee;
import impact.moija.domain.mentoring.Mentor;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String email;

    String password;
    String nickname;
    LocalDate birthday;
    Integer independenceYear;

    @Enumerated(EnumType.STRING)
    Location location;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    Set<UserRole> roles;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    RefreshToken refreshToken;


    @OneToOne(mappedBy = "user")
    Mentor mentor;

    @OneToMany(mappedBy = "user")
    List<Mentee> mentees;

    public String calculateIndependenceStatus() {
//        int currentYear = Year.now().getValue();
//        int independenceYears = currentYear - independenceYear;
//
//        if (independenceYear == null || independenceYears < 0) {
            return "자립 청소년";
//        }
//        return "자립 준비 " + independenceYears + " 년차";
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toSet());
    }

    public void addRole(UserRole role) {
        roles.add(role);
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
