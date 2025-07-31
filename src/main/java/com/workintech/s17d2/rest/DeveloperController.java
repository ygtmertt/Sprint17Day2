package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.Experience;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

  public Map<Integer, Developer> developers;
  private final Taxable taxable;

  public DeveloperController(Taxable taxable) {
    this.taxable = taxable;
  }

  @PostConstruct
  public void init() {
    developers = new HashMap<>();
  }

  @GetMapping
  public List<Developer> getAllDevelopers() {
    return new ArrayList<>(developers.values());
  }

  @GetMapping("/{id}")
  public Developer getDeveloperById(@PathVariable int id) {
    return developers.get(id);
  }

  @PostMapping
  public Developer addDeveloper(@RequestBody Developer developer) {
    double salary = developer.getSalary();
    Experience experience = developer.getExperience();
    double salaryWithTax = switch (experience) {
      case JUNIOR -> salary - salary * taxable.getSimpleTaxRate() / 100;
      case MID -> salary - salary * taxable.getMiddleTaxRate() / 100;
      case SENIOR -> salary - salary * taxable.getUpperTaxRate() / 100;
    };
    developer.setSalary(salaryWithTax);
    developers.put(developer.getId(), developer);
    return developer;
  }

  @PutMapping("/{id}")
  public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer developerToBeUpdated) {
    developers.put(id, developerToBeUpdated);
    return developerToBeUpdated;
  }

  @DeleteMapping("/{id}")
  public Developer deleteDeveloper(@PathVariable int id) {
    Developer developerToBeDeleted = developers.get(id);
    developers.remove(id);
    return developerToBeDeleted;
  }
}
