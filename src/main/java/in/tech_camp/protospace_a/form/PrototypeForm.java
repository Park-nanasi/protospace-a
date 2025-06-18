package in.tech_camp.protospace_a.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrototypeForm {
  @NotBlank(message="Error: Prototype.name can't blank")
  private String name;
  @NotBlank(message="Error: Prototype.catchphrase can't blank")
  private String catchphrase;
  @NotBlank(message="Error: Prototype.concept can't blank")
  private String concept;
  @NotBlank(message="Error: Prototype.image can't blank")
  private String image;
}
