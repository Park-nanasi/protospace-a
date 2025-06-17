package in.tech_camp.protospace_a.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {

  private final PrototypeRepository prototypeRepository;

  @GetMapping("/")
  String showAllPrototypes() {
    List<PrototypeEntity> prototypes =  prototypeRepository.getAllPrototypes();
    System.out.println("prototypes:" + prototypes);
    return "tmp/test";
  }
}
