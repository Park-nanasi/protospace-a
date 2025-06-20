package in.tech_camp.protospace_a.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TextController {

  private final PrototypeRepository prototypeRepository;

  @GetMapping("/")
    public String showAllPrototypes(Model model) {
    List<PrototypeEntity> prototypes =  prototypeRepository.findAllPrototypes();
    model.addAttribute("prototypes", prototypes);
    return "prototypes/index";


  @GetMapping("/detail")
  public String showProtoDetail(Model model) {
      // 必要があれば model に属性を追加できます
      return "prototypes/detail";
  }

  @GetMapping("/users/1")
  public String showUserPage(Model model) {
      // 必要があれば model に属性を追加できます
      return "users/userInfo";
  }
}