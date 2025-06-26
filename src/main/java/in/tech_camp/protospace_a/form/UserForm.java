package in.tech_camp.protospace_a.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import in.tech_camp.protospace_a.repository.UserRepository;
import in.tech_camp.protospace_a.validation.ValidationPriority1;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserForm {
  private String email;
  private String password;
  private String username;
  private String profile;

  @NotBlank(message = "Company can't be blank", groups = ValidationPriority1.class)
  private String company;

  @NotBlank(message = "Role can't be blank", groups = ValidationPriority1.class)
  private String role;

  private String passwordConfirmation;

  @Autowired
  private final UserRepository userRepository;

  public void validateUserForm(BindingResult result) {
    validateEmail(result);
    validatePassword(result);
    validateUsername(result);
    validateProfile(result);
  }

  public void validateUsername(BindingResult result) {
    if (username == null || username.isEmpty()) {
        result.rejectValue("username", "username", "ユーザー名を入力してください");
        return;
      }
      
      if (30 < username.length()) {
        result.rejectValue("username", "username", "ユーザー名は 30 文字で指定してください");
      }
    }
    
  public void validateEmail(BindingResult result) {
    if (email == null || email.isEmpty()) {
        result.rejectValue("email", "email", "メールアドレスを入力してください");
        return;
      }

    if (email.chars().filter(c -> c == '@').count() != 1) {
        result.rejectValue("email", "email", "メールアドレスには @ を1つだけ含めてください");
        return;
    }
    
    if (!email.matches("^[@a-z0-9.]+$")) {
        result.rejectValue("email", "email", "ASCII 文字 (a-z)、数字 (0-9)、およびピリオド (.) のみが使用できます");
        return;
    }

    if (email.contains("..")) {
        result.rejectValue("email", "email", "ピリオドを連続して使用することはできません");
        return;
    }

    char firstChar = email.charAt(0);
    if (!Character.isLowerCase(firstChar) && !Character.isDigit(firstChar)) {
        result.rejectValue("email", "email", "メールアドレスの最初の文字は、ASCII 文字（a-z）または数字（0-9）にする必要があります");
        return;
    }

    char lastChar = email.charAt(email.length() - 1);
    if (!Character.isLowerCase(lastChar) && !Character.isDigit(lastChar)) {
          result.rejectValue("email", "email", "メールアドレスの最後の文字は、ASCII 文字（a-z）または数字（0-9）にする必要があります");
        return;
    }

    if (userRepository.existsByEmail(email)) {
      result.rejectValue("email", "null", "このメールアドレスは既に使用されています。別のメールアドレスを作成してください");
    }
  }

  public void validatePassword(BindingResult result) {
    if (password == null || password.isEmpty()) {
        result.rejectValue("password", "password", "パスワードを入力してください");
        return;
    }
    
    if (password.length() < 6) {
        result.rejectValue("password", "password", "パスワードは 6 文字以上で設定してください");
        return;
    }

    if (128 < password.length()) {
        result.rejectValue("password", "password", "パスワードは 128 文字以下で設定してください");
        return;
    }

    String allowedRegex = "^[A-Za-z0-9]+$";
    boolean isValidCharacters = password.matches(allowedRegex);
    boolean hasLowercase = password.matches(".*[a-z].*");
    boolean hasUppercase = password.matches(".*[A-Z].*");
    boolean hasDigit = password.matches(".*[0-9].*");
    if (!(isValidCharacters && hasLowercase && hasUppercase && hasDigit)) {
        result.rejectValue("password", "password",
            "より強力なパスワードを選択してください。英小文字・英大文字・数字をそれぞれ1文字以上含めてください");
        return;
    }

    if (passwordConfirmation == null || passwordConfirmation.isEmpty()) {
        result.rejectValue("passwordConfirmation", "passwordConfirmation", "確認用のパスワードを入力してください");
        return;
    }
    
    if (!password.equals(passwordConfirmation)) {
      result.rejectValue("passwordConfirmation", "passwordConfirmation", "パスワードが一致しませんでした。もう一度お試しください。");
    }
  }

  public void validateProfile(BindingResult result) {
    if (profile != null || profile.isEmpty()) {
      result.rejectValue("profile", "profile", "プロフィールを入力してください");
    }

    if (140 < profile.length()) {
      result.rejectValue("profile", "profile", "プロフィールの文字数は140字以内で入力してください");
    }
  }
}
