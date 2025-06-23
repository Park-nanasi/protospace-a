package in.tech_camp.protospace_a.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import in.tech_camp.protospace_a.validation.ValidationPriority1;
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
    public void contentが空ならバリデーションエラーになる() {
        CommentForm form = new CommentForm();
        form.setContent("");

        Set<ConstraintViolation<CommentForm>> violations = validator.validate(form, ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Comment can't be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void contentが適切に入力されていればバリデーションエラーにならない() {
        CommentForm form = new CommentForm();
        form.setContent("This is a valid comment.");

        Set<ConstraintViolation<CommentForm>> violations = validator.validate(form, ValidationPriority1.class);
        assertEquals(0, violations.size());
    }
}
