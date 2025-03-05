package cat.udl.eps.softarch.tfgfinder.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class External extends Director {
    @Override
    @ElementCollection
    public Collection<GrantedAuthority> getAuthorities(){
        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_EXTERNAL");
    }
}
