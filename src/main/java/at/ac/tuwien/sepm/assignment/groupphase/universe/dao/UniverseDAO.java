package at.ac.tuwien.sepm.assignment.groupphase.universe.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.universe.dto.Answer;
import at.ac.tuwien.sepm.assignment.groupphase.universe.dto.Question;

/**
 * The <code>UniverseDAO</code> is capable to load answers to popular questions from a data store.
 */
public interface UniverseDAO {

    /**
     * Load answer for to a specific question.
     *
     * @param question which is asked
     * @return the answer for the given question
     */
    Answer readAnswerForQuestion(Question question) throws PersistenceException;

}
