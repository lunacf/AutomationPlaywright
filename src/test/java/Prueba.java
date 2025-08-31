import com.microsoft.playwright.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import org.junit.jupiter.api.*;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Prueba {

    Playwright playwright;
    List<Browser> browsers = new ArrayList<>();
    List<BrowserContext> contexts = new ArrayList<>();
    List<Page> pages = new ArrayList<>();

    String[] browserNames = {"chromium", "firefox", "webkit"};

    @BeforeAll
    void setup() {
        playwright = Playwright.create();

        for (String name : browserNames) {
            Browser browser;
            switch (name) {
                case "chromium":
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
                    break;
                case "firefox":
                    browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
                    break;
                case "webkit":
                    browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
                    break;
                default:
                    throw new IllegalArgumentException("Browser no soportado: " + name);
            }
            browsers.add(browser);
            BrowserContext context = browser.newContext();
            contexts.add(context);
            Page page = context.newPage();
            page.navigate("https://www.google.com");
            pages.add(page);
        }
    }

    @Test
    void openGoogleInAllBrowsers() {
        for (Page page : pages) {
            assertThat(page).hasTitle("Google");
        }
    }

    @AfterAll
    void tearDown() {
        for (Page page : pages) page.close();
        for (BrowserContext context : contexts) context.close();
        for (Browser browser : browsers) browser.close();
        if (playwright != null) playwright.close();
    }
}
