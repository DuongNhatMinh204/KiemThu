function showTeacherPanel() {
    hideAllPanels();
    document.getElementById('teacherPanel').style.display = 'block';
    fetch('/admin/teacher/get-all')
        .then(r => r.json())
        .then(j => {
            const tbody = document.getElementById('teacherTableBody');
            tbody.innerHTML = '';
            j.data.forEach((t, i) => {
                tbody.innerHTML += `
            <tr>
              <td>${t.fullName}</td>
              <td>${t.birthday}</td>
              <td>${t.phone}</td>
              <td>${t.email}</td>
              <td>${t.degree.fullName}</td>
              <td>${t.department.fullName}</td>
              <td>
                <button class="btn btn-sm btn-warning" onclick="editTeacher(${t.id})">Sửa</button>
                <button class="btn btn-sm btn-danger" onclick="deleteTeacher(${t.id})">Xóa</button>
              </td>
            </tr>`;
            });
        });
}

// Hiển thị form thêm mới
function showCreateTeacherForm() {
    hideAllPanels();
    document.getElementById('teacherFormTitle').innerText = 'Thêm Mới Giảng Viên';
    document.getElementById('teacherForm').reset();
    document.getElementById('teacherId').value = '';
    // load dropdown
    loadDegrees();
    loadDepartments();
    document.getElementById('createTeacherForm').style.display = 'block';
}

// Ẩn form
function hideCreateTeacherForm() {
    document.getElementById('createTeacherForm').style.display = 'none';
}

// Xóa
function deleteTeacher(id) {
    if (!confirm('Xác nhận xóa giảng viên?')) return;
    fetch(`/admin/teacher/delete/${id}`, {method: 'DELETE'})
        .then(r => r.json())
        .then(j => {
            if (j.code === 1000) showTeacherPanel();
            else alert('Xóa lỗi: ' + j.message);
        });
}

// Sửa: load thông tin vào form
function editTeacher(id) {
    hideAllPanels();
    fetch(`/admin/teacher/get/${id}`)  // giả sử bạn có API GET /get/{id}
        .then(r => r.json())
        .then(j => {
            const t = j.data;
            document.getElementById('teacherFormTitle').innerText = 'Cập Nhật Giảng Viên';
            document.getElementById('teacherId').value = t.id;
            document.querySelector('[name=fullName]').value = t.fullName;
            document.querySelector('[name=birthday]').value = t.birthday;
            document.querySelector('[name=email]').value = t.email;
            document.querySelector('[name=phone]').value = t.phone;
            loadDegrees(t.degree.id);
            loadDepartments(t.department.id);
            document.getElementById('createTeacherForm').style.display = 'block';
        });
}

// Lấy danh sách học vị
function loadDegrees(selectedId) {
    fetch('/admin/degree/get-all')
        .then(r => r.json())
        .then(j => {
            const dd = document.getElementById('degreeDropdown');
            dd.innerHTML = '<option value="">-- Chọn học vị --</option>';
            j.data.forEach(d => {
                const opt = document.createElement('option');
                opt.value = d.id;
                opt.text = d.fullName;
                if (d.id === selectedId) opt.selected = true;
                dd.appendChild(opt);
            });
        });
}

// Lấy danh sách khoa
function loadDepartments(selectedId) {
    fetch('/admin/department/getAll')
        .then(r => r.json())
        .then(data => {
            const select = document.getElementById('departmentDropdown');
            select.innerHTML = '<option value="">-- Chọn khoa --</option>';
            data.data.forEach(d => {
                const option = document.createElement('option');
                option.value = d.id;
                option.text = d.fullName;
                if (selectedId && d.id === selectedId) {
                    option.selected = true;
                }
                select.appendChild(option);
            });
        })
        .catch(err => {
            console.error("❌ Lỗi khi load department:", err);
        });
}

function isValidDateFormat(dateString) {
    const regex = /^\d{2}-\d{2}-\d{4}$/;
    if (!regex.test(dateString)) return false;

    const [day, month, year] = dateString.split('-').map(Number);
    const date = new Date(year, month - 1, day);

    return (
        date.getFullYear() === year &&
        date.getMonth() === month - 1 &&
        date.getDate() === day
    );
}
document.getElementById('teacherForm').addEventListener('submit', e => {
    e.preventDefault();
    const id = document.getElementById('teacherId').value;
    const birthday = e.target.birthday.value.trim();  // giá trị ngày tháng
    if (!isValidDateFormat(birthday)) {
        alert('❌ Ngày sinh không hợp lệ! Vui lòng nhập theo định dạng dd-MM-yyyy.');
        return; // ngăn không cho tiếp tục submit
    }
    const payload = {
        fullName: e.target.fullName.value,
        birthday: e.target.birthday.value,
        email: e.target.email.value,
        phone: e.target.phone.value,
        degreeId: +e.target.degreeId.value,
        departmentId: +e.target.departmentId.value
    };
    const method = id ? 'PUT' : 'POST';
    const url = id ? `/admin/teacher/update/${id}` : '/admin/teacher/create';
    fetch(url, {method, headers: {'Content-Type': 'application/json'}, body: JSON.stringify(payload)})
        .then(r => r.json())
        .then(j => {
            if (j.code === 1000) {
                alert(id ? 'Cập nhật thành công' : 'Thêm thành công');
                hideCreateTeacherForm();
                showTeacherPanel();
            } else alert('Lỗi: ' + j.message);
        });
});
