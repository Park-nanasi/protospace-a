package in.tech_camp.protospace_a.form;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PrototypeForm {
  private String name;
  private String catchphrase;
  private String concept;
  private MultipartFile image;

  public void validatePrototypeForm(BindingResult result) {
    validateName(result);
    if (catchphrase == null || catchphrase.isEmpty()) {
      result.rejectValue("catchphrase", "error.Prototype", "Please enter either catchphrase");
    } else if (concept == null || concept.isEmpty()) {
      result.rejectValue("concept", "error.Prototype", "Please enter either concept");
    } else if (image == null || image.isEmpty()) {
      result.rejectValue("image", "error.Prototype", "Please enter either image");
    }
  }
  
  public void validateName(BindingResult result) {
    if ((name == null || name.isEmpty())) {
      result.rejectValue("name", "error.Prototype", "プロトタイプ名を入力してください");
      return;
    }

    if (50 < name.length()) {
      result.rejectValue("name", "name", "プロトタイプ名は 50 文字以内で設定してください");
    }
  }

    }
  }
}
