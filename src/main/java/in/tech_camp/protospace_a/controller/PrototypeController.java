package in.tech_camp.protospace_a.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.form.PrototypeForm;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import lombok.AllArgsConstructor;



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
  public String showTargetPrototype(@PathVariable("prototypeId") Integer id, Model model) {
    PrototypeEntity prototype = prototypeRepository.findByPrototype(id);
    model.addAttribute("prototype", prototype);
    
    // commentForm追加する？
    return "prototypes/detail";
  }

  @GetMapping("/prototypes/new")
  public String showPrototypeForm(Model model) {
    PrototypeForm prototypeForm = new PrototypeForm();
    model.addAttribute("prototypeForm", prototypeForm);
    return "prototypes/new";
  }

  /*@PostMapping("/prototypes/new")
  public String createPrototype() {
      //TODO: process POST request
      // todo:　プロトタイプ投稿機能の統合
      return entity;
  }*/

  @GetMapping("/prototypes/{prototypeId}/delete")
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
