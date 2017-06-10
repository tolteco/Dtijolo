/*
 * Supervisor de atividades. Toma as decisoes de quem sobe e desce do caminhao
 */
package entidades;

import static disttijolo.DistTijolo.qtdetotal;
import static disttijolo.DistTijolo.randomGenerator;
import java.util.ArrayList;

/**
 *
 * @author Maycon
 */
public class Supervisor extends Thread implements Runnable {

  public static ArrayList<Pessoa> cima = new ArrayList();
  public static ArrayList<Pessoa> baixo = new ArrayList();
  boolean trabalhar1, trabalhar2, trabalhar;
  Caminhao C;

  @Override
  public void run() {
    //Distribuicao dos tijolos pelos espacos
    int V, j = 0;
    int Q = C.qtdetijolos;
    for (int i = C.pessoas; i > 1; i--) {
      V = (int) (Q / i);
      C.Tij[j] = V;
      C.Tot[j] = V/10;
      Q -= V;
      j++;
    }
    V = (int) Q;
    C.Tij[j] = V;
    C.Tot[j] = V/10;
    Q -= V;
    //Distribuicao das pessoas em cima ou nao do caminhao
    double D = qtdetotal / C.pessoas;
    int Pe = C.pessoas;
    double O;
    for (Pessoa pessoa : C.P) { //Sorteio para subir no caminhao
      O = randomGenerator.nextDouble();
      if (D > O && Pe > 0) {
        cima.add(pessoa);
        pessoa.espaco = cima.size(); //Metodo mais simples de controle
        D = qtdetotal / Pe;
        Pe--;
      } else {
        pessoa.espaco = -randomGenerator.nextInt(C.pessoas-1); //Aleatorio
        baixo.add(pessoa);
      }
    }
    if (Pe == C.pessoas) { //Significa que ninguem foi sorteado para subir
      cima.add(baixo.get(0));
      baixo.remove(0);
    }

    //Imprime caminhao e notifica todas as Threads para comecarem
    C.imprime(baixo.size());
    System.out.println("Tentei imprimir");
    
    /*if (!(C.acorda())) { //Funcao sincronizada para acionar notify
      System.out.println("Erro. Nao foi possivel acordar Threads");
      return;
    }*/

    //Comeca a trabalhar
    while(trabalhar){
      trabalhar1 = olhaBuffersSobre();
      trabalhar2 = olhaBuffersAbaixo();
    }
  }

  public Supervisor(Caminhao _C) {
    C = _C;
  }

  /**
   * Olha Os buffers sobre o caminhao
   * @return : Se ainda tem trabalho
   */
  private boolean olhaBuffersSobre() {
    int S = 0, i;
    for (i = 0; i < C.pessoas; i++){
      S += C.Tij[i];
    }
    C.qtdetijolos = S;
    return S != 0;
  }

  /**
   * Olha os buffers entre o caminhao e o chao
   * @return : Se ainda tem trabalho
   */
  private boolean olhaBuffersAbaixo() {
    int S = 0, i;
    for (i = 0; i < C.pessoas; i++){
      S += C.Buf[i];
      if (C.Buf[i] == 0) { //Se o buffer esta vazio
        
      }
      if (C.Buf[i] == C.qtdemax) { //Se o buffer esta cheio
        
      }
    }
    S; //Fazemos algo com S
    return true;
  }
}
