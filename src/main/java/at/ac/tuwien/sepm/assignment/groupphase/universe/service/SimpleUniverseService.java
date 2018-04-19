package at.ac.tuwien.sepm.assignment.groupphase.universe.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.universe.dao.UniverseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.universe.dto.Answer;
import at.ac.tuwien.sepm.assignment.groupphase.universe.dto.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class SimpleUniverseService implements UniverseService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int SLEEP_SECONDS = 2;
    private static final Question QUESTION_OF_LIFE_THE_UNIVERSE_AND_EVERYTHING =
        new Question("question of life, the universe, and everything");

    private final UniverseDAO universeDAO;

    public SimpleUniverseService(UniverseDAO universeDAO) {
        this.universeDAO = universeDAO;
    }

    @Override
    public Answer calculateAnswer() throws ServiceException {
        LOG.debug("called calculateAnswer");
        // sleep to simulate heavy load
        try {
            LOG.trace("Going to sleep for {} seconds", SLEEP_SECONDS);
            Thread.sleep(Duration.of(SLEEP_SECONDS, SECONDS).toMillis());
        } catch (InterruptedException e) {
            LOG.warn("Failed to sleep cause {}", e.getMessage());
        }
        try {
            final Answer answer = universeDAO.readAnswerForQuestion(QUESTION_OF_LIFE_THE_UNIVERSE_AND_EVERYTHING);
            return new Answer(
                answer.getId(),
                answer.getText() + '!'
            );
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

}
