package in.tech_camp.protospace_a.form;

import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.protospace_a.validation.ValidationPriority1;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentForm {
  @NotBlank(message = "コメントのタイトルを入力してください。", groups = ValidationPriority1.class)
  private String title;

  @NotBlank(message = "コメント内容を入力してください。", groups = ValidationPriority1.class)
  private String content;

  private MultipartFile image;
}
