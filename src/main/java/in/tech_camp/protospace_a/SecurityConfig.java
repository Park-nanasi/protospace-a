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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests

            .requestMatchers("/css/**", "/test/**", "/uploads/**", "/", "/js/**",
                "/users/sign_up", "/users/login", "/prototypes/{id:[0-9]+}",
                "/users/{id:[0-9]+}", "/prototypes/search",
                "/users/{id:[0-9]+}/search", "/prototypes/{prototypesId:[0-9]+}/comments/{commentsId:[0-9]+}",
                "/prototypes/{prototypeId:[0-9]+}/likes/info")
            .permitAll().requestMatchers(HttpMethod.POST, "/user").permitAll()
            .anyRequest().authenticated())
        .formLogin(login -> login.loginProcessingUrl("/login")
            .loginPage("/users/login").defaultSuccessUrl("/", true)
            .failureUrl("/login?error").usernameParameter("email").permitAll())

        .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
