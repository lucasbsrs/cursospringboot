package com.lucassilva.cursospringboot.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucassilva.cursospringboot.domain.ItemPedido;
import com.lucassilva.cursospringboot.domain.PagamentoComBoleto;
import com.lucassilva.cursospringboot.domain.Pedido;
import com.lucassilva.cursospringboot.domain.enums.EstadoPagamento;
import com.lucassilva.cursospringboot.repositories.ItemPedidoRepository;
import com.lucassilva.cursospringboot.repositories.PagamentoRepository;
import com.lucassilva.cursospringboot.repositories.PedidoRepository;
import com.lucassilva.cursospringboot.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;


	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		//emailService.sendOrderConfirmationEmail(obj);
		return obj;
	}

//	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
//		UserSS user = UserService.authenticated();
//		if (user == null) {
//			throw new AuthorizationException("Acesso negado");
//		}
//		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
//		Cliente cliente = clienteService.find(user.getId());
//		return repo.findByCliente(cliente, pageRequest);
//	}

}
