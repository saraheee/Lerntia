package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import org.springframework.stereotype.Service;

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
}
