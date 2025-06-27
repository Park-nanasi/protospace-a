package in.tech_camp.protospace_a.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_a.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {

  @Insert("INSERT INTO prototypes (name, catchphrase, concept, image, user_id, created_at, updated_at) VALUES (#{name}, #{catchphrase}, #{concept}, #{image}, #{user.id}, CURRENT_TIMESTAMP, '1970-01-01 00:00:01')")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertPrototype(PrototypeEntity prototype);

  @Select("SELECT p.id, p.name, p.catchphrase, p.image, u.id AS user_id, u.username AS user_name " +
          "FROM prototypes p " +
          "JOIN users u ON p.user_id = u.id " +
          "ORDER BY p.created_at DESC")
  @Results({
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "catchphrase", column = "catchphrase"),
      @Result(property = "image", column = "image"),
      @Result(property = "user.id", column = "user_id"),
      @Result(property = "user.username", column = "user_name")
  })
  List<PrototypeEntity> findAllPrototypes();

  
  @Select("SELECT p.*, u.id AS user_id, u.username AS user_name " +
          "FROM prototypes p " +
          "JOIN users u ON p.user_id = u.id " +
          "WHERE p.id = #{id}")
  @Results({
      @Result(property = "user.id", column = "user_id"),
      @Result(property = "user.username", column = "user_name")
  })
  PrototypeEntity findByPrototype(Integer id);

  @Delete("DELETE FROM prototypes WHERE id = #{id}")
  void deleteByPrototypeId(Integer id); 

  @Select("SELECT * FROM prototypes WHERE id = #{id}")
  @Results(value = {
    @Result(property = "id", column = "id"),
    @Result(property = "user", column = "user_id",
            one = @One(select = "in.tech_camp.protospace_a.repository.UserRepository.findById")),
    @Result(property = "comments", column = "id",
            many = @Many(select = "in.tech_camp.protospace_a.repository.CommentRepository.findByPrototypeId"))
  })
  PrototypeEntity findById(Integer id);

  @Select("SELECT * FROM prototypes WHERE user_id = #{userId}")
  @Results(value = {
    @Result(property = "user", column = "user_id",
            one = @One(select = "in.tech_camp.protospace_a.repository.UserRepository.findById"))
  })
  List<PrototypeEntity> findByUserId(Integer userId);

  @Update("UPDATE prototypes SET name = #{name}, catchphrase = #{catchphrase}, concept = #{concept}, image = #{image},  updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
  void updatePrototype(PrototypeEntity prototype);
}
