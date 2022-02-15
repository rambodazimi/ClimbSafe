package ca.mcgill.ecse.climbsafe.features;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty", features = "src/test/resources/InitiateAssignment.feature")
public class CucumberFeaturesTestRunner {
}