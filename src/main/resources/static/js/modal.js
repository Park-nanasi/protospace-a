// 显示modal的函数
function showLoginModal() {
  document.getElementById('myModal').style.display = 'block';
}

// 关闭modal
document.addEventListener('DOMContentLoaded', function () {
  var closeBtn = document.getElementById('modal-close-btn');
  if (closeBtn) {
    closeBtn.onclick = function () {
      document.getElementById('myModal').style.display = 'none';
    };
  }
  // 点击未登录的like弹窗
  document.querySelectorAll('.like-btn.not-logged-in').forEach(function(btn) {
    btn.addEventListener('click', function(e) {
      e.preventDefault();
      showLoginModal();
    });
  });
});
