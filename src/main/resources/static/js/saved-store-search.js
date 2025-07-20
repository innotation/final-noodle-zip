let collapseMap;
let lastSearchedCategories = []; // 마지막 검색 시점의 카테고리 상태 저장
let isSearchPerformed = true; // 검색이 실행되었는지 확인

// URL 파라미터에서 카테고리 상태 복원
function restoreCheckboxStateFromURL() {
  const urlParams = new URLSearchParams(window.location.search);
  const categoryIds = urlParams.getAll('categoryIdList');

  // 모든 체크박스 초기화
  document.querySelectorAll('input[type=checkbox]').forEach(cb => {
    cb.checked = false;
  });

  // URL에서 가져온 카테고리들 체크
  categoryIds.forEach(id => {
    const checkbox = document.querySelector(`input[type=checkbox][value="${id}"]`);
    if (checkbox) {
      checkbox.checked = true;
    }
  });

  // 검색이 실행된 상태로 설정
  if (categoryIds.length > 0 || urlParams.has('allCategory')) {
    isSearchPerformed = true;
    lastSearchedCategories = categoryIds;
  }
}

// URL 업데이트 함수
function updateURLWithCategories(selectedCategories, page = 1) {
  const url = new URL(window.location);

  // 기존 categoryIdList 파라미터 제거
  url.searchParams.delete('categoryIdList');
  url.searchParams.delete('allCategory');
  url.searchParams.delete('page');

  // 새로운 파라미터 추가
  selectedCategories.forEach(id => url.searchParams.append('categoryIdList', id));
  url.searchParams.set('allCategory', selectedCategories.length === 0);
  if (page > 1) {
    url.searchParams.set('page', page);
  }

  // URL 업데이트 (페이지 리로드 없이)
  window.history.replaceState({}, '', url);
}

function fetchAndRender(page, useLastSearchedCategories = false) {
  const userId = document.body.getAttribute('userId');

  let selectedCategories;

  if (useLastSearchedCategories && isSearchPerformed) {
    // 페이지네이션 시에는 마지막 검색 카테고리 사용
    selectedCategories = lastSearchedCategories;
  } else {
    // 검색 시에는 현재 체크박스 상태 사용
    selectedCategories = Array.from(document.querySelectorAll('input[type=checkbox]:checked'))
      .map(cb => cb.value);
  }

  const isAllCategory = selectedCategories.length === 0;

  const params = new URLSearchParams();
  selectedCategories.forEach(id => params.append('categoryIdList', id));
  params.append('allCategory', isAllCategory);
  params.append('page', page);

  // URL 업데이트
  updateURLWithCategories(selectedCategories, page);

  fetch(`/users/${userId}/saved-stores/category-filter-search?${params.toString()}`)
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
        top: 0,
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
                              <img src="${store.storeMainImageUrl}" class="img-fluid lazy" alt="">
                              <a href="/store/detail/${store.storeId}" class="strip_info" target="_blank" rel="noopener noreferrer">
                                  <small>${store.saveStoreCategoryName}</small>
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

      // 검색이 한 번도 실행되지 않았다면 페이지네이션 실행하지 않음
      if (!isSearchPerformed) {
        alert("먼저 검색을 실행해주세요.");
        return;
      }

      const page = parseInt(this.getAttribute('data-page'));
      fetchAndRender(page, true); // 페이지네이션 시에는 true 전달
    });
  });
}

// 페이지 로드 후 최초 동작
document.addEventListener("DOMContentLoaded", function () {
  collapseMap = new bootstrap.Collapse(document.getElementById('collapseMap'), {
    toggle: false
  });

  // URL에서 체크박스 상태 복원
  restoreCheckboxStateFromURL();

  // URL에 검색 파라미터가 있으면 자동으로 검색 실행
  const urlParams = new URLSearchParams(window.location.search);
  if (urlParams.has('categoryIdList') || urlParams.has('allCategory')) {
    const page = parseInt(urlParams.get('page')) || 1;
    fetchAndRender(page, true);
  }

  document.querySelector("#search").addEventListener("click", function (evt) {
    evt.preventDefault();

    // 검색 시점의 카테고리 상태 저장 (빈 배열이어도 전체조회로 처리)
    lastSearchedCategories = Array.from(document.querySelectorAll('input[type=checkbox]:checked'))
      .map(cb => cb.value);

    isSearchPerformed = true; // 검색 실행 플래그 설정

    fetchAndRender(1, false); // 검색 시에는 false 전달
    collapseMap.hide();

    const btnText = document.querySelector('.btn_map_txt');
    if (btnText) {
      btnText.textContent = btnText.dataset.textSwap;
    }
  });

  bindPagination();
});