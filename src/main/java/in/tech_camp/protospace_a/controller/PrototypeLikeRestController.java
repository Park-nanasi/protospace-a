package in.tech_camp.protospace_a.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.PrototypeLikeEntity;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.repository.PrototypeLikeRepository;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

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

