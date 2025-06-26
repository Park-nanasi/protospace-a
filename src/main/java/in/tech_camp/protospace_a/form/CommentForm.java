package in.tech_camp.protospace_a.form;

import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.protospace_a.validation.ValidationPriority1;
import in.tech_camp.protospace_a.validation.ValidationPriority2;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentForm {
  @NotBlank(message = "コメントのタイトルを入力してください。", groups = ValidationPriority1.class)
  @Size(max = 128, message = "タイトルは128文字以内で入力してください。", groups = ValidationPriority2.class)
  private String title;

  @NotBlank(message = "コメント内容を入力してください。", groups = ValidationPriority1.class)
  @Size(max = 1000, message = "コメント内容は1000文字以内で入力してください。", groups = ValidationPriority2.class)
  private String content;

  private MultipartFile image;
}
