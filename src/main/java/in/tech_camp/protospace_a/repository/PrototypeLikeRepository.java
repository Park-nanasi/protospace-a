package in.tech_camp.protospace_a.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Param;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.entity.PrototypeLikeEntity;


@Mapper
public interface PrototypeLikeRepository {
  // 「いいね」入れる
  @Insert("INSERT INTO prototype_likes (user_id, prototype_id, created_at) VALUES (#{userId}, #{prototypeId}, NOW())")
  void insert(PrototypeLikeEntity like);

  @Select("SELECT * FROM prototype_likes WHERE id = #{id}")
  @Results(value = {
    @Result(property = "created_at", column = "created_at"),
    @Result(property = "updated_at", column = "updated_at"),
    @Result(property = "id", column = "id"),
    @Result(property = "countLike", column = "count_like"),
    @Result(property = "user", column = "user_id",
            one = @One(select = "in.tech_camp.protospace_a.repository.UserRepository.findById")),
    @Result(property = "prototype", column = "prototype_id", one = @One(
          select = "in.tech_camp.protospace_a.repository.PrototypeRepository.findById"))})
  PrototypeLikeEntity findById(Integer id);

  
  // 「いいね」削除
  @Delete("DELETE FROM prototype_likes WHERE id = #{id}")
  void deleteByUserAndPrototype(Integer id);

  // ユーザーが「いいね」押したかどうかを検索する
  @Select("SELECT COUNT(*) FROM prototype_likes WHERE user_id = #{userId} AND prototype_id = #{prototypeId}")
  int existsByUserAndPrototype(@Param("userId") Integer userId, @Param("prototypeId") Integer PrototypeId);

  // 「いいね」数を記録
  @Select("SELECT COUNT(*) FROM prototype_likes WHERE prototype_id = #{prototypeId}")
  int countByPrototypeId(@Param("prototypeId") Integer prototypeId);

  // 「いいね」詳細記録
  @Select("SELECT id, user_id, prototype_id, created_at FROM prototype_likes WHERE prototype_id = #{prototypeId} ORDER BY created_at DESC")
  @Results({
    @Result(property = "id", column="id"),
    @Result(property = "userId", column = "user_id"),
    @Result(property = "prototypeId", column = "prototype_id"),
    @Result(property = "createAt", column= "created_at")
  })
  List<PrototypeLikeEntity> selectByPrototypeId(@Param("prototypeId") Integer prototypeId);

  @Select("SELECT * FROM prototype_likes WHERE user_id = #{userId} AND prototype_id = #{prototypeId} LIMIT 1")
  PrototypeLikeEntity selectByUserAndPrototype(@Param("userId") Integer userId, @Param("prototypeId") Integer prototypeId);

  // ユーザーが「いいね」をしたprototypeを取り出す
  // @Select("SELECT p.* FROM prototypes p INNER JOIN prototype_likes pl ON p.id = pl.prototype_id WHERE pl.user_id = #{userId}")
  @Select("""
  SELECT p.*, u.id as u_id, u.username as u_username 
  FROM prototypes p 
  INNER JOIN prototype_likes pl ON p.id = pl.prototype_id 
  INNER JOIN users u ON p.user_id = u.id 
  WHERE pl.user_id = #{userId} ORDER BY p.created_at DESC
""")
  @Results({
    @Result(property = "id", column = "id"),
    @Result(property = "name", column = "name"),
    @Result(property = "image", column = "image"),
    @Result(property = "catchphrase", column = "catchphrase"),
    @Result(property = "user.id", column = "u_id"),
    @Result(property = "user.username", column = "u_username")
})
  List<PrototypeEntity> findLikedPrototypesByUser(@Param("userId") Integer userId);

  @Select("""
  SELECT p.*, u.id as user_id, u.username as u_username FROM prototypes p
  INNER JOIN prototype_likes pl ON p.id = pl.prototype_id
  INNER JOIN users u ON p.user_id = u.id
  WHERE pl.user_id = #{userId} AND p.name ILIKE CONCAT('%', #{name}, '%')
""")
@Results({
  @Result(property = "id", column = "id"),
  @Result(property = "name", column = "name"),
  @Result(property = "image", column = "image"),
  @Result(property = "catchphrase", column = "catchphrase"),
  @Result(property = "user.id", column = "u_id"),
  @Result(property = "user.username", column = "u_username")
})
List<PrototypeEntity> findLikedPrototypesByUserAndName(@Param("userId") Integer userId, @Param("name") String name);
}