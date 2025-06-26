package in.tech_camp.protospace_a.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CommentEntity {
  private Integer id;
  private String content;
  private UserEntity user;
  private PrototypeEntity prototype;
  private Timestamp createdAt;
}
