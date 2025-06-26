package in.tech_camp.protospace_a.form;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BindingResult;

import in.tech_camp.protospace_a.validation.ValidationPriority1;
import in.tech_camp.protospace_a.validation.ValidationPriority2;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserForm {
  @NotBlank(message = "Email can't be blank", groups = ValidationPriority1.class)
  @Email(message = "Email should be valid", groups = ValidationPriority2.class)
  private String email;
  private String password;

  @NotBlank(message = "Username can't be blank", groups = ValidationPriority1.class)
  @Length(max = 50, message = "Username is too long", groups = ValidationPriority2.class)
  private String username;

  @NotBlank(message = "Profile can't be blank", groups = ValidationPriority1.class)
  private String profile;

  @NotBlank(message = "Company can't be blank", groups = ValidationPriority1.class)
  private String company;

  @NotBlank(message = "Role can't be blank", groups = ValidationPriority1.class)
  private String role;

  private String passwordConfirmation;

  public void validatePassword(BindingResult result) {
    String allowedRegex = "^[A-Za-z0-9]+$";
    boolean isValidCharacters = password.matches(allowedRegex);
    
    if (password == null || password.isEmpty()) {
        result.rejectValue("password", "password", "パスワードを入力してください");
        return;
    }
    
    if (!(isValidCharacters)) {
      result.rejectValue("password", "password", 
      "より強力なパスワードを選択してください。大小英字、数字の組み合わせをお試しください。");
      return;
    }
    
    if (passwordConfirmation == null || passwordConfirmation.isEmpty()) {
        result.rejectValue("passwordConfirmation", "passwordConfirmation", "確認用のパスワードを入力してください");
        return;
    }
    
    if (!password.equals(passwordConfirmation)) {
      result.rejectValue("passwordConfirmation", "password", "パスワードが一致しませんでした。もう一度お試しください。");
    }
  }
}
