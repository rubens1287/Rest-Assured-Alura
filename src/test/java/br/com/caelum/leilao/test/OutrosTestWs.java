package br.com.caelum.leilao.test;
import static com.jayway.restassured.RestAssured.*;
import org.testng.annotations.Test;

public class OutrosTestWs {
	@Test
	public void deveValidarHeader(){
		String sessionId = get("/cookie/teste").sessionId();		
		
		System.out.println("Sessão: " + sessionId);
		
		expect()
			.header("novo-header", "abc")
			.get("/cookie/teste");
				
		
	}
	@Test
	public void deveValidarCookie(){
				expect()
					.cookie("rest-assured","funciona")
					.get("/cookie/teste");
				
		
	}
}
