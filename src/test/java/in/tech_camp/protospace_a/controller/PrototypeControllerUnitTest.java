package in.tech_camp.protospace_a.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
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
import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.repository.PrototypeLikeRepository;
import in.tech_camp.protospace_a.repository.PrototypeRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PrototypeControllerUnitTest {

    @Mock
    private PrototypeRepository prototypeRepository;

    @InjectMocks
    private PrototypeController prototypeController;

    @Mock
    private PrototypeLikeRepository prototypeLikeRepository;

    @Test
    public void 投稿一覧ページにアクセスするとprototypes_indexのビューが返されること() {
        Model model = new ExtendedModelMap();

        UserEntity user = new UserEntity();
        user.setId(123);
        user.setUsername("unittest_user");
        user.setEmail("test@example.com");
        user.setPassword("securepassword");
        user.setProfile("profile");
        user.setProfileImage("test.jpg");
        
        CustomUserDetail userDetail = new CustomUserDetail(user);
        String viewName = prototypeController.showAllPrototypes(model, userDetail);
        
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
        when(prototypeLikeRepository.existsByUserAndPrototype(anyInt(), anyInt())).thenReturn(1);

        Model model = new ExtendedModelMap();

        UserEntity user = new UserEntity();
        user.setId(123);
        user.setUsername("unittest_user");
        user.setEmail("test@example.com");
        user.setPassword("securepassword");
        user.setProfile("profile");
        user.setProfileImage("test.jpg");
        
        CustomUserDetail userDetail = new CustomUserDetail(user);

        prototypeController.showAllPrototypes(model, userDetail);

        // モデルに設定された属性が期待通りか検証
        assertThat(model.getAttribute("prototypes"), is(expectedPrototypes));
    }
}

