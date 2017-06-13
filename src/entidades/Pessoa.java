/*
 * Entidade pessoa, responsavel pela geracao e 
 * permanencia de produtores e consumidores
 */
package entidades;

import static disttijolo.DistTijolo.TIME;
import static disttijolo.DistTijolo.randomGenerator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maycon
 */
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
   *
   * @param C : Referencia do Caminhao
   * @param qtdepvez : Quantidade de tijolos que leva por vez
   * @param velocidade : Tempo de espera entre operacoes
   * @param nro : Numero de identificacao
   */
  public Pessoa(Caminhao C, int qtdepvez, double velocidade, int nro) {
    this.C = C;
    this.qtdepvez = qtdepvez;
    this.velocidade = velocidade;
    this.nro = nro;
  }

  @Override
  public void run() {
    try {
      //Todos os que comecam esperam porque sao gerados juntos. Ficam
      //esperando ate que monitor acione todos juntos
      C.dorme();
    } catch (InterruptedException ex) {
      Logger.getLogger(Pessoa.class.getName()).log(Level.SEVERE, null, ex);
    }
    //Comeca a trabalhar
    while (C.trabalha) { //Enquanto o monitor falar que tem o que fazer
      switch (espaco) {
        case 1:
          C.espaco1(this);
          break;
        case 2:
          C.espaco2(this);
          break;
        case 3:
          C.espaco3(this);
          break;
        case 4:
          C.espaco4(this);
          break;
        case 5:
          C.espaco5(this);
          break;
        case 6:
          C.espaco6(this);
          break;
        case 7:
          C.espaco7(this);
          break;
        case 8:
          C.espaco8(this);
          break;
        case 9:
          C.espaco9(this);
          break;
        case 10:
          C.espaco10(this);
          break;
        //Negativos
        case -1:
          C.espaco1(this);
          break;
        case -2:
          C.espaco2(this);
          break;
        case -3:
          C.espaco3(this);
          break;
        case -4:
          C.espaco4(this);
          break;
        case -5:
          C.espaco5(this);
          break;
        case -6:
          C.espaco6(this);
          break;
        case -7:
          C.espaco7(this);
          break;
        case -8:
          C.espaco8(this);
          break;
        case -9:
          C.espaco9(this);
          break;
        case -10:
          C.espaco10(this);
          break;
        default:
          //System.out.println("Thread com defeito!!");
          break;
      }
      int randomInt = descansa();
      try {
        TimeUnit.MILLISECONDS.sleep(randomInt); //Para milisegundos
      } catch (InterruptedException ex) {
        Logger.getLogger(Pessoa.class.getName()).log(Level.SEVERE, null, ex);
      }
      Thread.yield(); //Forca a troca de contexto
    }
  }

  private int descansa() {
    int T;
    switch (TIME) {
      case 0:
        //0.1 ~ 1.1
        T = (int) (((randomGenerator.nextDouble() * 1000) + 1) * velocidade);
        break;
      case 1:
        //1 ~ 3
        T = (int) (((randomGenerator.nextInt(2) + 1) * velocidade) * 1000);
        break;
      default:
        //3 ~ 5
        T = (int) (((randomGenerator.nextInt(2) + 3) * velocidade) * 1000);
        break;
    }
    return T;
  }

  @Override
  public String toString() {
    return "Pessoa{" + "qtdepvez=" + qtdepvez + ", velocidade=" + velocidade + ", espaco=" + espaco + ", nro=" + nro + "}\n";
  }
}
