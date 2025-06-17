/**
 * Fetch danh sách giảng viên
 */
let teachers = [];
fetch('/admin/teacher/get-all')
    .then(res => res.json())
    .then(json => {
        teachers = json.data || [];
        fillSelect(document.getElementById('teacherSelect'), teachers, 'id', 'fullName');
    });

/**
 * Fetch danh sách học kỳ và năm học
 */
let semesters = [];
fetch('/admin/semester/get-all')
    .then(res => res.json())
    .then(json => {
        semesters = json.data || [];
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
        const years = [...new Set(semesters.map(s => s.schoolYear))].sort();
        fillSelect(
            document.getElementById('yearSelect'),
            years.map(y => ({ id: y, name: y })),
            'id',
            'name'
        );
    });

/**
 * Fetch danh sách khoa
 */
let departments = [];
fetch('/admin/department/getAll')
    .then(res => res.json())
    .then(json => {
        departments = json.data || [];
        fillSelect(document.getElementById('departmentSelect'), departments, 'id', 'fullName');
    });

/**
 * Helper functions
 */
function showLoading(show) {
    document.getElementById('loading').style.display = show ? 'block' : 'none';
}

function showError(container, msg) {
    container.innerHTML = `<div class="error">${msg}</div>`;
}

function formatCurrency(num) {
    return Number(num).toLocaleString('vi-VN') + " đ";
}

