package in.tech_camp.protospace_a.controller;

import java.sql.Timestamp;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_a.ImageUrl;
import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.CommentEntity;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.form.CommentForm;
import in.tech_camp.protospace_a.repository.CommentRepository;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class CommentController {
  private final PrototypeRepository prototypeRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  private final ImageUrl imageUrl;

  // コメントの詳細ページへアクセス
  @GetMapping("/prototypes/{prototypeId}/comments/{commentId}")
  public String showCommentDetail(
      @PathVariable("prototypeId") Integer prototypeId,
      @PathVariable("commentId") Integer commentId, Model model) {

    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    CommentEntity comment = commentRepository.findById(commentId);

    model.addAttribute("prototype", prototype);
    model.addAttribute("comment", comment);

    return "comments/detail";
  }

  // コメントの投稿ページへアクセス
  @GetMapping("/prototypes/{prototypeId}/comments/new")
  public String showCommentForm(
      @PathVariable("prototypeId") Integer prototypeId,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    model.addAttribute("prototype", prototype);
    model.addAttribute("commentForm", new CommentForm());
    return "comments/new";
  }

  // コメントの編集ページへアクセス
  @GetMapping("/prototypes/{prototypeId}/comments/{commentId}/edit")
  public String showEditComment(
      @PathVariable("prototypeId") Integer prototypeId,
      @PathVariable("commentId") Integer commentId,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

    CommentEntity comment = commentRepository.findById(commentId);

    if (comment.getUser().getId() != currentUser.getId()) {
      return "redirect:/";
    }

    CommentForm commentForm = new CommentForm();
    commentForm.setTitle(comment.getTitle());
    commentForm.setContent(comment.getContent());
    comment.setUpdated_at(new Timestamp(System.currentTimeMillis()));

    model.addAttribute("commentForm", commentForm);
    model.addAttribute("prototypeId", prototypeId);
    model.addAttribute("commentId", commentId);
    return "comments/edit";
  }

  // 新規コメントの投稿
  @PostMapping("/prototypes/{prototypeId}/comments/new")
  public String createComment(@PathVariable("prototypeId") Integer prototypeId,
      @ModelAttribute("commentForm") @Valid CommentForm commentForm,
      BindingResult result,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);


    if (result.hasErrors()) {
      model.addAttribute("prototype", prototype);
      model.addAttribute("comments", prototype.getComments());
      model.addAttribute("commentForm", commentForm);
      return "comments/new";
    }

    CommentEntity comment = new CommentEntity();
    comment.setContent(commentForm.getContent());
    comment.setTitle(commentForm.getTitle());
    comment.setPrototype(prototype);

    UserEntity user = userRepository.findById(currentUser.getId());
    comment.setUser(user);
    comment.setCreated_at(new Timestamp(System.currentTimeMillis()));

    try {
      commentRepository.insert(comment);
    }
    catch (Exception e) {
      System.out.println("Error：" + e);
      return "redirect:/prototypes/" + prototypeId;
    }

    return "redirect:/prototypes/" + prototypeId;
  }

  // コメントの更新処理
  @PostMapping("/prototypes/{prototypeId}/comments/{commentId}/update")
  public String updateComment(@PathVariable("prototypeId") Integer prototypeId,
      @PathVariable("commentId") Integer commentId,
      @ModelAttribute("commentForm") @Valid CommentForm commentForm,
      BindingResult result,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    CommentEntity comment = commentRepository.findById(commentId);
    if (prototype.getUser().getId() != currentUser.getId()) {
      return "redirect:/";
    }
    if (result.hasErrors()) {
      model.addAttribute("prototype", prototype);
      model.addAttribute("comment", comment);
      model.addAttribute("commentForm", commentForm);

      return "comments/edit";
    }

    comment.setTitle(commentForm.getTitle());
    comment.setContent(commentForm.getContent());
    
    try {
      System.out.println(comment.getId());
      commentRepository.update(comment);
    }
    catch (Exception e) {
      System.err.println("Error: " + e);
      return "redirect:/prototypes/" + prototypeId;
    }
    return "redirect:/prototypes/" + prototypeId;
  }

  // コメント削除
  @GetMapping("/prototypes/{prototypeId}/comments/{commentId}/delete")
  public String deleteComment(@PathVariable("prototypeId") Integer prototypeId,
      @PathVariable("commentId") Integer commentId,
      @AuthenticationPrincipal CustomUserDetail currentUser) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    CommentEntity comment = commentRepository.findById(commentId);
    if (prototype.getUser().getId() != currentUser.getId()) {
      return "redirect:/";
    }
    try {
      commentRepository.deleteById(commentId);
    }
    catch (Exception e) {
      System.err.println("Error: " + e);
      return "redirect:/prototypes/" + prototypeId;
    }
    return "redirect:/prototypes/" + prototypeId;
  }
}
