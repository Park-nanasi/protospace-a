package in.tech_camp.protospace_a.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.form.PrototypeForm;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TestPrototypeController {
  private final PrototypeRepository prototypeRepository;

  @GetMapping("/test")
  public String getMethodName() {
    return "tmp/test";
  }

  @GetMapping("/test/edit")
  public String showEditPage() {
    return "prototypes/edit";
  }
  
  @GetMapping("/test/prototypes")
  public String showAllPrototypes(Model model) {
    List<PrototypeEntity> prototypes =  prototypeRepository.findAllPrototypes();
    model.addAttribute("prototypes", prototypes);
    System.out.println("prototypes: " + prototypes);
    return "prototypes/index";
  }

  // testディレクトリ内は挙動確認を行っています。
  @GetMapping("/test/prototype-new")
  public String showPrototypePost(Model model) {
    // createPrototypeの挙動を確認するため
    PrototypeForm newPrototypeForm = new PrototypeForm();
    model.addAttribute("prototypeForm", newPrototypeForm);
    return "tmp/prototype";
  }

  @PostMapping("/test/prototype-new")
  public String createPrototype(@ModelAttribute("prototypeForm") @Validated PrototypeForm prototypeForm, BindingResult bindingResult, Model model) {
    // todo ログイン機能作成後
    // - ログイン状態の場合のみ、投稿ページへ遷移できること。
    // - ログアウト状態で投稿ページに遷移しようとすると、ログインページに遷移すること
    if (bindingResult.hasErrors()) {
        model.addAttribute("errors", bindingResult.getAllErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.toList()));
        return "tmp/prototype";
    }

    // todo プロトタイプの投稿画面作成後
    // - 投稿に必要な情報が入力されていない場合は、投稿できずにそのページに留まること。
    // - バリデーションによって投稿ができず、そのページに留まった場合でも、入力済みの項目（画像以外）は消えないこと

    PrototypeEntity prototype = new PrototypeEntity();
    prototype.setName(prototype.getName());
    prototype.setConcept(prototype.getConcept());
    prototype.setCatchphrase(prototype.getCatchphrase());
    prototype.setImage(prototype.getImage());

    try {
      prototypeRepository.insertPrototype(prototype);
    } catch (Exception e) {
      System.err.println("Error：" + e);
      return "tmp/prototype";
    }

    // todo トップページ作成後
    // - 正しく投稿できた場合は、トップページへ遷移すること。
    return "redirect:/tmp/test";
  }

  @GetMapping("/test/prototypes/{prototypeId}/delete")
  public String deletePrototype(@PathVariable("prototypeId") Integer prototypeId) {
    try {
      prototypeRepository.deleteByPrototypeId(prototypeId);
    } catch (Exception e) {
      System.err.println("Error^^^^^^^^^^^^^^^^^^^^^^^^^^^^^: " + e);
      return "redirect:/tmp/test";
    }
    return "redirect:/tmp/test";
  }

  
  @GetMapping("/test/prototype-update")
  public String updatePrototype(Model model) {
    // createPrototypeの挙動を確認するため
    PrototypeForm prototypeForm = new PrototypeForm();
    model.addAttribute("prototypeForm", prototypeForm);
    return "tmp/update_prototype";
  }

  @PostMapping("/test/prototype-update")
  public String postMethodName(@ModelAttribute("prototypeForm") @Validated PrototypeForm prototypeForm, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
        model.addAttribute("errors", bindingResult.getAllErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.toList()));
        return "tmp/prototype";
    }

    PrototypeEntity prototype = new PrototypeEntity();
    // test用 / 本来はHTMLからログインユーザーIdが入っている。
    prototype.setId(1);
    prototype.setName(prototypeForm.getName());
    prototype.setConcept(prototypeForm.getConcept());
    prototype.setCatchphrase(prototypeForm.getCatchphrase());
    // prototype.setImage(prototypeForm.getImage());
    
    try {
      prototypeRepository.updatePrototype(prototype);
    } catch (Exception e) {
      System.err.println("Error: " + e);
      return "redirect:/tmp/test";
    }

    System.out.println("Edit: :" + prototype);
    return "redirect:/tmp/test";
  }  
}
