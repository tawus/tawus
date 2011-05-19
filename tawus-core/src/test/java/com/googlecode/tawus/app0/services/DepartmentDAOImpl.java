package com.googlecode.tawus.app0.services;

import java.io.Serializable;

import com.googlecode.tawus.app0.models.Department;
import com.googlecode.tawus.services.EntityDAO;

public class DepartmentDAOImpl extends DummyEntityDAO<Department> implements EntityDAO<Department> {

   public DepartmentDAOImpl(Class<Department> type) {
      super(type);
      Department department = new Department();
      department.setId(1L);
      department.setName("Computers");
      save(department);

      department = new Department();
      department.setId(2L);
      department.setName("English");
      save(department);
   }

   @Override
   public Department find(Serializable id) {
      for(Department department: getEntities()){
         if(department.getId().equals(id)){
            return department;
         }
      }
      return null;
   }

   @Override
   public Serializable getIdentifier(Object object) {
      return ((Department)object).getId();
   }

   @Override
   public void setIdentifier(Department entity, Object value) {
      entity.setId((Long)value);
   }
}
