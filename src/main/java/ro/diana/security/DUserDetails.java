package ro.diana.security;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by diana on 2/11/14.
 */
public class DUserDetails implements UserDetails {

    private String email;
    private String password;
    private Integer userId;
    private String role;

    private boolean enabled;
    private Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    private HashMap<String, Object> authoritiesHash;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;

        if (authoritiesHash != null) {
            authoritiesHash.clear();
        } else {
            authoritiesHash = new HashMap<String, Object>();
        }
        for (GrantedAuthority ga : authorities) {
            authoritiesHash.put(ga.getAuthority(), new Object());
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
     */
    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
     */
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
    public String getUsername() {
        return email;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     */
    @JsonIgnore
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     */
    @JsonIgnore
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void addAuthority(String role) {
        GrantedAuthorityImpl gaImpl = new GrantedAuthorityImpl(role);
        authorities.add(gaImpl);

        if (authoritiesHash == null) {
            authoritiesHash = new HashMap<String, Object>();
        }
        authoritiesHash.put(role, new Object());
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    private boolean hasAuthority(String authority) {
        Object o = authoritiesHash.get(authority);
        return (o != null ? true : false);
    }

    @Override
    public String toString() {
        return "AuthenticatedUser [email=" + email + ", password="
                + password + ", userId=" + userId
                + ", enabled=" + enabled + ", authorities=" + authorities
                + ", authoritiesHash=" + authoritiesHash + "]";
    }

}

