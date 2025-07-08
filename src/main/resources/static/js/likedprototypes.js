document.addEventListener('DOMContentLoaded', function () {
  document.querySelectorAll('.like-btn').forEach(btn => {
    btn.addEventListener('click', function () {
      const card = btn.closest('.prototype-card');
      const prototypeId = card.dataset.prototypeid;

      fetch(`/prototypes/${prototypeId}/likes`, { method: 'DELETE' })
        .then(res => {
          if (res.status === 401) {
            alert("ログインしてから、いいねを押してね！");
            window.location.href = "/users/login";
            throw new Error("Unauthorized");
          }
          return res.text();
        })
        .then(() => {
          card.remove();
        })
        .catch(err => console.error(err));
    });
  });
});
