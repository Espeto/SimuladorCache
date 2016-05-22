/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulador.cache;

import static java.lang.Math.log;
import java.util.Random;

/**
 *
 * @author jaqueline
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

    public Cache() {
        _nblock = 1024;
        _ass = 1;
        _nsets = _nblock / _ass;
        _bsize = 4;
        _val = new int[_nsets];
        _tag = new int[_nsets];
        _cache_blocks = new BlockMem[_nblock / _ass][_ass];
        calcBits();

    }

    public Cache(int nblock, int bsize, int ass) {
        _nblock = nblock;
        _ass = ass;
        _nsets = _nblock / _ass;
        _bsize = bsize;
        _val = new int[_nsets];
        _tag = new int[_nsets];
        _cache_blocks = new BlockMem[_nblock / _ass][_ass];
        calcBits();
    }

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
    
    private void leitura(int end){
        
    }

    //Escrita feita após miss de leitura
    public void write(int end) {
        int indice = (end / _nbits_offset) & (2 ^ (_nbits_indice - 1));
        int tag = (end / (_nbits_offset + _nbits_indice));
        
        //O que fazer depois de possui o indice e tag na escrita?
        if (_val[indice] == 0) {
            
        }
    }

}
