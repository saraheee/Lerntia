package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ICourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleCourseService implements ICourseService {

    private final ICourseDAO courseDAO;

    public SimpleCourseService(ICourseDAO courseDAO){
        this.courseDAO = courseDAO;
    }

    @Override
    public void create(Course course) throws ServiceException {
        try {
            courseDAO.create(course);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Course course) throws ServiceException {
        try {
            courseDAO.update(course);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void search(Course course) throws ServiceException {
        try {
            courseDAO.search(course);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Course course) throws ServiceException {
        try {
            courseDAO.delete(course);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> readAll() throws ServiceException {

        List<Course> courses = null;
        try {
            courses = courseDAO.readAll();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return courses;
    }

    @Override
    public void validate(Course course) throws ServiceException {

        // pls note: mark is the name

        // -------------------------------------------------------------------------------------------------------------
        // name
        // -------------------------------------------------------------------------------------------------------------

        // name is not empty

        if (course.getMark().equals("")){
            throw new ServiceException("Der Name ist leer");
        }

        // name is not used already

        List<Course> courses = this.readAll();

        for (int i = 0; i < courses.size(); i++){
            if (course.getMark() == courses.get(i).getMark()){
                throw new ServiceException("Der Name ist bereits vergeben");
            }
        }

        // TODO - name is not too long

        // -------------------------------------------------------------------------------------------------------------
        // semester
        // -------------------------------------------------------------------------------------------------------------

        // starts with ws or ss

        if (
            ! course.getSemester().startsWith("ws") &&
            ! course.getSemester().startsWith("ss")
        ) {
            throw new ServiceException("Das Semester sollte mit 'ws' oder 'ss' beginnen");
        }

        // TODO - something about the year (only 2 digits or 4)
    }
}
