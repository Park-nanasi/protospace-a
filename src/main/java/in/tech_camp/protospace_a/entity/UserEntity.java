package in.tech_camp.protospace_a.entity;

import java.util.List;

import lombok.Data;

@Data
public class UserEntity {
  private Integer id;
  private String username;
  private String email;
  private String password;
  private String profile;
  private String company;
  private String role;
  private List<PrototypeEntity> prototypes;
}
