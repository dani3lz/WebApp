package com.endava.webapp.Repository;

import com.endava.webapp.Entity.Department;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
public class DepartmentRepository {
    private final EntityManager entityManager;

    public DepartmentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Department save(Department department) {
        try (Session session = entityManager.unwrap(Session.class)) {
            session.save(department);
            return department;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Department> getAll() {
        try (Session session = entityManager.unwrap(Session.class)) {
            return session.createQuery("from Department", Department.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Department getById(Long id) {
        try (Session session = entityManager.unwrap(Session.class)) {
            return session.get(Department.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Department editDepartment(Long id, Department department) {
        try (Session session = entityManager.unwrap(Session.class)) {
            Department departmentFromDB = session.get(Department.class, id);
            departmentFromDB.editEmployee(department);
            return departmentFromDB;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
