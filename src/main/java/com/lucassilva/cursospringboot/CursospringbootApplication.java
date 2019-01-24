package com.lucassilva.cursospringboot;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lucassilva.cursospringboot.domain.Categoria;
import com.lucassilva.cursospringboot.domain.Cidade;
import com.lucassilva.cursospringboot.domain.Cliente;
import com.lucassilva.cursospringboot.domain.Endereco;
import com.lucassilva.cursospringboot.domain.Estado;
import com.lucassilva.cursospringboot.domain.ItemPedido;
import com.lucassilva.cursospringboot.domain.Pagamento;
import com.lucassilva.cursospringboot.domain.PagamentoComBoleto;
import com.lucassilva.cursospringboot.domain.PagamentoComCartao;
import com.lucassilva.cursospringboot.domain.Pedido;
import com.lucassilva.cursospringboot.domain.Produto;
import com.lucassilva.cursospringboot.domain.enums.EstadoPagamento;
import com.lucassilva.cursospringboot.domain.enums.TipoCliente;
import com.lucassilva.cursospringboot.repositories.CategoriaRepository;
import com.lucassilva.cursospringboot.repositories.CidadeRepository;
import com.lucassilva.cursospringboot.repositories.ClienteRepository;
import com.lucassilva.cursospringboot.repositories.EnderecoRepository;
import com.lucassilva.cursospringboot.repositories.EstadoRepository;
import com.lucassilva.cursospringboot.repositories.ItemPedidoRepository;
import com.lucassilva.cursospringboot.repositories.PagamentoRepository;
import com.lucassilva.cursospringboot.repositories.PedidoRepository;
import com.lucassilva.cursospringboot.repositories.ProdutoRepository;

@SpringBootApplication
public class CursospringbootApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(CursospringbootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");

		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);

		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);

		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));

		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "02148964711", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("11111111", "22222222"));

		Endereco end1 = new Endereco();
		end1.setId(null);
		end1.setLogradouro("Rua Flores");
		end1.setNumero("300");
		end1.setComplemento("Apto 303");
		end1.setBairro("Jardim");
		end1.setCep("93458965");
		end1.setCliente(cli1);
		end1.setCidade(c1);

		Endereco end2 = new Endereco();
		end2.setId(null);
		end2.setLogradouro("Avenida Matos");
		end2.setNumero("105");
		end2.setComplemento("Sala 800");
		end2.setBairro("Centro");
		end2.setCep("6565666");
		end2.setCliente(cli1);
		end2.setCidade(c2);

		cli1.getEnderecos().addAll(Arrays.asList(end1, end2));

		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(end1, end2));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, end1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, end2);

		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);

		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"),
				null);
		ped2.setPagamento(pagto2);

		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));

		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2.000);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);

		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));

	}

}
