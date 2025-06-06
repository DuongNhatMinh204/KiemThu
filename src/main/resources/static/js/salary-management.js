// Lấy danh sách giảng viên
let teachers = [];
fetch('/admin/teacher/get-all')
    .then(res => res.json())
    .then(json => {
        teachers = json.data || [];
        fillSelect(document.getElementById('teacherSelect'), teachers, 'id', 'fullName');
    });

let semesters = [];
fetch('/admin/semester/get-all')
    .then(res => res.json())
    .then(json => {
        semesters = json.data || [];
        // Hiển thị tên học kỳ theo semesterName và schoolYear
        fillSelect(
            document.getElementById('semesterSelect'),
            semesters,
            'id',
            s => `${s.semesterName} (${s.schoolYear})`
        );
        fillSelect(
            document.getElementById('semesterListSelect'),
            semesters,
            'id',
            s => `${s.semesterName} (${s.schoolYear})`
        );
        fillSelect(
            document.getElementById('bulkSemesterSelect'),
            semesters,
            'id',
            s => `${s.semesterName} (${s.schoolYear})`
        );
        // Lấy danh sách năm học duy nhất
        const years = [...new Set(semesters.map(s => s.schoolYear))].sort();
        fillSelect(
            document.getElementById('yearSelect'),
            years.map(y => ({ id: y, name: y })),
            'id',
            'name'
        );
    });

// Lấy danh sách khoa từ API
let departments = [];
fetch('/admin/department/getAll')
    .then(res => res.json())
    .then(json => {
        departments = json.data || [];
        // fullName là tên khoa theo API
        fillSelect(document.getElementById('bulkDepartmentSelect'), departments, 'id', 'fullName');
        fillSelect(document.getElementById('departmentSelect'), departments, 'id', 'fullName');
    });

// Helper
function showLoading(show) {
    document.getElementById('loading').style.display = show ? 'block' : 'none';
}
function showError(container, msg) {
    container.innerHTML = `<div class="error">${msg}</div>`;
}
function formatCurrency(num) {
    return Number(num).toLocaleString('vi-VN') + " đ";
}

// Đổ dữ liệu dropdown
function fillSelect(select, arr, valueField = 'id', labelField = 'name') {
    // Lấy option mặc định nếu có
    let defaultOption = '';
    if (select.options.length && select.options[0].value === '') {
        defaultOption = select.options[0].outerHTML;
    }
    select.innerHTML = defaultOption + arr
        .map(x => {
            const label = typeof labelField === 'function' ? labelField(x) : x[labelField];
            return `<option value="${x[valueField]}">${label}</option>`;
        })
        .join('');
}
fillSelect(document.getElementById('bulkDepartmentSelect'), departments, 'id', 'name');
fillSelect(document.getElementById('departmentSelect'), departments, 'id', 'name');

// Sidebar navigation
document.querySelectorAll('.sidebar li').forEach(li => {
    li.onclick = function() {
        document.querySelectorAll('.sidebar li').forEach(x => x.classList.remove('active'));
        li.classList.add('active');
        document.querySelectorAll('.page').forEach(page => page.classList.remove('active'));
        document.getElementById(li.dataset.page).classList.add('active');
    };
});

// Lấy danh sách giáo viên theo kỳ/khoa/giáo viên
function getTeacherStats({ semesterId, departmentId, teacherId }, callback) {
    let url = `/admin/teacher/getList?semesterId=${semesterId}`;
    if (departmentId) url += `&departmentId=${departmentId}`;
    if (teacherId) url += `&teacherId=${teacherId}`;
    fetch(url)
        .then(res => res.json())
        .then(json => callback(json.data || []));
}

// Lấy danh sách giáo viên theo khoa
function loadTeachersByDepartment(departmentId, callback) {
    fetch(`/admin/teacher/get-all-of-department/${departmentId}`)
        .then(res => res.json())
        .then(json => callback(json.data || []));
}

// Lấy thông tin chi tiết giáo viên
function getTeacherDetail(id, callback) {
    fetch(`/admin/teacher/get/${id}`)
        .then(res => res.json())
        .then(json => callback(json.data));
}

