package at.ac.tuwien.lerntia.lerntia.service.impl;

import at.ac.tuwien.lerntia.exception.ConfigReaderException;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dao.ICourseDAO;
import at.ac.tuwien.lerntia.lerntia.dto.Course;
import at.ac.tuwien.lerntia.lerntia.service.ICourseService;
import at.ac.tuwien.lerntia.util.ConfigReader;
import at.ac.tuwien.lerntia.util.Semester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class CourseService implements ICourseService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ICourseDAO courseDAO;
    private ConfigReader configReaderCourse;

    private int maxLengthCourseMark;
    private int maxLengthCourseName;

    public CourseService(ICourseDAO courseDAO) {
        this.courseDAO = courseDAO;

        maxLengthCourseMark = 10;
        maxLengthCourseName = 60;

        try {
            configReaderCourse = new ConfigReader("course");
        } catch (ConfigReaderException e) {
            //throw new ServiceException(e.getCustomMessage());
        }

        maxLengthCourseMark = configReaderCourse.getValue("maxLengthCourseMark") != null ? configReaderCourse.getValueInt("maxLengthCourseMark") : maxLengthCourseMark;
        maxLengthCourseName = configReaderCourse.getValue("maxLengthCourseName") != null ? configReaderCourse.getValueInt("maxLengthCourseName") : maxLengthCourseName;
    }

    @Override
    public void create(Course course) throws ServiceException {
        try {
            LOG.debug("Create new course: {}", course);
            courseDAO.create(course);
            LOG.info("New course created: {}", course);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public void update(Course course) throws ServiceException {
        try {
            LOG.debug("Update existing course with new values, {}", course);
            courseDAO.update(course);
            LOG.info("Course successfully updated.");
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }


    @Override
    public void delete(Course course) throws ServiceException {
        try {
            LOG.info("Delete course: {}", course);
            courseDAO.delete(course);
            LOG.info("Course successfully deleted.");
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public List<Course> readAll() throws ServiceException {
        LOG.info("Retrieving all existing courses....");
        List<Course> courses;
        try {
            courses = courseDAO.readAll();
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }

        if (courses.isEmpty()) {
            throw new ServiceException("Es wurden noch keine LVAs angelegt!");
        }
        LOG.info("All courses retrieved.");
        return courses;
    }

    @Override
    public void validate(Course course) throws ServiceException {
        if (configReaderCourse == null) {
            LOG.debug("Opening a new config reader for course");
            try {
                configReaderCourse = new ConfigReader("course");
            } catch (ConfigReaderException e) {
                throw new ServiceException(e.getCustomMessage());
            }
        }
        LOG.info("Check if all mandatory values are valid.");
        boolean error = false;
        String message = "";
        if (course.getMark().equals("")) {
            error = true;
            message += "Die LVA-Nummer ist leer!\n";
        }

        if (course.getMark().length() > maxLengthCourseMark) {
            error = true;
            message += "Die LVA-Nummer ist zu lang!\n";
        }

        if (course.getName().equals("")) {
            error = true;
            message += "Der LVA-Name ist leer!\n";
        }

        if (course.getName().length() > maxLengthCourseName) {
            error = true;
            message += "Der LVA-Name ist zu lang!\n";
        }

        if (
            !course.getSemester().startsWith(Semester.WS.toString()) &&
                !course.getSemester().startsWith(Semester.SS.toString())) {
            message += "Das Semester sollte mit 'WS' oder 'SS' beginnen!\n";
            throw new ServiceException(message);
        }

        try {
            String yearStr = course.getSemester().substring(2);

            if (yearStr.length() != 4) {
                error = true;
                message += "Das Jahr sollte eine Zahl mit 4 Ziffern sein!\n";
            }
            int yearInt = Integer.parseInt(yearStr);
            if (yearInt < 0) {
                error = true;
                message += "Das Jahr sollte nicht negativ sein!\n";
            }
            if (!error) {
                course.setSemester(course.getSemester().substring(0, 2) + yearStr.substring(2, 4));
            }
            LOG.info("All course values are valid.");
        } catch (NumberFormatException e) {
            error = true;
            message += "Das Jahr ist keine Zahl!\n";
        }
        if (error) {
            throw new ServiceException(message);
        }
    }

}
