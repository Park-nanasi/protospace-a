package in.tech_camp.protospace_a.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import in.tech_camp.protospace_a.validation.ValidationPriority1;
import in.tech_camp.protospace_a.validation.ValidationPriority2;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CommentFormUnitTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    //入力が空の場合
    @Test
    void titleが空ならバリデーションエラーになる() {
        CommentForm form = new CommentForm();
        form.setTitle("");
        form.setContent("有効なコメント内容");

        Set<ConstraintViolation<CommentForm>> violations = validator.validate(form, ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("コメントのタイトルを入力してください。", violations.iterator().next().getMessage());
    }

    @Test
    void contentが空ならバリデーションエラーになる() {
        CommentForm form = new CommentForm();
        form.setTitle("タイトルあり");
        form.setContent("");

        Set<ConstraintViolation<CommentForm>> violations = validator.validate(form, ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("コメント内容を入力してください。", violations.iterator().next().getMessage());
    }

    //文字数制限を超えた場合
    @Test
    void titleが129文字以上ならバリデーションエラーになる() {
        CommentForm form = new CommentForm();
        form.setTitle("a".repeat(129));
        form.setContent("正常なコメント");

        Set<ConstraintViolation<CommentForm>> violations = validator.validate(form, ValidationPriority2.class);
        assertEquals(1, violations.size());
        assertEquals("タイトルは128文字以内で入力してください。", violations.iterator().next().getMessage());
    }

    @Test
    void contentが1001文字以上ならバリデーションエラーになる() {
        CommentForm form = new CommentForm();
        form.setTitle("有効なタイトル");
        form.setContent("a".repeat(1001));

        Set<ConstraintViolation<CommentForm>> violations = validator.validate(form, ValidationPriority2.class);
        assertEquals(1, violations.size());
        assertEquals("コメント内容は1000文字以内で入力してください。", violations.iterator().next().getMessage());
    }

    //入力が正しい場合
    @Test
    void titleとcontentが有効ならバリデーションエラーにならない() {
        CommentForm form = new CommentForm();
        form.setTitle("正しいタイトル");
        form.setContent("適切なコメント内容");

        Set<ConstraintViolation<CommentForm>> violations1 = validator.validate(form, ValidationPriority1.class);
        Set<ConstraintViolation<CommentForm>> violations2 = validator.validate(form, ValidationPriority2.class);

        assertEquals(0, violations1.size());
        assertEquals(0, violations2.size());
    }
}
