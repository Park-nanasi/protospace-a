package in.tech_camp.protospace_a.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.form.PrototypeForm;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@AllArgsConstructor
public class PrototypeController {

  private final PrototypeRepository prototypeRepository;

  @GetMapping("/")
  public String showAllPrototypes(Model model) {
    List<PrototypeEntity> prototypes =  prototypeRepository.findAllPrototypes();
    model.addAttribute("prototypes", prototypes);
    return "prototypes/index";
  }

  @GetMapping("/prototypes/{prototypeId}")
  public String get(@PathVariable("prototypeId") Integer id, Model model) {
    PrototypeEntity prototype = prototypeRepository.findByPrototype(id);
    model.addAttribute("prototype", prototype);
    
    // commentForm追加する？
    return "prototypes/detail";
  }

  @GetMapping("/prototype/{prototypeId}/delete")
  public String deletePrototype(@PathVariable("prototypeId") Integer prototypeId) {
    try {
      prototypeRepository.deleteByPrototypeId(prototypeId);
    } catch (Exception e) {
      System.err.println("Error: " + e);
      return "redirect:/";
    }
    return "redirect:/";
  }

}
