package in.tech_camp.protospace_a.entity;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class PrototypeLikeEntity {
  private Integer id;
  private Integer userId;
  private Integer prototypeId;
  private Timestamp createdAt;
  private PrototypeEntity prototype;
  private UserEntity user;
}