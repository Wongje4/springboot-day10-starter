package com.rest.springbootemployee.controller;

import com.rest.springbootemployee.controller.dto.CompanyRequest;
import com.rest.springbootemployee.controller.dto.CompanyResponse;
import com.rest.springbootemployee.controller.mapper.CompanyMapper;
import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.exception.InvalidIdException;
import com.rest.springbootemployee.service.CompanyService;
import com.rest.springbootemployee.entity.Employee;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private CompanyService companyService;
    private CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @GetMapping
    public List<CompanyResponse> getAll() {
        return companyMapper.toResponse(companyService.findAll());
    }

    @GetMapping("/{id}")
    public CompanyResponse getById(@PathVariable String id) {
        validateObjectId(id);
        return companyMapper.toResponse(companyService.findById(id));
    }

    @GetMapping("/{id}/employees")
    public List<Employee> getEmployees(@PathVariable String id) {
        validateObjectId(id);
        return companyService.getEmployees(id);
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<CompanyResponse> getByPage(Integer page, Integer pageSize) {
        List<Company> companies = companyService.findByPage(page, pageSize);
        return companyMapper.toResponse(companies);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CompanyResponse create(@RequestBody CompanyRequest companyRequest) {
        Company savedCompany = companyService.create(companyMapper.toEntity(companyRequest));
        return companyMapper.toResponse(savedCompany);
    }

    @PutMapping("/{id}")
    public CompanyResponse update(@PathVariable String id, @RequestBody CompanyRequest companyRequest) {
        validateObjectId(id);
        Company updatedCompany = companyService.update(id, companyMapper.toEntity(companyRequest));
        return companyMapper.toResponse(updatedCompany);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable String id) {
        validateObjectId(id);
        companyService.delete(id);
    }

    private void validateObjectId(String id){
        if(!ObjectId.isValid(id)){
            throw new InvalidIdException();
        }
    }
}
