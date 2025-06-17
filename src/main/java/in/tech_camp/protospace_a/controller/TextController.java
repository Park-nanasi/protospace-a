package in.tech_camp.protospace_a.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TextController {

  @GetMapping("/")
  public String showTopPage(Model model) {
      // 必要があれば model に属性を追加できます
      return "proto/index"; // templates/proto/index.html を表示
  }
}