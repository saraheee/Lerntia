package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnswerController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
