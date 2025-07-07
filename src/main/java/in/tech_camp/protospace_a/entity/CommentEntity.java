package in.tech_camp.protospace_a.entity;

import java.sql.Timestamp;

import lombok.Data;


@Data
public class CommentEntity {
  private Integer id;
  private String content;
  private Timestamp created_at;
  private UserEntity user;
  // private Timestamp createdAt;
  private Timestamp updated_at;
  private String title;
  private String image;
  private PrototypeEntity prototype;
}
