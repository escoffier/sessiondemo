package com.example.sessiondemo.service;

import com.example.sessiondemo.model.Employee;
import com.example.sessiondemo.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

//import javax.transaction.Transactional;

@Component
//@Transactional(value = "employeesTransactionManager")
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    final
    EmployeeRepository employeeRepository;

    final
    RedisService redisService;

    public EmployeeService(EmployeeRepository employeeRepository, RedisService redisService) {
        this.employeeRepository = employeeRepository;
        this.redisService = redisService;
    }

    //@Transactional(value = "employeesTransactionManager", propagation = Propagation.REQUIRED)
    public Employee insertEmp(Employee employee) {
        Employee emp = employeeRepository.save(employee);
//        List<Phone> phones = getPhones(employee.getEmployeeNo());
//        logger.info(phones.toString());
        return emp;
    }

    public Employee getEmployee(Long id) {
        String key = "em:" + id;
        Optional<Employee> emp = redisService.getEmployee1(key);
        return emp.orElseGet(() -> {
            Optional<Employee> employee = employeeRepository.findById(id);
            employee.ifPresent(employee1 -> redisService.saveEmployee(employee1));
            return employee.orElse(new Employee());
            //return  employeeRepository.findById(id).orElse(new Employee());
        });
        //logger.info("findById " + id);
        //return  employeeRepository.findById(id).orElse(new Employee());
    }

    public Page<Employee> getAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }


//    @Transactional(value = "employeesTransactionManager", propagation = Propagation.REQUIRED)
//    public Phone insertPhone(Phone phone) {
////        List<Phone> phones = getPhones(employee.getEmployeeNo());
//////        logger.info(phones.toString());
//        return phoneRepository.save(phone);
//    }
}
