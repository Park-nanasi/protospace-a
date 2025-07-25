package in.tech_camp.protospace_a.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void titleが空ならバリデーションエラーになる() {
        CommentForm form = new CommentForm();
        form.setTitle("");
        form.setContent("有効なコメント内容");

        Set<ConstraintViolation<CommentForm>> violations = validator.validate(form);
        assertEquals(1, violations.size());
        assertEquals("コメントのタイトルを入力してください。", violations.iterator().next().getMessage());
    }

    @Test
    void contentが空ならバリデーションエラーになる() {
        CommentForm form = new CommentForm();
        form.setTitle("タイトルあり");
        form.setContent("");

        Set<ConstraintViolation<CommentForm>> violations = validator.validate(form);
        assertEquals(1, violations.size());
        assertEquals("コメント内容を入力してください。", violations.iterator().next().getMessage());
    }

    @Test
    void titleが129文字以上ならバリデーションエラーになる() {
        CommentForm form = new CommentForm();
        form.setTitle("a".repeat(129));
        form.setContent("正常なコメント");

        Set<ConstraintViolation<CommentForm>> violations = validator.validate(form);
        assertEquals(1, violations.size());
        assertEquals("タイトルは128文字以内で入力してください。", violations.iterator().next().getMessage());
    }

    @Test
    void contentが1001文字以上ならバリデーションエラーになる() {
        CommentForm form = new CommentForm();
        form.setTitle("有効なタイトル");
        form.setContent("a".repeat(1001));

        Set<ConstraintViolation<CommentForm>> violations = validator.validate(form);
        assertEquals(1, violations.size());
        assertEquals("コメント内容は1000文字以内で入力してください。", violations.iterator().next().getMessage());
    }

    @Test
    void titleとcontentが有効ならバリデーションエラーにならない() {
        CommentForm form = new CommentForm();
        form.setTitle("正しいタイトル");
        form.setContent("適切なコメント内容");

        Set<ConstraintViolation<CommentForm>> violations = validator.validate(form);
        assertEquals(0, violations.size());
    }
}
