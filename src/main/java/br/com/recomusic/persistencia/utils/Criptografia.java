package br.com.recomusic.persistencia.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptografia
{
    public Criptografia() {   }  
    
        /**
         * Recebe uma senha e transforam em um hash de 32 caracteres
         * @param senha
         * @return
         */
        public static String md5(String senha)
        {  
            String sen = "";  
            MessageDigest md = null;  
            try
            {    md = MessageDigest.getInstance("MD5");   }
            catch (NoSuchAlgorithmException e) {	e.printStackTrace();	}
            
            BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));  
            sen = hash.toString(16);   
            return sen;  
        }  
          
        public static void main(String[] args) 
        {
        	md5("1234567");
		}
}
