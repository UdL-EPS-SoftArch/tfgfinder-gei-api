package cat.udl.eps.softarch.tfgfinder.domain;

import jakarta.persistence.ElementCollection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class Professor extends Director {
    @Override
    @ElementCollection
    public Collection<GrantedAuthority> getAuthorities(){
        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_PROFESSOR");
    }
}