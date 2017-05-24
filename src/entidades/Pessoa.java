/*
 * Entidade pessoa, responsavel pela geracao e 
 * permanencia de produtores e consumidores
 */
package entidades;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Pessoa extends Thread implements Runnable {
  public int qtdepvez; //Quantidade de tijolos que carrega por vez
  public double velocidade; //Velocidade de trabalho (tempo para sleep)
  public int espaco; //Espaco que essa Thread esta cuidando. Se for negativo 
  //e porque esta embaixo do caminhao, se for positivo e porque esta em cima
  public int nro; //Identificador da pessoa
  public boolean notificado = false;
  public boolean resposta = false;
  
  Caminhao C; //Caminhao para referencia

  /**
   * Inicializador de pessoas
   * @param C : Referencia do Caminhao
   * @param qtdepvez : Quantidade de tijolos que leva por vez
   * @param velocidade : Tempo de espera entre operacoes
   */
  public Pessoa(Caminhao C, int qtdepvez, double velocidade, int nro) {
    this.C = C;
    this.qtdepvez = qtdepvez;
    this.velocidade = velocidade;
    this.nro = nro;
  }
  
  @Override
  public void run() {
    try { //Todos os que comecam esperam porque sao gerados juntos. Ficam
      //esperando ate que monitor acione todos juntos
      wait();
    } catch (InterruptedException ex) {
      Logger.getLogger(Pessoa.class.getName()).log(Level.SEVERE, null, ex);
    }
    //Comeca a trabalhar
    while (C.trabalha){ //Enquanto o monitor falar que tem o que fazer
      
    }
  }
}
