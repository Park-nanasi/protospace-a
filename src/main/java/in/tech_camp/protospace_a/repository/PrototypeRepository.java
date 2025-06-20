package in.tech_camp.protospace_a.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_a.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {
  @Insert("INSERT INTO prototypes (name, catchphrase, concept, image) VALUES (#{name}, #{catchphrase}, #{concept}, #{image}) ")
  void insertPrototype(PrototypeEntity prototype);

  @Select("SELECT p.name, p.catchphrase, p.image, u.id AS user_id, u.username AS user_name " +
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

  @Delete("DELETE FROM prototypes WHERE id = #{id}")
  void deleteByPrototypeId(Integer id); 

  @Update("UPDATE prototypes SET name = #{name}, catchphrase = #{catchphrase}, concept = #{concept}, image = #{image} WHERE id = #{id}")
  void updatePrototype(PrototypeEntity prototype);
}
