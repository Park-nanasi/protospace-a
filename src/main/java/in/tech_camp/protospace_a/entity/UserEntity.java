package in.tech_camp.protospace_a.entity;

import java.util.List;

import lombok.Data;

@Data
public class UserEntity {
  private Integer id;
  private Integer snsLinksId;
  private String username;
  private String email;
  private String password;
  private String profile;
  private String profileImage;
  private List<PrototypeEntity> prototypes;
  private List<CommentEntity> comments;
}
