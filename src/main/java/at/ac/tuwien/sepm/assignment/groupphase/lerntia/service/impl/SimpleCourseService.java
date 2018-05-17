package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ICourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public void validate(Course course) throws ServiceException {

        if (course.getMark().equals("")){
            throw new ServiceException("Die ID ist leer");
        }

        if (course.getMark().length() > 255) {
            throw new ServiceException("Die ID ist zu lang");
        }

        if (course.getName().equals("")){
            throw new ServiceException("Der Name ist leer");
        }

        if (
            ! course.getSemester().startsWith("W") &&
            ! course.getSemester().startsWith("S")
        ) {
            throw new ServiceException("Das Semester sollte mit 'W' oder 'S' enden");
        }

        // year is a number with 4 digits

        String yearStr;
        int yearInt;

        try{
            yearStr = course.getSemester().substring(0,3);
            yearInt = Integer.parseInt(yearStr);
        } catch(NumberFormatException e) {
            throw new ServiceException("Das Jahr sollte eine Zahl sein mit 4 Ziffern sein");
        }
    }
}
