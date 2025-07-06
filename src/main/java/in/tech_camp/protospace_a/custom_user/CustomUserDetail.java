package in.tech_camp.protospace_a.custom_user;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.tech_camp.protospace_a.entity.UserEntity;
import lombok.Data;

@Data
public class CustomUserDetail implements UserDetails {
  private UserEntity user;

  public CustomUserDetail(UserEntity user) {
    this.user = user;
  }

  public Integer getId() {
    return user.getId();
  }

  public String getNickname() {
    return user.getUsername();
  }

  public void setUsername(String username) {
    user.setUsername(username);
  }

  public String getProfileImage() {
    return user.getProfileImage();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  public void setPassword(String password) {
    user.setPassword(password);
  }

  public void setProfile(String profile) {
    user.setProfile(profile);
  }

  public void setProfileImage(String profileImage) {
    user.setProfileImage(profileImage);
  }
  
  @Override
  public String getUsername() {
    return user.getEmail();
  }

  public void setEmail(String email) {
    user.setEmail(email);
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
