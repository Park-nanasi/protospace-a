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
    validateCatchphrase(result);
    validateConcept(result);
    if (image == null || image.isEmpty()) {
      result.rejectValue("image", "error.Prototype", "Please enter either image");
    }
  }
  
  public void validateName(BindingResult result) {
    if ((name == null || name.isEmpty())) {
      result.rejectValue("name", "error.Prototype", "プロトタイプ名を入力してください");
      return;
    }

    if (50 < name.length()) {
      result.rejectValue("name", "name", "プロトタイプ名は 50 文字以内で入力してください");
    }
  }

  public void validateCatchphrase(BindingResult result) {
    if (catchphrase == null || catchphrase.isEmpty()) {
      result.rejectValue("catchphrase", "catchphrase", "キャッチフレーズを入力してください");
      return;
    }
    
    if (128 < catchphrase.length()) {
      result.rejectValue("catchphrase", "catchphrase", "キャッチフレーズは 128 文字以内で入力してください");
    }
  }

  public void validateConcept(BindingResult result) {
    if (concept == null || concept.isEmpty()) {
      result.rejectValue("concept", "concept", "コンセプトを入力してください");
    } 
    
    if (512 < concept.length()) {
      result.rejectValue("concept", "concept", "コンセプトは 512 文字以内で入力してください");
    }
  }
  
}
