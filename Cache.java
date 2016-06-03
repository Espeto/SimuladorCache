/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorcache;

import static java.lang.Math.log;
import java.util.Random;

/**
 *
 * @author Gilberto Kreisler
 * @author Leticia Sampaio
 */
public abstract class Cache {

    private BlockMem _cache_blocks[][];
    private int _val[][];
    private int _tag[][];
    private int _nblock; //Numero de blocos na cache
    private int _nsets; //Numero de entradas na cache
    private int _bsize; //Tamanho do bloco
    private int _ass;   //Associatividade
    private int _nbits_offset; //Numero bits Offset
    private int _nbits_indice; //Numero bits Indice
    private int _nbits_tag;    //Numero bits Tag
    private int _conf_miss = 0, _cap_miss = 0, _comp_miss = 0; //Miss: Conflito, Capacidade, Compulsório
    private int _hit = 0;
    private int _total_access = 0;

    /* public Cache() {
     _nblock = 1024;
     _ass = 1;
     _nsets = _nblock / _ass;
     _bsize = 4;
     _val = new int[_nsets];
     _tag = new int[_nsets];
     _cache_blocks = new BlockMem[_nblock / _ass][_ass];
     calcBits();

     }*/
    
    //As entradas podem ser feitas de forma básica.
    //Antes de criar o objeto a gente define na SimuladorCache
    //os valores padrão
    /**
     * @param nblock integer - Número de blocos na cache
     * @param bsize integer - Tamanho de cada bloco em bytes
     * @param ass integer - Associatividade da cache
     */
    public Cache(int nblock, int bsize, int ass) {
        _nblock = nblock;
        _ass = ass;
        _nsets = _nblock / _ass; //A partir do número de blocos na cache é calculado o número de sets.
        _bsize = bsize;
        _val = new int[_nsets][_ass];
        _tag = new int[_nsets][_ass];
        _cache_blocks = new BlockMem[_nsets][_ass];
        calcBits();
    }

    //Na cache associativa a variavel _val[] tem que acompanhar a mudança
    //Associatividade > 1 -> _val = Matriz
    private void startValidade() {
        for (int i = 0; i < _nsets; i++) {
            for (int j = 0; j < _ass; j++) {
                _val[i][j] = 0;
            }
        }
    }

    private void calcBits() {
        _nbits_offset = (int) (log(_bsize) / log(2));
        _nbits_indice = (int) (log(_nsets) / log(2));
        _nbits_tag = 32 - _nbits_indice - _nbits_offset;
    }

    /**
     * @see Implementa a leitura da cache
     * @param end integer - Endereço passado
     */
    public void read(int end) {
        int indice = (end / _nbits_offset) & (2 ^ (_nbits_indice - 1));
        int tag = (end / (_nbits_offset + _nbits_indice));
        int invalCount = 0;//Contador dos bits de validade. Se val = 0 -> contador++

        //Ao chamar o método de leitura já é contabilizado um acesso
        _total_access++;

        for (int i = 0; i < _ass; i++) {

            if (_val[indice][i] == 0) {
                //Gera miss compulsorio
                //Miss compulsório será contabilizado ao final
                //Se em toda associatividade a validade for 0
                invalCount++;
            } else {
                if (_tag[indice][i] == tag) {
                    //Hit
                    _hit++;
                    return;
                } else {
                    if (i == _ass - 1) {
                        //Miss de conflito
                        _conf_miss++;
                        write(end);
                    }

                }
            }
        }

        if (invalCount == _ass) {
            _comp_miss++;
        }
    }

    //Escrita feita após miss de leitura
    public void write(int end) {
        int indice = (end / _nbits_offset) & (2 ^ (_nbits_indice - 1));
        int tag = (end / (_nbits_offset + _nbits_indice));
        Random rand = null;
        int random_number = rand.nextInt(_ass);

        //Ao chamar a escrita já é contabilizado um acesso
        _total_access++;

        //Escrita feita em posição randomica
         _val[indice][random_number] = 1;
         _tag[indice][random_number] = tag;
    }

}
