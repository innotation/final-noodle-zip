document.addEventListener('DOMContentLoaded', function () {
  const markersByCategory = {};
  const allMarkers = [];
  const infoWindows = [];
  let cluster = null;
  let currentMapCenter = new naver.maps.LatLng(37.5665, 126.9780);
  let currentZoom = 7;
  let currentOpenInfoWindow = null; // 현재 열린 인포윈도우 추적

  const map = new naver.maps.Map('map', {
    center: currentMapCenter,
    zoom: currentZoom
  });

  // 지도 중심점과 줌 레벨 변경 시 저장
  naver.maps.Event.addListener(map, 'center_changed', function () {
    currentMapCenter = map.getCenter();
  });

  naver.maps.Event.addListener(map, 'zoom_changed', function () {
    currentZoom = map.getZoom();
  });

  // collapse가 열릴 때 지도 리사이즈 & 중심 이동
  const collapseMapEl = document.getElementById('collapseMap');
  if (collapseMapEl) {
    collapseMapEl.addEventListener('shown.bs.collapse', function () {
      naver.maps.Event.trigger(map, 'resize');
      map.setCenter(currentMapCenter);
      map.setZoom(currentZoom);
    });
  }

  // 지도 보기 버튼 클릭 시 데이터 로드
  const btnMap = document.querySelector(".btn_map");
  if (btnMap) {
    const collapseMap = new bootstrap.Collapse(document.getElementById('collapseMap'), {
      toggle: false // 자동으로 열리지 않도록
    });

    btnMap.addEventListener("click", function (evt) {
      evt.preventDefault();

      const selectedCategories = Array.from(document.querySelectorAll('input[type=checkbox]:checked'))
        .map(cb => cb.value);
      if (selectedCategories.length === 0) {
        alert("조회할 카테고리를 검색하세요.");
        return; // collapse 아예 열리지 않음
      }

      collapseMap.show(); // 여기서만 collapse 열기
      loadSavedStoreMapData();
    });
  }

  function loadSavedStoreMapData() {
    const path = document.body.getAttribute('data-path');

    // 카테고리 선택된 것 가져오기
    const selectedCategories = Array.from(document.querySelectorAll('input[type=checkbox]:checked'))
      .map(cb => cb.value);
    const isAllCategory = selectedCategories.length === 0;

    // if(selectedCategories.length === 0){
    //   alert("조회할 카테고리를 검색하세요.");
    //   return;
    // }
    // URLSearchParams 구성
    const params = new URLSearchParams();
    selectedCategories.forEach(id => params.append('categoryIdList', id));
    params.append('isAllCategory', isAllCategory);

    fetch(`/mypage/${path}/saved-store/list/map?${params.toString()}`)
      .then(response => response.json())
      .then(data => {
        renderMapMarkers(data.locationListByCategoryId);
      })
      .catch(err => console.error('지도 데이터 불러오기 실패:', err));
  }


  function renderMapMarkers(locationListByCategoryId) {
    // 기존 마커 & 인포윈도우 제거
    Object.values(markersByCategory).flat().forEach(marker => marker.setMap(null));
    infoWindows.forEach(iw => iw.close());
    if (cluster) cluster.clear();
    allMarkers.length = 0;
    infoWindows.length = 0;

    // 데이터 순회하여 마커 생성
    for (const categoryId in locationListByCategoryId) {
      markersByCategory[categoryId] = [];
      const stores = locationListByCategoryId[categoryId];
      stores.forEach(store => {
        const position = new naver.maps.LatLng(store.storeLat, store.storeLng);
        const marker = new naver.maps.Marker({
          position: position,
          map: map,
          title: store.storeName
        });

        const infoWindow = new naver.maps.InfoWindow({
          content: `  
            <div class="marker_info" id="marker_info">              <img src="${store.storeMainImageUrl || '/img/lazy-placeholder.png'}" alt=""/>  
              <span>                <span class="infobox_rate">${store.saveStoreCategoryId || ''}</span>  
                <h3>${store.storeName}</h3>  
                <em>${store.address}</em>  
                <strong>${store.memo || ''}</strong>  
                <a href="/store/${store.storeId}" class="btn_infobox_detail">상세보기</a>  
                <form action="https://maps.google.com/maps" method="get" target="_blank">                  <input name="saddr" value="내 위치" type="hidden">                  <input type="hidden" name="daddr" value="${store.storeLat},${store.storeLng}">  
                  <button type="submit" class="btn_infobox_get_directions">길찾기</button>                </form>                <a href="tel://01012345678" class="btn_infobox_phone">전화하기</a>              </span>            
                  </div>          `
        });

        naver.maps.Event.addListener(marker, 'click', function () {
          infoWindows.forEach(iw => iw.close());
          infoWindow.open(map, marker);
        });

        markersByCategory[categoryId].push(marker);
        allMarkers.push(marker);
        infoWindows.push(infoWindow);
      });
    }

    if (allMarkers.length > 0) {
      cluster = new naver.maps.Marker({
        position: new naver.maps.LatLng(store.storeLat, store.storeLng),
        map: map,
        title: store.storeName
      });

      // 첫 번째 마커가 있는 경우에만 중심 이동 (최초 로드 시에만)
      if (currentMapCenter.lat() === 37.5665 && currentMapCenter.lng() === 126.9780) {
        currentMapCenter = allMarkers[0].getPosition();
        currentZoom = 14;
        map.setCenter(currentMapCenter);
        map.setZoom(currentZoom);
      }
    }
  }

  // 전역 함수들
  window.closeCurrentInfoWindow = function () {
    if (currentOpenInfoWindow) {
      currentOpenInfoWindow.close();
      currentOpenInfoWindow = null;
    }
  };

  window.goToStoreDetail = function (storeId) {
    window.location.href = `/store/${storeId}`;
  };

  // 지도 클릭 시 인포윈도우 닫기
  naver.maps.Event.addListener(map, 'click', function () {
    window.closeCurrentInfoWindow();
  });
});
