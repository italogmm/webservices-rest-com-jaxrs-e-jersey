package br.com.alura.loja;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

import com.thoughtworks.xstream.XStream;

public class ClienteTest {

	private HttpServer server;
	private WebTarget target;
	private Client client;

	@Before
	public void startaServidor() {
		this.server = Servidor.inicializaServidor();
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		this.client = ClientBuilder.newClient(config);
		this.target = client.target("http://localhost:8080/");
	}

	@After
	public void mataServidor() {
		server.stop();
	}

	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {

		/**
		 * Utilizando XStream
		 */
//		String conteudo = target.path("carrinhos/1").request().get(String.class);
//		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		
		/**
		 * Utilizando JAX-B:
		 */
		Carrinho carrinho = target.path("carrinhos/1").request().get(Carrinho.class);
		
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}
	
	@Test
	public void testaQueSuportaNovosCarrinhos(){
		
        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        
        /**utilizando xstream */
//        String xml = carrinho.toXML();
//        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
//        Response response = target.path("/carrinhos").request().post(entity);
        
        Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);
        Response response = target.path("/carrinhos").request().post(entity);
        
        Assert.assertEquals(201, response.getStatus());
        String location = response.getHeaderString("Location");
        Carrinho carrinhoConsultado = client.target(location).request().get(Carrinho.class);
        Assert.assertTrue(carrinhoConsultado.getRua().contains("Rua Vergueiro"));
	}
	
	@Test()
	public void testaDeleteCarrinho(){
		
		Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Produto 1", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Goiânia");
        
        /**utilizando xstream */
//        String xml = carrinho.toXML();
//        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
//        Response response = target.path("/carrinhos").request().post(entity);
        
        /** Utilizando JAX-B */
        Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);
        Response response = target.path("/carrinhos").request().post(entity);
        
        
        Assert.assertEquals(201, response.getStatus());
        String location = response.getHeaderString("Location");
        Carrinho carrinhoIncluido = client.target(location).request().get(Carrinho.class);
        
        Response responseDelete = target.path("/carrinhos/" + carrinhoIncluido.getId()).request().delete();
        Assert.assertEquals(200, responseDelete.getStatus());
        
        Carrinho carrinhoAposDelete = (Carrinho) client.target(location).request().get(Carrinho.class);
        Assert.assertNull(carrinhoAposDelete);
	}
}
