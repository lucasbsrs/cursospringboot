package com.lucassilva.cursospringboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucassilva.cursospringboot.domain.Cidade;
import com.lucassilva.cursospringboot.domain.Cliente;
import com.lucassilva.cursospringboot.domain.Endereco;
import com.lucassilva.cursospringboot.domain.enums.TipoCliente;
import com.lucassilva.cursospringboot.dto.ClienteDTO;
import com.lucassilva.cursospringboot.dto.ClienteNewDTO;
import com.lucassilva.cursospringboot.repositories.ClienteRepository;
import com.lucassilva.cursospringboot.repositories.EnderecoRepository;
import com.lucassilva.cursospringboot.services.exceptions.DataIntegrityException;
import com.lucassilva.cursospringboot.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		return repo.save(obj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relaciondas");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
	}

	public Cliente fromDTO(ClienteNewDTO clienteDTO) {
		Cliente cli = new Cliente(null, clienteDTO.getNome(), clienteDTO.getEmail(), clienteDTO.getCpfOuCnpj(),
				TipoCliente.toEnum(clienteDTO.getTipoCliente()));
		Cidade cid = new Cidade(clienteDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, clienteDTO.getLogradouro(), clienteDTO.getNumero(),
				clienteDTO.getComplemento(), clienteDTO.getBairro(), clienteDTO.getCep(), cid, cli);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(clienteDTO.getTelefone1());
		
		if(clienteDTO.getTelefone2() != null) {
			cli.getTelefones().add(clienteDTO.getTelefone2());				
		}
		
		if(clienteDTO.getTelefone3() != null) {
			cli.getTelefones().add(clienteDTO.getTelefone3());				
		}
		
		return cli;
	}
}
