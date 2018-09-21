package br.gov.mctic.sisgesac.automacao.core;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WDS {

    private static WebDriver driver;

    private static WebDriverWait wait;

    public static WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(get(), 10);
        }
        return wait;
    }

    public static WebDriver get() {
        if (driver == null) {
            String extensao = "";
            if (System.getProperty("os.name").startsWith("Win")) {
                extensao = ".exe";
            }
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver" + extensao);
            driver = new ChromeDriver();
            driver.get(PropriedadeUtils.get("pagina_inicial"));
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            delay();
            driver.manage().window().maximize();
            executarLogin();
        }
        return driver;
    }

    private static void executarLogin() {
        try {
            Class<?> c = Class.forName(PropriedadeUtils.get("classe_login"));
            Method method = c.getMethod(PropriedadeUtils.get("metodo_login"));
            method.invoke(c.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delay(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void delay() {
        delay(2000);
    }

    public static void fecharUltimaAba() {
        Set<String> abas = WDS.get().getWindowHandles();
        String ultimaAba = "";
        for (String aba : abas) {
            ultimaAba = aba;
        }
        WDS.get().switchTo().window(ultimaAba);
        WDS.get().close();
        abas = WDS.get().getWindowHandles();
        ultimaAba = "";
        for (String aba : abas) {
            ultimaAba = aba;
        }
        WDS.get().switchTo().window(ultimaAba);
    }

}
