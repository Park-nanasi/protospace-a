package in.tech_camp.protospace_a.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import in.tech_camp.protospace_a.validation.ValidationPriority1;
import in.tech_camp.protospace_a.validation.ValidationPriority2;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserFormUnitTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void 入力が全て正しい場合バリデーションエラーにならない() {
        UserForm form = createValidUserForm();

        // グループ1（必須チェックなど）とグループ2（形式や文字数チェックなど）の両方を検証
        Set<ConstraintViolation<UserForm>> violationsPriority1 = validator.validate(form, ValidationPriority1.class);
        Set<ConstraintViolation<UserForm>> violationsPriority2 = validator.validate(form, ValidationPriority2.class);

        assertTrue(violationsPriority1.isEmpty(), "ValidationPriority1でバリデーションエラーが発生しています");
        assertTrue(violationsPriority2.isEmpty(), "ValidationPriority2でバリデーションエラーが発生しています");

        // パスワード確認の手動バリデーション
        BindingResult result = new BeanPropertyBindingResult(form, "userForm");
        form.validatePassword(result);
        assertTrue(!result.hasFieldErrors("passwordConfirmation"), "パスワード確認でバリデーションエラーが発生しています");
    }

    @Test
    public void emailが空ならバリデーションエラーになる() {
        UserForm form = createValidUserForm();
        form.setEmail("");

        Set<ConstraintViolation<UserForm>> violations = validator.validate(form, ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Email can't be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void emailが不正な形式ならバリデーションエラーになる() {
        UserForm form = createValidUserForm();
        form.setEmail("invalid-email");

        Set<ConstraintViolation<UserForm>> violations = validator.validate(form, ValidationPriority2.class);
        assertEquals(1, violations.size());
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }

    @Test
    public void passwordが空ならバリデーションエラーになる() {
        UserForm form = createValidUserForm();
        form.setPassword("");

        Set<ConstraintViolation<UserForm>> violations = validator.validate(form, ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Password can't be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void passwordが6文字未満ならバリデーションエラーになる() {
        UserForm form = createValidUserForm();
        form.setPassword("123");

        Set<ConstraintViolation<UserForm>> violations = validator.validate(form, ValidationPriority2.class);
        assertEquals(1, violations.size());
        assertEquals("Password should be between 6 and 128 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void passwordとpasswordConfirmationが一致しない場合エラーになる() {
        UserForm form = createValidUserForm();
        form.setPassword("techcamp123");
        form.setPasswordConfirmation("different");

        BindingResult result = new BeanPropertyBindingResult(form, "userForm");
        form.validatePassword(result);

        assertTrue(result.hasFieldErrors("passwordConfirmation"));
        assertEquals("Password confirmation doesn't match Password",
                result.getFieldError("passwordConfirmation").getDefaultMessage());
    }

    @Test
    public void usernameが空ならバリデーションエラーになる() {
        UserForm form = createValidUserForm();
        form.setUsername("");

        Set<ConstraintViolation<UserForm>> violations = validator.validate(form, ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Username can't be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void profileが空ならバリデーションエラーになる() {
        UserForm form = createValidUserForm();
        form.setProfile("");

        Set<ConstraintViolation<UserForm>> violations = validator.validate(form, ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Profile can't be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void companyが空ならバリデーションエラーになる() {
        UserForm form = createValidUserForm();
        form.setCompany("");

        Set<ConstraintViolation<UserForm>> violations = validator.validate(form, ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Company can't be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void roleが空ならバリデーションエラーになる() {
        UserForm form = createValidUserForm();
        form.setRole("");

        Set<ConstraintViolation<UserForm>> violations = validator.validate(form, ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Role can't be blank", violations.iterator().next().getMessage());
    }

    private UserForm createValidUserForm() {
        UserForm form = new UserForm();
        form.setEmail("test@example.com");
        form.setPassword("techcamp123");
        form.setPasswordConfirmation("techcamp123");
        form.setUsername("TestUser");
        form.setProfile("テストプロフィール");
        form.setCompany("TechCamp");
        form.setRole("エンジニア");
        return form;
    }
}
