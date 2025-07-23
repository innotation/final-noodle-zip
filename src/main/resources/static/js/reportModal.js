$(document).ready(function () {
  $('#reportModal').on('show.bs.modal', function (event) {
    const button = $(event.relatedTarget);
    const userId = button.data('user-id');
    const reportType = button.data('type');
    const targetId = button.data('target-id');

    $('#reportUserId').val(userId);
    $('#reportType').val(reportType);
    $('#reportTargetId').val(targetId);
  });

  $('#reportForm').on('submit', function (e) {
    e.preventDefault();

    const checkedReasons = $('input[name="reasons"]:checked');
    if (checkedReasons.length === 0) {
      alert('신고 사유를 한 가지 이상 선택해주세요.');
      return;
    }

    const formData = $(this).serialize();

    $.ajax({
      type: 'POST',
      url: '/report',
      data: formData,
      success: function (response) {
        const modalEl = document.getElementById('reportModal');
        const modalInstance = bootstrap.Modal.getOrCreateInstance(modalEl);

        if (response === 'OK') {
          alert('신고가 접수되었습니다.');

          // if (modalInstance) {
            modalInstance.hide();
          // }

          $('#reportForm')[0].reset();

          // 백드롭 수동 제거 (혹시 안 사라질 경우 대비)
          // $('.modal-backdrop').remove();
          // $('body').removeClass('modal-open');
        } else {
          alert('처리 결과: ' + response);
        }
      },
      error: function () {
        alert('신고 처리 중 오류가 발생했습니다.');
      }
    });
  });
});