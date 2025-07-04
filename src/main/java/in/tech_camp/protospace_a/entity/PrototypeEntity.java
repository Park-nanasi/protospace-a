package in.tech_camp.protospace_a.entity;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class PrototypeEntity {
  private Integer id;
  private String name;
  private String catchphrase;
  private String concept;
  private String image;
  private Timestamp created_at;
  private Timestamp updated_at;
  private Integer count_likes;
  private UserEntity user;
  private List<CommentEntity> comments;
}
