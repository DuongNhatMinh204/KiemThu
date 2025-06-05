// Dữ liệu giả định
const teachers = [
  { id: 1, name: "Nguyen Van A", email: "a@school.edu", degree: "Thạc sĩ", department: "Khoa Công Nghệ Thông Tin" },
  { id: 2, name: "Tran Thi B", email: "b@school.edu", degree: "Tiến sĩ", department: "Khoa Kinh Tế" },
  { id: 3, name: "Le Van C", email: "c@school.edu", degree: "Thạc sĩ", department: "Khoa Công Nghệ Thông Tin" }
];
const semesters = [
  { id: "2025-1", name: "Học kỳ 1 - 2025" },
  { id: "2025-2", name: "Học kỳ 2 - 2025" }
];
const departments = [
  { id: 101, name: "Khoa Công Nghệ Thông Tin" },
  { id: 102, name: "Khoa Kinh Tế" }
];
const years = [2023, 2024, 2025];

// Dữ liệu lương mẫu (giả lập)
let salaryData = [
  {
    id: 1,
    teacherId: 1,
    semesterId: "2025-1",
    departmentId: 101,
    teacherName: "Nguyen Van A",
    email: "a@school.edu",
    degree: "Thạc sĩ",
    department: "Khoa Công Nghệ Thông Tin",
    classes: ["Lập trình Java", "Cơ sở dữ liệu"],
    totalSessions: 60,
    totalSalary: 18000000,
    isPaid: false
  },
  {
    id: 2,
    teacherId: 2,
    semesterId: "2025-1",
    departmentId: 102,
    teacherName: "Tran Thi B",
    email: "b@school.edu",
    degree: "Tiến sĩ",
    department: "Khoa Kinh Tế",
    classes: ["Kinh tế vi mô"],
    totalSessions: 40,
    totalSalary: 16000000,
    isPaid: true
  },
  {
    id: 3,
    teacherId: 3,
    semesterId: "2025-2",
    departmentId: 101,
    teacherName: "Le Van C",
    email: "c@school.edu",
    degree: "Thạc sĩ",
    department: "Khoa Công Nghệ Thông Tin",
    classes: ["Lập trình Python"],
    totalSessions: 50,
    totalSalary: 15000000,
    isPaid: false
  }
];

// Helper
function showLoading(show) {
  document.getElementById('loading').style.display = show ? 'block' : 'none';
}
function showError(container, msg) {
  container.innerHTML = `<div class="error">${msg}</div>`;
}
function formatCurrency(num) {
  return num.toLocaleString('vi-VN') + " đ";
}

// Sidebar navigation
document.querySelectorAll('.sidebar li').forEach(li => {
  li.onclick = function() {
    document.querySelectorAll('.sidebar li').forEach(x => x.classList.remove('active'));
    li.classList.add('active');
    document.querySelectorAll('.page').forEach(page => page.classList.remove('active'));
    document.getElementById(li.dataset.page).classList.add('active');
  };
});

// Đổ dữ liệu dropdown
function fillSelect(select, arr, valueField = 'id', labelField = 'name') {
  select.innerHTML = arr.map(x => `<option value="${x[valueField]}">${x[labelField]}</option>`).join('');
}
fillSelect(document.getElementById('teacherSelect'), teachers, 'id', 'name');
fillSelect(document.getElementById('semesterSelect'), semesters, 'id', 'name');
fillSelect(document.getElementById('semesterListSelect'), semesters, 'id', 'name');
fillSelect(document.getElementById('bulkSemesterSelect'), semesters, 'id', 'name');
fillSelect(document.getElementById('bulkDepartmentSelect'), departments, 'id', 'name');
fillSelect(document.getElementById('departmentSelect'), departments, 'id', 'name');
fillSelect(document.getElementById('yearSelect'), years.map(y => ({ id: y, name: y })), 'id', 'name');

// Tính lương giáo viên
document.getElementById('calcTeacherForm').onsubmit = function(e) {
  e.preventDefault();
  const teacherId = +document.getElementById('teacherSelect').value;
  const semesterId = document.getElementById('semesterSelect').value;
  showLoading(true);
  setTimeout(() => { // Giả lập API
    const salary = salaryData.find(s => s.teacherId === teacherId && s.semesterId === semesterId);
    const resultDiv = document.getElementById('teacherSalaryResult');
    if (!salary) {
      showError(resultDiv, "Không tìm thấy dữ liệu lương cho giáo viên này.");
      showLoading(false);
      return;
    }
    resultDiv.innerHTML = `
      <div class="card">
        <b>Thông tin giáo viên:</b><br>
        ID: ${salary.teacherId} <br>
        Họ tên: ${salary.teacherName} <br>
        Email: ${salary.email} <br>
        Bằng cấp: ${salary.degree} <br>
        Khoa: ${salary.department} <br>
        <b>Danh sách lớp dạy:</b>
        <table class="small-table"><tr><th>Lớp</th></tr>
          ${salary.classes.map(cls => `<tr><td>${cls}</td></tr>`).join('')}
        </table>
        <div style="margin-top:8px;">
          <b>Tổng số tiết quy đổi:</b> ${salary.totalSessions} <br>
          <b>Tổng tiền lương:</b> ${formatCurrency(salary.totalSalary)} <br>
          <b>Trạng thái:</b> <span class="${salary.isPaid ? 'status-paid' : 'status-unpaid'}">
            ${salary.isPaid ? 'Đã thanh toán' : 'Chưa thanh toán'}
          </span>
        </div>
        <button onclick="showSalaryDetail(${salary.teacherId}, '${salary.semesterId}')">Xem chi tiết</button>
      </div>
    `;
    showLoading(false);
  }, 500);
};

