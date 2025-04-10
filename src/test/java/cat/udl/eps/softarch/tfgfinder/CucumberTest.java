package cat.udl.eps.softarch.tfgfinder;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin={"pretty"}, features="src/test/resources/features/proposal.feature")
public class CucumberTest {

}
