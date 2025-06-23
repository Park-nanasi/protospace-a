package in.tech_camp.protospace_a.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.protospace_a.ImageUrl;
import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.form.CommentForm;
import in.tech_camp.protospace_a.form.PrototypeForm;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import lombok.AllArgsConstructor;



@Controller
@AllArgsConstructor
public class PrototypeController {

  private final UserRepository userRepository;
  private final PrototypeRepository prototypeRepository;
  private final ImageUrl imageUrl;

  @GetMapping("/")
  public String showAllPrototypes(Model model) {
    List<PrototypeEntity> prototypes =  prototypeRepository.findAllPrototypes();
    model.addAttribute("prototypes", prototypes);
    return "prototypes/index";
  }

  @GetMapping("/prototypes/{prototypeId}")
  public String showTargetPrototype(@PathVariable("prototypeId") Integer prototypeId, Model model) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    CommentForm commentForm = new CommentForm();
    model.addAttribute("prototype", prototype);
    model.addAttribute("commentForm", commentForm);
    if (prototype != null) {
      model.addAttribute("comments", prototype.getComments());
    }
    model.addAttribute("errorMessages", null);
    return "prototypes/detail";
  }

  @GetMapping("/prototypes/new")
  public String showPrototypeForm(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    PrototypeForm prototypeForm = new PrototypeForm();
    model.addAttribute("prototypeForm", prototypeForm);
    return "prototypes/new";
  }

  @PostMapping("/prototypes/new")
  public String createPrototype(@ModelAttribute("prototypeForm") @Validated PrototypeForm prototypeForm, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    prototypeForm.validatePrototypeForm(bindingResult);
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
  
    MultipartFile imageFile = prototypeForm.getImage();
    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        String uploadDir = imageUrl.getImageUrl();
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.copy(imageFile.getInputStream(), imagePath);
        prototype.setImage(fileName);
      } catch (IOException e) {
        System.out.println("Error：" + e);
        return "prototypes/new";
      }
    }
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
  public String deletePrototype(@PathVariable("prototypeId") Integer prototypeId, @AuthenticationPrincipal CustomUserDetail currentUser) {
     PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    if (prototype.getUser().getId() != currentUser.getId()) {
        return "redirect:/";
    }
    try {
      prototypeRepository.deleteByPrototypeId(prototypeId);
    } catch (Exception e) {
      System.err.println("Error: " + e);
      return "redirect:/";
    }
    return "redirect:/";
  }
  
  @GetMapping("/prototypes/{prototypeId}/edit")
  public String updatePrototype(@PathVariable("prototypeId") Integer prototypeId, @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    if (prototype.getUser().getId() != currentUser.getId()) {
        return "redirect:/";
    }

    PrototypeForm prototypeForm = new PrototypeForm();
    prototypeForm.setName(prototype.getName());
    prototypeForm.setCatchphrase(prototype.getCatchphrase());
    prototypeForm.setConcept(prototype.getConcept());

    model.addAttribute("prototypeForm", prototypeForm);
    model.addAttribute("prototypeId", prototypeId);
    return "prototypes/edit";
  }

  @PostMapping("/prototypes/{prototypeId}/update")
  public String postMethodName(@PathVariable("prototypeId") Integer prototypeId, @ModelAttribute("prototypeForm") @Validated PrototypeForm prototypeForm, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    if (prototype.getUser().getId() != currentUser.getId()) {
        return "redirect:/";
    }
    prototypeForm.validatePrototypeForm(bindingResult);
    if (bindingResult.hasErrors()) {
        model.addAttribute("errors", bindingResult.getAllErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.toList()));
        return "redirect:/prototypes/" + prototypeId + "/edit";
    }

    prototype.setName(prototypeForm.getName());
    prototype.setConcept(prototypeForm.getConcept());
    prototype.setCatchphrase(prototypeForm.getCatchphrase());

    MultipartFile imageFile = prototypeForm.getImage();
    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        String uploadDir = imageUrl.getImageUrl();
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.copy(imageFile.getInputStream(), imagePath);
        prototype.setImage(fileName);
      } catch (IOException e) {
        System.out.println("Error：" + e);
        return "redirect:/prototypes/" + prototypeId + "/edit";
      }
    }

    try {
      prototypeRepository.updatePrototype(prototype);
    } catch (Exception e) {
      System.err.println("Error: " + e);
      return "redirect:/prototypes/" + prototypeId;
    }
    return "redirect:/prototypes/" + prototypeId;
  }
}
