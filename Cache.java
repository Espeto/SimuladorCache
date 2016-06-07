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
public class Cache {

    //private BlockMem _cache_blocks[][];//Representativo - Sem necessidade de implementar no momento
    private final int _val[][];
    private final int _tag[][];
    private final int _nblock; //Numero de blocos na cache
    private final int _nsets; //Numero de entradas na cache
    private final int _bsize; //Tamanho do bloco
    private final int _ass;   //Associatividade
    private int _nbits_offset; //Numero bits Offset
    private int _nbits_indice; //Numero bits Indice
    private int _nbits_tag;    //Numero bits Tag
    private int _conf_miss = 0, _cap_miss = 0, _comp_miss = 0; //Miss: Conflito, Capacidade, Compulsório
    private int _hit = 0;
    private float _total_access = 0;
    private int _nwrites = 0, _nreads = 0;

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
        //_cache_blocks = new BlockMem[this._nsets][this._ass];
        calcBits();
        startValidade();
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
     * @param end integer - Endereço passado
     * @return integer
     */
    //Retorno de valores para identificação da necessidade de escrita
    //Caso chame a escrita, escrever na outra cache de acordo com a política write through
    //Se hit -> retorna 0; Miss -> Necessidade de escrita -> retorna -1
    public int read(int end) {
        int indice = (end / _nbits_offset) & (2 ^ (_nbits_indice - 1));
        int tag = (end / (_nbits_offset + _nbits_indice));
        int invalCount = 0;//Contador dos bits de validade. Se val = 0 -> contador++

        //Ao chamar o método de leitura já é contabilizado um acesso
        _nreads++;
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
                    return 0;
                } else {
                    if (i == _ass - 1) {
                        //Miss de conflito
                        _conf_miss++;
                        _total_access--;
                        return -1;
                    }

                }
            }
        }

        if (invalCount == _ass) {
            //Miss compulsório
            _comp_miss++;
            _total_access--;
        }

        return -1;//Identifica a necessidade de escrita
    }

    //Escrita feita após miss de leitura
    /**
     * 
     * @param end integer
     */
    public void write(int end) {
        int indice = (end / _nbits_offset) & (2 ^ (_nbits_indice - 1));
        int tag = (end / (_nbits_offset + _nbits_indice));

        Random rand = new Random();
        int random_number = rand.nextInt(_ass);

        //Ao chamar a escrita já é contabilizado um acesso
        _nwrites++;
        _total_access++;

        //Escrita feita em posição randomica
        _val[indice][random_number] = 1;
        _tag[indice][random_number] = tag;
    }
    
    /**
     * 
     * @return float
     */
    private float getMissRate(){
        return ((_comp_miss + _conf_miss + _cap_miss) / _total_access);
    }
    
    /**
     * 
     * @return float
     */
    private float getHitRate(){
        return (_hit / _total_access);
    }

    public void getRelatorio() {
        
        System.out.println("Total de acessos: " + _total_access
                + "\n" + "Total de escritas: " + _nwrites
                + "\n" + "Total de leituras: " + _nreads
                + "\n" + "Total de Hits: " + _hit
                + "\n" + "Total Miss Compulsório: " + _comp_miss
                + "\n" + "Total Miss Conflito: " + _conf_miss
                + "\n" + "Total Miss Capacidade: " + _cap_miss
                + "\n" + "Hit rate: " + getHitRate()
                + "\n" + "Miss rate: " + getMissRate());
    }
    
    public static void main(String[] args){
        int[] endereco = new int[10];
        Random random = new Random();
        Cache cache = new Cache(1024, 4, 1);
        int armazena;
        
        for (int i = 0; i < 10; i++) {
            endereco[i] = random.nextInt(100);
        }
        
        for (int i = 0; i < 10; i++) {
            armazena = cache.read(endereco[i]);
            if (armazena == -1) {
                cache.write(endereco[i]);
            }
        }
        
        cache.getRelatorio();
    }

}
