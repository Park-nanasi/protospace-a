package in.tech_camp.protospace_a.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public void createUserWithEncryptedPassword(UserEntity userEntity) {
    String encodedPassword = encodePassword(userEntity.getPassword());
    userEntity.setPassword(encodedPassword);
    userRepository.insert(userEntity);
  }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }
}
