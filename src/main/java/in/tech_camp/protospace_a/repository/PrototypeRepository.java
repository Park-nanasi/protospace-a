package in.tech_camp.protospace_a.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_a.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {
  @Insert("INSERT INTO prototypes (name, catchphrase, concept, image) VALUES (#{name}, #{catchphrase}, #{concept}, #{image}) ")
  void insertPrototype(PrototypeEntity prototype);

  // todo: user追加（user機能作成後）
  @Select("SELECT name, catchphrase, image FROM prototypes")
  List<PrototypeEntity> getAllPrototypes();

  @Delete("DELETE FROM prototypes WHERE id = #{id}")
  void deleteByPrototypeId(Integer id); 
}
