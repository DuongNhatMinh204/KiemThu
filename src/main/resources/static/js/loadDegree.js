function loadDegrees() {
    fetch("/admin/degree/get-all")
        .then(res => res.json())
        .then(result => {
            const dropdown = document.getElementById("degreeDropdown");
            dropdown.innerHTML = '<option value="">-- Chọn học vị --</option>';
            result.data.forEach(degree => {
                const option = document.createElement("option");
                option.value = degree.id;
                option.textContent = degree.fullName;
                dropdown.appendChild(option);
            });
        })
        .catch(err => console.error("Lỗi khi tải danh sách bằng cấp:", err));
}
