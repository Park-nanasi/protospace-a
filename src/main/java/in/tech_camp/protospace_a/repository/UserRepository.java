package in.tech_camp.protospace_a.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_a.entity.UserEntity;

@Mapper
public interface UserRepository {
    @Insert("INSERT INTO users (sns_links_id, username, email, password, profile, profile_image) VALUES (#{snsLinksId}, #{username}, #{email}, #{password}, #{profile}, #{profileImage})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserEntity user);

    @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
    boolean existsByEmail(String email);

    @Select("SELECT * FROM users WHERE email = #{email}")
    UserEntity findByEmail(String email);

    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results(value = {
        @Result(property = "id", column = "id"),
        @Result(property = "snsLinksId", column = "sns_links_id"),
        @Result(property = "profileImage", column = "profile_image"),
        @Result(property = "prototypes", column = "id", many = @Many(
                    select = "in.tech_camp.protospace_a.repository.PrototypeRepository.findByUserId"))})
    UserEntity findById(Integer id);

    @Select("SELECT * FROM users WHERE id = #{id}")
    UserEntity findByUserId(Integer id);


    @Update("UPDATE users SET username = #{username}, profile = #{profile}, profile_image = #{profileImage} WHERE id = #{id}")
    void updateUser(UserEntity user);

}
