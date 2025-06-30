package in.tech_camp.protospace_a.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import in.tech_camp.protospace_a.custom_user.CustomUserDetail;
import in.tech_camp.protospace_a.entity.CommentEntity;
import in.tech_camp.protospace_a.entity.PrototypeEntity;
import in.tech_camp.protospace_a.entity.UserEntity;
import in.tech_camp.protospace_a.form.CommentForm;
import in.tech_camp.protospace_a.repository.CommentRepository;
import in.tech_camp.protospace_a.repository.PrototypeRepository;
import in.tech_camp.protospace_a.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CommentControllerUnitTest {

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PrototypeRepository prototypeRepository;

  @Mock
  private BindingResult bindingResult;

  @Mock
  private CustomUserDetail currentUser;

  @InjectMocks
  private CommentController commentController;

  @Test
  public void コメントの投稿ができる() {
    Integer prototypeId = 1;

    CommentForm form = new CommentForm();
    form.setContent("テストコメント");
    form.setTitle("タイトル");

    PrototypeEntity prototype = new PrototypeEntity();
    UserEntity user = new UserEntity();
    user.setId(1);

    when(prototypeRepository.findById(prototypeId)).thenReturn(prototype);
    when(bindingResult.hasErrors()).thenReturn(false);
    when(currentUser.getId()).thenReturn(1);
    when(userRepository.findById(1)).thenReturn(user);

    Model model = new ExtendedModelMap();

    String viewName = commentController.createComment(prototypeId, form, bindingResult, currentUser, model);

    ArgumentCaptor<CommentEntity> captor = ArgumentCaptor.forClass(CommentEntity.class);
    verify(commentRepository).insert(captor.capture());
    CommentEntity captured = captor.getValue();

    assertThat(captured.getContent(), is("テストコメント"));
    assertThat(captured.getTitle(), is("タイトル"));
    assertThat(captured.getPrototype(), is(prototype));
    assertThat(captured.getUser(), is(user));

    assertThat(viewName, is("redirect:/prototypes/" + prototypeId));
  }

  @Test
  public void コメントが更新できる() {
    CommentForm form = new CommentForm();
    form.setContent("新しい内容");

    CommentEntity existingComment = new CommentEntity();
    existingComment.setId(100);
    existingComment.setContent("古い内容");

    existingComment.setContent(form.getContent());

    ArgumentCaptor<CommentEntity> captor = ArgumentCaptor.forClass(CommentEntity.class);

    commentRepository.update(existingComment);

    verify(commentRepository).update(captor.capture());

    CommentEntity updated = captor.getValue();

    assertThat(updated.getContent(), is("新しい内容"));
  }

  @Test
  public void コメントが削除できる() {
    Integer prototypeId = 1;
    Integer commentId = 10;

    PrototypeEntity prototype = new PrototypeEntity();
    UserEntity prototypeOwner = new UserEntity();
    prototypeOwner.setId(1);
    prototype.setUser(prototypeOwner);

    CommentEntity comment = new CommentEntity();
    comment.setId(commentId);

    when(prototypeRepository.findById(prototypeId)).thenReturn(prototype);
    when(commentRepository.findById(commentId)).thenReturn(comment);
    when(currentUser.getId()).thenReturn(1);

    String viewName = commentController.deleteComment(prototypeId, commentId, currentUser);


    ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
    verify(commentRepository, times(1)).deleteById(captor.capture());

    Integer deletedId = captor.getValue();

    assertThat(deletedId, is(commentId));
    assertThat(viewName, is("redirect:/prototypes/" + prototypeId));
  }
}
