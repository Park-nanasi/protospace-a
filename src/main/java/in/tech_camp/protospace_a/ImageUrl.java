package in.tech_camp.protospace_a;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ImageUrl {
  @Value("${image.url}")
  private String url;
  
  public String getUserProfileDefaultImageUrl() {
    return "/images/default.png";
  }

  public String getPrototypeImageUrl() {
    return url + "/prototypes/";
  }

  public String getCommentImageUrl() {
    return url + "/comments/";
  }

  public String getUserProfileImageUrl() {
    return url + "/user_profiles/";
  }

}
