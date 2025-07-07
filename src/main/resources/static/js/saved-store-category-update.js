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
      alert("수정할 카테고리를 선택하세요.");
      return;
    }

    fetch('/mypage/my/saved-store/categories/update', {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(payload)
    })
      .then(res => {
        if (!res.ok) {
          return res.json().then(errorData => {
            throw new Error(errorData.message);
          });
        }
        return res.json();
      })
      .then(json => {
        if (!json.success) throw new Error(json.message);
        alert(json.message);
        location.reload();
      })
      .catch(err => {
        alert(err.message || "저장 가게 카테고리 수정에 실패하였습니다.");
        location.reload();
      });
  });



  // === 삭제 ===
  deleteBtn?.addEventListener('click', function (evt) {
    evt.preventDefault();
    const selected = Array.from(document.querySelectorAll('.filter_type input.category_check:checked'))
      .map(input => Number(input.value));

    if (selected.length === 0) {
      alert("삭제할 카테고리를 선택하세요.");
      return;
    }

    fetch('/mypage/my/saved-store/categories/delete', {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(selected)
    })
      .then(res => {
        if (!res.ok) {
          return res.json().then(errorData => {
            throw new Error(errorData.message);
          });
        }
        return res.json();
      })
      .then(json => {
        if (!json.success) throw new Error(json.message);
        alert(json.message);
        location.reload();
      })
      .catch(err => {
        alert(err.message);
        location.reload();
      });
  });

});
