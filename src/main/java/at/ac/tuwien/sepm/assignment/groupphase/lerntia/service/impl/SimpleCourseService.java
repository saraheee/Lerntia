package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ICourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleCourseService implements ICourseService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ICourseDAO courseDAO;

    public SimpleCourseService(ICourseDAO courseDAO){
        this.courseDAO = courseDAO;
    }

    @Override
    public void create(Course course) throws ServiceException {
        try {
            courseDAO.create(course);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(Course course) throws ServiceException {
        try {
            courseDAO.update(course);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());        }
    }

    @Override
    public void search(Course course) throws ServiceException {
        try {
            courseDAO.search(course);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(Course course) throws ServiceException {
        try {
            courseDAO.delete(course);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Course> readAll() throws ServiceException {

        List<Course> courses = null;
        try {
            courses = courseDAO.readAll();
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }

        return courses;
    }
}
