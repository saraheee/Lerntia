package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IUserCourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.UserCourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserCourse;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserCourseService implements IUserCourseService {

    private final IUserCourseDAO iUserCourseDAO;

    public SimpleUserCourseService(IUserCourseDAO iUserCourseDAO){
        this.iUserCourseDAO = iUserCourseDAO;
    }

    @Override
    public void create(UserCourse userCourse) throws ServiceException {
        try {
            iUserCourseDAO.create(userCourse);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }
}
