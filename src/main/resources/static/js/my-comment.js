function deleteComment(id, boardId) {
  showConfirmModal('confirmDeleteComment', () => {

    fetch('/comments/' + id + '?boardId=' + boardId, {
      method: 'DELETE'
    })
      .then(res => {
        if (res.ok) {
          location.reload();
        } else {
          return res.json().then(err => {
            let msg = err.message || err.reason || "삭제 실패";
            showErrorModal('failDeleteComment', msg);
          });
        }
      })
      .catch(() => showErrorModal('failDeleteComment', "삭제 실패"));
  });
}