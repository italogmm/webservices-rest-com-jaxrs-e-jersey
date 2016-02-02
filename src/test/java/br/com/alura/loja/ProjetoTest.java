package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Projeto;

public class ProjetoTest {
	
	private HttpServer server;
	private WebTarget target;
	private Client client;

	@Before
	public void startaServidor() {
		this.server = Servidor.inicializaServidor();
		this.client = ClientBuilder.newClient();
		this.target = client.target("http://localhost:8080/");
	}

	@After
	public void mataServidor() {
		server.stop();
	}
	
	@Test
	public void testaQueAConexaoComOServidorFuncionaNoPathDeProjetos() {
		
		String conteudo = target.path("projetos/1").request().get(String.class);
		Projeto projeto = (Projeto) new XStream().fromXML(conteudo);
		Assert.assertEquals("Minha loja", projeto.getNome());
	}
	
	@Test
	public void testaQueSuportaNovosProjetos(){
		
		Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080");
        
        Projeto projeto = new Projeto(1l, "Meu novo projeto", 2016);
        String xml = projeto.toXML();
        
        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

        Response response = target.path("/projetos").request().post(entity);
        Assert.assertEquals(201, response.getStatus());
        String location = response.getHeaderString("Location");
        String conteudo = client.target(location).request().get(String.class);
        Assert.assertTrue(conteudo.contains("Meu novo projeto"));
	}
}
