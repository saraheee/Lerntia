package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Answer;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

/**
 * The <code>LerntiaDAO</code> is capable to load answers to popular questions from a data store.
 */
public interface LerntiaDAO {

    /**
     * Load answer for to a specific question.
     *
     * @param question which is asked
     * @return the answer for the given question
     */
    Answer readAnswerForQuestion(Question question) throws PersistenceException;

}
