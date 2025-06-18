package in.tech_camp.protospace_a.entity;

import java.security.Timestamp;

import lombok.Data;

@Data
public class PrototypeEntity {
  private Integer id;
  // private String name;
  private String catchphrase;
  private String concept;
  private String images;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private Integer userId;
  private UserEntity user;
}
