document.addEventListener('DOMContentLoaded', function () {

  const markers = [];
  const infoWindows = [];
  let currentLat = 37.5665;
  let currentLng = 126.9780;
  let isFilterMode = false;

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
  function loadStores() {
    const url = isFilterMode ? 
      `${contextPath}search/filter?lat=${currentLat}&lng=${currentLng}&page=${page}&size=${size}` :
      `${contextPath}search/stores?lat=${currentLat}&lng=${currentLng}&page=${page}&size=${size}`;

    fetch(url)
      .then(response => response.json())
      .then(data => {
        renderStores(data.content);
      })
      .catch(error => {
        console.error('매장 검색 실패: ', error);
        document.getElementById('store-list').innerHTML = '<p>매장을 불러오는데 실패했습니다.</p>';
      });
  }

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
        page: page,
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
        })
        .catch(error => {
          console.error('필터링된 매장 검색 실패: ', error);
          document.getElementById('store-list').innerHTML = '<p>매장을 불러오는데 실패했습니다.</p>';
        });
    } else {
      // 일반 검색
      loadStores();
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
    loadStores();
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

      item.innerHTML = `
      <div class="strip">
        <figure>
          <img src="${imgUrl}" class="img-fluid lazy" alt="${store.storeName}">
          <a href="#" class="strip_info">
            <small>${phone}</small>
            <div class="item_title">
              <h3>${store.storeName}</h3>
              <small>${address}</small>
            </div>
          </a>
        </figure>
        <ul>
          <li><a href="#0" onclick="onHtmlClick('Marker', ${idx})" class="address">맵에서 확인</a></li>
          <li><div class="score"><span>${phone}</span></div></li>
        </ul>
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
        content: `<div style="padding:10px;"><strong>${store.storeName}</strong><br>${address}</div>`
      });

      naver.maps.Event.addListener(marker, 'click', () => {
        infoWindows.forEach(iw => iw.close());
        infoWindow.open(map, marker);
      });

      markers.push(marker);
      infoWindows.push(infoWindow);

    });
  }
});

