package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.UserDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class SimpleUserService implements IUserService {

    private final UserDAO userDAO;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public SimpleUserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void create(User user) throws ServiceException {

    }

    @Override
    public void update(User user) throws ServiceException {

    }

    @Override
    public User read(User user) throws ServiceException {
        return null;
    }

    @Override
    public void delete(User user) throws ServiceException {

    }
}
