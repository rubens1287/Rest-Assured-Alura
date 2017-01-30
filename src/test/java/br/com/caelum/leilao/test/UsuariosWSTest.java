package br.com.caelum.leilao.test;

import static com.jayway.restassured.RestAssured.*;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.com.caelum.leilao.modelo.Leilao;
import br.com.caelum.leilao.modelo.Usuario;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;

public class UsuariosWSTest {
	
	private Usuario mauricio;
	private Usuario guilherme;
	private Leilao leilaoEsperado;

	@BeforeTest
	public void SetUp(){
		
		
		
		 mauricio = new Usuario(1l, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
		 guilherme = new Usuario(2l, "Guilherme Silveira", "guilherme.silveira@caelum.com.br");
		 leilaoEsperado = new Leilao(1L,"Geladeira",800.0,mauricio,false);
		 
		 //RestAssured.baseURI = "";
		 //RestAssured.port = 80;
				 
		 
	}
	
	@Test
	public void deveRetornarObjetoDeUsuariosXML(){
		XmlPath path = 
				given()
					.header("Accept", "application/xml")
					.get("/usuarios").andReturn().xmlPath();
					Usuario usuario1 = path.getObject("list.usuario[0]", Usuario.class);
					Usuario usuario2 = path.getObject("list.usuario[1]", Usuario.class);
					
					Assert.assertEquals(usuario1, mauricio);
					Assert.assertEquals(usuario2,guilherme);				
		
	}
	@Test
	public void deveRetornarListaDeUsuariosXML(){
		
		XmlPath path = 
				given()
					.header("Accept", "application/xml")
					.get("/usuarios").andReturn().xmlPath();
					List<Usuario> usuario1 = path.getList("list.usuario[0]", Usuario.class);
					List<Usuario> usuario2 = path.getList("list.usuario[1]", Usuario.class);

					Assert.assertEquals(usuario1.get(0), mauricio);
					Assert.assertEquals(usuario2.get(0),guilherme);
						
	}
	
	@Test
	public void deveRetornarObjetoDeUsuariosJSON(){
		
		JsonPath path = 
				given()
					.header("Accept", "application/json")
					.parameter("usuario.id", 1)
					.get("/usuarios/show")
					.andReturn()
					.jsonPath();
		
					Usuario usuario1 = path.getObject("usuario", Usuario.class);

					Assert.assertEquals(usuario1, mauricio);
					
					//System.out.println(path.getString("usuario.email"));
										
	}
	
	@Test
	public void deveRetornarObjetoDeLeilaoJSON(){
		
		JsonPath path = 
				given()
					.header("Accept", "application/json")
					.parameter("leilao.id", 1)
					.get("/leiloes/show")
					.andReturn()
					.jsonPath();
		
					Leilao leilao1 = path.getObject("leilao", Leilao.class);

					Assert.assertEquals(leilao1, leilaoEsperado);
					
	}
	
	@Test
	public void deveRetornarQuantidadeDeLeilaoJSON(){
		
		XmlPath path = 
				given()
					.header("Accept", "application/xml")
					.get("/leiloes/total")
					.andReturn()
					.xmlPath();
		
					int qtdLeiloes = path.getInt("/leiloes/total");
		
					Assert.assertEquals(qtdLeiloes, 6);
					
	}
	
	@Test
	public void deveadicionarUsuarioXML(){
		
		Usuario rubens = new Usuario("Rubens Lobo", "rubens@teste.com.br");
		
		XmlPath path = given()
				.header("Accept","application/xml")
				.contentType("application/xml")
				.body(rubens)
			.expect()
				.statusCode(200)
			.when()
				.post("/usuarios")
			.andReturn()
				.xmlPath();
		
		Usuario resposta = path.getObject("/usuarios", Usuario.class);
		
		Assert.assertEquals(resposta.getNome(), "Rubens Lobo");
		Assert.assertEquals(rubens.getEmail(), "rubens@teste.com.br");
		
		
					
	}
	@Test
	public void deveaDeletarUsuarioXML(){
				
		Usuario joao = new Usuario("Joao da Silva", "joao@dasilva.com");
		XmlPath retorno = given()
				.header("Accept", "application/xml")
				.contentType("application/xml")
				.body(joao)
		.expect()
			.statusCode(200)
		.when()	
			.post("/usuarios")
		.andReturn()
			.xmlPath();
		
		Usuario resposta = retorno.getObject("usuario", Usuario.class);
		Assert.assertEquals("Joao da Silva", resposta.getNome());
		Assert.assertEquals("joao@dasilva.com", resposta.getEmail());
		
		// deletando aqui 
		given()
		.contentType("application/xml").body(resposta)
		.expect().statusCode(200)
		.when().delete("/usuarios/deleta").andReturn().asString();
		
	}
	
	@Test
	public void deveaDeletarLeilaoXML(){
				
		
		XmlPath retorno = given()
				.header("Accept", "application/xml")
				.contentType("application/xml")
				.body(leilaoEsperado)
		.expect()
			.statusCode(200)
		.when()	
			.post("/leiloes")
		.andReturn()
			.xmlPath();
		
		Leilao resposta = retorno.getObject("leilao", Leilao.class);
		Assert.assertEquals("Geladeira", resposta.getNome());
		
		
	}


}
