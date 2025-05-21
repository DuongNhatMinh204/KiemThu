function showSemesterPanel() {
    hideAllPanels();
    document.getElementById('semesterPanel').style.display = 'block';
    fetch('/admin/semester/get-all')
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById('semesterTableBody');
            tbody.innerHTML = '';

            if (!data.data || !Array.isArray(data.data)) {
                console.error("Dữ liệu kỳ học không hợp lệ:", data);
                alert("Không tải được danh sách kỳ học!");
                return;
            }

            data.data.forEach((s, i) => {
                tbody.innerHTML += `
                    <tr>
                        <td>${i + 1}</td>
                        <td>${s.semesterName}</td>
                        <td>${s.schoolYear}</td>
                        <td>${s.timeBegin}</td>
                        <td>${s.timeEnd}</td>
                        <td>
                            <button class="btn btn-warning btn-sm"
                                onclick="editSemester(${s.id}, '${s.semesterName}', '${s.schoolYear}', '${s.timeBegin}', '${s.timeEnd}')">
                                ✏️ Sửa
                            </button>
                        </td>
                    </tr>
                `;
            });
        })
        .catch(err => {
            console.error("Lỗi khi fetch kỳ học:", err);
            alert("Không kết nối được đến server.");
        });
}

function loadSemesters() {
    fetch('/admin/semester/get-all')
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById('semesterTableBody');
            tbody.innerHTML = '';

            if (!data.data || !Array.isArray(data.data)) {
                console.error("Dữ liệu kỳ học không hợp lệ:", data);
                alert("Không tải được danh sách kỳ học!");
                return;
            }

            data.data.forEach((s, i) => {
                tbody.innerHTML += `
                    <tr>
                        <td>${i + 1}</td>
                        <td>${s.semesterName}</td>
                        <td>${s.schoolYear}</td>
                        <td>${s.timeBegin}</td>
                        <td>${s.timeEnd}</td>
                        <td>
                            <button class="btn btn-warning btn-sm"
                                onclick="editSemester(${s.id}, '${s.semesterName}', '${s.schoolYear}', '${s.timeBegin}', '${s.timeEnd}')">
                                ✏️ Sửa
                            </button>
                        </td>
                    </tr>
                `;
            });
        })
        .catch(err => {
            console.error("Lỗi khi fetch kỳ học:", err);
            alert("Không kết nối được đến server.");
        });
}


function showSemesterForm() {
    document.getElementById('semesterFormTitle').innerText = 'Thêm Kỳ Học';
    document.getElementById('semesterId').value = '';
    document.getElementById('semesterName').value = '';
    document.getElementById('schoolYear').value = '';
    document.getElementById('timeBegin').value = '';
    document.getElementById('timeEnd').value = '';
    document.getElementById('semesterFormPanel').style.display = 'block';
}

function hideSemesterForm() {
    document.getElementById('semesterFormPanel').style.display = 'none';
}

function editSemester(id, name, year, begin, end) {
    document.getElementById('semesterFormTitle').innerText = 'Cập Nhật Kỳ Học';
    document.getElementById('semesterId').value = id;
    document.getElementById('semesterName').value = name;
    document.getElementById('schoolYear').value = year;
    document.getElementById('timeBegin').value = begin;
    document.getElementById('timeEnd').value = end;
    document.getElementById('semesterFormPanel').style.display = 'block';
}

function submitSemester() {
    const id = document.getElementById('semesterId').value;
    const payload = {
        semesterName: document.getElementById('semesterName').value,
        schoolYear: document.getElementById('schoolYear').value,
        timeBegin: document.getElementById('timeBegin').value,
        timeEnd: document.getElementById('timeEnd').value
    };

    const url = id ? `/admin/semester/update/${id}` : '/admin/semester/create';
    const method = id ? 'PUT' : 'POST';

    fetch(url, {
        method,
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    })
        .then(r => r.json())
        .then(j => {
            if (j.code === 1000) {
                alert(id ? 'Cập nhật thành công' : 'Thêm mới thành công');
                hideSemesterForm();
                // loadSemesters();
                fetch('/admin/semester/get-all')
                    .then(res => res.json())
                    .then(data => {
                        const tbody = document.getElementById('semesterTableBody');
                        tbody.innerHTML = '';

                        if (!data.data || !Array.isArray(data.data)) {
                            console.error("Dữ liệu kỳ học không hợp lệ:", data);
                            alert("Không tải được danh sách kỳ học!");
                            return;
                        }

                        data.data.forEach((s, i) => {
                            tbody.innerHTML += `
                    <tr>
                        <td>${i + 1}</td>
                        <td>${s.semesterName}</td>
                        <td>${s.schoolYear}</td>
                        <td>${s.timeBegin}</td>
                        <td>${s.timeEnd}</td>
                        <td>
                            <button class="btn btn-warning btn-sm"
                                onclick="editSemester(${s.id}, '${s.semesterName}', '${s.schoolYear}', '${s.timeBegin}', '${s.timeEnd}')">
                                ✏️ Sửa
                            </button>
                        </td>
                    </tr>
                `;
                        });
                    })
                    .catch(err => {
                        console.error("Lỗi khi fetch kỳ học:", err);
                        alert("Không kết nối được đến server.");
                    });
            } else {
                alert('Lỗi: ' + j.message);
            }
        });
}

