document.addEventListener("DOMContentLoaded", function() {
  const likeBtn = document.getElementById('like-btn');
  const likeCountSpan = document.getElementById('like-count');
  const prototypeId = likeBtn.getAttribute('data-prototypeid');

  // 最初状態調べ
  fetch(`/prototypes/${prototypeId}/likes/info`, {method: 'GET'})
    .then(res => res.json())
    .then(data => updateLikeView(data.liked, data.count));

  function updateLikeView(liked, count) {
      likeBtn.innerHTML = liked ? '♥' : '♡'; // 赤or空白
      likeBtn.style.color = liked ? 'red' : '#ccc';
      likeCountSpan.textContent = count;
      likeBtn.setAttribute('data-liked', liked);
  }

  likeBtn.addEventListener('click', function() {
    const liked = likeBtn.getAttribute('data-liked') === 'true';
    const method = liked ? 'DELETE' : 'POST';
    
    likeBtn.disabled = true; // ダブルクリック防止

    fetch(`/prototypes/${prototypeId}/likes`, {method})
      .then(res => {
        // ログインしない状態、評価できない
        if (res.status === 401 || res.redirected) {
          alert("ログインしてから、いいねを押してね！");
          window.location.href = "/users/login";
          return Promise.reject(); 
        }
        return res.text(); 
      })
      .then(_ => {
        // 最新の状態を取得
        fetch(`/prototypes/${prototypeId}/likes/info`)
          .then(res => res.json())
          .then(data => updateLikeView(data.liked, data.count));
      })
      .catch(() => {});
});
});

window.addEventListener('pageshow', function(event) {
  if (event.persisted) {
    window.location.reload();
  }
})

