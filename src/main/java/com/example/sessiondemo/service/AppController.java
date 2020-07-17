package com.example.sessiondemo.service;

import com.example.sessiondemo.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    private static Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    EmployeeService employeeService;

    private RedisService redisService;


    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/")
    public String helloAdmin() {
        return "hello admin";
    }

    @GetMapping("/emp/{id}")
    public Employee getEmp(@PathVariable("id") Long id) {
        logger.info("get employee " + id);
        return employeeService.getEmployee(id);
    }

    @GetMapping("/emp/listPageable")
    public Page<Employee> getEmpPageable(Pageable pageable) {
        return employeeService.getAll(pageable);
    }

    @GetMapping("/admin/db2redis")
    public String db2Redis() {
        //logger.info("get employee );
        redisService.db2redis("pipeline");
        return "saving data to redis";
    }
}
