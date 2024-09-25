package fr.twitmund.keaver.db.service;

import fr.twitmund.keaver.db.entities.Student;
import fr.twitmund.keaver.db.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> studentFetch(){
        return studentRepository.findAll();
    }

    public Exception insertStudentIfNotExists(Student student){
        for (Student s : studentRepository.findAll()) {
            if (Objects.equals(student.getEmail(), s.getEmail())){
                return new Exception("Student already exists");
            }else {
                studentRepository.insert(student);
            }
        }
        return null;
    }
}
