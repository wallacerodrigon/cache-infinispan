package com.algaworks.gerenciador.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.algaworks.gerenciador.model.Status;
import com.algaworks.gerenciador.model.Usuario;

@Named
@ViewScoped
public class PesquisaUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long codigoUsuario;
	
	private Usuario usuario;
	
	@Inject
	private EntityManager manager;

	private List<Usuario> usuarios;
	
	public void pesquisar() {
		Session session = this.manager.unwrap(Session.class);
		this.usuario = (Usuario) session.createCriteria(Usuario.class)
						.setFetchMode("grupos", FetchMode.JOIN)
						.add(Restrictions.eq("codigo", this.codigoUsuario))
						.setCacheable(true)
						.uniqueResult();
	}
	
	public void listarTudo() {
		Session session = this.manager.unwrap(Session.class);
		this.setUsuarios(session.createCriteria(Usuario.class)
						.setCacheable(true)
						.list());
		
	}
	
	@Transactional(value=TxType.REQUIRES_NEW)
	public void mudarStatus() {
		this.usuario.setStatus(this.usuario.
				getStatus().equals(Status.ATIVO) ? Status.INATIVO : Status.ATIVO);
		this.manager.merge(this.usuario);
	}
	
	public Long getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Long codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
}
