package in.tech_camp.protospace_a.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.SnsLinkEntity;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.repository.SnsLinksRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final SnsLinksRepository snsLinksRepository;

  private final PasswordEncoder passwordEncoder;

  public void createUserWithEncryptedPassword(UserEntity userEntity, SnsLinkEntity snsLinkEntity, CustomUserDetail customUser) {
    String encodedPassword = encodePassword(userEntity.getPassword());
    userEntity.setPassword(encodedPassword);
    snsLinksRepository.insertSnsLink(snsLinkEntity);
    userEntity.setSnsLinksId(snsLinkEntity.getId());
    userRepository.insert(userEntity);
    if (customUser != null) {
      customUser.setProfileImage(userEntity.getProfileImage());
    }
  }
  
  public void updateUser(UserEntity userEntity,
      CustomUserDetail customUser) {
    // todo: パスワードの変更機能実装
    // String encodedPassword = encodePassword(userEntity.getPassword());
    // userEntity.setPassword(encodedPassword);
    userRepository.updateUser(userEntity);
    if (customUser != null) {
      updateCustomUser(customUser, userEntity);
    }
  }
  
  public void loginCurrentUser(UserEntity userEntity,
      CustomUserDetail customUser) {
    customUser.setUsername(userEntity.getUsername());
    customUser.setProfileImage(userEntity.getProfileImage());
  }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }

  private void updateCustomUser(CustomUserDetail customUser,
      UserEntity userEntity) {
    customUser.setUsername(userEntity.getUsername());
    customUser.setProfile(userEntity.getProfile());
    customUser.setProfileImage(userEntity.getProfileImage());
  }
}
