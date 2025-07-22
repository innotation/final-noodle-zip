function deleteBoard(boardId) {
    showConfirmModal('confirmDeleteBoard', () => {
        fetch(`/users/my/${boardId}/delete`, {
            method: 'POST'
        })
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url;
                    return;
                }
                if (res.ok) {
                    location.reload();
                } else {
                    return res.json().then(err => {
                        throw new Error(err.message || '삭제 실패');
                    })
                }
            })
            .catch(err => {
                showErrorModal('failDelete', err.message);
            });
    })
}

function cancelLike(boardId) {
    showConfirmModal('confirmDeleteLike', () => {
        fetch(`/board/like/${boardId}`, { method: 'POST' })
            .then(res => {
                if (!res.ok) return res.json().then(err => { throw new Error(err.message || '좋아요 취소 실패'); });
                return res.json();
            })
            .then(data => {
                location.reload();
            })
            .catch(err =>  {
                showErrorModal('failDelete', err.message);
            });
    })
}

function performSearch() {
    const searchInput = document.getElementById('searchInput');
    const keyword = searchInput.value.trim();

    if (keyword === "") {
        alert("검색어를 입력해주세요.");
        searchInput.focus();
        return;
    }

    const encodedKeyword = encodeURIComponent(keyword);

    // 현재 URL의 경로 부분을 가져옵니다. (예: /board/free/list)
    const currentPath = window.location.pathname;

    let targetUrl = "/board/list"; // 기본 검색 대상 URL (모든 게시글 검색 시)
    let communityType = null;

    // 정규 표현식을 사용하여 경로에서 communityType을 추출합니다.
    // 예: /board/free/list, /board/notice/list 등
    const pathMatch = currentPath.match(/\/board\/([a-zA-Z0-9]+)\/list/);

    if (pathMatch && pathMatch[1]) {
        // 정규 표현식에 매칭되면 두 번째 캡처 그룹([1])이 communityType입니다.
        communityType = pathMatch[1];
    }

    if (communityType) {
        targetUrl = `/board/${encodeURIComponent(communityType)}/list?search=${encodedKeyword}`;
    } else {
        targetUrl = `/board/list?search=${encodedKeyword}`;
    }

    location.href = targetUrl;
}

const searchInput = document.getElementById('searchInput');
searchInput.addEventListener('keypress', function (event) {
    if (event.key === 'Enter') {
        performSearch();
    }
});