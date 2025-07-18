var toolbarOptions = [
    [{'font': []}],
    [{'size': ['small', false, 'large', 'huge']}], // 글씨 크기
    [{'color': []}, {'background': []}], // dropdown with defaults from theme
    ['bold', 'underline'], // 굵게,밑줄
    [{'align': []}],
    [{'list': 'ordered'}, {'list': 'bullet'}], // 숫자,점 정렬
    [{'indent': '-1'}, {'indent': '+1'}], // outdent/indent
    ['blockquote', 'code-block'], // 블럭,코드블록
    ['link', 'image']
];
var quill = new Quill('#editor', {
    modules: {
        toolbar: toolbarOptions
    },
    placeholder: '내용을 작성해주세요.',
    theme: 'snow'  // or 'bubble'
});
quill.on('text-change', function (delta, oldDelta, source) {
    if ($('#content').length > 0) {
        $('#content').val(quill.container.firstChild.innerHTML);
    } else {
        $('#content').val(quill.container.firstChild.innerHTML);
    }
});
quill.getModule('toolbar').addHandler('image', function () {
    selectLocalImage();
});

function selectLocalImage() {
    const fileInput = document.createElement('input');
    fileInput.setAttribute('type', 'file');
    fileInput.setAttribute('accept', 'image/*');
    fileInput.setAttribute('multiple', 'true');
    console.log(fileInput.type);
    fileInput.click();
    fileInput.onchange = function () {
        const formData = new FormData();
        const files = fileInput.files;
        if (files.length === 0) {
            console.log("No files selected.");
            return; // 파일이 선택되지 않았다면 함수 종료
        }
        // const file = fileInput.files[0];
        // formData.append('uploadFile', files);
        for (let i = 0; i < files.length; i++) {
            formData.append('uploadFiles', files[i]);
        }
        $.ajax({
            type: 'post',
            enctype: 'multipart/form-data',
            url: 'imageUpload',
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                // console.log("data 업로드 성공 : " + data)
                // const range = quill.getSelection();
                // quill.insertEmbed(range.index, 'image', "/display?fileName=" + data.fileName + "&datePath=" + data.datePath);
                const range = quill.getSelection();
                let currentIndex = range.index;
                data.payload.forEach(imageInfo => {
                    // const imageUrl = "/display?fileName=" + data.fileName + "&datePath=" + data.datePath;
                    console.log(imageInfo.fileUrl);
                    quill.insertEmbed(currentIndex, 'image', imageInfo.fileUrl);
                    currentIndex += 1;
                });
            },
            error: function (err) {
                console.error("Err :: " + err);
            }
        });
    };
}

function getFirstImageUrlFromHtml(htmlString) {
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = htmlString;
    const imgElements = tempDiv.querySelectorAll('img');
    return imgElements.length > 0 ? imgElements[0].getAttribute('src') : null;
}

document.getElementById('reviewForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // 기본 폼 제출 동작 방지

    const editorHtmlContent = quill.root.innerHTML;

    const thumbnailUrl = getFirstImageUrlFromHtml(editorHtmlContent);

    const formData = new FormData();

    formData.append('title', document.getElementById('title').value);
    formData.append('content', editorHtmlContent);
    formData.append('imageUrl', thumbnailUrl || '');

    try {
        console.log(this.action, this.method);
        const response = await fetch(this.action, { // 폼의 action 속성 URL 사용
            method: this.method, // 폼의 method 속성 사용 (POST)
            body: formData // FormData 객체를 직접 body에 할당
        });

        if (!response.ok) {
            const errorData = await response.json(); // 서버에서 에러 메시지를 JSON으로 보낼 경우
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorData.message || response.statusText}`);
        }

        alert('게시글이 성공적으로 작성되었습니다.');
        location.href = '/board/list';
    } catch (error) {
        console.error('게시글 작성 실패:', error);
        alert('게시글 작성 중 오류가 발생했습니다: ' + error.message);
    }
});