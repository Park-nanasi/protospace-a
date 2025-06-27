package in.tech_camp.protospace_a.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    public void 必要な項目がすべて入力の場合成功() {
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);

        assertTrue(!result.hasErrors());
    }

    @Test
    public void nameが空の場合エラー() {
        form.setName("");
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        assertTrue(result.hasFieldErrors("name"));
        assertEquals("プロトタイプ名を入力してください", result.getFieldError("name").getDefaultMessage());
    }
    
    @Test
    public void nameの文字数が50文字の場合成功() {
        form.setName("a".repeat(50));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        assertFalse(result.hasFieldErrors("name"));
    }

    @Test
    public void nameの文字数が51文字の場合エラー() {
        form.setName("a".repeat(51));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        assertTrue(result.hasFieldErrors("name"));
        assertEquals("プロトタイプ名は 50 文字以内で入力してください", result.getFieldError("name").getDefaultMessage());
    }
    
    @Test
    public void catchphraseが空の場合エラー() {
        form.setCatchphrase("");
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        
        assertTrue(result.hasFieldErrors("catchphrase"));
        assertEquals("キャッチフレーズを入力してください", result.getFieldError("catchphrase").getDefaultMessage());
    }

    @Test
    public void catchphraseの文字数が128文字の場合成功() {
        form.setCatchphrase("a".repeat(128));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        assertFalse(result.hasFieldErrors("catchphrase"));
    }

    @Test
    public void catchphraseの文字数が129文字の場合エラー() {
        form.setCatchphrase("a".repeat(129));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        assertTrue(result.hasFieldErrors("catchphrase"));
        assertEquals("キャッチフレーズは 128 文字以内で入力してください", result.getFieldError("catchphrase").getDefaultMessage());
    }
    
    @Test
    public void conceptが空の場合エラー() {
        form.setConcept("");
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        
        assertTrue(result.hasFieldErrors("concept"));
        assertEquals("コンセプトを入力してください", result.getFieldError("concept").getDefaultMessage());
    }
    
    @Test
    public void conceptの文字数が512文字の場合成功() {
        form.setConcept("a".repeat(512));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        assertFalse(result.hasFieldErrors("concept"));
    }

    @Test
    public void conceptの文字数が513文字の場合エラー() {
        form.setConcept("a".repeat(513));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        assertTrue(result.hasFieldErrors("concept"));
        assertEquals("コンセプトは 512 文字以内で入力してください", result.getFieldError("concept").getDefaultMessage());
    }

    @Test
    public void imageがnullの場合エラー() {
        form.setImage(null);
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);

        assertTrue(result.hasFieldErrors("image"));
        assertEquals("画像を入力してください", result.getFieldError("image").getDefaultMessage());
    }

    @Test
    public void imageが空ファイルの場合エラー() {
        form.setImage(new MockMultipartFile("image", new byte[0]));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);

        assertTrue(result.hasFieldErrors("image"));
        assertEquals("画像を入力してください", result.getFieldError("image").getDefaultMessage());
    }

    @Test
    public void imageのurlの文字数が256文字の場合成功() {
        form.setImage(new MockMultipartFile("image", "a".repeat(252) + ".png", "image/png", "dummy image content".getBytes()));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        assertFalse(result.hasFieldErrors("image"));
    }

    @Test
    public void imageのurlの文字数が257文字の場合エラー() {
        form.setImage(new MockMultipartFile("image", "a".repeat(253) + ".png", "image/png", "dummy image content".getBytes()));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        assertTrue(result.hasFieldErrors("image"));
        assertEquals("画像URLは 256 文字以内で入力してください", result.getFieldError("image").getDefaultMessage());
    }
    
    @Test
    public void imageのファイルが10MBの場合成功() {
        form.setImage(new MockMultipartFile("image", new byte[10 * 1024 * 1024]));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        assertFalse(result.hasFieldErrors("image"));
    }

    @Test
    public void imageのファイルが11MBの場合エラー() {
        form.setImage(new MockMultipartFile("image", new byte[11 * 1024 * 1024]));
        BindingResult result = new BeanPropertyBindingResult(form, "prototypeForm");
        form.validatePrototypeForm(result);
        assertTrue(result.hasFieldErrors("image"));
        assertEquals("画像の最大メディア容量は10メガバイトまでです", result.getFieldError("image").getDefaultMessage());
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
