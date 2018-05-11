package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.UserDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.User;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserService implements IUserService {

    private final UserDAO userDAO;

    public SimpleUserService(UserDAO userDAO){
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
