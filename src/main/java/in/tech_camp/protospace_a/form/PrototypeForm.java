package in.tech_camp.protospace_a.form;

import in.tech_camp.protospace_a.validation.ValidationPriority1;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrototypeForm {
  @NotBlank(message="Error: Prototype.name can't blank")
  private String name;
  @NotBlank(message="Error: Prototype.catchphrase can't blank", groups = ValidationPriority1.class)
  private String catchphrase;
  @NotBlank(message="Error: Prototype.concept can't blank", groups = ValidationPriority1.class)
  private String concept;
  @NotBlank(message="Error: Prototype.image can't blank", groups = ValidationPriority1.class)
  private String image;
}
