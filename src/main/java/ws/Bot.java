package ws;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO: Auto-generated Javadoc
/**
 * The Class Bot.
 * @param <T>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Bot <T> {
	
	/** The driver. */
	WebDriver driver;
	String url;
	/** The articles. */
	List <Stock> objects;
	//TODO: INICIALIZA LA LISTA LOOMBOK?
	

	
	/**
	 * Sleep.
	 *
	 * @param seconds the seconds
	 */
	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		}catch(InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * Scroll page.
	 *
	 * @param howMany the how many
	 * @param scrollVolume the scroll volume
	 */
	public void scrollPage(int howMany, int scrollVolume) {
		JavascriptExecutor js =(JavascriptExecutor ) this.driver;
		for(int i = 0; i< howMany; i ++) {
			js.executeScript(String.format("window.scrollBy(0,%s)", scrollVolume));
		}
		
	}
	


	/**
	 * Este metodo crea el driver, lo  setea (navegador  que usa el bot) y lo ejecuta, luego se dirige a la direccion url indicada en el metodo .get(). ahi realiza las opreciones, una vez finalizado se cierra el navegador.
	 */
	public void search() {
		WebDriver driver = new ChromeDriver();
		this.setDriver(driver);
		this.driver.get(this.getUrl());
		sleep(7);
		scrollPage(3, 2000);
	    try {
		List<WebElement> elementosHTML = driver.findElements(By.xpath("//*[@id=\"cotizaciones\"]/tbody/tr"));
		this.getInfo(elementosHTML);
	    } finally {
            driver.quit();
        }
		
		
	}
	
	/**
	 * Este metodo se encarga de ir recorriendo los WebElement obtenidos y los transforma en los objetos que necesitamos tener en nuestro backend.
	 * En cada uno de los elementos se obtiene la info a traves del relative path ej ".//etiquetaHTML".
	 *
	 * @param elementos lista de elementos html (WebElement)
	 * @return void setea automaticamente los elementos convertidos en el bot.
	 */
	public void getInfo(List<WebElement> elementos) {
		
		   System.out.println("Número de filas encontradas: " + elementos.size()); 
		   
		List<Stock> stocks =  new ArrayList<Stock>();
		
		for(WebElement s : elementos) {
			Stock info = new Stock();
			info.setTicker(s.findElement(By.xpath(".//td[1]/a/b")).getText());
			info.setPrice(s.findElement(By.xpath(".//td[2]")).getText());
			info.setDescription(s.findElement(By.xpath(".//td[1]/a/span")).getText());
			WebElement linkElement =s.findElement(By.xpath(".//td[1]/a"));
			info.setUrl(linkElement.getAttribute("href"));
			stocks.add(info);			
		}
		
		this.setObjects( stocks);
		
	}

	

	/**
	 * Show objects.
	 */
	public void showResults() {
		for (Stock a : this.objects) {
			System.out.println(a.toString());
		}
		
	}

	
	/**
	 * Save as json.
	 */
	public void saveAsJson() {
		JSONArray array = new JSONArray();
		
		for (Stock s: this.objects) {
			JSONObject obj = new JSONObject();
			obj.put("ticker", s.getTicker());
			obj.put("description", s.getDescription());
			obj.put("price", s.getPrice());
			obj.put("url", s.getUrl());
			array.put(obj);
				
		}
		
		try (FileWriter fw = new FileWriter("stocks.json")) {
			fw.write(array.toString(4));
			System.out.println("Se guardara como json");
		} catch(IOException e) {
			System.out.println(e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


}
