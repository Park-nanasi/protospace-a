package in.tech_camp.protospace_a.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.form.SearchForm;
import in.tech_camp.protospace_a.repository.PrototypeRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PrototypeControllerUnitTest {

    @Mock
    private PrototypeRepository prototypeRepository;

    @InjectMocks
    private PrototypeController prototypeController;

    @Test
    public void 投稿一覧ページにアクセスするとprototypes_indexのビューが返されること() {
        Model model = new ExtendedModelMap();

        String viewName = prototypeController.showAllPrototypes(model);

        assertThat(viewName, is("prototypes/index"));
    }

    @Test
    public void 投稿一覧ページにアクセスするとプロトタイプ一覧がモデルに含まれること() {
        // ダミーのプロトタイプを準備
        PrototypeEntity prototype1 = new PrototypeEntity();
        prototype1.setId(1);
        prototype1.setName("プロトタイプ1");

        PrototypeEntity prototype2 = new PrototypeEntity();
        prototype2.setId(2);
        prototype2.setName("プロトタイプ2");

        List<PrototypeEntity> expectedPrototypes = Arrays.asList(prototype1, prototype2);

        // モックのリポジトリの戻り値を定義
        when(prototypeRepository.findAllPrototypes()).thenReturn(expectedPrototypes);

        Model model = new ExtendedModelMap();

        prototypeController.showAllPrototypes(model);

        // モデルに設定された属性が期待通りか検証
        assertThat(model.getAttribute("prototypes"), is(expectedPrototypes));
    }

     @Test
    public void 検索フォームで検索すると検索結果がモデルに含まれ検索ビューが返ること() {
        SearchForm searchForm = new SearchForm();
        searchForm.setName("テスト");

        PrototypeEntity p = new PrototypeEntity();
        p.setId(100);
        p.setName("テストプロトタイプ");
        List<PrototypeEntity> expected = Arrays.asList(p);

        when(prototypeRepository.findByNameContaining("テスト")).thenReturn(expected);

        Model model = new ExtendedModelMap();
        String resultView = prototypeController.searchPrototypes(searchForm, model);

        assertThat(resultView, is("prototypes/search"));
        assertThat(model.getAttribute("prototypes"), is(expected));
        assertThat(((SearchForm)model.getAttribute("searchForm")).getName(), is("テスト"));
    }
}

