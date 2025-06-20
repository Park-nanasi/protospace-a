package in.tech_camp.protospace_a.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.form.PrototypeForm;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import lombok.AllArgsConstructor;



@Controller
@AllArgsConstructor
public class PrototypeController {

  private final UserRepository userRepository;
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

  @PostMapping("/prototypes/new")
  public String createPrototype(@ModelAttribute("prototypeForm") @Validated PrototypeForm prototypeForm, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
      if (bindingResult.hasErrors()) {
        List<String> errorMessages = bindingResult.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .collect(Collectors.toList());
        model.addAttribute("errorMessages", errorMessages);
        model.addAttribute("prototypeForm", prototypeForm);
        return "prototypes/new";
      }
      PrototypeEntity prototype = new PrototypeEntity();
      prototype.setName(prototypeForm.getName());
      prototype.setCatchphrase(prototypeForm.getCatchphrase());
      prototype.setConcept(prototypeForm.getConcept());
      // todo: 画像の保存先をどうするか
      prototype.setImage("prototype1.jpg");
      // prototype.setImage(prototypeForm.getImage());
      prototype.setUser(userRepository.findByUserId(currentUser.getId()));
      try {
        prototypeRepository.insertPrototype(prototype);
      } catch (Exception e) {
        System.out.println("Error：" + e);
        return "redirect:/";
      }
        return "redirect:/";
      }

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
  
  @GetMapping("/prototypes/{prototypeId}/update")
  public String updatePrototype(Model model) {
    // createPrototypeの挙動を確認するため
    PrototypeForm prototypeForm = new PrototypeForm();
    model.addAttribute("prototypeForm", prototypeForm);
    return "prototypes/edit";
  }

  @PostMapping("/prototypes/{prototypeId}/update")
  public String postMethodName(@PathVariable("prototypeId") Integer prototypeId, @ModelAttribute("prototypeForm") @Validated PrototypeForm prototypeForm, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
        model.addAttribute("errors", bindingResult.getAllErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.toList()));
        return "prototypes/edit";
    }

    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    prototype.setName(prototypeForm.getName());
    prototype.setConcept(prototypeForm.getConcept());
    prototype.setCatchphrase(prototypeForm.getCatchphrase());
    // prototype.setImage(prototypeForm.getImage());

    try {
      prototypeRepository.updatePrototype(prototype);
    } catch (Exception e) {
      System.err.println("Error: " + e);
      return "redirect:/";
    }

    System.out.println("Edit: :" + prototype);
    return "redirect:/";
  }  
}
