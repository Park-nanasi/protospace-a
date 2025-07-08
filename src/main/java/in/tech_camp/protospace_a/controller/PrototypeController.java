package in.tech_camp.protospace_a.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Comparator;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.tech_camp.protospace_a.ImageUrl;
import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.CommentEntity;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.form.CommentForm;
import in.tech_camp.protospace_a.form.PrototypeForm;
import in.tech_camp.protospace_a.form.SearchForm;
import in.tech_camp.protospace_a.repository.PrototypeLikeRepository;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;

@Controller
@AllArgsConstructor
public class PrototypeController {

  private final UserRepository userRepository;
  private final PrototypeRepository prototypeRepository;
  private final PrototypeLikeRepository prototypeLikeRepository;
  private final ImageUrl imageUrl;

  @GetMapping("/")
  public String showAllPrototypes(Model model, @AuthenticationPrincipal CustomUserDetail currentUser) {
    List<PrototypeEntity> prototypes = prototypeRepository.findAllPrototypes();
    SearchForm searchForm = new SearchForm();
    model.addAttribute("prototypes", prototypes);
    model.addAttribute("searchForm", searchForm);

    Map<Integer, Boolean> likeStatusMap = new HashMap<>();
    if(currentUser != null) {
        Integer userId = currentUser.getId();
        for(PrototypeEntity p : prototypes) {
            boolean liked = prototypeLikeRepository.existsByUserAndPrototype(userId, p.getId()) > 0;
            likeStatusMap.put(p.getId(), liked);
        }
    }
    System.out.println("likeStatusMap内容：" + likeStatusMap); 
    model.addAttribute("likeStatusMap", likeStatusMap);
    return "prototypes/index";
  }

  @GetMapping("/prototypes/{prototypeId}")
  public String showTargetPrototype(
      @PathVariable("prototypeId") Integer prototypeId, Model model, @AuthenticationPrincipal CustomUserDetail currentUser) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    CommentForm commentForm = new CommentForm();
    model.addAttribute("prototype", prototype);
    model.addAttribute("commentForm", commentForm);
    if (prototype != null) {
      List<CommentEntity> sortedComments = prototype.getComments().stream()
          .sorted(Comparator.comparing(CommentEntity::getId).reversed())
          .collect(Collectors.toList());
      model.addAttribute("comments", sortedComments);
    }
    model.addAttribute("errorMessages", null);

    boolean liked = false;
    if(currentUser != null) {
        Integer userId = currentUser.getId();
        liked = prototypeLikeRepository.existsByUserAndPrototype(userId, prototype.getId()) > 0;
    }
    model.addAttribute("liked", liked);
 
