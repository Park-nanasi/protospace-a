package in.tech_camp.protospace_a.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.CommentEntity;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.form.CommentForm;
import in.tech_camp.protospace_a.repository.CommentRepository;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import in.tech_camp.protospace_a.validation.ValidationOrder;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class CommentController {

  private final CommentRepository commentRepository;

  private final UserRepository userRepository;

  private final PrototypeRepository prototypeRepository;

  @PostMapping("/prototypes/{prototypeId}/comment")
  public String createComment(@PathVariable("prototypeId") Integer prototypeId, 
                            @ModelAttribute("commentForm") @Validated(ValidationOrder.class) CommentForm commentForm,
                            BindingResult result,
                            @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);

    if (result.hasErrors()) {
      model.addAttribute("prototype", prototype);
      model.addAttribute("comments", prototype.getComments());
      model.addAttribute("commentForm", commentForm);
      model.addAttribute("errorMessages", result.getAllErrors());
        return "prototypes/detail";
    }

    CommentEntity comment = new CommentEntity();
    UserEntity user = userRepository.findById(currentUser.getId());
    System.out.println("Prototype: " + prototype);
    System.out.println("User: " + user);
    comment.setContent(commentForm.getContent());
    comment.setPrototype(prototype);
    comment.setUser(user);

  try {
      commentRepository.insert(comment);
  } catch (Exception e) {
      model.addAttribute("prototype", prototype);
      model.addAttribute("comments", prototype.getComments());
      model.addAttribute("commentForm", commentForm);
      model.addAttribute("errorMessages", "コメントの投稿に失敗しました。");
      System.out.println("エラー：" + e);
      return "prototypes/detail";
  }
    return "redirect:/prototypes/" + prototypeId;
  }
}
