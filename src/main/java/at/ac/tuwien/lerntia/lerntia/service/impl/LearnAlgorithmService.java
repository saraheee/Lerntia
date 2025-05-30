package at.ac.tuwien.lerntia.lerntia.service.impl;

import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dao.ILearnAlgorithmDAO;
import at.ac.tuwien.lerntia.lerntia.dto.Question;
import at.ac.tuwien.lerntia.lerntia.dto.QuestionLearnAlgorithm;
import at.ac.tuwien.lerntia.lerntia.service.ILearnAlgorithmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.*;

@Component
public class LearnAlgorithmService implements ILearnAlgorithmService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ILearnAlgorithmDAO learnAlgorithmDAO;
    private Map<Long, Integer> successMap;
    private Map<Long, Integer> failureMap;
    private Map<Long, Double> valueMap;

    @Autowired
    public LearnAlgorithmService(ILearnAlgorithmDAO learnAlgorithmDAO) {
        this.learnAlgorithmDAO = learnAlgorithmDAO;
    }

    @Override
    public void updateSuccessValue(Question question) throws ServiceException {
        try {
            LOG.debug("Update success value of the current question");
            if (failureMap.get(question.getId()) == 0) {
                switch (successMap.get(question.getId())) {
                    case 0:
                        successMap.put(question.getId(), 2);
                        break;
                    case 1:
                        successMap.put(question.getId(), 3);
                        break;
                    case 2:
                        successMap.put(question.getId(), 4);
                        break;
                    case 3:
                        successMap.put(question.getId(), 4);
                        break;
                    case 4:
                        successMap.put(question.getId(), 5);
                        break;
                    case 5:
                        successMap.put(question.getId(), 6);
                        break;
                    case 6:
                        successMap.put(question.getId(), 7);
                        break;
                    case 7:
                        successMap.put(question.getId(), 8);
                        break;
                    case 8:
                        successMap.put(question.getId(), 9);
                        break;
                    case 9:
                        successMap.put(question.getId(), 10);
                        break;
                    case 10:
                        successMap.put(question.getId(), 10);
                        break;
                    default:
                        successMap.put(question.getId(), 0);
                        break;
                }
            } else {
                switch (successMap.get(question.getId())) {
                    case 0:
                        successMap.put(question.getId(), 1);
                        break;
                    case 1:
                        successMap.put(question.getId(), 2);
                        break;
                    case 2:
                        successMap.put(question.getId(), 3);
                        break;
                    case 3:
                        successMap.put(question.getId(), 4);
                        failureMap.put(question.getId(), failureMap.get(question.getId()) - 1);
                        break;
                    case 4:
                        successMap.put(question.getId(), 5);
                        failureMap.put(question.getId(), failureMap.get(question.getId()) - 2);
                        break;
                    case 5:
                        successMap.put(question.getId(), 6);
                        failureMap.put(question.getId(), failureMap.get(question.getId()) - 2);
                        break;
                    case 6:
                        successMap.put(question.getId(), 7);
                        failureMap.put(question.getId(), failureMap.get(question.getId()) - 2);
                        break;
                    case 7:
                        successMap.put(question.getId(), 8);
                        failureMap.put(question.getId(), failureMap.get(question.getId()) - 2);
                        break;
                    case 8:
                        successMap.put(question.getId(), 9);
                        failureMap.put(question.getId(), failureMap.get(question.getId()) - 2);
                        break;
                    case 9:
                        successMap.put(question.getId(), 10);
                        failureMap.put(question.getId(), failureMap.get(question.getId()) - 3);
                        break;
                    case 10:
                        failureMap.put(question.getId(), 0);
                        break;
                    default:
                        successMap.put(question.getId(), 0);
                        break;
                }
            }
            LOG.debug("Determine new point value of the question.");
            if (failureMap.get(question.getId()) < 0) {
                failureMap.put(question.getId(), 0);
            }
            Double oldValue = valueMap.get(question.getId());
            Integer successValue = successMap.get(question.getId());
            Integer failureValue = failureMap.get(question.getId());
            Double newValue;
            if (failureValue < successValue) {
                newValue = oldValue + (successValue - failureValue);
            } else {
                newValue = oldValue + 1;
            }
            if (newValue <= 0.0) {
                newValue = 0.0;
            } else if (newValue >= 200.0) {
                newValue = 200.0;
            }
            valueMap.put(question.getId(), newValue);
            changeAlgorithmValues();
            LOG.debug("Update the value in the value-map.");
        } catch (Exception e) {
            throw new ServiceException("Exception");
        }
    }

    @Override
    public void updateFailureValue(Question question) throws ServiceException {
        LOG.debug("Update the failure of the current question.");
        if (successMap == null) {
            throw new ServiceException("Success map of the algorithm is null!");
        }
        if (successMap.get(question.getId()) == 0) {
            switch (failureMap.get(question.getId())) {
                case 0:
                    failureMap.put(question.getId(), 1);
                    break;
                case 1:
                    failureMap.put(question.getId(), 2);
                    break;
                case 2:
                    failureMap.put(question.getId(), 3);
                    break;
                case 3:
                    failureMap.put(question.getId(), 4);
                    break;
                case 4:
                    failureMap.put(question.getId(), 5);
                    break;
                case 5:
                    failureMap.put(question.getId(), 6);
                    break;
                case 6:
                    failureMap.put(question.getId(), 7);
                    break;
                case 7:
                    failureMap.put(question.getId(), 8);
                    break;
                case 8:
                    failureMap.put(question.getId(), 9);
                    break;
                case 9:
                case 10:
                    failureMap.put(question.getId(), 10);
                    break;
                default:
                    failureMap.put(question.getId(), 0);
                    break;
            }
        } else {
            switch (failureMap.get(question.getId())) {
                case 0:
                    failureMap.put(question.getId(), 1);
                    break;
                case 1:
                    failureMap.put(question.getId(), 2);
                    break;
                case 2:
                    failureMap.put(question.getId(), 3);
                    break;
                case 3:
                    successMap.put(question.getId(), successMap.get(question.getId()) - 1);
                    failureMap.put(question.getId(), 4);
                    break;
                case 4:
                    successMap.put(question.getId(), successMap.get(question.getId()) - 1);
                    failureMap.put(question.getId(), 5);
                    break;
                case 5:
                    successMap.put(question.getId(), successMap.get(question.getId()) - 2);
                    failureMap.put(question.getId(), 6);
                    break;
                case 6:
                    successMap.put(question.getId(), successMap.get(question.getId()) - 2);
                    failureMap.put(question.getId(), 7);
                    break;
                case 7:
                    successMap.put(question.getId(), successMap.get(question.getId()) - 2);
                    failureMap.put(question.getId(), 8);
                    break;
                case 8:
                    successMap.put(question.getId(), successMap.get(question.getId()) - 2);
                    failureMap.put(question.getId(), 9);
                    break;
                case 9:
                case 10:
                    failureMap.put(question.getId(), 10);
                    successMap.put(question.getId(), successMap.get(question.getId()) - 3);
                    break;
                default:
                    failureMap.put(question.getId(), 0);
                    break;
            }
        }
        LOG.debug("Determine new point value of the failed question.");
        if (successMap.get(question.getId()) < 0) {
            successMap.put(question.getId(), 0);
        }
        Double oldValue = valueMap.get(question.getId());
        Integer successValue = successMap.get(question.getId());
        Integer failureValue = failureMap.get(question.getId());
        Double newValue;
        if (failureValue > successValue) {
            newValue = oldValue - (failureValue - successValue);
        } else {
            newValue = oldValue - 1;
        }

        if (newValue <= 0.0) {
            newValue = 0.0;
        } else if (newValue >= 200.0) {
            newValue = 200.0;
        }
        valueMap.put(question.getId(), newValue);
        LOG.debug("New value has been added to the value-map.");
        changeAlgorithmValues();
    }

    @Override
    public List<Long> prepareQuestionValues(List<QuestionLearnAlgorithm> questionAlgorithmList) throws ServiceException {
        try {
            LOG.debug("Prepare question values of selected learn questionnaire.");
            successMap = new HashMap<>();
            failureMap = new HashMap<>();
            valueMap = new HashMap<>();
            QuestionLearnAlgorithm questionLearnAlgorithm;
            questionAlgorithmList = learnAlgorithmDAO.search(questionAlgorithmList);
            while (!questionAlgorithmList.isEmpty()) {
                questionLearnAlgorithm = questionAlgorithmList.get(0);
                successMap.put(questionLearnAlgorithm.getID(), questionLearnAlgorithm.getSuccessValue());
                failureMap.put(questionLearnAlgorithm.getID(), questionLearnAlgorithm.getFailureValue());
                valueMap.put(questionLearnAlgorithm.getID(), questionLearnAlgorithm.getPoints());
                questionAlgorithmList.remove(0);
            }
            List<Long> list = sortValueMap(valueMap);
            LOG.debug("All learn algorithm values have been found and added.");
            return list;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public List<Long> sortValueMap(Map<Long, Double> valueMap) {
        LOG.debug("Convert entry map to array");
        LOG.debug("Sort question values have been initiated.");
        LinkedHashMap<Long, Double> sortedMap = new LinkedHashMap<>();
        valueMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        List<Long> sortedList = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : sortedMap.entrySet()) {
            sortedList.add(entry.getKey());
        }
        LOG.info("Question values have been sorted successfully.");
        return sortedList;
    }

    @Override
    public void changeAlgorithmValues() throws ServiceException {
        try {
            LOG.debug("Prepare all the new values of the questionnaire questions for the database");
            if (failureMap != null && successMap != null && valueMap != null) {
                List<QuestionLearnAlgorithm> updatedValues = new ArrayList<>();
                QuestionLearnAlgorithm questionLearnAlgorithm;
                for (Map.Entry<Long, Integer> entry : successMap.entrySet()) {
                    questionLearnAlgorithm = new QuestionLearnAlgorithm();
                    questionLearnAlgorithm.setID(entry.getKey());
                    questionLearnAlgorithm.setSuccessValue(entry.getValue());
                    questionLearnAlgorithm.setFailureValue(failureMap.get(entry.getKey()));
                    questionLearnAlgorithm.setPoints(valueMap.get(entry.getKey()));
                    updatedValues.add(questionLearnAlgorithm);
                }
                learnAlgorithmDAO.update(updatedValues);
                LOG.debug("All algorithm values have been successfully sent to the next layer.");
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e.getCustomMessage());
        }

    }

    @Override
    public void shutdown() throws ServiceException {
        LOG.debug("Learn algorithm shutdown initiated");
        changeAlgorithmValues();
    }
}