function fillSelect(select, arr, valueField = 'id', labelField = 'name') {
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

/**
 * Sidebar navigation
 */
document.querySelectorAll('.sidebar li').forEach(li => {
    li.onclick = function() {
        document.querySelectorAll('.sidebar li').forEach(x => x.classList.remove('active'));
        li.classList.add('active');
        document.querySelectorAll('.page').forEach(page => page.classList.remove('active'));
        document.getElementById(li.dataset.page).classList.add('active');
    };
});

/**
 * Fetch danh sách giáo viên theo kỳ/khoa/giáo viên
 */
function getTeacherStats({ semesterId, departmentId, teacherId }, callback) {
    let url = `/admin/teacher/getList?semesterId=${semesterId}`;
    if (departmentId) url += `&departmentId=${departmentId}`;
    if (teacherId) url += `&teacherId=${teacherId}`;
    fetch(url)
        .then(res => res.json())
        .then(json => callback(json.data || []));
}

/**
 * Fetch danh sách giáo viên theo khoa
 */
function loadTeachersByDepartment(departmentId, callback) {
    fetch(`/admin/teacher/get-all-of-department/${departmentId}`)
        .then(res => res.json())
        .then(json => callback(json.data || []));
}

/**
 * Fetch thông tin chi tiết giáo viên
 */
function getTeacherDetail(id, callback) {
    fetch(`/admin/teacher/get/${id}`)
        .then(res => res.json())
        .then(json => callback(json.data));
}

/**
 * Tính lương giáo viên (theo form)
 */
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
            const teacherSalaryId = data.id;
            if (!teacherSalaryId) {
                showError(resultDiv, "Không tìm thấy dữ liệu lương cho giáo viên này.");
                showLoading(false);
                return;
            }
            // Hàm hiển thị chi tiết lương (dùng lại khi cần reload)
            function renderSalaryDetail() {
                showLoading(true);
                fetch(`/teacher-salary/export-report?teacherSalaryId=${teacherSalaryId}`)
                    .then(res => res.json())
                    .then(salary => {
                        showLoading(false);
                        const classRooms = salary.classRoomResponses || salary.classRoom || [];
                        const isPaid = salary.statusPayment === "DA_THANH_TOAN";
                        resultDiv.innerHTML = `
                            <div style="max-width:650px;padding:32px;background:#fff;border-radius:12px;box-shadow:0 4px 24px rgba(0,0,0,0.15);margin:24px auto;">
                                <h2 style="margin-bottom:24px;">THỐNG KÊ LƯƠNG GIÁO VIÊN</h2>
                                <b>Thông tin giáo viên</b>
                                <table style="width:100%;margin-bottom:24px;border-spacing:0 8px;">
                                    <tr><td style="padding:6px 12px;">ID:</td><td style="padding:6px 12px;">${salary.teacherResponse.id}</td></tr>
                                    <tr><td style="padding:6px 12px;">Họ tên:</td><td style="padding:6px 12px;">${salary.teacherResponse.name}</td></tr>
                                    <tr><td style="padding:6px 12px;">Khoa:</td><td style="padding:6px 12px;">${salary.teacherResponse.department}</td></tr>
                                    <tr><td style="padding:6px 12px;">Email:</td><td style="padding:6px 12px;">${salary.teacherResponse.email}</td></tr>
                                    <tr><td style="padding:6px 12px;">Bằng cấp:</td><td style="padding:6px 12px;">${salary.teacherResponse.degree ? salary.teacherResponse.degree.shortName : ""}</td></tr>
                                    <tr><td style="padding:6px 12px;">Hệ số bằng cấp:</td><td style="padding:6px 12px;">${salary.teacherResponse.degree ? salary.teacherResponse.degree.degreeCoefficient : ""}</td></tr>
                                </table>
                                <b>Danh sách lớp dạy</b>
                                <table border="1" style="width:100%;border-collapse:collapse;margin-bottom:24px;">
                                    <tr style="background:#f5f5f5;">
                                        <th style="padding:8px;">Lớp</th>
                                        <th style="padding:8px;">Số SV</th>
                                        <th style="padding:8px;">Hệ số lớp</th>
                                        <th style="padding:8px;">Học kỳ</th>
                                        <th style="padding:8px;">Năm học</th>
                                        <th style="padding:8px;">Môn học</th>
                                    </tr>
                                    ${classRooms.map(c => `
                                        <tr>
                                            <td style="padding:8px;">${c.className}</td>
                                            <td style="padding:8px;">${c.numberOfStudents}</td>
                                            <td style="padding:8px;">${c.classCoefficient}</td>
                                            <td style="padding:8px;">${c.semesterName}</td>
                                            <td style="padding:8px;">${c.schoolYear}</td>
                                            <td style="padding:8px;">${c.subject}</td>
                                        </tr>
                                    `).join('')}
                                </table>
                                <div style="margin-bottom:16px;">
                                    <b>Tổng số tiết quy đổi:</b> ${salary.totalHoursTeaching} <br>
                                    <b>Tổng tiền lương:</b> ${formatCurrency(salary.totalSalary)} <br>
                                    <b>Trạng thái thanh toán:</b> ${isPaid ? "Đã thanh toán" : "Chưa thanh toán"}
                                </div>
                                ${!isPaid ? `
                                    <div style="text-align:right;">
                                        <button id="paySalaryBtn" style="padding:8px 24px;border:none;background:#43a047;color:#fff;border-radius:6px;cursor:pointer;">Thanh toán</button>
                                    </div>
                                ` : ""}
                            </div>
                        `;
                        // Gắn sự kiện cho nút thanh toán nếu chưa thanh toán
                        if (!isPaid) {
                            document.getElementById('paySalaryBtn').onclick = function() {
                                if (confirm("Bạn có chắc chắn muốn xác nhận thanh toán lương cho giáo viên này?")) {
                                    showLoading(true);
                                    updatePaymentStatus(teacherSalaryId, true);
                                    // Đợi cập nhật xong thì reload lại chi tiết
                                    setTimeout(renderSalaryDetail, 800);
                                }
                            };
                        }
                    })
                    .catch(() => {
                        showLoading(false);
                        showError(resultDiv, "Không lấy được chi tiết lương giáo viên!");
                    });
            }
            renderSalaryDetail();
        })
        .catch(() => {
            showError(resultDiv, "Có lỗi khi tính lương.");
            showLoading(false);
        });
};

/**
 * Hiển thị danh sách lương theo học kỳ
 */
