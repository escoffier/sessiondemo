package com.example.sessiondemo.repository;


import com.example.sessiondemo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    //List<Employee> findFirst100ByEmployeeNo(Long id);
    //List<Employee> findFirst100All();
}
