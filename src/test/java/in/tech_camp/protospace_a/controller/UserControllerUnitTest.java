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

import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.form.SearchForm;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;
import in.tech_camp.protospace_a.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {

    @Mock
    private PrototypeRepository prototypeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void ユーザーのマイページ検索機能_検索結果とユーザー名がモデルに含まれてビューが返ること() {
        Integer userId = 1;

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUsername("ユーザー");
        user.setProfile("プロファイル");
        user.setCompany("会社");
        user.setRole("ロール");

        PrototypeEntity p = new PrototypeEntity();
        p.setId(101);
        p.setName("名前A");
        List<PrototypeEntity> expectedPrototypes = Arrays.asList(p);

        SearchForm searchForm = new SearchForm();
        searchForm.setName("名前");

        when(userRepository.findById(userId)).thenReturn(user);
        when(prototypeRepository.findByUserIdAndNameContaining(userId, "名前")).thenReturn(expectedPrototypes);

        Model model = new ExtendedModelMap();

        String view = userController.searchPrototypes(userId, searchForm, model);

        assertThat(view, is("users/userInfo"));
        assertThat(model.getAttribute("name"), is("ユーザー"));
        assertThat(model.getAttribute("prototypes"), is(expectedPrototypes));
        assertThat(model.getAttribute("searchForm"), is(searchForm));
    }
}
