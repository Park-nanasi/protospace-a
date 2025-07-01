package in.tech_camp.protospace_a.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import in.tech_camp.protospace_a.service.UserService;
import in.tech_camp.protospace_a.repository.UserRepository;
import in.tech_camp.protospace_a.validation.ValidationPriority1;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserForm {
  private String email;
  private String password;
  private String username;
  private String profile;  
  private MultipartFile profileImage;
  private String passwordConfirmation;

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

    boolean hasAscii = email.matches(".*[a-z].*");
    boolean hasPeriod = email.matches(".*[.].*");
    if (!(email.matches("^[@a-z0-9.]+$") && hasAscii && hasPeriod)) {
      result.rejectValue("email", "email", "ASCII 文字 (a-z)、ピリオド (.) を使用してください");
      return;
    }

    if (email.contains("..")) {
      result.rejectValue("email", "email", "ピリオドを連続して使用することはできません");
      return;
    }

    char firstChar = email.charAt(0);
    if (!Character.isLowerCase(firstChar) && !Character.isDigit(firstChar)) {
      result.rejectValue("email", "email",
          "メールアドレスの最初の文字は、ASCII 文字（a-z）または数字（0-9）にする必要があります");
      return;
    }

    char lastChar = email.charAt(email.length() - 1);
    if (!Character.isLowerCase(lastChar) && !Character.isDigit(lastChar)) {
      result.rejectValue("email", "email",
          "メールアドレスの最後の文字は、ASCII 文字（a-z）または数字（0-9）にする必要があります");
      return;
    }

    // todo: UserVaridationClass メールアドレスの重複のバリデーション機能作成
    // if (userService.existsByEmail(email)) {
    // result.rejectValue("email", "email",
    // "このメールアドレスは既に使用されています。別のメールアドレスを作成してください");
    // }
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
      result.rejectValue("passwordConfirmation", "passwordConfirmation",
          "確認用のパスワードを入力してください");
      return;
    }

    if (!password.equals(passwordConfirmation)) {
      result.rejectValue("passwordConfirmation", "passwordConfirmation",
          "パスワードが一致しませんでした。もう一度お試しください。");
    }
  }

  public void validateProfile(BindingResult result) {
    if (profile == null || profile.isEmpty()) {
      result.rejectValue("profile", "profile", "プロフィールを入力してください");
      return;
    }

    if (140 < profile.length()) {
      result.rejectValue("profile", "profile", "プロフィールの文字数は140字以内で入力してください");
    }
  }
}
