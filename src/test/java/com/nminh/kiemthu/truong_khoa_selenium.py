#
#
# import unittest
# from selenium import webdriver
# from selenium.webdriver.common.by import By
# from selenium.webdriver.support.ui import WebDriverWait
# from selenium.webdriver.support import expected_conditions as EC
# import time
#
# class TruongKhoaUITest(unittest.TestCase):
#     @classmethod
#     def setUpClass(cls):
#         cls.driver = webdriver.Chrome()
#         cls.driver.get("http://localhost:8080/admin")
#         cls.driver.maximize_window()
#         cls.wait = WebDriverWait(cls.driver, 15)
#
#     @classmethod
#     def tearDownClass(cls):
#         cls.driver.quit()
#
#     def test_01_sidebar_navigation(self):
#         driver = self.driver
#         sidebar = driver.find_element(By.CLASS_NAME, "sidebar")
#         nav_links = sidebar.find_elements(By.CLASS_NAME, "nav-link")
#         for link in nav_links:
#             driver.execute_script("arguments[0].scrollIntoView();", link)
#             link.click()
#             time.sleep(0.5)
#
#     def test_02_add_department(self):
#         driver = self.driver
#         driver.find_element(By.LINK_TEXT, "Quản Lý Khoa").click()
#         self.wait.until(EC.visibility_of_element_located((By.ID, "departmentPanel")))
#         driver.find_element(By.CSS_SELECTOR, "button[data-bs-target='#createDepartmentModal']").click()
#         self.wait.until(EC.visibility_of_element_located((By.ID, "createDepartmentModal")))
#         driver.find_element(By.ID, "fullName").send_keys("Khoa Test")
#         driver.find_element(By.ID, "shortName").send_keys("KT")
#         driver.find_element(By.ID, "description").send_keys("Khoa kiểm thử tự động")
#         driver.find_element(By.CSS_SELECTOR, "#createDepartmentForm button[type='submit']").click()
#         time.sleep(1)
#
#     def test_03_add_subject(self):
#         driver = self.driver
#         try:
#             WebDriverWait(driver, 5).until(EC.alert_is_present())
#             alert = driver.switch_to.alert
#             alert.accept()
#         except:
#             pass
#         driver.find_element(By.LINK_TEXT, "Quản lý môn học").click()
#         self.wait.until(EC.visibility_of_element_located((By.ID, "subjectPanel")))
#         dept_select = self.wait.until(EC.presence_of_element_located((By.ID, "departmentSelect")))
#         dept_select.click()
#         dept_select.find_elements(By.TAG_NAME, "option")[1].click()
#         driver.find_element(By.XPATH, "//button[contains(text(),'Thêm môn học')]").click()
#         self.wait.until(EC.visibility_of_element_located((By.ID, "addSubjectForm")))
#         driver.find_element(By.ID, "subjectName").send_keys("Môn Test")
#         driver.find_element(By.ID, "credits").send_keys("3")
#         driver.find_element(By.ID, "numberOfLessons").send_keys("30")
#         driver.find_element(By.ID, "moduleCoefficient").send_keys("1.0")
#         dept_form_select = driver.find_element(By.ID, "departmentSelectForm")
#         dept_form_select.find_elements(By.TAG_NAME, "option")[1].click()
#
#         # Wait for the "Lưu" button inside the form
#         self.wait.until(EC.presence_of_element_located((By.XPATH, "//form[@id='addSubjectForm']//button[contains(text(),'Lưu')]")))
#         save_button = driver.find_element(By.XPATH, "//form[@id='addSubjectForm']//button[contains(text(),'Lưu')]")
#         if not save_button.is_enabled():
#             driver.save_screenshot("debug_add_subject.png")
#             print("Save button is not enabled. Check form validation and screenshot.")
#             self.fail("Save button is not enabled.")
#         save_button.click()
#         time.sleep(1)
#
#     def test_04_add_classroom(self):
#         driver = self.driver
#         driver.find_element(By.LINK_TEXT, "Quản lý lớp học phần").click()
#         self.wait.until(EC.visibility_of_element_located((By.ID, "classroomPanel")))
#         semester_select = driver.find_element(By.ID, "semesterSelect")
#         semester_select.find_elements(By.TAG_NAME, "option")[1].click()
#         dept_select = driver.find_element(By.ID, "departmentSelectClassroom")
#         dept_select.find_elements(By.TAG_NAME, "option")[1].click()
#         driver.find_element(By.ID, "addClassroomBtn").click()
#         self.wait.until(EC.visibility_of_element_located((By.ID, "addClassroomModal")))
#         driver.find_element(By.ID, "numberOfStudents").send_keys("40")
#         driver.find_element(By.ID, "numberOfClasses").send_keys("1")
#         subject_select = driver.find_element(By.ID, "subjectSelect")
#         subject_select.find_elements(By.TAG_NAME, "option")[1].click()
#         driver.find_element(By.CSS_SELECTOR, "#addClassroomForm button[type='submit']").click()
#         time.sleep(1)
#
# if __name__ == "__main__":
#     unittest.main()


