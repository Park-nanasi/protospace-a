package in.tech_camp.protospace_a.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import in.tech_camp.protospace_a.entity.SnsLinkEntity;
import in.tech_camp.protospace_a.entity.UserEntity;

@Mapper
public interface SnsLinksRepository {
  @Insert("INSERT INTO sns_links(x_url, facebook_url) VALUES (#{x}, #{facebook})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertSnsLink(SnsLinkEntity snsLink);

  @Select("SELECT * FROM sns_links WHERE id = #{id}")
  @Results(value = {@Result(property = "x", column = "x_url"),
      @Result(property = "facebook", column = "facebook_url")})
  SnsLinkEntity findById(Integer id);

  @Update("UPDATE sns_links SET x_url = #{x}, facebook_url = #{facebook} WHERE id = #{id}")
  void updateSnsLink(SnsLinkEntity snsLinks);
}
