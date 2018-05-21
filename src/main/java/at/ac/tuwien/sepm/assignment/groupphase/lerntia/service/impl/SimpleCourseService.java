package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ICourseService;
import at.ac.tuwien.sepm.assignment.groupphase.util.Semester;
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
            throw new ServiceException("Die LVA-Nummer ist leer");
        }

        if (course.getMark().length() > 255) {
            throw new ServiceException("Die LVA-Nummer ist zu lang");
        }

        if (course.getName().equals("")){
            throw new ServiceException("Der Name ist leer");
        }

        if (course.getName().length() > 255) {
            throw new ServiceException("Der Name ist zu lang");
        }

        if (
            ! course.getSemester().startsWith(Semester.WS.toString()) &&
            ! course.getSemester().startsWith(Semester.SS.toString())
        ) {
            throw new ServiceException("Das Semester sollte mit 'WS' oder 'SS' beginnen");
        }

        try{
            String yearStr = course.getSemester().substring(2);
            if (yearStr.length() > 4) {
                throw new ServiceException("Das Jahr sollte eine Zahl sein mit 4 Ziffern sein");
            }
            if (yearStr.length() > 2) { // has 4 digits
                yearStr = yearStr.substring(2); // only the last 2 digits are important
            }
            int yearInt = Integer.parseInt(yearStr);
            if (yearInt < 0) {
                throw new ServiceException("Das Jahr sollte nicht negativ sein");
            }
            course.setSemester(course.getSemester().substring(0,2)+yearInt);
        } catch(NumberFormatException e) {
            throw new ServiceException("Das Jahr sollte eine Zahl sein mit 4 Ziffern sein");
        }
    }
}
