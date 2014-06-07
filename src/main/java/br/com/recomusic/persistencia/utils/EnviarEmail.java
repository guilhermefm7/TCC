package br.com.recomusic.persistencia.utils;

import java.net.URL;

import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class EnviarEmail
{
	private Thread thread;
	
	public static void main(String[] args)
	{
		try 
		{
/*			// TODO Auto-generated method stub
			HtmlEmail email = new HtmlEmail();  
		       
	        // adiciona uma imagem ao corpo da mensagem e retorna seu id  
	        URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");  
	        String cid = email.embed(url, "Apache logo");     
	          
	        // configura a mensagem para o formato HTML  
	        email.setHtmlMsg("<html>Logo do Apache - <img ></html>");  
	  
	        // configure uma mensagem alternativa caso o servidor não suporte HTML  
	        email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");  
	          
	        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail  
	        email.addTo("gfmtofu@gmail.com", "Guilherme"); //destinatário  
	        email.setFrom("gfmtofu@gmail.com", "Eu"); // remetente  
	        email.setSubject("Teste -> Html Email"); // assunto do e-mail  
	        email.setMsg("Teste de Email HTML utilizando commons-email"); //conteudo do e-mail  
	        email.setAuthentication("gfmtofu@gmail.com", "789qwe");  
	        email.setSmtpPort(465);  
	        email.setSSL(true);  
	        email.setTLS(true);  
	        // envia email  
	        email.send();  */
	        
	        
	        
			HtmlEmail email = new HtmlEmail();
	        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail  
	        email.addTo("gfmtofu@gmail.com", "Guilherme"); //destinatário  
	        email.setFrom("gfmtofu@gmail.com", "Eu"); // remetente  
	        email.setSubject("Teste "); // assunto do e-mail  
	        email.setHtmlMsg("<a href=http://localhost:8080/RecoMusic/index.xhtml>Clique aqui</a> ");
	        email.setAuthentication("gfmtofu", "");  
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
