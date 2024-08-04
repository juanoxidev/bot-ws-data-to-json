package ws;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
	
	public static void main(String[] args) {
		System.out.println("Ejecutando Web Scraping");

		
		
		Bot<Stock> bot = new Bot<Stock>();
		bot.setUrl("https://iol.invertironline.com/mercado/cotizaciones/argentina/acciones/panel-general");
		bot.search();
		bot.showResults();
		bot.saveAsJson();

		
	}

}
