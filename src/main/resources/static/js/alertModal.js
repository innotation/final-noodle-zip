function showConfirmModal(modalId, onConfirm, onCancel) {
  const modalEl = document.getElementById(modalId);
  const modal = new bootstrap.Modal(modalEl);
  modal.show();

  const confirmBtn = modalEl.querySelector('.btn-confirm');
  const cancelBtn = modalEl.querySelector('.btn-cancel');

  // 이벤트 중복 제거 후 재등록
  if (confirmBtn) {
    const newConfirm = confirmBtn.cloneNode(true);
    confirmBtn.replaceWith(newConfirm);
    newConfirm.addEventListener('click', () => {
      if (onConfirm) onConfirm();
      modal.hide();
    }, { once: true });
  }

  if (cancelBtn) {
    const newCancel = cancelBtn.cloneNode(true);
    cancelBtn.replaceWith(newCancel);
    newCancel.addEventListener('click', () => {
      if (onCancel) onCancel();
      modal.hide();
    }, { once: true });
  }
}

function showErrorModal(modalId, errorMessageHtml) {
  const modalEl = document.getElementById(modalId);
  const modal = new bootstrap.Modal(modalEl);

  const bodyEl = modalEl.querySelector('.modal-body');
  if (bodyEl) {
    bodyEl.innerHTML = errorMessageHtml; // <p>태그 등 포함 가능
  }

  modal.show();
}