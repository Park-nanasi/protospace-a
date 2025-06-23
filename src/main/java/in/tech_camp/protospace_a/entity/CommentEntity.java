package in.tech_camp.protospace_a.entity;

import lombok.Data;

@Data
public class CommentEntity {
  private Integer id;
  private String content;
  private UserEntity user;
  private PrototypeEntity prototype;
}
