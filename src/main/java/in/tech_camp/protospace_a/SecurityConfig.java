package in.tech_camp.protospace_a;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import in.tech_camp.protospace_a.custom_user.CustomLoginSuccessHandler;
import in.tech_camp.protospace_a.repository.UserRepository;
import in.tech_camp.protospace_a.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationSuccessHandler customLoginSuccessHandler)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests

            .requestMatchers("/css/**", "/test/**", "/images/**", "/uploads/**", "/",
                "/users/sign_up", "/users/login", "/prototypes/{id:[0-9]+}",
                "/users/{id:[0-9]+}", "/prototypes/search",
                "/users/{id:[0-9]+}/search", "/prototypes/{prototypesId:[0-9]+}/comments/{commentsId:[0-9]+}",
                "/prototypes/{prototypeId:[0-9]+}/likes/info")
            .permitAll().requestMatchers(HttpMethod.POST, "/user").permitAll()
            .anyRequest().authenticated())
        .formLogin(login -> login.loginProcessingUrl("/login")
            .loginPage("/users/login").defaultSuccessUrl("/", true)
            .failureUrl("/login?error").usernameParameter("email")
            .successHandler(customLoginSuccessHandler).permitAll())

        .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationSuccessHandler customLoginSuccessHandler(UserService userService,
                                                                UserRepository userRepository) {
      return new CustomLoginSuccessHandler(userService, userRepository);
  }
}
