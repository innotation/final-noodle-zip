document.addEventListener('DOMContentLoaded', function () {
  const markersByCategory = {};
  const allMarkers = [];
  const infoWindows = [];
  let cluster = null;
  // let currentMapCenter = new naver.maps.LatLng(37.5665, 126.9780);
  let currentMapCenter = new naver.maps.LatLng(36.5670, 126.9780);
  let currentZoom = 7;
  let currentOpenInfoWindow = null;
  let previousCategories = []; // 이전 선택된 카테고리 기억

  window.closeInfoWindow = function (buttonElement) {
    try {
      if (currentOpenInfoWindow) {
        currentOpenInfoWindow.close();
        currentOpenInfoWindow = null;
      }
    } catch (error) {
      console.error('인포윈도우 닫기 오류:', error);
    }
  };

  window.closeCurrentInfoWindow = function () {
    try {
      if (currentOpenInfoWindow) {
        currentOpenInfoWindow.close();
        currentOpenInfoWindow = null;
      }
    } catch (error) {
      console.error('인포윈도우 닫기 오류:', error);
    }
  };

  window.goToStoreDetail = function (storeId) {
    window.location.href = `/store/${storeId}`;
  };

  const map = new naver.maps.Map('map', {
    center: currentMapCenter,
    zoom: currentZoom
  });

  naver.maps.Event.addListener(map, 'center_changed', function () {
    currentMapCenter = map.getCenter();
  });

  naver.maps.Event.addListener(map, 'zoom_changed', function () {
    currentZoom = map.getZoom();
  });

  const collapseMapEl = document.getElementById('collapseMap');
  const collapseMap = new bootstrap.Collapse(collapseMapEl, {
    toggle: false
  });

  collapseMapEl.addEventListener('shown.bs.collapse', function () {
    naver.maps.Event.trigger(map, 'resize');
    map.setCenter(currentMapCenter);
    map.setZoom(currentZoom);

    const selectedCategories = getSelectedCategories();
    if (selectedCategories.length === 0) {
      alert("조회할 카테고리를 선택하세요.");
      return;
    }

    // 이전 선택과 비교
    if (arraysEqual(selectedCategories, previousCategories)) {
      return;
    }
    loadSavedStoreMapData(selectedCategories);

    // 현재 카테고리 기억
    previousCategories = [...selectedCategories];
  });

  const btnMap = document.querySelector(".btn_map");
  if (btnMap) {
    btnMap.addEventListener("click", function (evt) {
      evt.preventDefault();

      if (!collapseMapEl.classList.contains('show')) {
        collapseMap.show();
      }
    });
  }

  function getSelectedCategories() {
    return Array.from(document.querySelectorAll('input[type=checkbox]:checked'))
      .map(cb => cb.value)
      .filter(val => val && val.trim() !== '');
  }

  function arraysEqual(arr1, arr2) {
    if (arr1.length !== arr2.length) return false;
    const sorted1 = [...arr1].sort();
    const sorted2 = [...arr2].sort();
    return sorted1.every((val, idx) => val === sorted2[idx]);
  }

  function clearAllMarkers() {
    Object.values(markersByCategory).flat().forEach(marker => marker.setMap(null));
    infoWindows.forEach(iw => iw.close());
    if (cluster) cluster.clear();
    allMarkers.length = 0;
    infoWindows.length = 0;
    for (const categoryId in markersByCategory) {
      markersByCategory[categoryId] = [];
    }
  }

  function loadSavedStoreMapData(selectedCategories) {
    const path = document.body.getAttribute('data-path');

    if (!selectedCategories || selectedCategories.length === 0) {
      console.warn('선택된 카테고리가 없습니다.');
      return;
    }

    const params = new URLSearchParams();
    selectedCategories.forEach(id => params.append('categoryIdList', id));
    params.append('isAllCategory', false);

    fetch(`/mypage/${path}/saved-store/list/map?${params.toString()}`)
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .then(data => {
        if (data && data.locationListByCategoryId) {
          renderMapMarkers(data.locationListByCategoryId);
        } else {
          console.warn('받은 데이터가 예상 형식과 다릅니다:', data);
          alert('지도 데이터를 불러올 수 없습니다.');
        }
      })
      .catch(err => {
        console.error('지도 데이터 불러오기 실패:', err);
        alert('지도 데이터를 불러오는 중 오류가 발생했습니다.');
      });
  }

  function renderMapMarkers(locationListByCategoryId) {
    clearAllMarkers();

    for (const categoryId in locationListByCategoryId) {
      markersByCategory[categoryId] = [];
      const stores = locationListByCategoryId[categoryId];

      if (!stores || stores.length === 0) continue;

      stores.forEach(store => {
        if (!store.storeLat || !store.storeLng) {
          console.warn('좌표가 없는 매장:', store);
          return;
        }

        const position = new naver.maps.LatLng(store.storeLat, store.storeLng);
        const marker = new naver.maps.Marker({
          position: position,
          map: map,
          title: store.storeName
        });

        const infoWindow = new naver.maps.InfoWindow({
          content: createInfoWindowContent(store)
        });

        naver.maps.Event.addListener(marker, 'click', function () {
          infoWindows.forEach(iw => iw.close());
          currentOpenInfoWindow = infoWindow;
          infoWindow.open(map, marker);
        });

        markersByCategory[categoryId].push(marker);
        allMarkers.push(marker);
        infoWindows.push(infoWindow);
      });
    }

    if (allMarkers.length > 0) {
      map.setCenter(currentMapCenter);
      map.setZoom(currentZoom);
    }
  }

  function createInfoWindowContent(store) {
    return `
      <div class="map_infowindow">
        <button class="info_close_btn" onclick="closeInfoWindow(this)">×</button>
        <div class="info_image ${store.storeMainImageUrl ? '' : 'no_image'}">
          ${store.storeMainImageUrl
            ? `<img src="${store.storeMainImageUrl}" alt="${store.storeName}"
                      onerror="this.parentElement.classList.add('no_image');
                               this.style.display='none';
                               this.parentElement.innerHTML='이미지 없음';" />`
            : '이미지 없음'}
        </div>
        <div class="info_content">
          <span class="info_category">${store.saveStoreCategoryName || ''}</span>
          <h3 class="info_title">${store.storeName}</h3>
          <em class="info_address">${store.address}</em>
          ${store.memo
            ? `<div class="info_memo">${store.memo}</div>`
            : ''}
          <a href="/store/detail/${store.storeId}" class="info_detail_btn" target="_blank" rel="noopener noreferrer">상세보기</a>
        </div>
      </div>
    `;
  }

  naver.maps.Event.addListener(map, 'click', function () {
    window.closeCurrentInfoWindow();
  });
});
