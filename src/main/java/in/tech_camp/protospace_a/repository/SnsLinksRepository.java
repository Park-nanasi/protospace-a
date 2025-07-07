package in.tech_camp.protospace_a.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_a.entity.SnsLinkEntity;

@Mapper
public interface SnsLinksRepository {
  @Insert("INSERT INTO sns_links(x_url, facebook_url) VALUES (#{x}, #{facebook})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertSnsLink(SnsLinkEntity snsLink);
  
  @Select("SELECT * FROM sns_links WHERE id = #{id}")
  SnsLinkEntity findSnsLink(Integer id);
}
