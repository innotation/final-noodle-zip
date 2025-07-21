document.addEventListener("DOMContentLoaded", function () {
  const updateBtn = document.querySelector('#update_categories');
  const deleteBtn = document.querySelector('#delete_categories');

  // === 카테고리 수정 ===
  updateBtn?.addEventListener('click', function (evt) {
    evt.preventDefault();

    const payload = [];

    document.querySelectorAll('.filter_type input.category_check:checked').forEach(cb => {
      const li = cb.closest('li');
      const categoryId = Number(cb.value);
      const categoryName = li.querySelector('.category_name_input').value.trim();

      const publicRadio = li.querySelector(`input[name="public_${categoryId}"]:checked`);
      const isPublic = publicRadio ? (publicRadio.value === "true") : false;

      payload.push({
        saveStoreCategoryId: categoryId,
        savedStoreCategoryName: categoryName,
        isPublic: isPublic
      });
    });

    if (payload.length === 0) {
      showConfirmModal('noCheckUpdateCategory');
      return;
    }

    fetch('/users/my/saved-store/categories/update', {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(payload)
    })
      .then(res => {
        showConfirmModal('successUpdateCategory', () => {
          location.reload();
        });
      })
      .catch(err => {
        showErrorModal('failSavedStoreShow', err.message);
      });
  });


  // === 삭제 ===
  deleteBtn?.addEventListener('click', function (evt) {
    evt.preventDefault();
    const selected = Array.from(document.querySelectorAll('.filter_type input.category_check:checked'))
      .map(input => Number(input.value));

    if (selected.length === 0) {
      showConfirmModal('noCheckDeleteCategory');
      return;
    }

    fetch('/users/my/saved-store/categories/delete', {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(selected)
    })
      .then(res => {
        showConfirmModal('successDeleteCategory', () => {
          location.reload();
        });
      })
      .catch(err => {
        showErrorModal('failSavedStoreShow', err.message);
      });
  });

});
