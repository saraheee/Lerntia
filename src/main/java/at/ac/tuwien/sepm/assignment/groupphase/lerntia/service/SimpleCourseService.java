package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleCourseService implements ICourseService {
    @Override
    public void create(Course course) throws ServiceException {

    }

    @Override
    public void update(Course course) throws ServiceException {

    }

    @Override
    public void search(Course course) throws ServiceException {

    }

    @Override
    public void delete(Course course) throws ServiceException {

    }

    @Override
    public List<Course> readAll() throws ServiceException {
        return null;
    }
}
