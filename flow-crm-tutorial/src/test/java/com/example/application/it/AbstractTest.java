package com.example.application.it;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.vaadin.testbench.IPAddress;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.parallel.ParallelTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.slf4j.LoggerFactory;

public abstract class AbstractTest extends ParallelTest {
    private static final String SERVER_HOST = IPAddress.findSiteLocalAddress();
    private static final int SERVER_PORT = 8080;
    private final String route;

    static {
        // Prevent debug logging from Apache HTTP client
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
    }

    //Start by invoking the Chrome WebDriverManager before any test method is invoked. TestBench doesn’t invoke the WebDriver manager.
    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup(); 
    }

    //ScreenshotOnFailureRule tells TestBench to grab a screenshot before exiting, if a test fails. This helps you understand what went wrong when tests don’t pass.
    @Rule 
    public ScreenshotOnFailureRule rule = new ScreenshotOnFailureRule(this, true);

    @Before
    public void setup() throws Exception {
        super.setup();
        //Open the browser to the correct URL before each test. For this, you need the host name where the application runs ("localhost" in development), the port the server uses (set to 8080 in application.properties), and information about the route to start from.
        getDriver().get(getURL(route)); 
    }

    protected AbstractTest(String route) {
        this.route = route;
    }

    private static String getURL(String route) {
        return String.format("http://%s:%d/%s", SERVER_HOST, SERVER_PORT, route);
    }
}