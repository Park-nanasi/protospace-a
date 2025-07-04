package in.tech_camp.protospace_a.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_a.entity.CommentEntity;

@Mapper
public interface CommentRepository {
  @Select("SELECT c.*, u.id AS user_id, u.username AS user_username FROM comments c JOIN users u ON c.user_id = u.id WHERE c.prototype_id = #{prototypeId}")
  @Results(value = {@Result(property = "user.id", column = "user_id"),
      @Result(property = "user.username", column = "user_username"),
      @Result(property = "created_at", column = "created_at"),
      @Result(property = "updated_at", column = "updated_at"),
      @Result(property = "prototype", column = "prototype_id", one = @One(
          select = "in.tech_camp.protospace_a.repository.PrototypeRepository.findById"))})
  List<CommentEntity> findByPrototypeId(Integer prototypeId);

  @Select("SELECT c.*, u.id AS user_id, u.username AS user_username, u.profile_image AS profile_image "
      + "FROM comments c " + "JOIN users u ON c.user_id = u.id "
      + "WHERE c.id = #{id}")
  @Results(value = {@Result(property = "id", column = "id"),
      @Result(property = "content", column = "content"),
      // @Result(property = "createdAt", column = "created_at"),
      @Result(property = "created_at", column = "created_at"),
      @Result(property = "updated_at", column = "updated_at"),
      @Result(property = "user.id", column = "user_id"),
      @Result(property = "user.username", column = "user_username"),
      @Result(property = "user.profileImage", column = "profile_image"),
      @Result(property = "prototype", column = "prototype_id", one = @One(
          select = "in.tech_camp.protospace_a.repository.PrototypeRepository.findById"))})
  CommentEntity findById(Integer id);

  @Insert("INSERT INTO comments (title, image, content, user_id, prototype_id, created_at) VALUES (#{title}, #{image}, #{content}, #{user.id}, #{prototype.id}, now())")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(CommentEntity comment);

  @Update("UPDATE comments SET title = #{title}, content = #{content}, image = #{image}, created_at = #{created_at}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
  void update(CommentEntity comment);

  @Delete("DELETE FROM comments WHERE id = #{id}")
  void deleteById(Integer id);
}