// Xem chi tiết lương giáo viên
window.showSalaryDetail = function(teacherId, semesterId) {
  alert("Chức năng xem chi tiết lương giáo viên (ID: " + teacherId + ", Học kỳ: " + semesterId + ")");
};

// Hiển thị danh sách lương theo học kỳ
function renderSalaryTable(data, containerId, filterStatus = "") {
  const container = document.getElementById(containerId);
  if (!data.length) {
    container.innerHTML = "<div>Không có dữ liệu.</div>";
    return;
  }
  let html = `<table class="data-table">
    <tr>
      <th>ID giáo viên</th><th>Tên</th><th>Email</th><th>Bằng cấp</th><th>Khoa</th>
      <th>Lớp dạy</th><th>Tổng số tiết</th><th>Tổng tiền lương</th><th>Trạng thái</th><th>Hành động</th>
    </tr>`;
  data.forEach(s => {
    if (filterStatus !== "" && String(s.isPaid) !== filterStatus) return;
    html += `<tr>
      <td>${s.teacherId}</td>
      <td>${s.teacherName}</td>
      <td>${s.email}</td>
      <td>${s.degree}</td>
      <td>${s.department}</td>
      <td>${s.classes.join(", ")}</td>
      <td>${s.totalSessions}</td>
      <td>${formatCurrency(s.totalSalary)}</td>
      <td><span class="${s.isPaid ? 'status-paid' : 'status-unpaid'}">
        ${s.isPaid ? 'Đã thanh toán' : 'Chưa thanh toán'}
      </span></td>
      <td class="actions">
        <button onclick="openStatusModal(${s.id})">Cập nhật trạng thái</button>
        <button onclick="showSalaryDetail(${s.teacherId}, '${s.semesterId}')">Xem chi tiết</button>
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
  showLoading(true);
  setTimeout(() => {
    const data = salaryData.filter(s => s.semesterId === semesterId);
    renderSalaryTable(data, 'salarySemesterTable', filterStatus);
    showLoading(false);
  }, 400);
}
document.getElementById('semesterListSelect').onchange = updateSalarySemesterTable;
document.getElementById('statusFilterSemester').onchange = updateSalarySemesterTable;
updateSalarySemesterTable();

// Lọc và hiển thị lương theo khoa
function updateSalaryDepartmentTable() {
  const departmentId = +document.getElementById('departmentSelect').value;
  const filterStatus = document.getElementById('statusFilterDepartment').value;
  showLoading(true);
  setTimeout(() => {
    const data = salaryData.filter(s => s.departmentId === departmentId);
    renderSalaryTable(data, 'salaryDepartmentTable', filterStatus);
    showLoading(false);
  }, 400);
}
document.getElementById('departmentSelect').onchange = updateSalaryDepartmentTable;
document.getElementById('statusFilterDepartment').onchange = updateSalaryDepartmentTable;
updateSalaryDepartmentTable();

// Lọc và hiển thị lương theo năm
function updateSalaryYearTable() {
  const year = document.getElementById('yearSelect').value;
  const filterStatus = document.getElementById('statusFilterYear').value;
  showLoading(true);
  setTimeout(() => {
    // Giả lập: lấy các học kỳ thuộc năm đó
    const semesterIds = semesters.filter(s => s.id.startsWith(year)).map(s => s.id);
    const data = salaryData.filter(s => semesterIds.includes(s.semesterId));
    renderSalaryTable(data, 'salaryYearTable', filterStatus);
    showLoading(false);
  }, 400);
}
document.getElementById('yearSelect').onchange = updateSalaryYearTable;
document.getElementById('statusFilterYear').onchange = updateSalaryYearTable;
updateSalaryYearTable();

// Popup cập nhật trạng thái
let currentSalaryId = null;
window.openStatusModal = function(salaryId) {
  currentSalaryId = salaryId;
  const salary = salaryData.find(s => s.id === salaryId);
  if (!salary) return;
  document.getElementById('currentStatus').value = salary.isPaid ? "Đã thanh toán" : "Chưa thanh toán";
  document.getElementById('newStatusSelect').value = salary.isPaid ? "true" : "false";
  document.getElementById('modalError').innerText = "";
  document.getElementById('statusModal').style.display = 'flex';
};
document.getElementById('closeModal').onclick = closeModal;
document.getElementById('cancelStatusBtn').onclick = closeModal;
function closeModal() {
  document.getElementById('statusModal').style.display = 'none';
  currentSalaryId = null;
}
document.getElementById('saveStatusBtn').onclick = function() {
  const salary = salaryData.find(s => s.id === currentSalaryId);
  const newStatus = document.getElementById('newStatusSelect').value === "true";
  if (salary.isPaid && !newStatus) {
    document.getElementById('modalError').innerText = "Không thể chuyển từ 'Đã thanh toán' sang 'Chưa thanh toán'.";
    return;
  }
  salary.isPaid = newStatus;
  closeModal();
  updateSalarySemesterTable();
  updateSalaryDepartmentTable();
  updateSalaryYearTable();
};

// Tính lương hàng loạt theo học kỳ
document.getElementById('bulkCalcSemesterBtn').onclick = function() {
  const semesterId = document.getElementById('semesterListSelect').value;
  showLoading(true);
  setTimeout(() => {
    // Giả lập: cập nhật lương cho tất cả giáo viên học kỳ này
    let count = 0;
    teachers.forEach(t => {
      if (!salaryData.find(s => s.teacherId === t.id && s.semesterId === semesterId)) {
        salaryData.push({
          id: salaryData.length + 1,
          teacherId: t.id,
          semesterId,
          departmentId: t.department === "Khoa Công Nghệ Thông Tin" ? 101 : 102,
          teacherName: t.name,
          email: t.email,
          degree: t.degree,
          department: t.department,
          classes: ["Lớp mới"],
          totalSessions: 45,
          totalSalary: 12000000,
          isPaid: false
        });
        count++;
      }
    });
    updateSalarySemesterTable();
    showLoading(false);
    alert("Đã tính lương cho " + count + " giáo viên mới.");
  }, 700);
};
// Tính lương hàng loạt theo khoa
document.getElementById('bulkCalcDepartmentBtn').onclick = function() {
  const departmentId = +document.getElementById('departmentSelect').value;
  showLoading(true);
  setTimeout(() => {
    let count = 0;
    teachers.filter(t => (t.department === departments.find(d => d.id === departmentId).name)).forEach(t => {
      if (!salaryData.find(s => s.teacherId === t.id && s.departmentId === departmentId)) {
        salaryData.push({
          id: salaryData.length + 1,
          teacherId: t.id,
          semesterId: semesters[0].id,
          departmentId,
          teacherName: t.name,
          email: t.email,
          degree: t.degree,
          department: t.department,
          classes: ["Lớp mới"],
          totalSessions: 45,
          totalSalary: 12000000,
          isPaid: false
        });
        count++;
      }
    });
    updateSalaryDepartmentTable();
    showLoading(false);
    alert("Đã tính lương cho " + count + " giáo viên mới.");
  }, 700);
};
// Tính lương hàng loạt ở trang riêng
document.getElementById('bulkCalcSemesterOnlyBtn').onclick = function() {
  const semesterId = document.getElementById('bulkSemesterSelect').value;
  showLoading(true);
  setTimeout(() => {
    let count = 0;
    teachers.forEach(t => {
      if (!salaryData.find(s => s.teacherId === t.id && s.semesterId === semesterId)) {
        salaryData.push({
          id: salaryData.length + 1,
          teacherId: t.id,
          semesterId,
          departmentId: t.department === "Khoa Công Nghệ Thông Tin" ? 101 : 102,
          teacherName: t.name,
          email: t.email,
          degree: t.degree,
          department: t.department,
          classes: ["Lớp mới"],
          totalSessions: 45,
          totalSalary: 12000000,
          isPaid: false
        });
        count++;
      }
    });
    document.getElementById('bulkCalcResult').innerHTML = "Đã tính lương cho " + count + " giáo viên.";
    showLoading(false);
  }, 700);
};
document.getElementById('bulkCalcDepartmentOnlyBtn').onclick = function() {
  const departmentId = +document.getElementById('bulkDepartmentSelect').value;
  showLoading(true);
  setTimeout(() => {
    let count = 0;
    teachers.filter(t => (t.department === departments.find(d => d.id === departmentId).name)).forEach(t => {
      if (!salaryData.find(s => s.teacherId === t.id && s.departmentId === departmentId)) {
        salaryData.push({
          id: salaryData.length + 1,
          teacherId: t.id,
          semesterId: semesters[0].id,
          departmentId,
          teacherName: t.name,
          email: t.email,
          degree: t.degree,
          department: t.department,
          classes: ["Lớp mới"],
          totalSessions: 45,
          totalSalary: 12000000,
          isPaid: false
        });
        count++;
      }
    });
    document.getElementById('bulkCalcResult').innerHTML = "Đã tính lương cho " + count + " giáo viên.";
    showLoading(false);
  }, 700);
};

// Đăng xuất
document.getElementById('logoutBtn').onclick = function() {
  alert("Đăng xuất thành công!");
  // window.location.href = '/login';
};