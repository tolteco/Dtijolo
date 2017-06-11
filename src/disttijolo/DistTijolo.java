/*
 * Exemplo de problema com threads usando o descarregamento de tijolos
 */
package disttijolo;

import entidades.*;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Maycon
 */
public class DistTijolo {

  public static Random randomGenerator;
  public static int qtdetotal;
  public static int TIME;

  public static void main(String[] args) {
    //Inicia variaveis "estaticas"
    Caminhao cam = new Caminhao(0, 20, 0); //Para alterar a quantidade maxima 
    //de tijolos por espaco, trocar valor do meio
    randomGenerator = new Random();
    Scanner input = new Scanner(System.in);

    //Faz perguntas
    System.out.println("Quantidade total de tijolos:");
    cam.qtdetijolos = input.nextInt();
    if (cam.qtdetijolos <= 0) {
      System.out.println("Erro - Valor Invalido.");
      return;
    }
    System.out.println("Quantidade de pessoas sobre o caminhao (1~10):");
    cam.pessoas = input.nextInt();
    if (cam.pessoas <= 0) {
      System.out.println("Erro - Valor Invalido.");
      return;
    }
    System.out.println("Quantidade total de pessoas:");
    qtdetotal = input.nextInt();
    if (qtdetotal <= 0) {
      System.out.println("Erro - Valor Invalido.");
      return;
    }
    System.out.println("Velocidade de execucao: (0 - Rapida (0.1-1.1s), 1 - Media (1-3s), 2 - Lenta (3-5s):");
    TIME = input.nextInt();
    if (TIME != 0 && TIME != 1 && TIME != 2) {
      System.out.println("Erro - Valor Invalido.");
      return;
    }
    System.out.println("Etrada de pessoas: 0 - Manual, 1 - Aleatorio");
    int o = input.nextInt();
    switch (o) {
      case 0: //Entrada Manual
        manualEntry(cam);
        break;
      case 1: //Entrada Aleatoria
        randomEntry(cam);
        break;
      default:
        System.out.println("Erro - Valor Invalido.");
        return;
    }
    //Inicia Monitor
    Supervisor M = new Supervisor(cam);
    Thread b = new Thread(M);
    b.start();
  }

  /**
   * Entrada manual
   *
   * @param cam : Caminhao
   */
  private static void manualEntry(Caminhao cam) {
    Scanner input = new Scanner(System.in);
    double V;
    int Q;
    for (int i = 0; i < qtdetotal; i++) {
      System.out.println("------Pessoa " + (i + 1) + "------");
      System.out.println("Velocidade (>0 - Mais rapida, 1 - Mais lenta (0~1)):");
      V = input.nextDouble();
      if (V <= 0 || V > 1) {
        System.out.println("Erro - Valor Invalido.");
        return;
      }
      System.out.println("Quantidade (1~9):");
      Q = input.nextInt();
      if (Q <= 0 || Q > 9) {
        System.out.println("Erro - Valor Invalido.");
        return;
      }
      Pessoa P = new Pessoa(cam, Q, V, i);
      cam.P.add(P);
      Thread a = new Thread(P);
      a.start(); //Inicia Thread
    }
  }

  /**
   * Entrada Aleatoria
   *
   * @param cam : Caminhao
   */
  private static void randomEntry(Caminhao cam) {
    double V;
    int Q;
    for (int i = 0; i < qtdetotal; i++) {
      Q = randomGenerator.nextInt(9) + 1;
      V = randomGenerator.nextDouble();
      Pessoa P = new Pessoa(cam, Q, V, i);
      cam.P.add(P);
      Thread a = new Thread(P);
      a.start(); //Inicia Thread
    }
  }
}
