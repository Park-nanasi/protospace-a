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
  
  @NotBlank(message = "Password can't be blank", groups = ValidationPriority1.class)
  @Length(min = 6, max = 128, message = "Password should be between 6 and 128 characters", groups = ValidationPriority2.class)
  private String password;

  @NotBlank(message = "Username can't be blank", groups = ValidationPriority1.class)
  @Length(min = 6, max = 50, message = "Username is too long (maximum is 6 characters)", groups = ValidationPriority2.class)
  private String username;

  @NotBlank(message = "Profile can't be blank", groups = ValidationPriority1.class)
  @Length(min = 6, max = 128, message = "Profile should be between 6 and 128 characters", groups = ValidationPriority2.class)
  private String profile;

  @NotBlank(message = "Company can't be blank", groups = ValidationPriority1.class)
  @Length(min = 6, max = 50, message = "Company should be between 6 and 128 characters", groups = ValidationPriority2.class)
  private String company;

  @NotBlank(message = "Role can't be blank", groups = ValidationPriority1.class)
  @Length(min = 6, max = 50, message = "Role should be between 6 and 128 characters", groups = ValidationPriority2.class)
  private String role;

  private String passwordConfirmation;

  public void validatePasswordConfirmation(BindingResult result) {
      if (!password.equals(passwordConfirmation)) {
          result.rejectValue("passwordConfirmation", null, "Password confirmation doesn't match Password");
      }
  }
}
