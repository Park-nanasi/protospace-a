package in.tech_camp.protospace_a.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_a.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {

    @Insert("INSERT INTO prototypes (name, catchphrase, concept, image, user_id, created_at) VALUES (#{name}, #{catchphrase}, #{concept}, #{image}, #{user.id}, CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPrototype(PrototypeEntity prototype);

  @Select("SELECT p.id, p.name, p.catchphrase, p.image, p.count_likes, u.id AS user_id, u.username AS user_name, u.profile_image AS profile_image " +
          "FROM prototypes p " +
          "JOIN users u ON p.user_id = u.id " +
          "ORDER BY p.created_at DESC")
  @Results({
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "catchphrase", column = "catchphrase"),
      @Result(property = "image", column = "image"),
      @Result(property = "created_at", column = "created_at"),
      @Result(property = "updated_at", column = "updated_at"),
      @Result(property = "count_likes", column = "count_likes"),
      @Result(property = "user.id", column = "user_id"),
      @Result(property = "user.username", column = "user_name"),
      @Result(property = "user.profileImage", column = "profile_image")
  })
  List<PrototypeEntity> findAllPrototypes();

  
  @Select("SELECT p.*, u.id AS user_id, u.username AS user_name " +
          "FROM prototypes p " +
          "JOIN users u ON p.user_id = u.id " +
          "WHERE p.id = #{id}")
  @Results({
      @Result(property = "created_at", column = "created_at"),
      @Result(property = "updated_at", column = "updated_at"),
      @Result(property = "user.id", column = "user_id"),
      @Result(property = "user.username", column = "user_name")
  })
  PrototypeEntity findByPrototype(Integer id);

  @Select("SELECT * FROM prototypes WHERE id = #{id}")
  @Results(value = {
    @Result(property = "name", column = "name"),
    @Result(property = "created_at", column = "created_at"),
    @Result(property = "updated_at", column = "updated_at"),
    @Result(property = "id", column = "id"),
    @Result(property = "user", column = "user_id",
    one = @One(select = "in.tech_camp.protospace_a.repository.UserRepository.findById")),
    @Result(property = "comments", column = "id",
    many = @Many(select = "in.tech_camp.protospace_a.repository.CommentRepository.findByPrototypeId"))
})
PrototypeEntity findById(Integer id);

@Select("SELECT * FROM prototypes WHERE user_id = #{userId} ORDER BY created_at DESC")
@Results(value = {
    @Result(property = "name", column = "name"),
    @Result(property = "created_at", column = "created_at"),
    @Result(property = "updated_at", column = "updated_at"),
    @Result(property = "user", column = "user_id",
            one = @One(select = "in.tech_camp.protospace_a.repository.UserRepository.findById"))
  })
  List<PrototypeEntity> findByUserId(Integer userId);

  @Update("UPDATE prototypes SET name = #{name}, catchphrase = #{catchphrase}, concept = #{concept}, image = #{image},  updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
  @Results(value = {
    @Result(property = "updated_at", column = "updated_at")
  })
  void updatePrototype(PrototypeEntity prototype);

  @Delete("DELETE FROM prototypes WHERE id = #{id}")
  void deleteByPrototypeId(Integer id);

    // prototype一覧ページに検索機能表示
    @Select("SELECT * FROM prototypes WHERE name ILIKE CONCAT('%', #{name}, '%')")
    @Results(value = {@Result(property = "user", column = "user_id", one = @One(
            select = "in.tech_camp.protospace_a.repository.UserRepository.findById"))})
    List<PrototypeEntity> findByNameContaining(String name);

    // ユーザーの詳細ページにて検索機能
    @Select("SELECT p.*, u.id as user_id, u.username as user_name "
            + "FROM prototypes p " + "LEFT JOIN users u ON p.user_id = u.id "
            + "WHERE p.user_id = #{userId} AND p.name ILIKE CONCAT('%', #{name}, '%')")
    @Results({@Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "user.id", column = "user_id"),
            @Result(property = "user.username", column = "username")})
    List<PrototypeEntity> findByUserIdAndNameContaining(
            @Param("userId") Integer userId, @Param("name") String name);

    //   「いいね」数＋１
    @Update("UPDATE prototypes SET count_likes = count_likes + 1 WHERE id = #{prototypeId}")
    @Results(value = {
        @Result(property = "count_likes", column = "count_likes")
      })
    int incrementLikeCount(@Param("prototypeId") Integer prototypeId);

    // 「いいね」数ー１
    @Update("UPDATE prototypes SET count_likes = GREATEST(count_likes - 1, 0) WHERE id = #{prototypeId}")
    @Results(value = {
        @Result(property = "count_likes", column = "count_likes")
      })
    int decrementLikeCount(@Param("prototypeId") Integer prototypeId);
}
