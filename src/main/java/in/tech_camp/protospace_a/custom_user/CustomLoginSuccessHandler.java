package in.tech_camp.protospace_a.custom_user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.repository.UserRepository;
import in.tech_camp.protospace_a.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
  private final UserService userService;
  private final UserRepository userRepository;

  
  public CustomLoginSuccessHandler(UserService userService, UserRepository userRepository) {
      this.userService = userService;
      this.userRepository = userRepository;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
    CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
    UserEntity currentUser =
        userRepository.findById(user.getId());
    response.sendRedirect("/");
    System.out.println("onAuthenticationsSuccess");
    System.out.println("User: " + currentUser.getUsername());
    System.out.println("Email: " + currentUser.getEmail());
    System.out.println("Profile: " + currentUser.getProfile());
    System.out.println("ProfileImage: " + currentUser.getProfileImage());
    userService.updateUserWithEncryptedPassword(currentUser, user);
  }
}
