package com.example.englishapp.service;

import com.example.englishapp.entity.Admin;
import com.example.englishapp.repository.AdminRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired //автоматич создает данный объект
    private AdminRepository adminRepository;


    public List<Admin> findAll(){
        return adminRepository.findAll();
    }


    public Admin create(Admin admin){
        return adminRepository.save(admin);
    }

    public Admin update(Admin admin, Admin adminFromDB) {
        BeanUtils.copyProperties(admin, adminFromDB, "login");
        return adminRepository.save(adminFromDB);
    }

    public boolean delete(Admin admin) {
        if (admin != null){
            adminRepository.delete(admin);
            return true;
        }
        return false;
    }
}