import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

class TruongKhoaUITest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.driver = webdriver.Chrome()
        cls.driver.get("http://localhost:8080/admin")
        cls.driver.maximize_window()
        cls.wait = WebDriverWait(cls.driver, 15)

    @classmethod
    def tearDownClass(cls):
        cls.driver.quit()

    def test_01_sidebar_navigation(self):
        driver = self.driver
        sidebar = driver.find_element(By.CLASS_NAME, "sidebar")
        nav_links = sidebar.find_elements(By.CLASS_NAME, "nav-link")
        for link in nav_links:
            driver.execute_script("arguments[0].scrollIntoView();", link)
            link.click()
            time.sleep(0.5)

    def test_02_add_department(self):
        driver = self.driver
        driver.find_element(By.LINK_TEXT, "Quản Lý Khoa").click()
        self.wait.until(EC.visibility_of_element_located((By.ID, "departmentPanel")))
        driver.find_element(By.CSS_SELECTOR, "button[data-bs-target='#createDepartmentModal']").click()
        self.wait.until(EC.visibility_of_element_located((By.ID, "createDepartmentModal")))
        driver.find_element(By.ID, "fullName").send_keys("Khoa Test")
        driver.find_element(By.ID, "shortName").send_keys("KT")
        driver.find_element(By.ID, "description").send_keys("Khoa kiểm thử tự động")
        driver.find_element(By.CSS_SELECTOR, "#createDepartmentForm button[type='submit']").click()
        time.sleep(1)

    def test_03_add_subject(self):
        driver = self.driver
        try:
            WebDriverWait(driver, 5).until(EC.alert_is_present())
            alert = driver.switch_to.alert
            alert.accept()
        except:
            pass
        driver.find_element(By.LINK_TEXT, "Quản lý môn học").click()
        self.wait.until(EC.visibility_of_element_located((By.ID, "subjectPanel")))
        dept_select = self.wait.until(EC.presence_of_element_located((By.ID, "departmentSelect")))
        dept_select.click()
        dept_select.find_elements(By.TAG_NAME, "option")[1].click()
        driver.find_element(By.XPATH, "//button[contains(text(),'Thêm môn học')]").click()
        self.wait.until(EC.visibility_of_element_located((By.ID, "addSubjectForm")))
        driver.find_element(By.ID, "subjectName").send_keys("Môn Test")
        driver.find_element(By.ID, "credits").send_keys("3")
        driver.find_element(By.ID, "numberOfLessons").send_keys("30")
        driver.find_element(By.ID, "moduleCoefficient").send_keys("1.0")
        dept_form_select = driver.find_element(By.ID, "departmentSelectForm")
        dept_form_select.find_elements(By.TAG_NAME, "option")[1].click()

        # Debug: Save screenshot and print page source before clicking "Lưu"
        driver.save_screenshot("debug_add_subject.png")
        print(driver.page_source)

        # The "Lưu" button for subject form has 💾 in text, so match that
        save_button = self.wait.until(
            EC.element_to_be_clickable((By.XPATH, "//div[@id='addSubjectForm']//button[contains(text(),'Lưu')]"))
        )
        save_button.click()
        time.sleep(1)

    def test_04_add_classroom(self):
        driver = self.driver
        driver.find_element(By.LINK_TEXT, "Quản lý lớp học phần").click()
        self.wait.until(EC.visibility_of_element_located((By.ID, "classroomPanel")))
        semester_select = driver.find_element(By.ID, "semesterSelect")
        semester_select.find_elements(By.TAG_NAME, "option")[1].click()
        dept_select = driver.find_element(By.ID, "departmentSelectClassroom")
        dept_select.find_elements(By.TAG_NAME, "option")[1].click()
        driver.find_element(By.ID, "addClassroomBtn").click()
        self.wait.until(EC.visibility_of_element_located((By.ID, "addClassroomModal")))
        driver.find_element(By.ID, "numberOfStudents").send_keys("40")
        driver.find_element(By.ID, "numberOfClasses").send_keys("1")
        subject_select = driver.find_element(By.ID, "subjectSelect")
        subject_select.find_elements(By.TAG_NAME, "option")[1].click()
        driver.find_element(By.CSS_SELECTOR, "#addClassroomForm button[type='submit']").click()
        time.sleep(1)

if __name__ == "__main__":
    unittest.main()