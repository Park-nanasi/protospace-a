package in.tech_camp.protospace_a.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_a.repository.CommentRepository;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import lombok.AllArgsConstructor;

import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.CommentEntity;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.form.CommentForm;
import in.tech_camp.protospace_a.form.UserForm;
import in.tech_camp.protospace_a.validation.ValidationOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@AllArgsConstructor
public class CommentController {

  private final CommentRepository commentRepository;

  private final UserRepository userRepository;

  private final PrototypeRepository prototypeRepository;

  // @GetMapping("/tmp/{prototypeId}")
  // public String showDetail(@PathVariable Integer prototypeId, Model model) {
  //     PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
  //     System.out.println("prototype = " + prototype); // 可以打印一下调试
  //     model.addAttribute("prototype", prototype);
  //     model.addAttribute("commentForm", new CommentForm());
  //     return "tmp/detail";
  // }
  

  @PostMapping("/prototypes/{prototypeId}/comment")
  public String createComment(@PathVariable("prototypeId") Integer prototypeId, 
                            @ModelAttribute("commentForm") @Validated(ValidationOrder.class) CommentForm commentForm,
                            BindingResult result,
                            @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);

    if (result.hasErrors()) {
        model.addAttribute("errorMessages", result.getAllErrors());
        model.addAttribute("prototype", prototype);
        model.addAttribute("commentForm", commentForm);
        return "prototypes/detail";
    }

    CommentEntity comment = new CommentEntity();
    comment.setContent(commentForm.getContent());
    comment.setPrototype(prototype);
    comment.setUser(userRepository.findById(currentUser.getId()));

    // try {
    //   commentRepository.insert(comment);
    // } catch (Exception e) {
    //   model.addAttribute("prototype", prototype);
    //   model.addAttribute("commentForm", commentForm);
    //   System.out.println("エラー：" + e);
    //   return "prototypes/detail";
    // }

    try {
      commentRepository.insert(comment);
  } catch (Exception e) {
      model.addAttribute("prototype", prototype);
      model.addAttribute("commentForm", commentForm);
      model.addAttribute("errorMessage", "コメントの投稿に失敗しました。");
      System.out.println("エラー：" + e);
      return "prototypes/detail";
  }
  

    return "redirect:/prototypes/" + prototypeId;
  }
}
