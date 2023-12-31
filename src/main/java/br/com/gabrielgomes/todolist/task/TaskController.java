package br.com.gabrielgomes.todolist.task;


import br.com.gabrielgomes.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController{

    @Autowired
    private  ITaskRepository taskRepository;
    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
        var idUser= request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);
        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início/ data de término deve ser maior do que a data atual");

        }
        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início/ data de término deve ser maior do que a data atual");

        }
        var task = this.taskRepository.save(taskModel);
        return  ResponseEntity.status(HttpStatus.OK).body(task);

    }

    @GetMapping("/")
    public List<TaskModel> list (HttpServletRequest request){
        var idUser= request.getAttribute("idUser");
        return this.taskRepository.findByIdUser((UUID) idUser);

    }

    @PutMapping("/{id}")
    public TaskModel update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){
        var idUser= request.getAttribute("idUser");


        var task = this.taskRepository.findById(id).orElse(null);

        Utils.copyNonNUllProperties(taskModel, task);

        assert task != null;
        return this.taskRepository.save(task);




    }

}