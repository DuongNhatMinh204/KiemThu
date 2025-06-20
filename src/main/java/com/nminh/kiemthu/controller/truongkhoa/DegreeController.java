package com.nminh.kiemthu.controller.truongkhoa;

import com.nminh.kiemthu.entity.Degree;
import com.nminh.kiemthu.model.request.DegreeCreateDTO;
import com.nminh.kiemthu.model.response.ApiResponse;
import com.nminh.kiemthu.service.DegreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/degree")
@Slf4j
public class DegreeController {

    @Autowired
    private DegreeService degreeService;

    @PostMapping("/create")
    public ApiResponse createDegree(@RequestBody  DegreeCreateDTO degreeCreateDTO) {
        log.info("degree createDTO: {}", degreeCreateDTO);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("degree created");
        apiResponse.setData(degreeService.createDegree(degreeCreateDTO));
        return apiResponse;
    }

    @GetMapping("/get-all")
    public ApiResponse getAllDegree() {
        log.info("getAllDegree");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("degree get all");
        apiResponse.setData(degreeService.getAllDegrees());
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteDegree(@PathVariable Long id) {
        log.info("degree delete: {}", id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(degreeService.deleteDegree(id));
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    public ApiResponse updateDegree(@PathVariable Long id,@RequestBody DegreeCreateDTO degreeUpdateDTO) {
        log.info("degree updateDTO: {}", degreeUpdateDTO);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("degree updated");
        apiResponse.setData(degreeService.update(id, degreeUpdateDTO));
        return apiResponse;
    }
    @PutMapping("/setCoefficient/{id}")
    public void setDegreeCoefficient(@PathVariable Long id, @RequestParam Double coefficient){
        degreeService.setDegreeCoefficient(id, coefficient);
    }
}