// Tính lương giáo viên (theo form)
document.getElementById('calcTeacherForm').onsubmit = function(e) {
    e.preventDefault();
    const teacherId = document.getElementById('teacherSelect').value;
    const semesterId = document.getElementById('semesterSelect').value;
    const resultDiv = document.getElementById('teacherSalaryResult');
    if (!teacherId || !semesterId) {
        showError(resultDiv, "Vui lòng chọn giáo viên và học kỳ.");
        return;
    }
    showLoading(true);
    fetch(`/teacher-salary/calculate?teacherId=${teacherId}&semesterId=${semesterId}`, {
        method: 'POST'
    })
        .then(res => res.json())
        .then(data => {
            const salary = data;
            if (!salary || !salary.teacherResponse) {
                showError(resultDiv, "Không tìm thấy dữ liệu lương cho giáo viên này.");
                showLoading(false);
                return;
            }
            resultDiv.innerHTML = `
        <div class="card">
          <b>Thông tin giáo viên:</b><br>
          ID: ${salary.teacherResponse.id} <br>
          Họ tên: ${salary.teacherResponse.name} <br>
          Khoa: ${salary.teacherResponse.department} <br>
          Email: ${salary.teacherResponse.email} <br>
          Bằng cấp: ${salary.teacherResponse.degree ? salary.teacherResponse.degree.shortName : ""} <br>
          Lớp dạy: ${(salary.teacherResponse.classRoom || []).join(", ")} <br>
          <b>Tổng số tiết quy đổi:</b> ${salary.totalHoursTeaching} <br>
          <b>Tổng tiền lương:</b> ${formatCurrency(salary.totalSalary)} <br>
          <b>Trạng thái thanh toán:</b> ${salary.statusPayment === "DA_THANH_TOAN" ? "Đã thanh toán" : "Chưa thanh toán"}
        </div>
      `;
            showLoading(false);
        })
        .catch(() => {
            showError(resultDiv, "Có lỗi khi tính lương.");
            showLoading(false);
        });
};

// Hiển thị danh sách lương theo học kỳ
function renderSalaryTable(data, containerId, filterStatus = "") {
    const container = document.getElementById(containerId);
    if (!data || !data.length) {
        container.innerHTML = "<div>Không có dữ liệu.</div>";
        return;
    }
    let html = `<table class="data-table">
    <tr>
      <th>Tên giáo viên</th>
      <th>Khoa</th>
      <th>Email</th>
      <th>Bằng cấp</th>
      <th>Lớp dạy</th>
      <th>Tổng số tiết</th>
      <th>Tổng tiền lương</th>
      <th>Trạng thái thanh toán</th>
      <th>Hành động</th>
    </tr>`;
    data.forEach(s => {
        if (filterStatus && ((filterStatus === "true" && s.statusPayment !== "DA_THANH_TOAN") || (filterStatus === "false" && s.statusPayment !== "CHUA_THANH_TOAN"))) return;
        html += `<tr>
      <td>${s.teacherResponse ? s.teacherResponse.name : ""}</td>
      <td>${s.teacherResponse ? s.teacherResponse.department : ""}</td>
      <td>${s.teacherResponse ? s.teacherResponse.email : ""}</td>
      <td>${s.teacherResponse && s.teacherResponse.degree ? s.teacherResponse.degree.shortName : ""}</td>
      <td>${s.teacherResponse && s.teacherResponse.classRoom ? s.teacherResponse.classRoom.join(", ") : ""}</td>
      <td>${s.totalHoursTeaching}</td>
      <td>${formatCurrency(s.totalSalary)}</td>
      <td>${s.statusPayment === "DA_THANH_TOAN" ? "Đã thanh toán" : "Chưa thanh toán"}</td>
      <td>
        ${s.statusPayment === "CHUA_THANH_TOAN" ? `<button onclick="updatePaymentStatus(${s.id}, true)">Xác nhận thanh toán</button>` : ""}
      </td>
    </tr>`;
    });
    html += "</table>";
    container.innerHTML = html;
}
// Lọc và hiển thị lương theo học kỳ
function updateSalarySemesterTable() {
    const semesterId = document.getElementById('semesterListSelect').value;
    const filterStatus = document.getElementById('statusFilterSemester').value;
    const container = document.getElementById('salarySemesterTable');
    if (!semesterId) {
        container.innerHTML = "<div>Vui lòng chọn học kỳ.</div>";
        return;
    }
    showLoading(true);
    fetch(`/teacher-salary/by-semester?semesterId=${semesterId}`)
        .then(res => res.json())
        .then(data => {
            renderSalaryTable(data, 'salarySemesterTable', filterStatus);
            showLoading(false);
        })
        .catch(() => {
            showError(container, "Có lỗi khi lấy bảng lương.");
            showLoading(false);
        });
}
document.getElementById('semesterListSelect').onchange = updateSalarySemesterTable;
document.getElementById('statusFilterSemester').onchange = updateSalarySemesterTable;
updateSalarySemesterTable();

// Lọc và hiển thị lương theo khoa
function updateSalaryDepartmentTable() {
    const departmentId = document.getElementById('departmentSelect').value;
    const filterStatus = document.getElementById('statusFilterDepartment').value;
    const container = document.getElementById('salaryDepartmentTable');
    if (!departmentId) {
        container.innerHTML = "<div>Vui lòng chọn khoa.</div>";
        return;
    }
    showLoading(true);
    fetch(`/teacher-salary/by-department?departmentId=${departmentId}`)
        .then(res => res.json())
        .then(data => {
            renderSalaryTable(data, 'salaryDepartmentTable', filterStatus);
            showLoading(false);
        })
        .catch(() => {
            showError(container, "Có lỗi khi lấy bảng lương.");
            showLoading(false);
        });
}
document.getElementById('departmentSelect').onchange = updateSalaryDepartmentTable;
document.getElementById('statusFilterDepartment').onchange = updateSalaryDepartmentTable;
updateSalaryDepartmentTable();

