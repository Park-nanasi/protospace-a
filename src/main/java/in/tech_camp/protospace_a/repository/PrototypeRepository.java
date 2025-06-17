package in.tech_camp.protospace_a.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_a.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {
  // todo: user追加（user機能作成後）
  @Select("SELECT name, catchphrase, image FROM prototypes")
  List<PrototypeEntity> getAllPrototypes();
}
