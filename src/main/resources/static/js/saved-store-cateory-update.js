document.addEventListener("DOMContentLoaded", function() {
    const updateBtn = document.querySelector('#update_categories');
    const deleteBtn = document.querySelector('#delete_categories');

    // === 카테고리 수정 ===
    updateBtn?.addEventListener('click', function(evt) {
        evt.preventDefault();

        const payload = [];

        document.querySelectorAll('.filter_type input.category_check:checked').forEach(cb => {
            const li = cb.closest('li');
            const categoryId = Number(cb.value);
            const categoryName = li.querySelector('.category_name_input').value.trim();

            // 라디오 그룹에서 name 으로 선택해서 isPublic 값 추출
            const publicRadio = li.querySelector(`input[name="public_${categoryId}"]:checked`);
            const isPublic = publicRadio ? (publicRadio.value === "true") : false;

            console.log(`[DEBUG] categoryId=${categoryId}, name=${categoryName}, isPublic=${isPublic}`);

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

        // 낙관적 업데이트를 위해 백업 저장
        const backup = JSON.parse(JSON.stringify(payload));

        fetch('/mypage/my/saved-store/categories/update', {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        })
            .then(res => res.json())
            .then(json => {
                if (!json.isSuccess) throw new Error(json.message);
                alert(json.message);
            })
            .catch(err => {
                alert("카테고리 수정 실패\n롤백합니다.");
                // 롤백
                document.querySelectorAll('.filter_type input.category_check:checked').forEach((cb, i) => {
                    const li = cb.closest('li');
                    li.querySelector('.category_name_input').value = backup[i].savedStoreCategoryName;
                    li.querySelector(`input[name="public_${backup[i].saveStoreCategoryId}"][value="${backup[i].isPublic}"]`).checked = true;
                });
            });
    });

    // === 삭제 ===
    deleteBtn?.addEventListener('click', function(evt) {
        evt.preventDefault();
        const selected = Array.from(document.querySelectorAll('.filter_type input.category_check:checked'))
            .map(input => Number(input.value));

        if (selected.length === 0) {
            alert("삭제할 카테고리를 선택하세요.");
            return;
        }

        const removedElements = [];
        selected.forEach(id => {
            const el = document.querySelector(`.filter_type input.category_check[value="${id}"]`).closest('li');
            removedElements.push({id, el});
            el.remove();
        });

        fetch('/mypage/my/saved-store/categories/delete', {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(selected)
        })
            .then(res => res.json())
            .then(json => {
                if (!json.isSuccess) throw new Error(json.message);
                alert(json.message);
            })
            .catch(err => {
                alert("삭제 실패, 복원합니다.");
                removedElements.forEach(({el}) => {
                    document.querySelector('#filter_2 ul').appendChild(el);
                });
            });
    });
});
