package br.com.recomusic.aplicacao;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.recomusic.persistencia.utils.ThreadProcuraMusica;
import br.com.recomusic.singleton.ConectaBanco;

/**
 * Servlet implementation class MainRecMusic
 */
@WebServlet("/MainRecMusic")
public class MainRecMusic extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException 
	{
		//Conecta no Banco de Dados
		System.out.println("RecMusic Incializado com Sucesso!");
		ThreadProcuraMusica tph = new ThreadProcuraMusica();
		Thread t = new Thread(tph);
        t.start();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() 
	{
		ConectaBanco.getInstance().close();
		System.out.println("RecMusic Fechado com Sucesso!");
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() 
	{
		// TODO Auto-generated method stub
		return null; 
	}
	
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
	}
}
