document.addEventListener("DOMContentLoaded", () => {
  const tabB = document.getElementById("tab-B");
  const paneB = document.getElementById("pane-B");
  const storeId = document.getElementById("store-id").value;

  let reviewLoaded = false;

  tabB.addEventListener("click", () => {
    if (reviewLoaded) return;
    fetch(`/store/detail/${storeId}/reviews?page=1`)
      .then(res => res.text())
      .then(html => {
        paneB.innerHTML = html;
        initLoadMore();
        reviewLoaded = true;
      })
      .catch(err => console.error("리뷰 탭 로딩 실패", err));
  });

  function initLoadMore() {
    const container = document.getElementById("reviews");
    const loadMoreWrapper = document.getElementById("load-more-review-wrapper");
    if (!loadMoreWrapper) return;

    loadMoreWrapper.addEventListener("click", (e) => {
      const btn = e.target.closest("#load-more-review-btn");
      if (!btn) return;

      const nextPage = btn.dataset.nextPage;

      fetch(`/store/detail/${storeId}/reviews?page=${nextPage}`)
        .then(res => res.text())
        .then(html => {
          const tempDiv = document.createElement("div");
          tempDiv.innerHTML = html;

          const newCards = tempDiv.querySelectorAll(".review_card");
          const newWrapper = tempDiv.querySelector("#load-more-review-wrapper");

          newCards.forEach(card => container.appendChild(card));
          loadMoreWrapper.remove(); // 기존 버튼 제거
          if (newWrapper) container.after(newWrapper); // 새 버튼 삽입

        })
        .catch(err => console.error("더보기 실패", err));
    });
  }
});
