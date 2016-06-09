/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorcache;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 *
 * @author Gilberto Kreisler
 * @author Leticia Sampaio
 */
public class SimuladorCache {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        InputStream arquivo;
        arquivo = SimuladorCache.class.getResourceAsStream("arqTeste/arqBinario1_rw_10.dat");
        DataInputStream input = new DataInputStream(arquivo);
        int nblokL1d, bsizeL1d, assL1d, nblokL1i, bsizeL1i, assL1i;
        int nblokL2, bsizeL2, assL2;
        Cache L1d, L1i, L2;

        Scanner scan = new Scanner(System.in);
        nblokL1d = scan.nextInt();
        bsizeL1d = scan.nextInt();
        assL1d = scan.nextInt();
        nblokL1i = scan.nextInt();
        bsizeL1i = scan.nextInt();
        assL1i = scan.nextInt();
        nblokL2 = scan.nextInt();
        bsizeL2 = scan.nextInt();
        assL2 = scan.nextInt();

        if (nblokL1d == 0) {
            nblokL1d = 1024;
        }

        if (bsizeL1d == 0) {
            bsizeL1d = 4;
        }

        if (assL1d == 0) {
            assL1d = 1;
        }

        if (nblokL1i == 0) {
            nblokL1i = 1024;
        }

        if (bsizeL1i == 0) {
            bsizeL1i = 4;
        }

        if (assL1i == 0) {
            assL1i = 1;
        }

        if (nblokL2 == 0) {
            nblokL2 = 1024;
        }

        if (bsizeL2 == 0) {
            bsizeL2 = 4;
        }

        if (assL2 == 0) {
            assL2 = 1;
        }

        L1d = new Cache(nblokL1d, bsizeL1d, assL1d);
        L1i = new Cache(nblokL1i, bsizeL1i, assL1i);
        L2 = new Cache(nblokL2, bsizeL2, assL2);

        try {
            int instruction;
            int end;
            boolean needWriteL1i;
            boolean needWriteL1d;
            boolean needWriteL2;

            do {
                end = input.readInt();
                instruction = input.readInt();

                if (instruction == 0) {
                    if (end > 100) {
                        needWriteL1d = L1d.read(end);
                        needWriteL2 = L2.read(end);
                        if (needWriteL1d) {
                            if (needWriteL2) {
                                L1d.write(end, true);
                                L2.write(end, true);
                            } else {
                                L1d.write(end, true);
                            }
                        } else {
                            if (needWriteL2) {
                                L2.write(end, true);
                            }
                        }
                    } else {
                        needWriteL1i = L1i.read(end);
                        needWriteL2 = L2.read(end);
                        if (needWriteL1i) {
                            if (needWriteL2) {
                                L1i.write(end, true);
                                L2.write(end, true);
                            } else{
                                L1i.write(end, true);
                            }
                        } else {
                            if (needWriteL2) {
                                L2.write(end, true);
                            }
                        }
                    }
                } else if (instruction == 1) {
                    if (end > 100) {
                        L1d.write(end, false);
                        L2.write(end, false);
                    } else {
                        L1i.write(end, false);
                        L2.write(end, false);
                    }
                }

            } while (input.available() > 0);

        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado");

        } catch (IOException ex) {
            System.out.println("Problema na leitura do arquivo");
        }

        System.out.println("\n");
        System.out.println("****** L1 dados ******");
        L1d.getRelatorio();
        System.out.println("********************");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("****** L1 instruções ******");
        L1i.getRelatorio();
        System.out.println("********************");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("****** L2 ******");
        L2.getRelatorio();
        System.out.println("********************");
        System.out.println("\n");

    }

}
