document.addEventListener('DOMContentLoaded', function () {

  const markers = [];
  const infoWindows = [];
  let currentLat = 37.5665;
  let currentLng = 126.9780;
  let isFilterMode = false;
  let currentPage = 1;
  let totalPages = 1;
  let totalElements = 0;

  // 지도 초기화 (서울 시청 기준)
  const map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.5665, 126.9780),
    zoom: 13,
  });

  // 필터 옵션 로드
  loadFilterOptions();

  // 현재 위치 설정
  navigator.geolocation.getCurrentPosition(function (location) {
      currentLat = location.coords.latitude;
      currentLng = location.coords.longitude;
      map.setCenter(new naver.maps.LatLng(currentLat, currentLng)); // 지도 중심 지정
      map.setZoom(15); // 줌 조정

      // 초기 매장 로드
      loadStores();
    }, function () { // 위치 정보 실패
      // console.warn('위치 정보를 가져오지 못했습니다.');
      loadStores();
    }
  );

  // 필터 옵션 로드 함수
  function loadFilterOptions() {
    fetch(`${contextPath}search/filter-options`)
      .then(response => response.json())
      .then(data => {
        renderFilterOptions(data);
      })
      .catch(error => {
        console.error('필터 옵션 로드 실패:', error);
      });
  }

  // 필터 옵션 렌더링
  function renderFilterOptions(data) {
    // 카테고리 필터
    const categoryContainer = document.getElementById('category-filters');
    categoryContainer.innerHTML = '';
    if (data.categories && data.categories.length > 0) {
      data.categories.forEach(category => {
        categoryContainer.innerHTML += `
          <li>
            <label class="container_check">${category}
              <input type="checkbox" name="category" value="${category}">
              <span class="checkmark"></span>
            </label>
          </li>
        `;
      });
    } else {
      categoryContainer.innerHTML = '<li><small>카테고리 데이터가 없습니다.</small></li>';
    }

    // 육수 필터
    const soupContainer = document.getElementById('soup-filters');
    soupContainer.innerHTML = '';
    if (data.soups && data.soups.length > 0) {
      data.soups.forEach(soup => {
        soupContainer.innerHTML += `
          <li>
            <label class="container_check">${soup}
              <input type="checkbox" name="soup" value="${soup}">
              <span class="checkmark"></span>
            </label>
          </li>
        `;
      });
    } else {
      soupContainer.innerHTML = '<li><small>육수 데이터가 없습니다.</small></li>';
    }

    // 토핑 필터
    const toppingContainer = document.getElementById('topping-filters');
    toppingContainer.innerHTML = '';
    if (data.toppings && data.toppings.length > 0) {
      data.toppings.forEach(topping => {
        toppingContainer.innerHTML += `
          <li>
            <label class="container_check">${topping}
              <input type="checkbox" name="topping" value="${topping}">
              <span class="checkmark"></span>
            </label>
          </li>
        `;
      });
    } else {
      toppingContainer.innerHTML = '<li><small>토핑 데이터가 없습니다.</small></li>';
    }
  }

  // 매장 로드 함수
  function loadStores(page = 1) {
    currentPage = page;
    const url = isFilterMode ? 
      `${contextPath}search/filter?lat=${currentLat}&lng=${currentLng}&page=${page}&size=${size}` :
      `${contextPath}search/stores?lat=${currentLat}&lng=${currentLng}&page=${page}&size=${size}`;

    fetch(url)
      .then(response => response.json())
      .then(data => {
        renderStores(data.content);
        renderPagination(data);
        updateSearchResultCount(data);
      })
      .catch(error => {
        console.error('매장 검색 실패: ', error);
        document.getElementById('store-list').innerHTML = '<p>매장을 불러오는데 실패했습니다.</p>';
        document.getElementById('search-result-count').textContent = '검색 중 오류가 발생했습니다.';
      });
  }

  // 검색 결과 수 업데이트 함수
  function updateSearchResultCount(data) {
    const resultCountElement = document.getElementById('search-result-count');
    const totalElements = data.totalElements;
    
    if (totalElements === 0) {
      resultCountElement.textContent = '검색 결과가 없습니다.';
    } else {
      resultCountElement.textContent = `총 ${totalElements}개의 매장을 찾았습니다.`;
    }
  }

  // 페이징 렌더링 함수
  function renderPagination(data) {
    totalPages = data.totalPages;
    totalElements = data.totalElements;
    currentPage = data.number + 1; // Spring Data는 0부터 시작하므로 +1

    const paginationContainer = document.getElementById('pagination-container');
    
    if (totalPages <= 1) {
      paginationContainer.innerHTML = '';
      return;
    }

    let paginationHTML = '';
    
    // 이전 페이지 버튼
    if (currentPage > 1) {
      paginationHTML += `<a href="#" onclick="changePage(${currentPage - 1})">&laquo;</a>`;
    }

    // 페이지 번호들
    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, currentPage + 2);

    if (startPage > 1) {
      paginationHTML += `<a href="#" onclick="changePage(1)">1</a>`;
      if (startPage > 2) {
        paginationHTML += `<span>...</span>`;
      }
    }

    for (let i = startPage; i <= endPage; i++) {
      if (i === currentPage) {
        paginationHTML += `<a href="#" class="active">${i}</a>`;
      } else {
        paginationHTML += `<a href="#" onclick="changePage(${i})">${i}</a>`;
      }
    }

    if (endPage < totalPages) {
      if (endPage < totalPages - 1) {
        paginationHTML += `<span>...</span>`;
      }
      paginationHTML += `<a href="#" onclick="changePage(${totalPages})">${totalPages}</a>`;
    }

    // 다음 페이지 버튼
    if (currentPage < totalPages) {
      paginationHTML += `<a href="#" onclick="changePage(${currentPage + 1})">&raquo;</a>`;
    }

    paginationContainer.innerHTML = paginationHTML;
  }

  // 페이지 변경 함수 (전역 함수로 등록)
  window.changePage = function(page) {
    loadStores(page);
    // 페이지 상단으로 스크롤
    document.querySelector('.content-left').scrollTop = 0;
  };

  // 맵에서 확인 함수 (전역 함수로 등록)
  window.showMarkerOnMap = function(index) {
    if (index >= 0 && index < markers.length) {
      const marker = markers[index];
      const infoWindow = infoWindows[index];
      
      // 지도를 해당 마커 위치로 이동
      map.setCenter(marker.getPosition());
      map.setZoom(16); // 상세 줌 레벨
      
      // 다른 인포윈도우들 닫기
      infoWindows.forEach(iw => iw.close());
      
      // 해당 인포윈도우 열기
      infoWindow.open(map, marker);
      
      // 부드러운 애니메이션을 위한 약간의 지연
      setTimeout(() => {
        // 마커에 깜빡임 효과 추가 (선택사항)
        marker.setAnimation(naver.maps.Animation.BOUNCE);
        setTimeout(() => {
          marker.setAnimation(null);
        }, 1000);
      }, 300);
    }
  };

  // 필터 적용 함수
  function applyFilters() {
    const selectedCategories = Array.from(document.querySelectorAll('input[name="category"]:checked')).map(cb => cb.value);
    const selectedSoups = Array.from(document.querySelectorAll('input[name="soup"]:checked')).map(cb => cb.value);
    const selectedToppings = Array.from(document.querySelectorAll('input[name="topping"]:checked')).map(cb => cb.value);
    const keyword = document.getElementById('search-keyword').value.trim();
    const searchType = document.getElementById('search-type').value;

    // 필터가 하나라도 선택되었거나 검색어가 있으면 필터 모드로 전환
    isFilterMode = selectedCategories.length > 0 || selectedSoups.length > 0 || 
                   selectedToppings.length > 0 || keyword.length > 0;

    if (isFilterMode) {
      // 필터링된 검색
      const filterParams = new URLSearchParams({
        lat: currentLat,
        lng: currentLng,
        page: 1, // 필터 적용 시 첫 페이지로
        size: size
      });

      if (selectedCategories.length > 0) {
        selectedCategories.forEach(cat => filterParams.append('ramenCategory', cat));
      }
      if (selectedSoups.length > 0) {
        selectedSoups.forEach(soup => filterParams.append('ramenSoup', soup));
      }
      if (selectedToppings.length > 0) {
        selectedToppings.forEach(topping => filterParams.append('topping', topping));
      }
      if (keyword.length > 0) {
        filterParams.append('keyword', keyword);
        filterParams.append('searchType', searchType);
      }

      fetch(`${contextPath}search/filter?${filterParams.toString()}`)
        .then(response => response.json())
        .then(data => {
          renderStores(data.content);
          renderPagination(data);
          updateSearchResultCount(data);
        })
        .catch(error => {
          console.error('필터링된 매장 검색 실패: ', error);
          document.getElementById('store-list').innerHTML = '<p>매장을 불러오는데 실패했습니다.</p>';
          document.getElementById('search-result-count').textContent = '검색 중 오류가 발생했습니다.';
        });
    } else {
      // 일반 검색
      loadStores(1);
    }
  }

  // 필터 초기화 함수
  function clearFilters() {
    document.querySelectorAll('input[type="checkbox"]').forEach(cb => cb.checked = false);
    document.getElementById('search-keyword').value = '';
    document.getElementById('search-type').value = 'ALL';
    document.getElementById('distance-range').value = 30;
    document.getElementById('distance-value').textContent = 30;
    
    isFilterMode = false;
    loadStores(1);
  }

  // 이벤트 리스너 등록
  document.getElementById('apply-filters').addEventListener('click', applyFilters);
  document.getElementById('clear-filters').addEventListener('click', clearFilters);

  // 거리 슬라이더 이벤트
  document.getElementById('distance-range').addEventListener('input', function() {
    document.getElementById('distance-value').textContent = this.value;
  });

  // 매장 랜더링
  function renderStores(stores) {
    const list = document.getElementById('store-list'); // 리스트 표출
    list.innerHTML = '';

    // 기존 마커,인포 제거
    markers.forEach(marker => marker.setMap(null));
    markers.length = 0;
    infoWindows.length = 0;
    console.log(stores);

    // 검색된 매장이 없을 시
    if (stores == null || stores.length === 0) {
      const item = document.createElement('div');
      item.classList.add('col-lg-12', 'col-sm-6');
      item.innerHTML = `<p>검색 결과가 없습니다.</p>`;
      list.appendChild(item);
      return;
    }

    // 검색된 매장 존재 시
    stores.forEach((store, idx) => {
      // 리스트 생성
      const item = document.createElement('div');
      item.classList.add('col-lg-12', 'col-sm-6');

      const imgUrl = store.storeMainImageUrl || `${contextPath}img/lazy-placeholder.png`;
      const address = store.address || '';
      const phone = store.phone || '';
      const hasParking = store.hasParking || '';
      const distance = store.distance ? Math.round(store.distance) : null;

      // 태그 생성 함수
      function createTags() {
        let tags = '';
        
        // 주차 태그
        if (hasParking && hasParking !== 'NOT_AVAILABLE') {
          const parkingText = hasParking === 'FREE' ? '무료주차' : '유료주차';
          const parkingColor = hasParking === 'FREE' ? '#28a745' : '#fd7e14';
          tags += `<li><a href="#0" style="border: 1px solid ${parkingColor}; color: ${parkingColor}; background-color: transparent;">
            <i class="icon_cart" style="margin-right: 4px;"></i>${parkingText}
          </a></li>`;
        }
        
        // 지역카드 태그
        if (store.isLocalCard) {
          tags += `<li><a href="#0" style="border: 1px solid #007bff; color: #007bff; background-color: transparent;">
            <i class="icon_creditcard" style="margin-right: 4px;"></i>지역카드
          </a></li>`;
        }
        
        // 어린이동반 태그
        if (store.isChildAllowed) {
          tags += `<li><a href="#0" style="border: 1px solid #ffc107; color: #ffc107; background-color: transparent;">
            <i class="icon_gift" style="margin-right: 4px;"></i>어린이동반
          </a></li>`;
        }
        
        // 거리 태그 (회색으로 표시)
        if (distance !== null) {
          const distanceText = distance < 1000 ? `${distance}m` : `${(distance/1000).toFixed(1)}km`;
          tags += `<li><a href="#0" style="color: #6c757d; background-color: transparent; border: none;">
            <i class="icon_pin_alt" style="margin-right: 4px;"></i>${distanceText}
          </a></li>`;
        }
        
        return tags;
      }

      // 소유자 코멘트 표시
      const ownerCommentHtml = store.ownerComment ? 
        `<div class="owner-comment">
          <strong><i class="icon_comment" style="margin-right: 4px;"></i>사장님 한마디:</strong> ${store.ownerComment}
        </div>` : '';

      item.innerHTML = `
      <div class="strip store-list">
        <figure>
          <img src="${imgUrl}" class="img-fluid lazy" alt="${store.storeName}" 
               onerror="this.onerror=null; this.src='${contextPath}img/lazy-placeholder.png';">
          <a href="${contextPath}store/detail/${store.id}" class="strip_info">
            <div class="item_title">
              <h3>${store.storeName}</h3>
              <small>${address}</small>
            </div>
          </a>
        </figure>
        <ul>
          <li><a href="#0" onclick="showMarkerOnMap(${idx})" class="address">맵에서 확인</a></li>
          <li><div class="score"><span><i class="icon_phone" style="margin-right: 4px;"></i>${phone}</span></div></li>
        </ul>
        <ul class="tags">
          ${createTags()}
        </ul>
        ${ownerCommentHtml}
      </div>
      `;
      list.appendChild(item);
      // 마커 생성
      const marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(store.storeLat, store.storeLng),
        map: map,
        title: store.storeName
      });

      // 인포 윈도우 생성
      const infoWindow = new naver.maps.InfoWindow({
        content: `
          <div style="padding:10px; min-width:200px;">
            <strong>${store.storeName}</strong><br>
            ${address}<br>
            <a href="${contextPath}store/detail/${store.id}" style="color:#007bff; text-decoration:none; font-size:12px;">
              상세보기 →
            </a>
          </div>
        `
      });

      // 마커 클릭 이벤트 - 인포윈도우 토글
      naver.maps.Event.addListener(marker, 'click', () => {
        if (infoWindow.getMap()) {
          // 이미 열려있으면 닫기
          infoWindow.close();
        } else {
          // 닫혀있으면 열기 (다른 인포윈도우들 먼저 닫기)
          infoWindows.forEach(iw => iw.close());
          infoWindow.open(map, marker);
        }
      });

      markers.push(marker);
      infoWindows.push(infoWindow);

    });

    // 지도 위치 조정
    adjustMapToStores(stores);
  }

  // 지도 위치 조정 함수
  function adjustMapToStores(stores) {
    if (stores.length === 0) return;

    if (stores.length === 1) {
      // 매장이 1개인 경우: 해당 위치로 중심 이동
      const store = stores[0];
      const position = new naver.maps.LatLng(store.storeLat, store.storeLng);
      map.setCenter(position);
      map.setZoom(16); // 상세 줌
    } else {
      // 매장이 여러 개인 경우: 모든 매장이 보이도록 영역 계산
      const bounds = new naver.maps.LatLngBounds();
      
      stores.forEach(store => {
        bounds.extend(new naver.maps.LatLng(store.storeLat, store.storeLng));
      });

      // 경계에 여백 추가
      const padding = 0.01; // 약 1km 여백
      bounds.extend(new naver.maps.LatLng(
        bounds.getMin().lat() - padding,
        bounds.getMin().lng() - padding
      ));
      bounds.extend(new naver.maps.LatLng(
        bounds.getMax().lat() + padding,
        bounds.getMax().lng() + padding
      ));

      // 지도 영역 설정
      map.fitBounds(bounds);
    }
  }
});

