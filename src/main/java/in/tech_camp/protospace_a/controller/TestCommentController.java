package in.tech_camp.protospace_a.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.CommentEntity;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.form.CommentForm;
import in.tech_camp.protospace_a.repository.CommentRepository;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class TestCommentController {
  private final PrototypeRepository prototypeRepository;
  private final CommentRepository commentRepository;
  
  @GetMapping("/prototypes/{prototypeId}/comments/{commentId}")
  public String showCommentDetail(@PathVariable("prototypeId") Integer prototypeId,
                                  @PathVariable("commentId") Integer commentId, Model model) {
    
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    CommentEntity comment = commentRepository.findById(commentId);

    model.addAttribute("prototype", prototype);
    model.addAttribute("comment", comment);

    return "comments/detail";
  }

  @GetMapping("/prototypes/{prototypeId}/comments/new")
  public String showCommentForm(@PathVariable("prototypeId") Integer prototypeId,
                                @AuthenticationPrincipal CustomUserDetail currentUser,
                                Model model) {
    
    model.addAttribute("commentForm", new CommentForm());
    return "comments/new";
  }
}
