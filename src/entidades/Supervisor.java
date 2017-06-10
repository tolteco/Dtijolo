/*
 * Supervisor de atividades. Toma as decisoes de quem sobe e desce do caminhao
 */
package entidades;

import static disttijolo.DistTijolo.TIME;
import static disttijolo.DistTijolo.qtdetotal;
import static disttijolo.DistTijolo.randomGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maycon
 */
public class Supervisor extends Thread implements Runnable {

  public static ArrayList<Pessoa> cima = new ArrayList();
  public static ArrayList<Pessoa> baixo = new ArrayList();
  boolean trabalhar1, trabalhar2, trabalhar = true;
  Caminhao C;

  @Override
  public void run() {
    //Distribuicao dos tijolos pelos espacos
    int V, j = 0;
    int Q = C.qtdetijolos;
    for (int i = C.pessoas; i > 1; i--) {
      V = (int) (Q / i);
      C.Tij[j] = V;
      C.Tot[j] = V / 10;
      Q -= V;
      j++;
    }
    V = (int) Q;
    C.Tij[j] = V;
    C.Tot[j] = V / 10;
    Q -= V;
    System.out.println("Tij = " + Arrays.toString(C.Tij) + ". Tot = " + Arrays.toString(C.Tot));
    //Distribuicao das pessoas em cima ou nao do caminhao
    double D = C.pessoas / qtdetotal;
    int Pe = C.pessoas;
    double O;
    for (Pessoa pessoa : C.P) { //Sorteio para subir no caminhao
      O = randomGenerator.nextDouble();
      if (D > O && Pe > 0) {
        cima.add(pessoa);
        pessoa.espaco = cima.size(); //Metodo mais simples de controle
        D = Pe / qtdetotal;
        Pe--;
      } else {
        if (C.pessoas == 1) {
          pessoa.espaco = 0;
          baixo.add(pessoa);
        } else {
          pessoa.espaco = -randomGenerator.nextInt(C.pessoas - 1) + 1; //Aleatorio
          baixo.add(pessoa);
        }
      }
    }
    if (Pe == C.pessoas) { //Significa que ninguem foi sorteado para subir
      baixo.get(0).espaco = -baixo.get(0).espaco;
      cima.add(baixo.get(0));
      baixo.remove(0);
    }
    for (int z = 0; z < C.pessoas; z++) {
      if (!procuraProdutor(z)) {
        Pessoa P = new Pessoa(C, 0, 0.0, -1);
        P.espaco = 11;
        cima.add(P);
      }
    }

    //Imprime caminhao e notifica todas as Threads para comecarem
    C.imprime(baixo.size());

    if (!(C.acorda())) { //Funcao sincronizada para acionar notify
      System.out.println("Erro. Nao foi possivel acordar Threads");
      return;
    }
    //Comeca a trabalhar
    while (trabalhar) {
      int T = descansa();
      try {
        TimeUnit.MILLISECONDS.sleep(T); //Para milisegundos
      } catch (InterruptedException ex) {
        Logger.getLogger(Supervisor.class.getName()).log(Level.SEVERE, null, ex);
      }
      trabalhar1 = olhaBuffersSobre();
      trabalhar2 = olhaBuffersAbaixo();
      C.imprime(baixo.size());
      Thread.yield(); //Forca a troca de contexto
    }
  }

  public Supervisor(Caminhao _C) {
    C = _C;
  }

  /**
   * Olha Os buffers sobre o caminhao
   *
   * @return : Se ainda tem trabalho
   */
  private boolean olhaBuffersSobre() {
    int S = 0, i;
    for (i = 0; i < C.pessoas; i++) {
      S += C.Tij[i];
    }
    C.qtdetijolos = S;
    return S != 0;
  }

