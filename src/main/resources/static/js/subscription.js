document.addEventListener("DOMContentLoaded", function() {

  document.querySelectorAll(".subscribe-btn").forEach(function(btn) {
    btn.addEventListener("click", function(evt) {
      evt.preventDefault();
      const userId = btn.getAttribute("data-userId");

      fetch(`/users/${userId}/subscribe`, { method: 'GET' })
        .then(response => {
          if (!response.ok) throw new Error("Network response was not ok");
          const isSubscribed = btn.getAttribute("data-subscribed") === "true";
          btn.setAttribute("data-subscribed", (!isSubscribed).toString());
          btn.textContent = !isSubscribed ? "구독취소" : "구독";
        })
        .catch(err => {
          alert("구독 처리 중 오류가 발생했습니다.");
          console.error(err);
        });
    });
  });
});