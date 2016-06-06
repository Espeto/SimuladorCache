/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorcache;

/**
 *
 * @author jaqueline
 */
public class BlockMem {
    private int _nsize;
    private byte[] _data;
    
    public BlockMem(){
        _nsize = 4;
        _data = new byte[4];
    }
    
    public BlockMem(int size){
        _nsize = size;
        _data = new byte[size];
    }
    
    private void setBlock(){
        
    }
    
    private void getBlock(){
        
    }
}
