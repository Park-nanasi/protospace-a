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
    if ((name == null || name.isEmpty())) {
      result.rejectValue("name", "error.Prototype", "Please enter either name");
    } else if (catchphrase == null || catchphrase.isEmpty()) {
      result.rejectValue("catchphrase", "error.Prototype", "Please enter either catchphrase");
    } else if (concept == null || concept.isEmpty()) {
      result.rejectValue("concept", "error.Prototype", "Please enter either concept");
    } else if (image == null || image.isEmpty()) {
      result.rejectValue("image", "error.Prototype", "Please enter either image");
    }
  }
}
