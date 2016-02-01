package br.com.alura.loja.modelo;

import com.thoughtworks.xstream.XStream;

public class Projeto {
	
	private long id;
	
	private String nome;
	
	private int anoDeInicio;
	
	public Projeto(long id, String nome, int anoDeInicio){
		this.nome = nome;
		this.id = id;
		this.anoDeInicio = anoDeInicio;
	}
	
	public Projeto(){}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public int getAnoDeInicio() {
		return anoDeInicio;
	}

	public String toXML() {
		String xml = new XStream().toXML(this);
		return xml;
	}
}