function renderSalaryTable(data, containerId, filterStatus = "") {
    const container = document.getElementById(containerId);
    if (!data || !data.length) {
        container.innerHTML = "<div>Không có dữ liệu.</div>";
        return;
    }
    let html = `<table class="data-table">
    <tr>
      <th>ID Lương</th>
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
        console.log(s.statusPayment);
        if (filterStatus && ((filterStatus === "true" && s.statusPayment !== "DA_THANH_TOAN") || (filterStatus === "false" && s.statusPayment !== "CHUA_THANH_TOAN"))) return;
        html += `<tr>
        <td>${s.id || "N/A"}</td>
        <td>${s.teacherResponse ? s.teacherResponse.name : ""}</td>
        <td>${s.teacherResponse ? s.teacherResponse.department : ""}</td>
        <td>${s.teacherResponse ? s.teacherResponse.email : ""}</td>
        <td>${s.teacherResponse && s.teacherResponse.degree ? s.teacherResponse.degree.fullName : ""}</td>
        <td>${s.classRoom ? s.classRoom.join(", ") : ""}</td>
        <td>${s.totalHoursTeaching}</td>
        <td>${formatCurrency(s.totalSalary)}</td>
        <td>${s.statusPayment === "DA_THANH_TOAN" ? "Đã thanh toán" : "Chưa thanh toán"}</td>
        <td>
            <button class="show-salary-detail" data-salary='${JSON.stringify(s)}'>Xem chi tiết</button>
            ${s.statusPayment === "CHUA_THANH_TOAN" ? `<button class="update-payment-status" data-id="${s.id || ''}">Xác nhận thanh toán</button>` : ""}
        </td>
    </tr>`;
    });
    html += "</table>";
    container.innerHTML = html;

    // Attach event listeners programmatically
    document.querySelectorAll('.show-salary-detail').forEach(button => {
        button.addEventListener('click', () => {
            const salary = JSON.parse(button.getAttribute('data-salary'));
            showSalaryDetail(salary);
        });
    });

    document.querySelectorAll('.update-payment-status').forEach(button => {
        button.addEventListener('click', () => {
            const teacherSalaryId = button.getAttribute('data-id');
            console.log(`Updating payment status for ID: ${teacherSalaryId}`);
            if (!teacherSalaryId) {
                alert("Không tìm thấy ID lương giáo viên!");
                return;
            }
            updatePaymentStatus(teacherSalaryId, true);
        });
    });

    // Log teacherSalaryId to console for debugging
    data.forEach(s => {
        console.log(`TeacherSalaryId: ${s.id || "N/A"}`);
    });
}

/**
 * Lọc và hiển thị lương theo học kỳ
 */
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
            const salaryData = Array.isArray(data) ? data : (data.data || []);
            renderSalaryTable(salaryData, 'salarySemesterTable', "");
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

/**
 * Lọc và hiển thị lương theo khoa
 */
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
            console.log("Dữ liệu bảng lương theo học kỳ:", data); // Thêm dòng này
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

/**
 * Lọc và hiển thị lương theo năm
 */
function updateSalaryYearTable() {
    const year = document.getElementById('yearSelect').value;
    const filterStatus = document.getElementById('statusFilterYear').value;
    const container = document.getElementById('salaryYearTable');
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

/**
 * Xem chi tiết lương giáo viên
 */
window.showSalaryDetail = function(salary) {
    const teacherSalaryId = salary.id;
    if (!teacherSalaryId) {
        alert("Không tìm thấy ID lương giáo viên!");
        return;
    }
    showLoading(true);
    fetch(`/teacher-salary/export-report?teacherSalaryId=${teacherSalaryId}`)
        .then(res => res.json())
        .then(data => {
            showLoading(false);
            let html = `
                <div style="max-width:650px;padding:32px;background:#fff;border-radius:12px;box-shadow:0 4px 24px rgba(0,0,0,0.15);">
                    <h2 style="margin-bottom:24px;">THỐNG KÊ LƯƠNG GIÁO VIÊN</h2>
                    <b>Thông tin giáo viên</b>
                    <table style="width:100%;margin-bottom:24px;border-spacing:0 8px;">
                        <tr><td style="padding:6px 12px;">ID:</td><td style="padding:6px 12px;">${data.teacherResponse.id}</td></tr>
                        <tr><td style="padding:6px 12px;">Họ tên:</td><td style="padding:6px 12px;">${data.teacherResponse.name}</td></tr>
                        <tr><td style="padding:6px 12px;">Khoa:</td><td style="padding:6px 12px;">${data.teacherResponse.department}</td></tr>
                        <tr><td style="padding:6px 12px;">Email:</td><td style="padding:6px 12px;">${data.teacherResponse.email}</td></tr>
                        <tr><td style="padding:6px 12px;">Bằng cấp:</td><td style="padding:6px 12px;">${data.teacherResponse.degree ? data.teacherResponse.degree.fullName : ""}</td></tr>
                        <tr><td style="padding:6px 12px;">Hệ số bằng cấp:</td><td style="padding:6px 12px;">${data.teacherResponse.degree ? data.teacherResponse.degree.degreeCoefficient : ""}</td></tr>
                    </table>
                    <b>Danh sách lớp dạy</b>
                    <table border="1" style="width:100%;border-collapse:collapse;margin-bottom:24px;">
                        <tr style="background:#f5f5f5;">
                            <th style="padding:8px;">Lớp</th>
                            <th style="padding:8px;">Số SV</th>
                            <th style="padding:8px;">Hệ số lớp</th>
                            <th style="padding:8px;">Học kỳ</th>
                            <th style="padding:8px;">Năm học</th>
                            <th style="padding:8px;">Môn học</th>
                        </tr>
                        ${data.classRoomResponses.map(c => `
                            <tr>
                                <td style="padding:8px;">${c.className}</td>
                                <td style="padding:8px;">${c.numberOfStudents}</td>
                                <td style="padding:8px;">${c.classCoefficient}</td>
                                <td style="padding:8px;">${c.semesterName}</td>
                                <td style="padding:8px;">${c.schoolYear}</td>
                                <td style="padding:8px;">${c.subject}</td>
                            </tr>
                        `).join('')}
                    </table>
                    <div style="margin-bottom:16px;">
                        <b>Tổng số tiết quy đổi:</b> ${data.totalHoursTeaching} <br>
                        <b>Tổng tiền lương:</b> ${formatCurrency(data.totalSalary)} <br>
                        <b>Trạng thái thanh toán:</b> ${data.statusPayment === "DA_THANH_TOAN" ? "Đã thanh toán" : "Chưa thanh toán"}
                    </div>
                    <div style="text-align:right;margin-top:24px;">
                        <button onclick="document.getElementById('salary-detail-modal').remove()" style="padding:8px 24px;border:none;background:#1976d2;color:#fff;border-radius:6px;cursor:pointer;">Đóng</button>
                    </div>
                </div>
            `;
            let modal = document.createElement('div');
            modal.id = 'salary-detail-modal';
            modal.style = 'position:fixed;top:0;left:0;width:100vw;height:100vh;background:rgba(0,0,0,0.6);z-index:9999;display:flex;align-items:center;justify-content:center;';
            modal.innerHTML = html;
            document.body.appendChild(modal);
        })
        .catch(() => {
            showLoading(false);
            alert("Không lấy được chi tiết lương giáo viên!");
        });
};

/**
 * Cập nhật trạng thái thanh toán
 */
window.updatePaymentStatus = function(teacherSalaryId, isPaid) {
    if (!teacherSalaryId) {
        alert("ID lương giáo viên không hợp lệ!");
        showLoading(false);
        return;
    }
    showLoading(true);
    fetch(`/teacher-salary/${teacherSalaryId}/payment-status?isPaid=${isPaid}`, {
        method: 'PUT'
    })
        .then(res => {
            if (!res.ok) {
                throw new Error("Lỗi khi cập nhật trạng thái thanh toán.");
            }
            return res.json();
        })
        .then(() => {
            updateSalarySemesterTable();
            updateSalaryDepartmentTable();
            showLoading(false);
        })
        .catch(() => {
            alert("Cập nhật trạng thái thất bại!");
            showLoading(false);
        });
};

/**
 * Đăng xuất
 */
document.getElementById('logoutBtn').onclick = function() {
    alert("Đăng xuất thành công!");
    // window.location.href = '/login';
};

/**
 * Ẩn loading khi load trang
 */
document.addEventListener('DOMContentLoaded', function() {
    showLoading(false);
});

// Lấy danh sách học phí
function loadTuitions() {
    fetch('/tuition/getAll')
        .then(res => res.json())
        .then(json => {
            // Nếu trả về mảng thì dùng luôn, nếu trả về object thì lấy .data
            const tuitions = Array.isArray(json) ? json : (json.data || []);
            console.log("Danh sách học phí:", tuitions);
            renderTuitionTable(tuitions);
        });
}

// Hiển thị bảng học phí
function renderTuitionTable(tuitions) {
    const table = document.getElementById('tuitionTable');
    let html = `<tr>
        <th>ID</th>
        <th>Học kỳ</th>
        <th>Tiền/tiết</th>
        <th>Tiền/tiết trước điều chỉnh</th>
        <th>Hành động</th>
    </tr>`;
    tuitions.forEach(t => {
        html += `<tr>
            <td>${t.id}</td>
            <td>${t.semester ? (t.semester.semesterName + " (" + t.semester.schoolYear + ")") : ""}</td>
            <td><input type="number" value="${t.money}" min="0" id="tuition-money-${t.id}" style="width:100px"></td>
            <td>${t.pre_money}</td>
            <td>
                <button onclick="updateTuition(${t.id})">Lưu</button>
            </td>
        </tr>`;
    });
    table.innerHTML = html;
}

// Cập nhật tiền học phí
window.updateTuition = function(id) {
    const money = document.getElementById(`tuition-money-${id}`).value;
    if (!money || isNaN(money) || money < 0) {
        alert("Tiền học phí không hợp lệ!");
        return;
    }
    fetch(`/tuition/update/${id}?money=${money}`, { method: 'PUT' })
        .then(res => res.json())
        .then(() => {
            alert("Cập nhật thành công!");
            loadTuitions();
        })
        .catch(() => alert("Cập nhật thất bại!"));
};

// Lấy danh sách bằng cấp
function loadDegrees() {
    fetch('/admin/degree/get-all')
        .then(res => res.json())
        .then(json => renderDegreeTable(json.data || []));
}

// Hiển thị bảng hệ số bằng cấp
function renderDegreeTable(degrees) {
    const table = document.getElementById('degreeTable');
    let html = `<tr>
        <th>ID</th>
        <th>Tên viết tắt</th>
        <th>Tên đầy đủ</th>
        <th>Hệ số</th>
        <th>Hành động</th>
    </tr>`;
    degrees.forEach(d => {
        html += `<tr>
            <td>${d.id}</td>
            <td>${d.shortName}</td>
            <td>${d.fullName}</td>
            <td><input type="number" step="0.01" value="${d.degreeCoefficient}" id="degree-coef-${d.id}" style="width:80px"></td>
            <td>
                <button onclick="updateDegreeCoef(${d.id})">Lưu</button>
            </td>
        </tr>`;
    });
    table.innerHTML = html;
}

// Cập nhật hệ số bằng cấp
window.updateDegreeCoef = function(id) {
    const coef = document.getElementById(`degree-coef-${id}`).value;
    if (!coef || isNaN(coef) || coef <= 0) {
        alert("Hệ số không hợp lệ!");
        return;
    }
    fetch(`/admin/degree/setCoefficient/${id}?coefficient=${coef}`, { method: 'PUT' })
        .then(() => {
            alert("Cập nhật thành công!");
            loadDegrees();
        })
        .catch(() => alert("Cập nhật thất bại!"));
};

// Khi vào tab, tự động load dữ liệu
document.querySelector('li[data-page="tuition-degree"]').addEventListener('click', function() {
    loadTuitions();
    loadDegrees();
});