package fr.twitmund.keaver.controllers;

import fr.twitmund.keaver.db.service.StudentService;
import fr.twitmund.keaver.db.entities.Student;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @RequestMapping("api/v1/students/fetch")
    public List<Student> studentFetchAll() {
        return studentService.studentFetch();
    }
}

