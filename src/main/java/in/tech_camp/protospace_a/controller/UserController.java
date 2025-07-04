package in.tech_camp.protospace_a.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import in.tech_camp.protospace_a.ImageUrl;
import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.form.SearchForm;
import in.tech_camp.protospace_a.form.UserForm;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import in.tech_camp.protospace_a.service.UserService;
import in.tech_camp.protospace_a.validation.ValidationOrder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@AllArgsConstructor
public class UserController {

  private final PrototypeRepository prototypeRepository;
  private final UserRepository userRepository;
  private final UserService userService;
  private final ImageUrl imageUrl;

  @GetMapping("/users/sign_up")
  public String showSignUp(Model model) {
    model.addAttribute("userForm", new UserForm());
    return "users/signUp";
  }

  @PostMapping("/user")
  public String createUser(
      @ModelAttribute("userForm") @Validated(ValidationOrder.class) UserForm userForm,
      BindingResult result,
      @AuthenticationPrincipal CustomUserDetail currentUser,
      RedirectAttributes redirectAttributes, Model model) {
    userForm.validateNewUserForm(result);
    if (result.hasErrors()) {
      Map<String, String> fieldErrors = result.getFieldErrors().stream()
          .collect(Collectors.toMap(FieldError::getField,
              FieldError::getDefaultMessage, (msg1, msg2) -> msg1));
      model.addAttribute("fieldErrors", fieldErrors);
      model.addAttribute("userForm", userForm);
      return "users/signUp";
    }

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(userForm.getUsername());
    userEntity.setEmail(userForm.getEmail());
    userEntity.setPassword(userForm.getPassword());
    userEntity.setProfile(userForm.getProfile());

    MultipartFile imageFile = userForm.getProfileImage();
    if (imageFile == null || imageFile.isEmpty()) {
      userEntity.setProfileImage(imageUrl.getUserProfileDefaultImageUrl());
    }
    else {
      try {
        String uploadDir = imageUrl.getUserProfileImageUrl();

        Path uploadDirPath = Paths.get(uploadDir);
        if (!Files.exists(uploadDirPath)) {
          Files.createDirectories(uploadDirPath);
        }

        String fileName = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_"
            + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.copy(imageFile.getInputStream(), imagePath);
        userEntity.setProfileImage("/uploads/user_profiles/" + fileName);
      }
      catch (IOException e) {
        System.out.println("Error：" + e);
        return "users/new";
      }
    }

    try {
      userService.createUserWithEncryptedPassword(userEntity, currentUser);
    }
    catch (Exception e) {
      System.out.println("エラー：" + e);
      redirectAttributes.addFlashAttribute("profileImage",
          userEntity.getProfileImage());
      return "redirect:/";
    }
    redirectAttributes.addFlashAttribute("profileImage",
        userEntity.getProfileImage());
    return "redirect:/";
  }

  @GetMapping("/users/login")
  public String showLogin() {
    return "users/login";
  }

  @GetMapping("/login")
  public String showLoginWithError(
      @RequestParam(value = "error", required = false) String error,
      Model model) {
    if (error != null) {
      model.addAttribute("loginError", "Invalid email or password.");
    }
    return "users/login";
  }

  @GetMapping("/users/{userId}")
  public String showMypage(@PathVariable("userId") Integer userId,
      @ModelAttribute("searchForm") SearchForm searchForm, Model model) {
    UserEntity user = userRepository.findById(userId);
    List<PrototypeEntity> prototypes = prototypeRepository.findByUserId(userId);
    model.addAttribute("name", user.getUsername());
    model.addAttribute("profile", user.getProfile());
    model.addAttribute("profileImage", user.getProfileImage());
    model.addAttribute("prototypes", prototypes);
    model.addAttribute("userId", user.getId());
    return "users/userInfo";
  }

  @GetMapping("/users/{userId}/edit")
  public String editMypage(@PathVariable("userId") Integer userId,
      @AuthenticationPrincipal CustomUserDetail currentUser,
      RedirectAttributes redirectAttributes, Model model) {
    if (!currentUser.getId().equals(userId)) {
      // todo: トップページに画面表示
      System.err.println("Error: 他のユーザーのプロフィールは編集できません");
      redirectAttributes.addFlashAttribute("profileImage",
          currentUser.getProfileImage());
      return "redirect:/";
    }
    UserEntity user = userRepository.findById(userId);
    UserForm userForm = new UserForm();
    userForm.setUsername(user.getUsername());
    userForm.setProfile(user.getProfile());
    model.addAttribute("userForm", userForm);
    return "users/edit";
  }

  @PostMapping("/users/{userId}/update")
  public String updateMyPage(@PathVariable("userId") Integer userId,
      @ModelAttribute("userForm") @Validated(ValidationOrder.class) UserForm userForm,
      BindingResult result, RedirectAttributes redirectAttributes,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    userForm.validateUpdateUserForm(result);
    if (result.hasErrors()) {
      Map<String, String> fieldErrors = result.getFieldErrors().stream()
          .collect(Collectors.toMap(FieldError::getField,
              FieldError::getDefaultMessage, (msg1, msg2) -> msg1));
      model.addAttribute("fieldErrors", fieldErrors);
      model.addAttribute("userForm", userForm);
      fieldErrors.forEach((field, error) -> System.err
          .println("Field: " + field + " - Error: " + error));
      return "users/edit";
    }

    UserEntity user = new UserEntity();
    user.setId(userId);
    user.setUsername(userForm.getUsername());
    user.setPassword(userForm.getPassword());
    user.setProfile(userForm.getProfile());

    MultipartFile imageFile = userForm.getProfileImage();
    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        String uploadDir = imageUrl.getUserProfileImageUrl();

        Path uploadDirPath = Paths.get(uploadDir);
        if (!Files.exists(uploadDirPath)) {
          Files.createDirectories(uploadDirPath);
        }

        String fileName = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_"
            + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.copy(imageFile.getInputStream(), imagePath);
        user.setProfileImage("/uploads/user_profiles/" + fileName);
      }
      catch (IOException e) {
        System.out.println("Error：" + e);
        return "redirect:/users/" + userId + "/edit";
      }
    }

    try {
      userService.updateUser(user, currentUser);
    }
    catch (Exception e) {
      System.err.println("Error:" + e);
      redirectAttributes.addFlashAttribute("profileImage",
          user.getProfileImage());
      return "redirect:/";
    }
    redirectAttributes.addFlashAttribute("profileImage",
        user.getProfileImage());
    return "redirect:/";
  }


  // 検索機能
  @GetMapping("/users/{userId}/search")
  public String searchPrototypes(@PathVariable("userId") Integer userId,
      @ModelAttribute("searchForm") SearchForm searchForm, Model model) {
    // 名前の長さ判定、50以上だったら、プリントアウト
    if (searchForm.getName() != null && searchForm.getName().length() > 50) {
      System.out.println(String.format("検索に入力した名前の文字数：%d、50を超えています!!",
          searchForm.getName().length()));

    }

    UserEntity user = userRepository.findById(userId);
    List<PrototypeEntity> prototypes = prototypeRepository
        .findByUserIdAndNameContaining(userId, searchForm.getName());

    model.addAttribute("name", user.getUsername());
    model.addAttribute("profile", user.getProfile());
    model.addAttribute("profileImage", user.getProfileImage());
    model.addAttribute("prototypes", prototypes);
    model.addAttribute("searchForm", searchForm);
    model.addAttribute("userId", user.getId());
    return "users/userInfo";
  }
}