  /**
   * Olha os buffers entre o caminhao e o chao
   *
   * @return : Se ainda tem trabalho
   */
  private boolean olhaBuffersAbaixo() {
    int S = 0, i;
    for (i = 0; i < C.pessoas; i++) {
      S += C.Buf[i];
      if (C.Buf[i] == 0) { //Se o buffer esta vazio
        //Significa que o produtor e inexistente ou que esta muito devagar
        if (procuraProdutor(i)) { //Produtor Lerdo
          trocaProdutorL(i);
        } else { //Produtor inexistente
          colocaProdutor(i);
        }
      }
      if (C.Buf[i] == C.qtdemax) { //Se o buffer esta cheio
        //Significa que o produtor e muito rapido ou que no tem ninguem olhando esse setor debaixo
        int SU = contaConsumidores(i);
        if (SU != 0) { //Produtor muito rapido (ou poucos consumidores)
          if (SU < (C.pessoas / qtdetotal)) { //Se a quantidade de pessoas nao esta igualmente distribuida (poucos consumidores)
            colocaPessoas(SU, i);
          } else { //Se esta distribuido (Produtor Rapido)
            trocaProdutorR(i);
          }
        } else { //Consumidores inexistentes
          colocaPessoas(0, i);
        }
      }
    }
    return !(S == 0 && trabalhar1);
  }

  /**
   * Procura produtor no espaco i
   *
   * @param i : espaco
   * @return : Produtor encontrado
   */
  private boolean procuraProdutor(int i) {
    for (Pessoa pessoa : cima) {
      if (pessoa.espaco == i) {
        return true;
      }
    }
    return false;
  }

  /**
   * Coloca um produtor no espaco i (Considera que nao tem ninguem la)
   *
   * @param i : espaco
   */
  private void colocaProdutor(int i) {
    int T = randomGenerator.nextInt(baixo.size() - 1);
    baixo.get(T).espaco = i;
    cima.add(baixo.get(T));
    baixo.remove(T);
  }

  /**
   * Conta a quantidade de consumidores para o espaco i
   *
   * @param i : espaco
   * @return : quantidade encontrada de consumidores
   */
  private int contaConsumidores(int i) {
    int S = 0;
    for (Pessoa pessoa : baixo) {
      if (pessoa.espaco == -i) {
        S++;
      }
    }
    return S;
  }

  /**
   * Coloca pessoas de acordo com uma distribuicao no espaco i
   *
   * @param SU : quantidade atual de pessoas
   * @param i : espaco
   */
  private void colocaPessoas(int SU, int i) {
    while (SU < (C.pessoas / qtdetotal)) { //Enquanto nao tem pessoas que chegue
      int T = randomGenerator.nextInt(baixo.size() - 1);
      if (baixo.get(T).espaco != -i) {
        baixo.get(T).espaco = -i;
        SU++;
      }
    }
  }

  /**
   * Troca produtor lerdo
   *
   * @param i : espaco
   */
  private void trocaProdutorL(int i) {
    int V = baixo.get(0).qtdepvez;
    int I = 0;
    for (int j = 1; j < baixo.size(); j++) {
      if (baixo.get(j).qtdepvez < V && baixo.get(j).qtdepvez > cima.get(i).qtdepvez) {
        V = baixo.get(j).qtdepvez;
        I = j;
      }
    }
    cima.get(i).espaco = -cima.get(i).espaco;
    baixo.get(I).espaco = -baixo.get(I).espaco;
    cima.add(baixo.get(I));
    baixo.remove(I);
    baixo.add(cima.get(i));
    cima.remove(i);
  }

  /**
   * Troca produtor rapido
   *
   * @param i : espaco
   */
  private void trocaProdutorR(int i) {
    int V = baixo.get(0).qtdepvez;
    int I = 0;
    for (int j = 1; j < baixo.size(); j++) {
      if (baixo.get(j).qtdepvez > V && baixo.get(j).qtdepvez < cima.get(i).qtdepvez) {
        V = baixo.get(j).qtdepvez;
        I = j;
      }
    }
    cima.get(i).espaco = -cima.get(i).espaco;
    baixo.get(I).espaco = -baixo.get(I).espaco;
    cima.add(baixo.get(I));
    baixo.remove(I);
    baixo.add(cima.get(i));
    cima.remove(i);
  }

  private int descansa() {
    int T;
    switch (TIME) {
      case 0:
        //0.1 ~ 1.1
        T = 1000;
        break;
      case 1:
        //1 ~ 3
        T = 1000;
        break;
      default:
        //3 ~ 5
        T = 5000;
        break;
    }
    return T;
  }
}
