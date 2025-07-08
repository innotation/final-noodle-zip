let collapseMap;

function fetchAndRender(page) {
  const path = document.body.getAttribute('data-path');

  const selectedCategories = Array.from(document.querySelectorAll('input[type=checkbox]:checked'))
    .map(cb => cb.value);
  const isAllCategory = selectedCategories.length === 0;

  const params = new URLSearchParams();
  selectedCategories.forEach(id => params.append('categoryIdList', id));
  params.append('isAllCategory', isAllCategory);
  params.append('page', page);

  fetch(`/mypage/${path}/saved-store/list/category-filter-search?${params.toString()}`)
    .then(response => response.json())
    .then(data => {
      renderStoreList(data.savedStoreList);
      renderPagination(
        data.page,
        data.totalPage,
        data.beginPage,
        data.endPage
      );

      window.scrollTo({
        top: document.querySelector("#store-list").offsetTop,
        behavior: "smooth"
      });
    })
    .catch(err => console.error('오류 발생:', err));
}

function renderStoreList(storeList) {
  const container = document.querySelector("#store-list");
  container.innerHTML = '';

  storeList.forEach(store => {
    const div = document.createElement('div');
    div.className = "col-xl-4 col-lg-6 col-md-6 col-sm-6";
    div.innerHTML = `
                      <div class="strip">
                          <figure>
                              <img src="img/lazy-placeholder.png" data-src="${store.storeMainImageUrl}" class="img-fluid lazy" alt="">
                              <a href="/store/${store.storeId}" class="strip_info">
                                  <div class="item_title">
                                      <h3>${store.storeName}</h3>
                                      <small>${store.address}</small>
                                  </div>
                              </a>
                          </figure>
                          <ul>
                               <li>
                                  <span class="memo-tooltip" title="${store.memo}">${store.memo}</span>
                              </li>
                              <li>
                                  <div class="score"></div>
                              </li>
                          </ul>
                      </div>
                `;
    container.appendChild(div);
  });
}

function renderPagination(currentPage, totalPage, beginPage, endPage) {
  const container = document.querySelector(".pagination_fg");
  container.innerHTML = '';

  const prev = document.createElement('a');
  prev.href = "#";
  prev.setAttribute("data-page", currentPage > 1 ? currentPage - 1 : 1);
  if (currentPage === 1) prev.classList.add("disabled");
  prev.innerHTML = "&laquo;";
  container.appendChild(prev);

  for (let i = beginPage; i <= endPage; i++) {
    const page = document.createElement('a');
    page.href = "#";
    page.setAttribute("data-page", i);
    if (i === currentPage) page.classList.add("active");
    page.innerHTML = `<span>${i}</span>`;
    container.appendChild(page);
  }

  const next = document.createElement('a');
  next.href = "#";
  next.setAttribute("data-page", currentPage < totalPage ? currentPage + 1 : totalPage);
  if (currentPage === totalPage) next.classList.add("disabled");
  next.innerHTML = "&raquo;";
  container.appendChild(next);

  bindPagination();
}

function bindPagination() {
  document.querySelectorAll('.pagination_fg a[data-page]').forEach(function (pageBtn) {
    pageBtn.addEventListener("click", function (evt) {
      evt.preventDefault();
      const page = parseInt(this.getAttribute('data-page'));
      fetchAndRender(page);
    });
  });
}

// 페이지 로드 후 최초 동작
document.addEventListener("DOMContentLoaded", function () {
  collapseMap = new bootstrap.Collapse(document.getElementById('collapseMap'), {
    toggle: false
  });

  document.querySelector("#search").addEventListener("click", function (evt) {
    evt.preventDefault();
    fetchAndRender(1);
    collapseMap.hide(); ///이거 없어도 될 거같다
  });
  bindPagination();
});
