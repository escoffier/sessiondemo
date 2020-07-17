package com.example.sessiondemo.service;


import com.example.sessiondemo.model.Employee;
import com.example.sessiondemo.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.BoundKeyOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    final
    EmployeeRepository employeeRepository;

    HashMapper<Object, String, Object> mapper = new Jackson2HashMapper(false);

    @Resource(name = "redisEmpTemplate")
    private HashOperations<String, String, Object> hashOperations;

    //@Resource(name = "redisTemplate")
    //@Qualifier("redisTemplate")
    final
    //@Resource
    //@Qualifier("redisTemplate")
    PipeLineHashOperations<String, String, Object> pipeLineHashOperations;

    BoundKeyOperations<String> boundKeyOperations;

    private int number = 0;

    public RedisService(EmployeeRepository employeeRepository, PipeLineHashOperations<String, String, Object> pipeLineHashOperations) {
        this.employeeRepository = employeeRepository;
        this.pipeLineHashOperations = pipeLineHashOperations;
    }

    @PostConstruct
    public void init(){
        //pipeLineHashOperations = new PipeLineHashOperations<String, String, Object>(hashOperations.getOperations())

    }

    public void db2redis(String mode) {
        Pageable pageable = PageRequest.of(0, 5000);
        Page<Employee> emPage = null;

        while (true) {
            emPage = employeeRepository.findAll(pageable);

            List<Employee> employeeList = emPage.getContent();
            logger.info("get page: "  + employeeList.size());
            //pipelineWrite(employeeList);
            if (mode.equals("pipeline")) {
                pipelineWrite(employeeList);
            } else {
                for (Employee em : employeeList) {
//                Map<String, Object> stringObjectMap = mapper.toHash(em);
//                hashOperations.putAll("em:" + em.getEmployeeNo(),stringObjectMap );
                    saveEmployee(em);
                }
            }

            if (!emPage.hasNext()) {
                break;
            }
            pageable = emPage.nextPageable();
        }
    }

    public void db2redis(Long num) {
        //var empList = employeeRepository.findFirst100ByEmployeeNo()
    }

    public Employee getEmployee(String id) {
        if(!hashOperations.getOperations().hasKey(id)) {
            return null;
        }

        Map map = hashOperations.entries(id);
        Employee employee = (Employee) mapper.fromHash(map);
        logger.info(employee.toString());
        return employee;
    }

    public Optional<Employee> getEmployee1(String id) {
        Employee employee = null;
        if(hashOperations.getOperations().hasKey(id)) {
            Map map = hashOperations.entries(id);
            employee = (Employee) mapper.fromHash(map);
            logger.info(employee.toString());
        }
        return Optional.ofNullable(employee);
    }

    public void saveEmployee(Employee employee) {
        Map<String, Object> stringObjectMap = mapper.toHash(employee);
        hashOperations.putAll("em:" + employee.getEmployeeNo(),stringObjectMap );
    }

    private void pipelineWrite(List<Employee> employeeList) {
//        hashOperations.getOperations().executePipelined((RedisCallback<Object>) connection -> {
//            for (Employee em : employeeList) {
//                Map<String, Object> stringObjectMap = mapper.toHash(em);
//                String key = "em:" + em.getEmployeeNo();
//                //connection.hashCommands().hMSet();
//                hashOperations.putAll(key, stringObjectMap);
//            }
//            return null;
//        });
        pipeLineHashOperations.putAll(employeeList);
    }

}
