package com.example.englishapp.controller;

import com.example.englishapp.entity.Admin;
import com.example.englishapp.entity.User;
import com.example.englishapp.service.AdminService;
import com.example.englishapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<List<Admin>> readAll(){
        final List<Admin> adminList = adminService.findAll();
        return adminList != null && !adminList.isEmpty()
                ? new ResponseEntity<>(adminList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //получение значения по login
    @GetMapping("/{login}")
    public ResponseEntity<Admin> getOne(@PathVariable(name = "login") Admin admin){
        final Admin thisAdmin = admin;
        return thisAdmin != null
                ? new ResponseEntity<>(thisAdmin, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //добавление записи
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Admin admin){
        Admin newAdmin = adminService.create(admin);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    @PutMapping("/{login}")
    public ResponseEntity<?> update(@PathVariable(name = "login") Admin adminFromDB,
                                    @RequestBody Admin admin) //user от пользователя
    {
        Admin updatedAdmin = adminService.update(admin, adminFromDB);
        if (updatedAdmin != null) {
            return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{login}")
    public ResponseEntity<List<Admin>> delete(@PathVariable("login") Admin admin) {
        if (adminService.delete(admin)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
