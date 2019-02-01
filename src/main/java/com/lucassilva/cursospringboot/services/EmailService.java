package com.lucassilva.cursospringboot.services;

import com.lucassilva.cursospringboot.domain.Cliente;
import com.lucassilva.cursospringboot.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);

	//void sendEmail(SimpleMailMessage msg);

	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
