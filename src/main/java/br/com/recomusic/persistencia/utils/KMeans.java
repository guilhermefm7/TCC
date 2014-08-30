package br.com.recomusic.persistencia.utils;

import java.util.ArrayList;
import java.util.List;

import br.com.recomusic.im.RetornoKMeans;
import br.com.recomusic.om.MediaUsuarioGenero;

/**
 *
 * @author Guilherme7
 */
public class KMeans
{
	public KMeans() {   }
	
	public static final int Blocos = 3;

    public static RetornoKMeans rodaAlgoritmo(List<MediaUsuarioGenero> lista)
    {
    	List<Double> valores = new ArrayList<Double>();
    	
    	for (MediaUsuarioGenero mediaUsuarioGenero : lista)
    	{
    		valores.add(mediaUsuarioGenero.getMedia());
		}
    	
    	double[][] centroides = {{0, 0, 0}, {valores.get(0), valores.get(1), valores.get(2)}};
        int tamanho = valores.size();
        double[][] divisaoBlocos = new double[Blocos][tamanho];
        int[] contadorBloco = new int[Blocos];
        int posicao;
        double primeiroBloco, blocoDiferente;
        boolean convergiu = false;
        double somaCentroide;
        int numMaxRepetições = 0;
        
        do
        {
        	//Passa para a posição 0 os valores das centroides, no final elas serão usadas a fim de verificar se o algoritmo convergiu.
            for(int i = 0; i < Blocos; i++) 
            {
                centroides[0][i] = centroides[1][i];

                contadorBloco[i] = 0;

                centroides[1][i] = 0;
            }

            for(int i = 0; i < tamanho; i++)
            {
            	//Começa setando como 0 valor do bloco. Caso este valor faça parte deste bloco, ele será inserido no final da iteração, quando passar pelas verificações.
                divisaoBlocos[0][i] = 0;
                
                //Pega o valor relativo ao primeiro bloco.
                primeiroBloco = centroides[0][0] > valores.get(i) ? centroides[0][0] - valores.get(i) : valores.get(i) - centroides[0][0];

                posicao = 0;
                
                for(int j = 1; j < Blocos; j++)
                {
                    divisaoBlocos[j][i] = 0;

                    blocoDiferente = centroides[0][j] > valores.get(i) ? centroides[0][j] - valores.get(i) : valores.get(i) - centroides[0][j];

                    //Verifica se a diferença para outro bloco é menor, se for, guarda a posição e a diferença para esse bloco para que ela seja usada para comparar com outro bloco.
                    if(blocoDiferente < primeiroBloco)
                    {
                        primeiroBloco = blocoDiferente;

                        posicao = j;
                    }
                }
                
                //Seta o valor no bloco certo e incrementa a quantidade de elementos desse bloco.
                divisaoBlocos[posicao][i] = valores.get(i);
                contadorBloco[posicao]++;
            }
            
            numMaxRepetições++;
            convergiu = true;

            for(int i = 0; i < Blocos; i++)
            {
                if(contadorBloco[i] > 0)
                {
                	somaCentroide = 0;
                    for(int j = 0; j < tamanho; j++)
                    {
                    	somaCentroide += divisaoBlocos[i][j];
                    }
                    
                    centroides[1][i] = somaCentroide/contadorBloco[i];
                }

                //Verifica se convergiu
                if(convergiu)
                {
                	if(!(centroides[0][i] == centroides[1][i]))
                	{
                		convergiu=false;
                	}
                }
            }
        }
        while(!convergiu && numMaxRepetições<150);

        RetornoKMeans ret = new RetornoKMeans(centroides, divisaoBlocos, tamanho);
        
        for(int i = 0; i < Blocos; i++)
        {
            System.out.println(centroides[1][i]);
        }
        
        return ret;
    }
}