// Lọc và hiển thị lương theo năm
function updateSalaryYearTable() {
    const year = document.getElementById('yearSelect').value;
    const filterStatus = document.getElementById('statusFilterYear').value;
    const container = document.getElementById('salaryYearTable');

    // Kiểm tra xem năm học đã được chọn hay chưa
    if (!year) {
        container.innerHTML = "<div>Vui lòng chọn năm học.</div>";
        return;
    }

    showLoading(true);
    fetch(`/teacher-salary/by-schoolYear?year=${encodeURIComponent(year)}`)
        .then(res => {
            if (!res.ok) {
                if (res.status === 404) {
                    throw new Error("Không tìm thấy học kỳ nào trong năm học này.");
                }
                throw new Error("Lỗi khi lấy dữ liệu bảng lương.");
            }
            return res.json();
        })
        .then(json => {
            // Kiểm tra cấu trúc dữ liệu trả về
            const data = Array.isArray(json) ? json : json.data || [];
            if (!data.length) {
                container.innerHTML = "<div>Không có dữ liệu lương cho năm học này.</div>";
            } else {
                renderSalaryTable(data, 'salaryYearTable', filterStatus);
            }
            showLoading(false);
        })
        .catch(err => {
            showError(container, err.message || "Có lỗi khi lấy bảng lương.");
            showLoading(false);
        });
}
document.getElementById('yearSelect').onchange = updateSalaryYearTable;
document.getElementById('statusFilterYear').onchange = updateSalaryYearTable;
updateSalaryYearTable();

// Xem chi tiết lương giáo viên
window.showSalaryDetail = function(teacherId, semesterId) {
    showLoading(true);
    getTeacherDetail(teacherId, function(teacher) {
        getTeacherStats({ semesterId, teacherId }, function(stats) {
            const salary = stats && stats.length ? stats[0] : null;
            alert(
                `Họ tên: ${teacher.fullName}\n` +
                `Email: ${teacher.email}\n` +
                `Bằng cấp: ${teacher.degree}\n` +
                `Khoa: ${teacher.department && teacher.department.name}\n` +
                `Tổng số tiết: ${salary ? salary.totalHoursTeaching : ''}\n` +
                `Tổng tiền lương: ${salary ? formatCurrency(salary.totalSalary) : ''}`
            );
            showLoading(false);
        });
    });
};

// Đăng xuất
document.getElementById('logoutBtn').onclick = function() {
    alert("Đăng xuất thành công!");
    // window.location.href = '/login';
};

window.updatePaymentStatus = function(teacherSalaryId, isPaid) {
    showLoading(true);
    fetch(`/teacher-salary/${teacherSalaryId}/payment-status?isPaid=${isPaid}`, {
        method: 'PUT'
    })
        .then(res => res.json())
        .then(() => {
            // Sau khi cập nhật, reload lại bảng lương hiện tại
            updateSalarySemesterTable();
            updateSalaryDepartmentTable();
            showLoading(false);
        })
        .catch(() => {
            alert("Cập nhật trạng thái thất bại!");
            showLoading(false);
        });
};

document.getElementById('bulkCalcSemesterBtn').onclick = function() {
    const semesterId = document.getElementById('semesterListSelect').value;
    if (!semesterId) {
        alert("Vui lòng chọn học kỳ.");
        return;
    }
    showLoading(true);
    fetch(`/teacher-salary/calculate-by-semester/${semesterId}`, { method: 'POST' })
        .then(res => res.json())
        .then(() => {
            updateSalarySemesterTable();
            showLoading(false);
        })
        .catch(() => {
            alert("Tính lương hàng loạt thất bại!");
            showLoading(false);
        });
};

// Tính lương hàng loạt theo khoa
document.getElementById('bulkCalcDepartmentOnlyBtn').onclick = function() {
    const departmentId = document.getElementById('bulkDepartmentSelect').value;
    if (!departmentId) {
        alert("Vui lòng chọn khoa.");
        return;
    }
    showLoading(true);
    fetch(`/teacher-salary/calculate-by-department/${departmentId}`, { method: 'POST' })
        .then(res => res.json())
        .then(() => {
            updateSalaryDepartmentTable();
            showLoading(false);
        })
        .catch(() => {
            alert("Tính lương hàng loạt thất bại!");
            showLoading(false);
        });
};

document.addEventListener('DOMContentLoaded', function() {
    showLoading(false);
});