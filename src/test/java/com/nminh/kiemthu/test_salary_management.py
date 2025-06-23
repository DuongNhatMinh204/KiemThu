import time
import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select, WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

class SalaryManagementTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.driver = webdriver.Chrome()
        cls.driver.get("http://localhost:8080/ketoan")

    @classmethod
    def tearDownClass(cls):
        cls.driver.quit()

    def test_01_sidebar_navigation(self):
        driver = self.driver
        sidebar = driver.find_element(By.CLASS_NAME, "sidebar")
        items = sidebar.find_elements(By.TAG_NAME, "li")
        for item in items:
            item.click()
            time.sleep(0.5)
            page_id = item.get_attribute("data-page")
            page = driver.find_element(By.ID, page_id)
            self.assertIn("active", page.get_attribute("class"))

    def test_02_calc_teacher_salary(self):
        driver = self.driver
        driver.find_element(By.CSS_SELECTOR, 'li[data-page="calc-teacher"]').click()
        time.sleep(1)
        teacher_select = Select(driver.find_element(By.ID, "teacherSelect"))
        semester_select = Select(driver.find_element(By.ID, "semesterSelect"))
        teacher_select.select_by_index(1)
        semester_select.select_by_index(1)
        driver.find_element(By.ID, "calcTeacherForm").submit()
        WebDriverWait(driver, 10).until(
            EC.visibility_of_element_located((By.ID, "teacherSalaryResult"))
        )
        result = driver.find_element(By.ID, "teacherSalaryResult").text
        self.assertIn("THỐNG KÊ LƯƠNG GIÁO VIÊN", result)

    def test_03_salary_semester_table(self):
        driver = self.driver
        driver.find_element(By.CSS_SELECTOR, 'li[data-page="salary-semester"]').click()
        time.sleep(1)
        semester_select = Select(driver.find_element(By.ID, "semesterListSelect"))
        semester_select.select_by_index(1)
        time.sleep(1)
        table = driver.find_element(By.ID, "salarySemesterTable")
        self.assertIn("ID Lương", table.text)

    def test_04_salary_department_table(self):
        driver = self.driver
        driver.find_element(By.CSS_SELECTOR, 'li[data-page="salary-department"]').click()
        time.sleep(1)
        department_select = Select(driver.find_element(By.ID, "departmentSelect"))
        department_select.select_by_index(1)
        time.sleep(1)
        table = driver.find_element(By.ID, "salaryDepartmentTable")
        self.assertIn("ID Lương", table.text)

    def test_05_salary_year_table(self):
        driver = self.driver
        driver.find_element(By.CSS_SELECTOR, 'li[data-page="salary-year"]').click()
        time.sleep(1)
        year_select = Select(driver.find_element(By.ID, "yearSelect"))
        year_select.select_by_index(1)
        time.sleep(1)
        table = driver.find_element(By.ID, "salaryYearTable")
        self.assertIn("ID Lương", table.text)

    def test_06_tuition_degree_edit(self):
        driver = self.driver
        driver.find_element(By.CSS_SELECTOR, 'li[data-page="tuition-degree"]').click()
        time.sleep(2)
        # Sửa tiền học phí đầu tiên
        tuition_table = driver.find_element(By.ID, "tuitionTable")
        first_input = tuition_table.find_element(By.TAG_NAME, "input")
        first_input.clear()
        first_input.send_keys("12345")
        save_btn = tuition_table.find_element(By.TAG_NAME, "button")
        save_btn.click()
        time.sleep(1)
        alert = driver.switch_to.alert
        self.assertIn("Cập nhật thành công", alert.text)
        alert.accept()

        # Sửa hệ số bằng cấp đầu tiên
        degree_table = driver.find_element(By.ID, "degreeTable")
        first_input = degree_table.find_element(By.TAG_NAME, "input")
        first_input.clear()
        first_input.send_keys("2.5")
        save_btn = degree_table.find_element(By.TAG_NAME, "button")
        save_btn.click()
        time.sleep(1)
        alert = driver.switch_to.alert
        self.assertIn("Cập nhật thành công", alert.text)
        alert.accept()

    def test_07_logout(self):
        driver = self.driver
        driver.find_element(By.ID, "logoutBtn").click()
        time.sleep(1)
        alert = driver.switch_to.alert
        self.assertIn("Đăng xuất thành công", alert.text)
        alert.accept()

if __name__ == "__main__":
    unittest.main()