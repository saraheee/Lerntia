package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class SimpleLerntiaService implements LerntiaService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int SLEEP_SECONDS = 2;


  //  private final LerntiaDAO lerntiaDAO;

 //   public SimpleLerntiaService(LerntiaDAO lerntiaDAO) {
 //      this.lerntiaDAO = lerntiaDAO;
 //   }


}
