package in.tech_camp.protospace_a.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import in.tech_camp.protospace_a.repository.UserRepository;
import in.tech_camp.protospace_a.validation.ValidationPriority1;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@SpringBootTest
public class UserFormUnitTest {

    private Validator validator;

    @Autowired
    private UserRepository userRepository;

    private UserForm form;
    private BindingResult result;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        form = createValidUserForm();
        result = new BeanPropertyBindingResult(form, "userForm");
    }

    @Test
    public void 入力が正しい場合成功() {
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertFalse(result.hasFieldErrors("username"));
        assertFalse(result.hasFieldErrors("email"));
        assertFalse(result.hasFieldErrors("password"));
        assertFalse(result.hasFieldErrors("profile"));
    }

    @Test
    public void emailが空の場合エラー() {
        form.setEmail("");
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("email"));
        assertEquals("メールアドレスを入力してください", result.getFieldError("email").getDefaultMessage());
    }
    
    @Test
    public void emailにアットマークが含まれていない場合エラー() {
        form.setEmail("invalid-email");
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("email"));
        assertEquals("メールアドレスには @ を1つだけ含めてください", result.getFieldError("email").getDefaultMessage());
    }

    @Test
    public void emailが英字数字ドット以外の文字が含まれている場合エラー() {
        form.setEmail("t?est@test.com");
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("email"));
        assertEquals("ASCII 文字 (a-z)、数字 (0-9)、およびピリオド (.) のみが使用できます", result.getFieldError("email").getDefaultMessage());
    }

    @Test
    public void emailの最初の文字が英字数字以外の場合エラー() {
        form.setEmail(".test@test.com");
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("email"));
        assertEquals("メールアドレスの最初の文字は、ASCII 文字（a-z）または数字（0-9）にする必要があります", result.getFieldError("email").getDefaultMessage());
    }

    @Test
    public void emailの最後の文字が英字数字以外の場合エラー() {
        form.setEmail("test@test.com.");
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("email"));
        assertEquals("メールアドレスの最後の文字は、ASCII 文字（a-z）または数字（0-9）にする必要があります", result.getFieldError("email").getDefaultMessage());
    }

    @Test
    public void emailのドットが連続して含まれている場合エラー() {
        form.setEmail("test@test..com");
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("email"));
        assertEquals("ピリオドを連続して使用することはできません", result.getFieldError("email").getDefaultMessage());
    }

    @Test
    public void passwordが空の場合エラー() {
        form.setPassword("");
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("password"));
        assertEquals("パスワードを入力してください", result.getFieldError("password").getDefaultMessage());
    }

    @Test
    public void passwordが5文字の場合エラー() {
        form.setPassword("1".repeat(5));
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("password"));
        assertEquals("パスワードは 6 文字以上で設定してください", result.getFieldError("password").getDefaultMessage());
    }

    @Test
    public void passwordが6文字の場合成功() {
        form.setPassword("1aA".repeat(6));
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertFalse(result.hasFieldErrors("password"));
    }

    @Test
    public void passwordが128文字の場合成功() {
        form.setPassword("1".repeat(126) + "a" + "A");
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertFalse(result.hasFieldErrors("password"));
    }
    
    @Test
    public void passwordが129文字の場合エラー() {
        form.setPassword("1".repeat(129));
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("password"));
        assertEquals("パスワードは 128 文字以下で設定してください",  result.getFieldError("password").getDefaultMessage());
    }

    @Test
    public void passwordが小文字のみの場合エラー() {
        form.setPassword("a".repeat(6));
        form.setPasswordConfirmation("a".repeat(6));
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("password"));
        assertEquals("より強力なパスワードを選択してください。英小文字・英大文字・数字をそれぞれ1文字以上含めてください", result.getFieldError("password").getDefaultMessage());
    }

    @Test
    public void passwordが大文字のみの場合エラー() {
        form.setPassword("A".repeat(6));
        form.setPasswordConfirmation("A".repeat(6));
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("password"));
        assertEquals("より強力なパスワードを選択してください。英小文字・英大文字・数字をそれぞれ1文字以上含めてください", result.getFieldError("password").getDefaultMessage());
    }

    @Test
    public void passwordが数字のみの場合エラー() {
        form.setPassword("1".repeat(6));
        form.setPasswordConfirmation("1".repeat(6));
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("password"));
        assertEquals("より強力なパスワードを選択してください。英小文字・英大文字・数字をそれぞれ1文字以上含めてください", result.getFieldError("password").getDefaultMessage());
    }

    @Test
    public void passwordConfirmationが空の場合エラー() {
        form.setPasswordConfirmation("");
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("passwordConfirmation"));
        assertEquals("確認用のパスワードを入力してください", result.getFieldError("passwordConfirmation").getDefaultMessage());
    }

    @Test
    public void passwordとpasswordConfirmationが一致しない場合エラー() {
        form.setPassword("techcamp123");
        form.setPasswordConfirmation("different");
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("passwordConfirmation"));
        assertEquals("パスワードが一致しませんでした。もう一度お試しください。", result.getFieldError("passwordConfirmation").getDefaultMessage());
    }

    @Test
    public void usernameが空の場合エラー() {
        form.setUsername("");
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("username"));
        assertEquals("ユーザー名を入力してください", result.getFieldError("username").getDefaultMessage());
    }

    @Test
    public void usernameの文字数が30文字の場合成功() {
        form.setUsername("あ".repeat(30));
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertFalse(result.hasFieldErrors("username"));
    }

    @Test
    public void usernameの文字数が31文字の場合エラー() {
        form.setUsername("あ".repeat(31));
        result = new BeanPropertyBindingResult(form, "userForm");
        form.validateUserForm(result);
        assertTrue(result.hasFieldErrors("username"));
        assertEquals("ユーザー名は 30 文字で指定してください", result.getFieldError("username").getDefaultMessage());
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
        UserForm form = new UserForm(userRepository);
        form.setEmail("test@example.com");
        form.setPassword("1aA".repeat(6));
        form.setPasswordConfirmation("1aA".repeat(6));
        form.setUsername("TestUser");
        form.setProfile("テストプロフィール");
        form.setCompany("TechCamp");
        form.setRole("エンジニア");
        return form;
    }
}
