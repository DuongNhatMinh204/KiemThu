package com.nminh.kiemthu.controller.ketoan;

import com.nminh.kiemthu.entity.Tuition;
import com.nminh.kiemthu.service.TutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tuition")
public class TuitionController {
    @Autowired
    private TutionService tutionService;

    @GetMapping("/getAll")
    public List<Tuition> getAll(){
        return tutionService.getAll();
    }
    @PutMapping("/update/{id}")
    public Tuition update(@PathVariable Long id, @RequestParam Long money){
        return tutionService.updateTuition(id, money);
    }
    @PostMapping("/create")
    public Tuition create(@RequestBody Tuition tuition){
        return tutionService.createTuition(tuition);
    }
}
