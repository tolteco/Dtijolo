/*
 * Monitor de atividades. Toma as decisoes de quem sobe e desce do caminhao
 */
package entidades;

import static dtijolo.DTijolo.qtdetotal;
import static dtijolo.DTijolo.randomGenerator;
import java.util.ArrayList;

public class Monitor extends Thread implements Runnable {

  public static ArrayList<Pessoa> cima = new ArrayList();
  public static ArrayList<Pessoa> baixo = new ArrayList();

  Caminhao C;

  @Override
  public void run() {
    //Distribuicao dos tijolos pelos espacos
    Integer V;
    int Q = C.qtdetijolos;
    for (int i = C.pessoas; i > 1; i++) {
      V = (int) (Q / i);
      C.Tij.add(V);
      C.Tot.add(V/10);
      Q -= V;
    }
    V = (int) Q;
    C.Tij.add(V);
    Q -= V;
    System.out.println("Q = " + Q);

    //Distribuicao das pessoas em cima ou nao do caminhao
    double D = qtdetotal / C.pessoas;
    int Pe = C.pessoas;
    double O;
    for (Pessoa pessoa : C.P) { //Sorteio para subir no caminhao
      O = randomGenerator.nextDouble();
      if (D > O) {
        cima.add(pessoa);
        pessoa.espaco = cima.size() - 1; //Metodo mais simples de controle
        Pe--;
      } else {
        baixo.add(pessoa);
        pessoa.espaco = randomGenerator.nextInt(C.pessoas-1); //Aleatorio
      }
      D = qtdetotal / Pe;
    }
    if (Pe == C.pessoas) { //Significa que ninguem foi sorteado para subir
      cima.add(baixo.get(0));
      baixo.remove(0);
    }

    //Imprime caminhao e notifica todas as Threads para comecarem
    C.imprime(baixo.size());
    if (!(C.acorda())) { //Funcao sincronizada para acionar notify
      System.out.println("Erro. Nao foi possivel acordar Threads");
      return;
    }

    //Comeca a trabalhar
  }

  public Monitor(Caminhao C) {
    this.C = C;
  }
}
