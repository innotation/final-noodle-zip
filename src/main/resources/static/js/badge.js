document.addEventListener('DOMContentLoaded', function() {
  const modal = document.getElementById('badgeModal');
  const modalClose = document.getElementById('modalClose');
  const modalLoading = document.getElementById('modalLoading');
  const modalContent = document.getElementById('modalContent');

  function closeModal() {
    modal.classList.remove('show');
    document.body.style.overflow = '';
  }

  function formatDate(isoString) {
    if (!isoString) return '';
    const date = new Date(isoString);
    return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일`;
  }

  modalClose.addEventListener('click', closeModal);
  modal.addEventListener('click', (evt) => {
    if (evt.target === modal) closeModal();
  });
  document.addEventListener('keydown', (evt) => {
    if (evt.key === 'Escape') closeModal();
  });

  document.querySelectorAll('.categories_carousel .item a').forEach(a => {
    a.addEventListener('click', evt => {
      evt.preventDefault();

      const userId = a.dataset.userId;
      const badgeId = a.dataset.badgeId;
      const badgeCategoryId = a.dataset.badgeCategoryId;

      modal.classList.add('show');
      document.body.style.overflow = 'hidden';
      modalLoading.style.display = 'flex';
      modalContent.style.display = 'none';

      const params = new URLSearchParams({
        userId: userId,
        badgeCategoryId: badgeCategoryId
      });

      fetch(`/users/badges/${badgeId}?${params.toString()}`)
        .then(response => response.json())
        .then(data => {
          modalLoading.style.display = 'none';
          modalContent.style.display = 'block';

          document.getElementById('badgeName').textContent = data.mainTitle;
          document.getElementById('badgeDescription').textContent = data.description ?? '';
          document.getElementById('badgeMainImage').src = data.mainImageUrl;

          const badgeLevelsGrid = document.getElementById('badgeLevelsGrid');
          const levelSectionWrapper = document.getElementById('levelSectionWrapper');

          badgeLevelsGrid.innerHTML = '';

          if (data.levelBadgeDetailList && data.levelBadgeDetailList.length > 0) {
            levelSectionWrapper.style.display = 'block';
            data.levelBadgeDetailList.forEach(level => {
              const div = document.createElement('div');
              div.className = 'level-badge-item';
              div.innerHTML = `
                                <img src="${level.imageUrl}" alt="Level Badge" class="badge-detail-image">
                                <h6>${level.levelTitle}</h6>
                                <small>${level.obtainedDate ? formatDate(level.obtainedDate) : '-미달성-'}</small>
                              `;
              badgeLevelsGrid.appendChild(div);
            });
          } else {
            levelSectionWrapper.style.display = 'none';
            document.getElementById('badgeDescription').innerHTML +=
              `<small>${level.obtainedDate ? formatDate(level.obtainedDate) : ''}</small>`;
          }
        })
        .catch(error => {
          console.error('Error fetching badge detail:', error);
          modal.classList.remove('show');
          document.body.style.overflow = '';
        });
    });
  });
});