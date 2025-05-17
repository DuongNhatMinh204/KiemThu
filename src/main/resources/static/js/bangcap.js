function hideAllPanels1() {
    ['subjectPanel', 'classroomPanel', 'teacherPanel', 'departmentPanel', 'teachingStatsPanel', 'degreePanel', 'degreeFormPanel']
        .forEach(id => document.getElementById(id)?.style && (document.getElementById(id).style.display = 'none'));
}

// Hiển thị panel chính và load danh sách
function showDegreePanel() {
    hideAllPanels1();
    document.getElementById('degreePanel').style.display = 'block';
    fetch('/admin/degree/get-all')
        .then(r => r.json())
        .then(j => {
            const tbody = document.querySelector('#degreeTable tbody');
            tbody.innerHTML = '';
            j.data.forEach((deg, i) => {
                tbody.innerHTML += `
            <tr>
              <td>${i + 1}</td>
              <td>${deg.shortName}</td>
              <td>${deg.fullName}</td>
              <td>
                <button class="btn btn-sm btn-warning" onclick='editDegree(${JSON.stringify(deg)})'>Sửa</button>
                <button class="btn btn-sm btn-danger" onclick="deleteDegree(${deg.id})">Xóa</button>
              </td>
            </tr>`;
            });
        });
}

function showAddDegreeForm() {
    document.getElementById('degreeFormTitle').innerText = 'Thêm Bằng Cấp';
    document.getElementById('degreeId').value = '';
    document.getElementById('degreeShortName').value = '';
    document.getElementById('degreeFullName').value = '';
    document.getElementById('degreeFormPanel').style.display = 'block';
}

function hideDegreeForm() {
    document.getElementById('degreeFormPanel').style.display = 'none';
}

function editDegree(deg) {
    document.getElementById('degreeFormTitle').innerText = 'Cập Nhật Bằng Cấp';
    document.getElementById('degreeId').value = deg.id;
    document.getElementById('degreeShortName').value = deg.shortName;
    document.getElementById('degreeFullName').value = deg.fullName;
    document.getElementById('degreeFormPanel').style.display = 'block';
}

function submitDegree() {
    const id = document.getElementById('degreeId').value;
    const payload = {
        shortName: document.getElementById('degreeShortName').value.trim(),
        fullName: document.getElementById('degreeFullName').value.trim()
    };
    const method = id ? 'PUT' : 'POST';
    const url = id
        ? `/admin/degree/update/${id}`
        : '/admin/degree/create';

    fetch(url, {
        method,
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    })
        .then(r => r.json())
        .then(j => {
            if (j.code === 1000) {
                alert((id ? 'Cập nhật' : 'Thêm') + ' thành công!');
                hideDegreeForm();
                showDegreePanel();
            } else alert('Lỗi: ' + j.message);
        });
}

function deleteDegree(id) {
    if (!confirm('Xác nhận xóa?')) return;
    fetch(`/admin/degree/${id}`, {method: 'DELETE'})
        .then(r => r.json())
        .then(j => {
            if (j.code === 1000) {
                alert('Xóa thành công!');
                showDegreePanel();
            } else alert('Lỗi: ' + j.message);
        });
}

