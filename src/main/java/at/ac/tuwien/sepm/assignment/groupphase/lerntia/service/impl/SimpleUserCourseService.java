package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IUserCourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserCourse;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IUserCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class SimpleUserCourseService implements IUserCourseService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IUserCourseDAO iUserCourseDAO;

    public SimpleUserCourseService(IUserCourseDAO iUserCourseDAO){
        this.iUserCourseDAO = iUserCourseDAO;
    }

    @Override
    public void create(UserCourse userCourse) throws ServiceException {
        try {
            iUserCourseDAO.create(userCourse);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }
}
