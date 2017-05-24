/*
 * Entidade caminhao, onde a magica acontece
 */
package entidades;

import static entidades.Monitor.baixo;
import static entidades.Monitor.cima;
import java.util.ArrayList;

public class Caminhao {

  public ArrayList<Pessoa> P = new ArrayList();
  public ArrayList<Integer> Tij = new ArrayList();
  public ArrayList<Integer> Tot = new ArrayList();
  public ArrayList<Integer> Buf = new ArrayList();
  public int pessoas;
  public int qtdemax;
  public int qtdetijolos;
  public boolean trabalha = true; //Variavel que faz as threads pararem

  /**
   * Inicializador do Caminhao
   *
   * @param pessoas: Quantidade de pessoas que podem subir no caminhao
   * @param qtdemax: Quantidade maxima de tijolos por espaco
   * @param qtdetijolos: Quantidade de tijolos sobre o caminhao que deve ser
   * descarregada
   */
  public Caminhao(int pessoas, int qtdemax, int qtdetijolos) {
    this.pessoas = pessoas;
    this.qtdemax = qtdemax;
    this.qtdetijolos = qtdetijolos;
  }

  /**
   * Funcao de Impressao do caminhao em console
   *
   * @param B : Quantidade de pessoas embaixo
   */
  public void imprime(int B) {
    //Primeira linha
    System.out.println("----------------------------------");
    //Segunda linha
    System.out.print("-");
    for (int i = 0; i < Tij.size(); i++) {
      int K = Tij.get(i) / Tot.get(i);
      for (int j = 10; j > K; j--) {
        System.out.print(" ");
      }
      for (int j = 0; j < K; j++) {
        System.out.print("T");
      }
      System.out.print("-");
    }
    //Terceira linha
    System.out.print("\n-");
    for (int i = 0; i < pessoas; i++) {
      System.out.print("          -");
    }
    //Quarta linha
    System.out.print("\n-");
    int j = 0;
    for (int i = 0; i < pessoas; i++) {
      System.out.print("    ");
      if (cima.get(j).espaco == i) {
        System.out.print("P" + cima.get(j).nro);
        j++;
      } else {
        System.out.print("  ");
      }
      System.out.print("    -");
    }
    //Quinta linha
    System.out.print("\n-");
    j = 0;
    for (int i = 0; i < pessoas; i++) {
      System.out.print(" ");
      if (cima.get(j).espaco == i) {
        System.out.print("V=" + String.format("%.4f", cima.get(j).velocidade));
        j++;
      } else {
        System.out.print("        ");
      }
      System.out.print(" -");
    }
    //Sexta linha
    System.out.print("\n-");
    j = 0;
    for (int i = 0; i < pessoas; i++) {
      System.out.print(" ");
      if (cima.get(j).espaco == i) {
        if (cima.get(j).qtdepvez < 10) {
          System.out.print("C=" + cima.get(j).qtdepvez + "      ");
        } else {
          System.out.print("C=" + cima.get(j).qtdepvez + "     ");
        }
        j++;
      } else {
        System.out.print("          ");
      }
      System.out.print("-");
    }
    //Sabado linha
    System.out.print("\n-");
    j = 0;
    for (int i = 0; i < pessoas; i++) {
      System.out.print(" ");
      if (cima.get(j).espaco == i) {

        if (cima.get(j).notificado) {
          System.out.print("N");
        } else {
          System.out.print(" ");
        }
        System.out.print("     ");
        if (cima.get(j).resposta) {
          System.out.print("A");
        } else {
          System.out.print(" ");
        }
        System.out.print(" ");
        j++;
      } else {
        System.out.print("         ");
      }
      System.out.print("-");
    }
    //Oitava linha
    System.out.print("\n-");
    for (int i = 0; i < pessoas; i++) {
      System.out.print("          -");
    }
    //Nona linha
    System.out.print("\n-");
    for (int i = 0; i < Buf.size(); i++) {
      for (j = 20; j > 10; j--) {
        if (Buf.get(i) > j) {
          System.out.print("T");
        } else {
          System.out.print(" ");
        }
      }
      System.out.print("-");
    }
    //Decima e decima primeira linhas
    System.out.print("\n-");
    for (int i = 0; i < Buf.size(); i++) {
      for (j = 10; j > 0; j--) {
        if (Buf.get(i) > j) {
          System.out.print("T");
        } else {
          System.out.print(" ");
        }
      }
      System.out.print("-");
    }
    System.out.print("\n\n-");
    //Decima segunda linha
    for (int i = 0; i < baixo.size(); i++) {
      System.out.print("---");
      System.out.print(baixo.get(i).espaco + "P" + baixo.get(i).nro);
      System.out.print("-----");
    }
    //Decima terceira linha
    System.out.print("\n-");
    for (int i = 0; i < baixo.size(); i++) {
      System.out.print(" ");
      System.out.print("V=" + String.format("%.4f", baixo.get(i).velocidade));
      System.out.print(" -");
    }
    //Decima quarta linha
    System.out.print("\n-");
    for (int i = 0; i < baixo.size(); i++) {
      System.out.print(" ");
      if (baixo.get(i).qtdepvez < 10) {
        System.out.print("C=" + baixo.get(i).qtdepvez + "      ");
      } else {
        System.out.print("C=" + baixo.get(i).qtdepvez + "     ");
      }
      System.out.print("          ");
      System.out.print("-");
    }
    //Decima quinta linha
    System.out.print("\n-");
    for (int i = 0; i < baixo.size(); i++) {
      System.out.print(" ");
      if (baixo.get(i).notificado) {
        System.out.print("N");
      } else {
        System.out.print(" ");
      }
      System.out.print("     ");
      if (baixo.get(i).resposta) {
        System.out.print("A");
      } else {
        System.out.print(" ");
      }
      System.out.print(" -");
    }
    //Decima sexta linha
    System.out.print("\n-");
    for (int i = 0; i < baixo.size(); i++) {
      System.out.print("-----------");
    }
    System.out.print("\n");
  }

  /**
   * Acorda pessoas para comecar trabalho
   *
   * @return : True se conseguir. (Retorno necessario por causa do synchronized)
   */
  public synchronized boolean acorda() {
    notifyAll();
    return true;
  }

  public synchronized boolean espaco1() {
    return true;
  }

}
