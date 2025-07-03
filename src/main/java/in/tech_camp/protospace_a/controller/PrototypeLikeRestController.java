package in.tech_camp.protospace_a.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import jakarta.validation.Valid;
import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.CommentEntity;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.entity.PrototypeLikeEntity;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.form.CommentForm;
import in.tech_camp.protospace_a.repository.PrototypeLikeRepository;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

// @Controller
// @AllArgsConstructor
// @RequestMapping("/prototypes/{prototypeId}/likes")
// public class PrototypeLikeController {
//   private final PrototypeRepository prototypeRepository;
//   private final UserRepository userRepository;
//   private final PrototypeLikeRepository prototypeLikeRepository;

//   // ユーザーが評価したかどうか調査
//   @GetMapping("/info")
//   public Map<String, Object> getLikeInfo(@PathVariable Integer prototypeId, @AuthenticationPrincipal CustomUserDetail currentUser) {
//       int count = prototypeLikeRepository.countByPrototypeId(prototypeId);
//       boolean liked = false;
//       if (currentUser != null) {
//           liked = prototypeLikeRepository.existsByUserAndPrototype(currentUser.getId(), prototypeId) > 0;
//       }
//       Map<String, Object> resp = new HashMap<>();
//       resp.put("count", count);
//       resp.put("liked", liked);
//       return resp;
//     }

//   @GetMapping("/{likeId}")
//   public String showLike(
//       @PathVariable("prototypeId") Integer prototypeId,
//       @PathVariable("likeId") Integer likeId, Model model) {

//     PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
//     PrototypeLikeEntity like = prototypeLikeRepository.findById(likeId);

//     model.addAttribute("prototype", prototype);
//     model.addAttribute("comment", like);

//     return "like/detail";
//   }

//   // 新規評価
//   @PostMapping("/new")
//     public String createLike(@PathVariable("prototypeId") Integer prototypeId,
//       @ModelAttribute("prototypeLikeForm") @Valid PrototypeLikeForm prototypeLikeForm,
//       BindingResult result,
//       @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

//     PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    
//     if (result.hasErrors()) {
//       model.addAttribute("prototypes", prototype);
//       model.addAttribute("likes", prototype.getLikes());
//       model.addAttribute("prototypeLikeForm", prototypeLikeForm);
//       return "likes/new";
//     }

//       PrototypeLikeEntity like = new PrototypeLikeEntity();
//       UserEntity user = userRepository.findById(currentUser.getId());
//       int countLikes = prototypeRepository.incrementLikeCount(prototypeId);
//       like.setUser(user);
      
//       like.setPrototypeId(prototypeId);
//       prototype.setCountLikes(countLikes);

//       like.setCreatedAt(new Timestamp(System.currentTimeMillis()));
 
//       try {
//         prototypeLikeRepository.insert(like);
//       }
//       catch (Exception e) {
//         return "redirect:/prototypes/" + prototypeId;
//     }

//     return "redirect:/prototypes/" + prototypeId;

//   }

//     // 「いいね」取り消し
//   @GetMapping("/delete")
//   public String deleteLike(@PathVariable("prototypeId") Integer prototypeId, @RequestParam Integer userId, 
//     @PathVariable("likeId") Integer likeId,
//     @AuthenticationPrincipal CustomUserDetail currentUser) {
//     PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
//     PrototypeLikeEntity like = prototypeLikeRepository.findById(likeId);
//     if (prototype.getUser().getId() != currentUser.getId()) {
//       return "redirect:/";
//     }
//     try {
//       prototypeRepository.incrementLikeCount(likeId);
//       prototypeLikeRepository.deleteByUserAndPrototype(likeId);
//     }
//     catch (Exception e) {
//       System.err.println("Error: " + e);
//       return "redirect:/prototypes/" + prototypeId;
//     }
//   return "redirect:/prototypes/" + prototypeId;
//     } 
// }

@RestController
@RequestMapping("/prototypes/{prototypeId}/likes")
@AllArgsConstructor
public class PrototypeLikeRestController {
    private final PrototypeLikeRepository prototypeLikeRepository;
    private final PrototypeRepository prototypeRepository;
    private final UserRepository userRepository;

    @GetMapping("/info")
    public Map<String, Object> getLikeInfo(@PathVariable Integer prototypeId, @AuthenticationPrincipal CustomUserDetail currentUser) {
        int count = prototypeLikeRepository.countByPrototypeId(prototypeId);
        boolean liked = false;
        if (currentUser != null) {
            liked = prototypeLikeRepository.existsByUserAndPrototype(currentUser.getId(), prototypeId) > 0;
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("count", count);
        resp.put("liked", liked);
        return resp;
    }

    @PostMapping
    public ResponseEntity<String> like(@PathVariable Integer prototypeId, @AuthenticationPrincipal CustomUserDetail currentUser) {
        if (currentUser == null) return ResponseEntity.status(401).body("unauthenticated");
        if (prototypeLikeRepository.existsByUserAndPrototype(currentUser.getId(), prototypeId) == 0) {
            UserEntity user = userRepository.findById(currentUser.getId());
            PrototypeLikeEntity like = new PrototypeLikeEntity();
            like.setUser(user);
            like.setUserId(user.getId());
            like.setPrototypeId(prototypeId);
            like.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            prototypeLikeRepository.insert(like);
            prototypeRepository.incrementLikeCount(prototypeId);
        }
        return ResponseEntity.ok("liked");
    }

    @DeleteMapping
    public ResponseEntity<String> unlike(@PathVariable Integer prototypeId, @AuthenticationPrincipal CustomUserDetail currentUser) {
        if (currentUser == null) return ResponseEntity.status(401).body("unauthenticated");
        int userId = currentUser.getId();
        PrototypeLikeEntity like = prototypeLikeRepository.selectByUserAndPrototype(userId, prototypeId);
        if (like != null) {
            prototypeLikeRepository.deleteByUserAndPrototype(like.getId());
            prototypeRepository.decrementLikeCount(prototypeId);
        }
        return ResponseEntity.ok("unliked");
    }
}

