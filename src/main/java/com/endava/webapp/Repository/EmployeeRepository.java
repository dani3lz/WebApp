package com.endava.webapp.Repository;

import com.endava.webapp.Entity.Employee;
import com.fasterxml.jackson.core.exc.InputCoercionException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.InputMismatchException;
import java.util.List;

@Repository
public class EmployeeRepository {
    private final EntityManager entityManager;

    public EmployeeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Employee save(Employee employee) {
        try (Session session = entityManager.unwrap(Session.class)) {
            session.save(employee);
            return employee;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Employee> getAll() {
        try (Session session = entityManager.unwrap(Session.class)) {
            return session.createQuery("from Employee", Employee.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Employee getById(Long id) {
        try (Session session = entityManager.unwrap(Session.class)) {
            return session.get(Employee.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Employee editEmployee(Long id, Employee employee){
        try (Session session = entityManager.unwrap(Session.class)) {
            Employee employeeFromDB = session.get(Employee.class, id);
            employeeFromDB.editEmployee(employee);
            return employeeFromDB;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
