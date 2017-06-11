/*
 * Entidade caminhao, onde a magica acontece
 */
package entidades;

import static disttijolo.DistTijolo.randomGenerator;
import static entidades.Supervisor.baixo;
import static entidades.Supervisor.cima;
import java.util.ArrayList;

public class Caminhao {

  public ArrayList<Pessoa> P = new ArrayList();
  public int[] Tij = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  public int[] Tot = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  public int[] Buf = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
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
    System.out.print("-");
    for (int i = 0; i < pessoas; i++) {
      System.out.print("-----------");
    }
    System.out.print("\n");
    //Segunda linha
    System.out.print("-");
    for (int i = 0; i < pessoas; i++) {
      int K = Tij[i] / Tot[i];
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
    for (int i = 1; i < pessoas + 1; i++) {
      System.out.print("    ");
      int J = procuraProdutor(i);
      if (J != -1) {
        System.out.print("P" + cima.get(J).nro);
      } else {
        System.out.print("  ");
      }
      System.out.print("    -");
    }
    //Quinta linha
    System.out.print("\n-");
    for (int i = 1; i < pessoas + 1; i++) {
      System.out.print(" ");
      int J = procuraProdutor(i);
      if (J != -1) {
        System.out.print("V=" + String.format("%.4f", cima.get(J).velocidade));
      } else {
        System.out.print("        ");
      }
      System.out.print(" -");
    }
    //Sexta linha
    System.out.print("\n-");
    for (int i = 1; i < pessoas + 1; i++) {
      System.out.print(" ");
      int J = procuraProdutor(i);
      if (J != -1) {
        if (cima.get(J).qtdepvez < 10) {
          System.out.print("C=" + cima.get(J).qtdepvez + "      ");
        } else {
          System.out.print("C=" + cima.get(J).qtdepvez + "     ");
        }
      } else {
        System.out.print("         ");
      }
      System.out.print("-");
    }
    //Sabado Linha linha
    System.out.print("\n-");
    for (int i = 0; i < pessoas; i++) {
      System.out.print("          -");
    }
    //Oitava linha
    System.out.print("\n-");
    int j;
    for (int i = 0; i < pessoas; i++) {
      for (j = 20; j > 10; j--) {
        if (Buf[i] > j) {
          System.out.print("T");
        } else {
          System.out.print(" ");
        }
      }
      System.out.print("-");
    }
    //Nona linha
    System.out.print("\n-");
    for (int i = 0; i < pessoas; i++) {
      for (j = 10; j > 0; j--) {
        if (Buf[i] > j) {
          System.out.print("T");
        } else {
          System.out.print(" ");
        }
      }
      System.out.print("-");
    }
    System.out.print("\n");
    //Decima linha
    for (int i = 0; i < baixo.size(); i++) {
      System.out.print("====");
      System.out.print("P" + baixo.get(i).nro);
      System.out.print("=====");
    }
    //Decima primeira linha
    System.out.print("\n-");
    for (int i = 0; i < baixo.size(); i++) {
      System.out.print(" ");
      System.out.print("V=" + String.format("%.4f", baixo.get(i).velocidade));
      System.out.print(" -");
    }
    //Decima segunda linha
    System.out.print("\n-");
    for (int i = 0; i < baixo.size(); i++) {
      System.out.print(" ");
      if (baixo.get(i).qtdepvez < 10) {
        System.out.print("C=" + baixo.get(i).qtdepvez + "      ");
      } else {
        System.out.print("C=" + baixo.get(i).qtdepvez + "     ");
      }
      System.out.print("-");
    }
    //Decima terceira linha
    System.out.print("\n-");
    for (int i = 0; i < baixo.size(); i++) {
      if (baixo.get(i).espaco < 0) {
        System.out.print("    " + baixo.get(i).espaco + "    -");
      } else if (baixo.get(i).espaco < 10) {
        System.out.print("     " + baixo.get(i).espaco + "    -");
      } else {
        System.out.print("    " + baixo.get(i).espaco + "    -");
      }
    }
    //Decima quarta linha
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

  /**
   * Controle de espaco
   *
   * @param P : Pessoa que esta usando o espaco
   * @return
   */
  public synchronized boolean espaco1(Pessoa P) {
    if (P.espaco < 0) { //Em baixo do caminhao
      if (Buf[0] > 0) { //Se tem tijolos no buffer
        int Q = randomGenerator.nextInt(P.qtdepvez);
        if (Q <= Buf[0]) { //Se a pessoa decidiu pegar pelo o menos quantos tijolos tem
          Buf[0] = Buf[0] - Q;
        } else {
          Buf[0] = 0;
        }
      } else { //Se o buffer esta vazio
        if (pessoas > 1) {
          return espaco2(P);
        }
      }
    } else { //Em cima do caminhao
      if (Buf[0] < qtdemax) { //Se o buffer nao esta cheio
        if (Tij[0] > 0) { //Se ainda tem tijolos
          int Q = randomGenerator.nextInt(P.qtdepvez);
          if ((Buf[0] + Q) <= qtdemax) { //Se o buffer nao estourar
            Tij[0] = Tij[0] - Q;
            Buf[0] = Buf[0] + Q;
          } else { //Se o buffer estourar, tem que tirar alguns
            if ((Tij[0] - (qtdemax - Buf[0])) < 0) { //Se tira mais tijolos do que tem disponivel
              Buf[0] = Buf[0] + Tij[0];
              Tij[0] = 0;
            } else {
              Tij[0] = Tij[0] - (qtdemax - Buf[0]);
              Buf[0] = qtdemax;
            }
          }
        } else {
          if (pessoas > 1) {
            return espaco2(P);
          }
        }
      }
    }
    return true;
  }

  /**
   * Controle de espaco
   *
   * @param P : Pessoa que esta usando o espaco
   * @return
   */
  public synchronized boolean espaco2(Pessoa P) {
    if (P.espaco < 0) { //Em baixo do caminhao
      if (Buf[1] > 0) { //Se tem tijolos no buffer
        int Q = randomGenerator.nextInt(P.qtdepvez);
        if (Q <= Buf[1]) { //Se a pessoa decidiu pegar pelo o menos quantos tijolos tem
          Buf[1] = Buf[1] - Q;
        } else {
          Buf[1] = 0;
        }
      } else { //Se o buffer esta vazio
        if (pessoas > 2) {
          return espaco3(P);
        }
      }
    } else { //Em cima do caminhao
      if (Buf[1] < qtdemax) { //Se o buffer nao esta cheio
        if (Tij[1] > 0) { //Se ainda tem tijolos
          int Q = randomGenerator.nextInt(P.qtdepvez);
          if (Buf[1] + Q <= qtdemax) { //Se o buffer nao estourar
            Tij[1] = Tij[1] - Q;
            Buf[1] = Buf[1] + Q;
          } else { //Se o buffer estourar, tem que tirar alguns
            if ((Tij[1] - (qtdemax - Buf[1])) < 0) { //Se tira mais tijolos do que tem disponivel
              Buf[1] = Buf[1] + Tij[1];
              Tij[1] = 1;
            } else {
              Tij[1] = Tij[1] - (qtdemax - Buf[1]);
              Buf[1] = qtdemax;
            }
          }
        } else {
          if (pessoas > 2) {
            return espaco3(P);
          }
        }
      }
    }
    return true;
  }

  /**
   * Controle de espaco
   *
   * @param P : Pessoa que esta usando o espaco
   * @return
   */
  public synchronized boolean espaco3(Pessoa P) {
    if (P.espaco < 0) { //Em baixo do caminhao
      if (Buf[2] > 0) { //Se tem tijolos no buffer
        int Q = randomGenerator.nextInt(P.qtdepvez);
        if (Q <= Buf[2]) { //Se a pessoa decidiu pegar pelo o menos quantos tijolos tem
          Buf[2] = Buf[2] - Q;
        } else {
          Buf[2] = 0;
        }
      } else { //Se o buffer esta vazio
        if (pessoas > 3) {
          return espaco4(P);
        }
      }
    } else { //Em cima do caminhao
      if (Buf[2] < qtdemax) { //Se o buffer nao esta cheio
        if (Tij[2] > 0) { //Se ainda tem tijolos
          int Q = randomGenerator.nextInt(P.qtdepvez);
          if (Buf[2] + Q <= qtdemax) { //Se o buffer nao estourar
            Tij[2] = Tij[2] - Q;
            Buf[2] = Buf[2] + Q;
          } else { //Se o buffer estourar, tem que tirar alguns
            if ((Tij[2] - (qtdemax - Buf[2])) < 0) { //Se tira mais tijolos do que tem disponivel
              Buf[2] = Buf[2] + Tij[2];
              Tij[2] = 2;
            } else {
              Tij[2] = Tij[2] - (qtdemax - Buf[2]);
              Buf[2] = qtdemax;
            }
          }
        } else {
          if (pessoas > 3) {
            return espaco4(P);
          }
        }
      }
    }
    return true;
  }

  /**
   * Controle de espaco
   *
   * @param P : Pessoa que esta usando o espaco
   * @return
   */
  public synchronized boolean espaco4(Pessoa P) {
    if (P.espaco < 0) { //Em baixo do caminhao
      if (Buf[3] > 0) { //Se tem tijolos no buffer
        int Q = randomGenerator.nextInt(P.qtdepvez);
        if (Q <= Buf[3]) { //Se a pessoa decidiu pegar pelo o menos quantos tijolos tem
          Buf[3] = Buf[3] - Q;
        } else {
          Buf[3] = 0;
        }
      } else { //Se o buffer esta vazio
        if (pessoas > 5) {
          return espaco5(P);
        }
      }
    } else { //Em cima do caminhao
      if (Buf[3] < qtdemax) { //Se o buffer nao esta cheio
        if (Tij[3] > 0) { //Se ainda tem tijolos
          int Q = randomGenerator.nextInt(P.qtdepvez);
          if (Buf[3] + Q <= qtdemax) { //Se o buffer nao estourar
            Tij[3] = Tij[3] - Q;
            Buf[3] = Buf[3] + Q;
          } else { //Se o buffer estourar, tem que tirar alguns
            if ((Tij[3] - (qtdemax - Buf[3])) < 0) { //Se tira mais tijolos do que tem disponivel
              Buf[3] = Buf[3] + Tij[3];
              Tij[3] = 3;
            } else {
              Tij[3] = Tij[3] - (qtdemax - Buf[3]);
              Buf[3] = qtdemax;
            }
          }
        } else {
          if (pessoas > 5) {
            return espaco5(P);
          }
        }
      }
    }
    return true;
  }

  /**
   * Controle de espaco
   *
   * @param P : Pessoa que esta usando o espaco
   * @return
   */
  public synchronized boolean espaco5(Pessoa P) {
    if (P.espaco < 0) { //Em baixo do caminhao
      if (Buf[4] > 0) { //Se tem tijolos no buffer
        int Q = randomGenerator.nextInt(P.qtdepvez);
        if (Q <= Buf[4]) { //Se a pessoa decidiu pegar pelo o menos quantos tijolos tem
          Buf[4] = Buf[4] - Q;
        } else {
          Buf[4] = 0;
        }
      } else { //Se o buffer esta vazio
        if (pessoas > 5) {
          return espaco6(P);
        }
      }
    } else { //Em cima do caminhao
      if (Buf[4] < qtdemax) { //Se o buffer nao esta cheio
        if (Tij[4] > 0) { //Se ainda tem tijolos
          int Q = randomGenerator.nextInt(P.qtdepvez);
          if (Buf[4] + Q <= qtdemax) { //Se o buffer nao estourar
            Tij[4] = Tij[4] - Q;
            Buf[4] = Buf[4] + Q;
          } else { //Se o buffer estourar, tem que tirar alguns
            if ((Tij[4] - (qtdemax - Buf[4])) < 0) { //Se tira mais tijolos do que tem disponivel
              Buf[4] = Buf[4] + Tij[4];
              Tij[4] = 4;
            } else {
              Tij[4] = Tij[4] - (qtdemax - Buf[4]);
              Buf[4] = qtdemax;
            }
          }
        } else {
          if (pessoas > 5) {
            return espaco6(P);
          }
        }
      }
    }
    return true;
  }

  /**
   * Controle de espaco
   *
   * @param P : Pessoa que esta usando o espaco
   * @return
   */
  public synchronized boolean espaco6(Pessoa P) {
    if (P.espaco < 0) { //Em baixo do caminhao
      if (Buf[5] > 0) { //Se tem tijolos no buffer
        int Q = randomGenerator.nextInt(P.qtdepvez);
        if (Q <= Buf[5]) { //Se a pessoa decidiu pegar pelo o menos quantos tijolos tem
          Buf[5] = Buf[5] - Q;
        } else {
          Buf[5] = 0;
        }
      } else { //Se o buffer esta vazio
        if (pessoas > 6) {
          return espaco7(P);
        }
      }
    } else { //Em cima do caminhao
      if (Buf[5] < qtdemax) { //Se o buffer nao esta cheio
        if (Tij[5] > 0) { //Se ainda tem tijolos
          int Q = randomGenerator.nextInt(P.qtdepvez);
          if (Buf[5] + Q <= qtdemax) { //Se o buffer nao estourar
            Tij[5] = Tij[5] - Q;
            Buf[5] = Buf[5] + Q;
          } else { //Se o buffer estourar, tem que tirar alguns
            if ((Tij[5] - (qtdemax - Buf[5])) < 0) { //Se tira mais tijolos do que tem disponivel
              Buf[5] = Buf[5] + Tij[5];
              Tij[5] = 5;
            } else {
              Tij[5] = Tij[5] - (qtdemax - Buf[5]);
              Buf[5] = qtdemax;
            }
          }
        } else {
          if (pessoas > 6) {
            return espaco7(P);
          }
        }
      }
    }
    return true;
  }

  /**
   * Controle de espaco
   *
   * @param P : Pessoa que esta usando o espaco
   * @return
   */
  public synchronized boolean espaco7(Pessoa P) {
    if (P.espaco < 0) { //Em baixo do caminhao
      if (Buf[6] > 0) { //Se tem tijolos no buffer
        int Q = randomGenerator.nextInt(P.qtdepvez);
        if (Q <= Buf[6]) { //Se a pessoa decidiu pegar pelo o menos quantos tijolos tem
          Buf[6] = Buf[6] - Q;
        } else {
          Buf[6] = 0;
        }
      } else { //Se o buffer esta vazio
        if (pessoas > 7) {
          return espaco8(P);
        }
      }
    } else { //Em cima do caminhao
      if (Buf[6] < qtdemax) { //Se o buffer nao esta cheio
        if (Tij[6] > 0) { //Se ainda tem tijolos
          int Q = randomGenerator.nextInt(P.qtdepvez);
          if (Buf[6] + Q <= qtdemax) { //Se o buffer nao estourar
            Tij[6] = Tij[6] - Q;
            Buf[6] = Buf[6] + Q;
          } else { //Se o buffer estourar, tem que tirar alguns
            if ((Tij[6] - (qtdemax - Buf[6])) < 0) { //Se tira mais tijolos do que tem disponivel
              Buf[6] = Buf[6] + Tij[6];
              Tij[6] = 6;
            } else {
              Tij[6] = Tij[6] - (qtdemax - Buf[6]);
              Buf[6] = qtdemax;
            }
          }
        } else {
          if (pessoas > 7) {
            return espaco8(P);
          }
        }
      }
    }
    return true;
  }

  /**
   * Controle de espaco
   *
   * @param P : Pessoa que esta usando o espaco
   * @return
   */
  public synchronized boolean espaco8(Pessoa P) {
    if (P.espaco < 0) { //Em baixo do caminhao
      if (Buf[7] > 0) { //Se tem tijolos no buffer
        int Q = randomGenerator.nextInt(P.qtdepvez);
        if (Q <= Buf[7]) { //Se a pessoa decidiu pegar pelo o menos quantos tijolos tem
          Buf[7] = Buf[7] - Q;
        } else {
          Buf[7] = 0;
        }
      } else { //Se o buffer esta vazio
        if (pessoas > 8) {
          return espaco9(P);
        }
      }
    } else { //Em cima do caminhao
      if (Buf[7] < qtdemax) { //Se o buffer nao esta cheio
        if (Tij[7] > 0) { //Se ainda tem tijolos
          int Q = randomGenerator.nextInt(P.qtdepvez);
          if (Buf[7] + Q <= qtdemax) { //Se o buffer nao estourar
            Tij[7] = Tij[7] - Q;
            Buf[7] = Buf[7] + Q;
          } else { //Se o buffer estourar, tem que tirar alguns
            if ((Tij[7] - (qtdemax - Buf[7])) < 0) { //Se tira mais tijolos do que tem disponivel
              Buf[7] = Buf[7] + Tij[7];
              Tij[7] = 7;
            } else {
              Tij[7] = Tij[7] - (qtdemax - Buf[7]);
              Buf[7] = qtdemax;
            }
          }
        } else {
          if (pessoas > 8) {
            return espaco9(P);
          }
        }
      }
    }
    return true;
  }

  /**
   * Controle de espaco
   *
   * @param P : Pessoa que esta usando o espaco
   * @return
   */
  public synchronized boolean espaco9(Pessoa P) {
    if (P.espaco < 0) { //Em baixo do caminhao
      if (Buf[8] > 0) { //Se tem tijolos no buffer
        int Q = randomGenerator.nextInt(P.qtdepvez);
        if (Q <= Buf[8]) { //Se a pessoa decidiu pegar pelo o menos quantos tijolos tem
          Buf[8] = Buf[8] - Q;
        } else {
          Buf[8] = 0;
        }
      } else { //Se o buffer esta vazio
        if (pessoas > 9) {
          return espaco10(P);
        }
      }
    } else { //Em cima do caminhao
      if (Buf[8] < qtdemax) { //Se o buffer nao esta cheio
        if (Tij[8] > 0) { //Se ainda tem tijolos
          int Q = randomGenerator.nextInt(P.qtdepvez);
          if (Buf[8] + Q <= qtdemax) { //Se o buffer nao estourar
            Tij[8] = Tij[8] - Q;
            Buf[8] = Buf[8] + Q;
          } else { //Se o buffer estourar, tem que tirar alguns
            if ((Tij[8] - (qtdemax - Buf[8])) < 0) { //Se tira mais tijolos do que tem disponivel
              Buf[8] = Buf[8] + Tij[8];
              Tij[8] = 8;
            } else {
              Tij[8] = Tij[8] - (qtdemax - Buf[8]);
              Buf[8] = qtdemax;
            }
          }
        } else {
          if (pessoas > 9) {
            return espaco10(P);
          }
        }
      }
    }
    return true;
  }

  /**
   * Controle de espaco
   *
   * @param P : Pessoa que esta usando o espaco
   * @return
   */
  public synchronized boolean espaco10(Pessoa P) {
    if (P.espaco < 0) { //Em baixo do caminhao
      if (Buf[9] > 0) { //Se tem tijolos no buffer
        int Q = randomGenerator.nextInt(P.qtdepvez);
        if (Q <= Buf[9]) { //Se a pessoa decidiu pegar pelo o menos quantos tijolos tem
          Buf[9] = Buf[9] - Q;
        } else {
          Buf[9] = 0;
        }
      } else { //Se o buffer esta vazio
        return false;
      }
    } else { //Em cima do caminhao
      if (Buf[9] < qtdemax) { //Se o buffer nao esta cheio
        if (Tij[9] > 0) { //Se ainda tem tijolos
          int Q = randomGenerator.nextInt(P.qtdepvez);
          if (Buf[9] + Q <= qtdemax) { //Se o buffer nao estourar
            Tij[9] = Tij[9] - Q;
            Buf[9] = Buf[9] + Q;
          } else { //Se o buffer estourar, tem que tirar alguns
            if ((Tij[9] - (qtdemax - Buf[9])) < 0) { //Se tira mais tijolos do que tem disponivel
              Buf[9] = Buf[9] + Tij[9];
              Tij[9] = 9;
            } else {
              Tij[9] = Tij[9] - (qtdemax - Buf[9]);
              Buf[9] = qtdemax;
            }
          }
        } else {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Faz threads dormirem (especificamente as pessoas)
   *
   * @return
   * @throws InterruptedException
   */
  public synchronized boolean dorme() throws InterruptedException {
    wait();
    return true;
  }

  private int procuraProdutor(int i) {
    for (int j = 0; j < cima.size(); j++) {
      if (cima.get(j).espaco == i) {
        return j;
      }
    }
    return -1;
  }
}
