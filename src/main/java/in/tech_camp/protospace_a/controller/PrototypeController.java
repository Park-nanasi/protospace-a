package in.tech_camp.protospace_a.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.form.PrototypeForm;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class PrototypeController {

  private final PrototypeRepository prototypeRepository;

  // testディレクトリ内は挙動確認を行っています。
  @GetMapping("/test")
  public String showAllPrototypes(Model model) {
    List<PrototypeEntity> prototypes =  prototypeRepository.getAllPrototypes();
    System.out.println("prototypes:" + prototypes);

    // createPrototypeの挙動を確認するため
    PrototypeForm newPrototypeForm = new PrototypeForm();
    model.addAttribute("prototypeForm", newPrototypeForm);
    return "tmp/test";
  }

  @PostMapping("/test")
  public String createPrototype(@ModelAttribute("prototypeForm") PrototypeForm prototypeForm, Model model) {
    // todo ログイン機能作成後
    // - ログイン状態の場合のみ、投稿ページへ遷移できること。
    // - ログアウト状態で投稿ページに遷移しようとすると、ログインページに遷移すること

    // todo プロトタイプの投稿画面作成後
    // - 投稿に必要な情報が入力されていない場合は、投稿できずにそのページに留まること。
    // - バリデーションによって投稿ができず、そのページに留まった場合でも、入力済みの項目（画像以外）は消えないこと

    PrototypeEntity prototype = new PrototypeEntity();
    prototype.setName(prototype.getName());
    prototype.setConcept(prototype.getConcept());
    prototype.setCatchphrase(prototype.getCatchphrase());
    prototype.setImages(prototype.getImages());

    try {
      prototypeRepository.insertPrototype(prototype);
    } catch (Exception e) {
      System.err.println("Error：" + e);
      return "tmp/test";
    }

    // todo トップページ作成後
    // - 正しく投稿できた場合は、トップページへ遷移すること。
    return "tmp/test";
  }
  
}
