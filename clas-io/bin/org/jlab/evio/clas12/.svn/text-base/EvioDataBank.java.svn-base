package org.jlab.evio.clas12;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jlab.data.io.DataBank;
import org.jlab.data.io.DataDescriptor;

public class EvioDataBank implements DataBank {
    private HashMap<String,double[]>  doubleContainer  = new HashMap<String,double[]>();
    private HashMap<String,int[]>     integerContainer = new HashMap<String,int[]>();
    private EvioDescriptor            bankDescriptor = null;
    
    public EvioDataBank(EvioDescriptor desc){
        bankDescriptor = desc;
    }
    
    @Override
    public String[] getColumnList() {
        // TODO Auto-generated method stub
        return bankDescriptor.getEntryList();
    }

    @Override
    public DataDescriptor getDescriptor() {
        // TODO Auto-generated method stub
        return bankDescriptor;
    }

    @Override
    public float[] getFloat(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setFloat(String path, float[] arr) {
        // TODO Auto-generated method stub
        
    }

	@Override
	public void appendFloat(String path, float[] arr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] getInt(String path) {
            if(integerContainer.containsKey(path)==true){
                return integerContainer.get(path);
            }
            System.err.println("---> ERROR : there is no variable " +
                    path + " in the bank");
            return null;
	}

        @Override
        public void setInt(String path, int[] arr) {
		// TODO Auto-generated method stub
            if(integerContainer.containsKey(path)==false){
                integerContainer.put(path, arr);
            }
		
	}

	@Override
	public void appendInt(String path, int[] arr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short[] getShort(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setShort(String path, short[] arr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appendShort(String path, short[] arr) {
		// TODO Auto-generated method stub
		
	}
        public void reset(){
            integerContainer.clear();
            doubleContainer.clear();
        }

    @Override
    public double[] getDouble(String path) {
        if(doubleContainer.containsKey(path)==true){
            return doubleContainer.get(path);
        }
        System.err.println("---> ERROR : there is no variable " +
                path + " in the bank");
        return null;
    }

    @Override
    public void setDouble(String path, double[] arr) {
        if(doubleContainer.containsKey(path)==false){
                doubleContainer.put(path, arr);
        }
    }

    @Override
    public void appendDouble(String path, double[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void show() {
        int size = doubleContainer.size() + integerContainer.size();
        System.out.println("*****>>>>> BANK " + this.bankDescriptor.getName() 
                + "  >>>> SIZE = " + size);
        
        for(Map.Entry<String,double[]> item : doubleContainer.entrySet()){
            System.out.print(String.format("%s : (double) \n",item.getKey()));
            double[] itemdata = item.getValue();
            for(int loop = 0; loop < itemdata.length;loop++) {                
                System.out.print(String.format(" %12.3f  ", itemdata[loop]));
                if((loop+1)%5==0) System.out.println();
            }
            System.out.println();
        }
       
        for(Map.Entry<String,int[]> item : integerContainer.entrySet()){
            System.out.print(String.format("%s : (integer) \n",item.getKey()));
            int[] itemdata = item.getValue();
            for(int loop = 0; loop < itemdata.length;loop++){ 
                System.out.print(String.format(" %12d  ", itemdata[loop]));
                if((loop+1)%5==0) System.out.println();
            }
            System.out.println();
        }
        
    }

    @Override
    public int columns() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int rows() {        
        if(integerContainer.isEmpty()==false){
            Set<String> keys = integerContainer.keySet();
            Iterator<String> iter = keys.iterator();
            int[] value = integerContainer.get(iter.next());
            return value.length;
        }
        if(doubleContainer.isEmpty()==false){
            Set<String> keys = doubleContainer.keySet();
            Iterator<String> iter = keys.iterator();
            double[] value = doubleContainer.get(iter.next());
            return value.length;
        }
        return 0;
    }
}
