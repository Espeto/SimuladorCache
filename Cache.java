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
    private int _val[];
    private int _tag[];
    private int _nblock; //Numero de blocos na cache
    private int _nsets; //Numero de entradas na cache
    private int _bsize; //Tamanho do bloco
    private int _ass;   //Associatividade
    private int _nbits_offset; //Numero bits Offset
    private int _nbits_indice; //Numero bits Indice
    private int _nbits_tag;    //Numero bits Tag
    private int _conf_miss, _cap_miss, _comp_miss; //Miss: Conflito, Capacidade, Compulsório

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
        _val = new int[_nsets];
        _tag = new int[_nsets];
        _cache_blocks = new BlockMem[_nblock / _ass][_ass];
        calcBits();
    }

    //Na cache associativa a variavel _val[] tem que acompanhar a mudança
    //Associatividade > 1 -> _val = Matriz
    private void startValidade() {
        for (int i = 0; i < _nsets; i++) {
            _val[i] = 0;
        }
    }

    private void calcBits() {
        _nbits_offset = (int) (log(_bsize) / log(2));
        _nbits_indice = (int) (log(_nsets) / log(2));
        _nbits_tag = 32 - _nbits_indice - _nbits_offset;
    }

    /**
     * @see Implementação da leitura da cache
     * @param end integer - Endereço passado
     */
    private void read(int end) {
        int indice = (end / _nbits_offset) & (2 ^ (_nbits_indice - 1));
        int tag = (end / (_nbits_offset + _nbits_indice));
        
        if (_val[indice] == 0) {
            //Gera miss compulsorio
        }
        else{
            if (_tag[indice] == tag) {
                //Hit
            }
            else{
                //Miss de conflito
                write(end);
                
            }
        }
    }

    //Escrita feita após miss de leitura
    private void write(int end) {
        int indice = (end / _nbits_offset) & (2 ^ (_nbits_indice - 1));
        int tag = (end / (_nbits_offset + _nbits_indice));

        if (_val[indice] == 0) {

        }
    }

}
