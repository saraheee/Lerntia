package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearnAlgorithmDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionLearnAlgorithm;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ILearnAlgorithmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.*;

@Component
public class LearnAlgorithmService implements ILearnAlgorithmService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private ILearnAlgorithmDAO learnAlgorithmDAO;
    private Map<Long,Integer> successMap;
    private Map<Long,Integer> failureMap;
    private Map<Long,Double> valueMap;
    private List<QuestionLearnAlgorithm> questionLearnAlgorithmList;

    @Autowired
    public LearnAlgorithmService(ILearnAlgorithmDAO learnAlgorithmDAO){
        this.learnAlgorithmDAO = learnAlgorithmDAO;
    }

    @Override
    public void updateSuccessValue(Question question) throws ServiceException {
        try {
            if (failureMap.get(question.getId()) == 0) {
                switch (successMap.get(question.getId())) {
                    case 0:
                        successMap.put(question.getId(),2);
                        break;
                    case 1:
                        successMap.put(question.getId(),3);
                        break;
                    case 2:
                        successMap.put(question.getId(), 4);
                        break;
                    case 3:
                        successMap.put(question.getId(),4);
                        break;
                    case 4:
                        successMap.put(question.getId(),5);
                        break;
                    case 5:
                        successMap.put(question.getId(),6);
                        break;
                    case 6:
                        successMap.put(question.getId(),7);
                        break;
                    case 7:
                        successMap.put(question.getId(),8);
                        break;
                    case 8:
                        successMap.put(question.getId(),9);
                        break;
                    case 9:
                        successMap.put(question.getId(),10);
                        break;
                    case 10:
                        successMap.put(question.getId(),10);
                        break;
                }
            } else {
                switch (successMap.get(question.getId())) {
                    case 0:
                        successMap.put(question.getId(),1);
                        break;
                    case 1:
                        successMap.put(question.getId(),2);
                        break;
                    case 2:
                        successMap.put(question.getId(),3);
                        break;
                    case 3:
                        successMap.put(question.getId(),4);
                        failureMap.put(question.getId(),failureMap.get(question.getId())-1);
                        break;
                    case 4:
                        successMap.put(question.getId(),5);
                        failureMap.put(question.getId(),failureMap.get(question.getId())-2);
                        break;
                    case 5:
                        successMap.put(question.getId(),6);
                        failureMap.put(question.getId(),failureMap.get(question.getId())-2);
                        break;
                    case 6:
                        successMap.put(question.getId(),7);
                        failureMap.put(question.getId(),failureMap.get(question.getId())-2);
                        break;
                    case 7:
                        successMap.put(question.getId(),8);
                        failureMap.put(question.getId(),failureMap.get(question.getId())-2);
                        break;
                    case 8:
                        successMap.put(question.getId(),9);
                        failureMap.put(question.getId(),failureMap.get(question.getId())-2);
                        break;
                    case 9:
                        successMap.put(question.getId(),10);
                        failureMap.put(question.getId(),failureMap.get(question.getId())-3);
                        break;
                    case 10:
                        failureMap.put(question.getId(),0);
                        break;
                }
            }

            if (failureMap.get(question.getId())<0){
                failureMap.put(question.getId(),0);
            }

            Double oldValue = valueMap.get(question.getId());
            Integer succesvalue =successMap.get(question.getId());
            Integer failurevalue = failureMap.get(question.getId());
            Double newValue;
            if (failurevalue<succesvalue){
                newValue = oldValue  + (succesvalue-failurevalue);
            } else {
                newValue = oldValue + 1;
            }

            if (newValue <= 0.0){
                newValue = 0.0;
            }else if (newValue >= 200.0){
                newValue = 200.0;
            }

            valueMap.put(question.getId(),oldValue*newValue);
            changeAlgorithmValues();
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void updateFailureValue(Question question) throws ServiceException {
        if (successMap.get(question.getId())==0){
            switch (failureMap.get(question.getId())){
                case 0:
                    failureMap.put(question.getId(),1);
                    break;
                case 1:
                    failureMap.put(question.getId(),2);
                    break;
                case 2:
                    failureMap.put(question.getId(),3);
                    break;
                case 3:
                    failureMap.put(question.getId(),4);
                    break;
                case 4:
                    failureMap.put(question.getId(),5);
                    break;
                case 5:
                    failureMap.put(question.getId(),6);
                    break;
                case 6:
                    failureMap.put(question.getId(),7);
                    break;
                case 7:
                    failureMap.put(question.getId(),8);
                    break;
                case 8:
                    failureMap.put(question.getId(),9);
                    break;
                case 9:
                case 10:
                    failureMap.put(question.getId(),10);
                    break;
            }
        }else {

            switch (failureMap.get(question.getId())){
                case 0:
                    failureMap.put(question.getId(),1);
                    break;
                case 1:
                    failureMap.put(question.getId(),2);
                    break;
                case 2:
                    failureMap.put(question.getId(),3);
                    break;
                case 3:
                    successMap.put(question.getId(),successMap.get(question.getId())-1);
                    failureMap.put(question.getId(),4);
                    break;
                case 4:
                    successMap.put(question.getId(),successMap.get(question.getId())-1);
                    failureMap.put(question.getId(),5);
                    break;
                case 5:
                    successMap.put(question.getId(),successMap.get(question.getId())-2);
                    failureMap.put(question.getId(),6);
                    break;
                case 6:
                    successMap.put(question.getId(),successMap.get(question.getId())-2);
                    failureMap.put(question.getId(),7);
                    break;
                case 7:
                    successMap.put(question.getId(),successMap.get(question.getId())-2);
                    failureMap.put(question.getId(),8);
                    break;
                case 8:
                    successMap.put(question.getId(),successMap.get(question.getId())-2);
                    failureMap.put(question.getId(),9);
                    break;
                case 9:
                case 10:
                    failureMap.put(question.getId(),10);
                    successMap.put(question.getId(),successMap.get(question.getId())-3);
                    break;
            }
        }
        if (successMap.get(question.getId())<0){
            successMap.put(question.getId(),0);
        }
        Double oldValue = valueMap.get(question.getId());
        Integer succesvalue =successMap.get(question.getId());
        Integer failurevalue = failureMap.get(question.getId());
        Double newValue;
        if (failurevalue>succesvalue){
            newValue = oldValue  + (failurevalue-succesvalue);
        } else {
            newValue = oldValue + 1;
        }

        if (newValue <= 0.0){
            newValue = 0.0;
        }else if (newValue >= 200.0){
            newValue = 200.0;
        }

        valueMap.put(question.getId(),oldValue*newValue);
        changeAlgorithmValues();
    }

    @Override
    public List<Long> prepareQuestionvalues(List<QuestionLearnAlgorithm> questionAlgorithmList) throws ServiceException {
        try {
            successMap = new HashMap<>();
            failureMap = new HashMap<>();
            valueMap = new HashMap<>();
            QuestionLearnAlgorithm questionLearnAlgorithm;
            questionAlgorithmList = learnAlgorithmDAO.search(questionAlgorithmList);
            while (!questionAlgorithmList.isEmpty()){
                questionLearnAlgorithm = questionAlgorithmList.get(0);
                successMap.put(questionLearnAlgorithm.getID(),questionLearnAlgorithm.getSuccessvalue());
                failureMap.put(questionLearnAlgorithm.getID(),questionLearnAlgorithm.getFailurevalue());
                valueMap.put(questionLearnAlgorithm.getID(),questionLearnAlgorithm.getPoints());
                questionAlgorithmList.remove(0);
            }
            List<Long> list = sortValueMap(valueMap);
            return list;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    //Sorts the value Map by ascending value
    private List<Long> sortValueMap(Map<Long,Double> valueMap) {
        // 1. Convert the entry Map to a List
        LinkedHashMap<Long,Double> sortedMap = new LinkedHashMap<>();
        valueMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x ->sortedMap.put(x.getKey(),x.getValue()));

        List<Long> sortedList = new ArrayList<>();
        for (Map.Entry<Long,Double> entry : sortedMap.entrySet()){
            sortedList.add(entry.getKey());
        }
        return sortedList;
    }

    @Override
    public void changeAlgorithmValues()throws ServiceException{
        try {
            if (failureMap!=null && successMap != null && valueMap!=null) {
                List<QuestionLearnAlgorithm> updatedValues = new ArrayList();
                QuestionLearnAlgorithm questionLearnAlgorithm;
                for (Map.Entry<Long, Integer> entry : successMap.entrySet()) {
                    questionLearnAlgorithm = new QuestionLearnAlgorithm();
                    questionLearnAlgorithm.setID(entry.getKey());
                    questionLearnAlgorithm.setSuccessvalue(entry.getValue());
                    questionLearnAlgorithm.setFailurevalue(failureMap.get(entry.getKey()));
                    questionLearnAlgorithm.setPoints(valueMap.get(entry.getKey()));
                    updatedValues.add(questionLearnAlgorithm);
                }
                learnAlgorithmDAO.update(updatedValues);
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    @Override
    public void shutdown() throws ServiceException {
        changeAlgorithmValues();
    }
}