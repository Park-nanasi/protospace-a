package in.tech_camp.protospace_a.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public class PrototypeFormUnitTest {

    private PrototypeForm form;

    @BeforeEach
    public void setup() {
        form = createValidPrototypeForm();
    }

    @Test
    public void 必要な項目がすべて入力されていればバリデーションエラーなし() {
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);

        assertTrue(!result.hasErrors());
    }

    @Test
    public void nameが空ならバリデーションエラー() {
        form.setName("");
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);

        assertTrue(result.hasFieldErrors("name"));
        assertEquals("Please enter either name", result.getFieldError("name").getDefaultMessage());
    }

    @Test
    public void catchphraseが空ならバリデーションエラー() {
        form.setCatchphrase("");
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);

        assertTrue(result.hasFieldErrors("catchphrase"));
        assertEquals("Please enter either catchphrase", result.getFieldError("catchphrase").getDefaultMessage());
    }

    @Test
    public void conceptが空ならバリデーションエラー() {
        form.setConcept("");
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);

        assertTrue(result.hasFieldErrors("concept"));
        assertEquals("Please enter either concept", result.getFieldError("concept").getDefaultMessage());
    }

    @Test
    public void imageがnullならバリデーションエラー() {
        form.setImage(null);
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);

        assertTrue(result.hasFieldErrors("image"));
        assertEquals("Please enter either image", result.getFieldError("image").getDefaultMessage());
    }

    @Test
    public void imageが空ファイルならバリデーションエラー() {
        form.setImage(new MockMultipartFile("image", new byte[0]));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);

        assertTrue(result.hasFieldErrors("image"));
        assertEquals("Please enter either image", result.getFieldError("image").getDefaultMessage());
    }

    // --- ヘルパーメソッド ---
    private PrototypeForm createValidPrototypeForm() {
        PrototypeForm form = new PrototypeForm();
        form.setName("プロトタイプ名");
        form.setCatchphrase("キャッチコピー");
        form.setConcept("コンセプトの説明");
        form.setImage(new MockMultipartFile("image", "test.png", "image/png", "dummy image content".getBytes()));
        return form;
    }
}
