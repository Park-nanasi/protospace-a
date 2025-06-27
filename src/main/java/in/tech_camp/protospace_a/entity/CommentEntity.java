package in.tech_camp.protospace_a.entity;

import lombok.Data;
import java.sql.Timestamp;


@Data
public class CommentEntity {
  private Integer id;
  private String content;
  private Timestamp created_at;
  private UserEntity user;
  private PrototypeEntity prototype;
}
