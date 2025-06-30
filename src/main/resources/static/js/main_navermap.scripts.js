document.addEventListener('DOMContentLoaded', function () {

  const markers = [];
  const infoWindows = [];

  // 지도 초기화 (서울 시청 기준)
  const map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.5665, 126.9780),
    zoom: 13,
  });

  // 현재 위치 설정
  navigator.geolocation.getCurrentPosition(function (location) {
      const lat = location.coords.latitude;
      const lng = location.coords.longitude;
      map.setCenter(new naver.maps.LatLng(lat, lng)); // 지도 중심 지정
      map.setZoom(15); // 줌 조정


      // fetch 요청
      fetch(`${contextPath}search/stores?lat=${lat}&lng=${lng}&page=${page}&size=${size}`)
        .then(response => response.json())
        .then(data => {
          renderStores(data.content);
        })

    }, function () { // 위치 정보 실패
      // console.warn('위치 정보를 가져오지 못했습니다.');
      fetch(`${contextPath}search/stores?page=${page}&size=${size}`)
        .then(response => response.json())
        .then(data => {
          renderStores(data.content);
          console.log(data.content);
        });
    }
  );

  // 매장 랜더링
  function renderStores(stores) {
    const list = document.getElementById('store-list'); // 리스트 표출
    list.innerHTML = '';

    // 기존 마커,인포 제거
    markers.forEach(marker => marker.setMap(null));
    markers.length = 0;
    infoWindows.lenth = 0;

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

