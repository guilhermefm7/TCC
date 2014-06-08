package br.com.recomusic.persistencia.utils;

import org.apache.commons.mail.HtmlEmail;

public class EnviarEmail
{
	public EnviarEmail() {   }  
	
	/**
	 * Envia um email através dos parâmetros recebidos
	 * @param emailDestino
	 * @param assunto
	 * @param mensagem
	 */
	public void enviaEmail(String emailDestino, String assunto, String mensagem, String usuario)
	{
		try 
		{
			// TODO Auto-generated method stub
			HtmlEmail email = new HtmlEmail();
	        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail  
	        email.addTo(emailDestino, usuario); //destinatário  
	        email.setFrom("recomusiccontrole@gmail.com", "RecoMusic"); // remetente  
	        email.setSubject(assunto); // assunto do e-mail  
	        email.setHtmlMsg(mensagem);
	        email.setAuthentication("recomusiccontrole", "recomusic123");  
	        email.setSmtpPort(465);  
	        email.setSSL(true);  
	        email.setTLS(true);  
	        email.send();     
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			return;
		}
	}

}
