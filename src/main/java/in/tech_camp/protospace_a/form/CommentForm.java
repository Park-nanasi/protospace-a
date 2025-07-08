package in.tech_camp.protospace_a.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentForm {
  @NotBlank(message = "コメントのタイトルを入力してください。")
  @Size(max = 128, message = "タイトルは128文字以内で入力してください。")
  private String title;

  @NotBlank(message = "コメント内容を入力してください。")
  @Size(max = 1000, message = "コメント内容は1000文字以内で入力してください。")
  private String content;
}
