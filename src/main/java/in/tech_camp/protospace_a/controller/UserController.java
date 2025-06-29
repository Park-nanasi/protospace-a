package in.tech_camp.protospace_a.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.form.UserForm;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import in.tech_camp.protospace_a.service.UserService;
import in.tech_camp.protospace_a.validation.ValidationOrder;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class UserController {

  private final PrototypeRepository prototypeRepository;
  private final UserRepository userRepository;
  private final UserService userService;

  @GetMapping("/users/sign_up")
  public String showSignUp(Model model) {
    model.addAttribute("userForm", new UserForm(userRepository));
      return "users/signUp";
  }
  
  @PostMapping("/user")
  public String createUser(@ModelAttribute("userForm") @Validated(ValidationOrder.class) UserForm userForm, BindingResult result, Model model) {
    userForm.validateUserForm(result);

    if (result.hasErrors()) {
      Map<String, String> fieldErrors = result.getFieldErrors().stream()
              .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (msg1, msg2) -> msg1));
      model.addAttribute("fieldErrors", fieldErrors);
      model.addAttribute("userForm", userForm);
      return "users/signUp";
    }

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(userForm.getUsername());
    userEntity.setEmail(userForm.getEmail());
    userEntity.setPassword(userForm.getPassword());
    userEntity.setProfile(userForm.getProfile());
    userEntity.setCompany(userForm.getCompany());
    userEntity.setRole(userForm.getRole());
    

    try {
      userService.createUserWithEncryptedPassword(userEntity);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "redirect:/";
    }

    return "redirect:/";
  }

  // ログインに成功した時
  @GetMapping("/users/login")
  public String showLogin() {
      return "users/login";
  }

  // 失敗した時の表示
  @GetMapping("/login")
  public String showLoginWithError(@RequestParam(value = "error", required = false) String error, Model model) {
    if (error != null) {
      model.addAttribute("loginError", "Invalid email or password.");
    }
    return "users/login";
  }

  @GetMapping("/users/{userId}")
  public String showMypage(@PathVariable("userId") Integer userId, Model model) {
    UserEntity user = userRepository.findById(userId);
    List<PrototypeEntity> prototypes = prototypeRepository.findByUserId(userId);

    model.addAttribute("name", user.getUsername());
    model.addAttribute("profile", user.getProfile());
    model.addAttribute("role", user.getRole());
    model.addAttribute("company", user.getCompany());
    model.addAttribute("prototypes", prototypes);
    return "users/userInfo";
  }
  
}