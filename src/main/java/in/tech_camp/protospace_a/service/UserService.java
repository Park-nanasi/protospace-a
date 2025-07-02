package in.tech_camp.protospace_a.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public void createUserWithEncryptedPassword(UserEntity userEntity, CustomUserDetail customUser) {
    String encodedPassword = encodePassword(userEntity.getPassword());
    userEntity.setPassword(encodedPassword);
    userRepository.insert(userEntity);
    if (customUser != null) {
      customUser.setProfileImage(userEntity.getProfileImage());
    }
  }
  
  public void updateUserWithEncryptedPassword(UserEntity userEntity,
  CustomUserDetail customUser) {
    String encodedPassword = encodePassword(userEntity.getPassword());
    userEntity.setPassword(encodedPassword);
    userRepository.updateUser(userEntity);
    if (customUser != null) {
      updateCustomUser(customUser, userEntity);
    }
  }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }

  private void updateCustomUser(CustomUserDetail customUser,
      UserEntity userEntity) {
    customUser.setUserName(userEntity.getUsername());
    customUser.setPassword(userEntity.getPassword());
    customUser.setProfile(userEntity.getProfile());
    customUser.setProfileImage(userEntity.getProfileImage());
  }
}
