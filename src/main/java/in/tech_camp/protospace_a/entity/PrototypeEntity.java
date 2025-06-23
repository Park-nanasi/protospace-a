package in.tech_camp.protospace_a.entity;

import java.util.List;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class PrototypeEntity {
  private Integer id;
  private String name;
  private String catchphrase;
  private String concept;
  private String image;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private UserEntity user;
  private List<CommentEntity> comments;
}
