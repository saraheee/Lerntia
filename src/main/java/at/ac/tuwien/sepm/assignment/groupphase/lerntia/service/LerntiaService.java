package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Answer;

/**
 * The <code>LerntiaService</code> is capable to calculate the answer to
 * <blockquote>Ultimate Question of Life, the Universe, and Everything</blockquote>.
 * Depending on the implementation it might take a while.
 */
public interface LerntiaService {

    /**
     * Calculate the answer to the ultimate question of life, the universe, and everything.
     *
     * @return the answer to the ultimate question of life, the universe, and everything
     */
    Answer goToExamMode() throws ServiceException;

}