    return "prototypes/detail";
  }

  @GetMapping("/prototypes/new")
  public String showPrototypeForm(
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    PrototypeForm prototypeForm = new PrototypeForm();
    model.addAttribute("prototypeForm", prototypeForm);
    return "prototypes/new";
  }

  @PostMapping("/prototypes/new")
  public String createPrototype(
      @ModelAttribute("prototypeForm") @Validated PrototypeForm prototypeForm,
      BindingResult bindingResult,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    prototypeForm.validatePrototypeForm(bindingResult);
    if (bindingResult.hasErrors()) {
      Map<String, String> fieldErrors = bindingResult.getFieldErrors().stream()
          .collect(Collectors.toMap(FieldError::getField,
              FieldError::getDefaultMessage, (msg1, msg2) -> msg1));
      model.addAttribute("fieldErrors", fieldErrors);
      model.addAttribute("prototypeForm", prototypeForm);
      return "prototypes/new";
    }
    PrototypeEntity prototype = new PrototypeEntity();
    prototype.setName(prototypeForm.getName());
    prototype.setCatchphrase(prototypeForm.getCatchphrase());
    prototype.setConcept(prototypeForm.getConcept());
    prototype.setCreated_at(new Timestamp(System.currentTimeMillis()));

    MultipartFile imageFile = prototypeForm.getImage();
    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        String uploadDir = imageUrl.getPrototypeImageUrl();
        Path uploadDirPath = Paths.get(uploadDir);
        if (!Files.exists(uploadDirPath)) {
          Files.createDirectories(uploadDirPath);
        }
        String fileName = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_"
            + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.copy(imageFile.getInputStream(), imagePath);
        prototype.setImage("/uploads/prototypes/" + fileName);
      }
      catch (IOException e) {
        System.out.println("Error：" + e);
        return "prototypes/new";
      }
    }
    prototype.setUser(userRepository.findByUserId(currentUser.getId()));
    try {
      prototypeRepository.insertPrototype(prototype);
    }
    catch (Exception e) {
      System.out.println("Error：" + e);
      return "redirect:/";
    }
    return "redirect:/";
  }

  @GetMapping("/prototypes/{prototypeId}/delete")
  public String deletePrototype(
      @PathVariable("prototypeId") Integer prototypeId,
      @AuthenticationPrincipal CustomUserDetail currentUser) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    if (prototype.getUser().getId() != currentUser.getId()) {
      return "redirect:/";
    }
    try {
      prototypeRepository.deleteByPrototypeId(prototypeId);
    }
    catch (Exception e) {
      System.err.println("Error: " + e);
      return "redirect:/";
    }
    return "redirect:/";
  }

  @GetMapping("/prototypes/{prototypeId}/edit")
  public String updatePrototype(
      @PathVariable("prototypeId") Integer prototypeId,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    if (prototype.getUser().getId() != currentUser.getId()) {
      return "redirect:/";
    }

    PrototypeForm prototypeForm = new PrototypeForm();
    prototypeForm.setName(prototype.getName());
    prototypeForm.setCatchphrase(prototype.getCatchphrase());
    prototypeForm.setConcept(prototype.getConcept());
    prototype.setUpdated_at(new Timestamp(System.currentTimeMillis()));


    if (!(model.containsAttribute("prototypeForm"))) {
      model.addAttribute("prototypeForm", prototypeForm);
    }

    model.addAttribute("prototypeId", prototypeId);
    return "prototypes/edit";
  }

  @PostMapping("/prototypes/{prototypeId}/update")
  public String postMethodName(@PathVariable("prototypeId") Integer prototypeId,
      @ModelAttribute("prototypeForm") @Validated PrototypeForm prototypeForm,
      BindingResult bindingResult,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model,
      RedirectAttributes redirectAttributes) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    if (prototype.getUser().getId() != currentUser.getId()) {
      return "redirect:/";
    }
    prototypeForm.validatePrototypeForm(bindingResult);
    if (bindingResult.hasErrors()) {
      Map<String, String> fieldErrors = bindingResult.getFieldErrors().stream()
          .collect(Collectors.toMap(FieldError::getField,
              FieldError::getDefaultMessage, (msg1, msg2) -> msg1));
      redirectAttributes.addFlashAttribute("prototypeForm", prototypeForm);
      redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
      return "redirect:/prototypes/" + prototypeId + "/edit";
    }
    prototype.setName(prototypeForm.getName());
    prototype.setConcept(prototypeForm.getConcept());
    prototype.setCatchphrase(prototypeForm.getCatchphrase());


    MultipartFile imageFile = prototypeForm.getImage();
    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        String uploadDir = imageUrl.getPrototypeImageUrl();
        Path uploadDirPath = Paths.get(uploadDir);
        if (!Files.exists(uploadDirPath)) {
          Files.createDirectories(uploadDirPath);
        }
        String fileName = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_"
            + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.copy(imageFile.getInputStream(), imagePath);
        prototype.setImage("/uploads/prototypes/" + fileName);
      }
      catch (IOException e) {
        System.out.println("Error：" + e);
        Map<String, String> fieldErrors = new HashMap<>();
        fieldErrors.put("image", "画像は存在しませんでした");
        redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
        return "redirect:/prototypes/" + prototypeId + "/edit";
      }
    }

    try {
      prototypeRepository.updatePrototype(prototype);
    }
    catch (Exception e) {
      System.err.println("Error: " + e);
      return "redirect:/prototypes/" + prototypeId;
    }
    return "redirect:/prototypes/" + prototypeId;
  }

  // 検索機能
  @GetMapping("/prototypes/search")
  public String searchPrototypes(
      @ModelAttribute("searchForm") SearchForm searchForm, 
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    // 名前の長さ判定、50以上だったら、プリントアウト
    if (searchForm.getName() != null && searchForm.getName().length() > 50) {
      System.out.println(String.format("検索に入力した名前の文字数：%d、50を超えています!!",
          searchForm.getName().length()));
    }

    List<PrototypeEntity> prototypes =
        prototypeRepository.findByNameContaining(searchForm.getName());
    model.addAttribute("prototypes", prototypes);
    model.addAttribute("searchForm", searchForm);

    Map<Integer, Boolean> likeStatusMap = new HashMap<>();
    if(currentUser != null) {
        Integer userId = currentUser.getId();
        for(PrototypeEntity p : prototypes) {
            boolean liked = prototypeLikeRepository.existsByUserAndPrototype(userId, p.getId()) > 0;
            likeStatusMap.put(p.getId(), liked);
        }
    }
    System.out.println("likeStatusMap内容：" + likeStatusMap); 
    model.addAttribute("likeStatusMap", likeStatusMap);

    return "prototypes/search";
  }
}
