package org.jlab.evio.clas12;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.coda.jevio.ByteDataTransformer;
import org.jlab.coda.jevio.DataType;
import org.jlab.coda.jevio.EventBuilder;
import org.jlab.coda.jevio.EvioBank;
import org.jlab.coda.jevio.EvioCompactStructureHandler;
import org.jlab.coda.jevio.EvioEvent;
import org.jlab.coda.jevio.EvioException;
import org.jlab.coda.jevio.EvioNode;
import org.jlab.data.io.DataBank;
import org.jlab.data.io.DataDescriptor;
import org.jlab.data.io.DataDictionary;
import org.jlab.data.io.DataEntryType;
import org.jlab.data.io.DataEvent;
import org.jlab.utils.TablePrintout;

public class EvioDataEvent implements DataEvent {
    
    private HashMap<String,String> eventProperties = new HashMap<String,String>();
    private ByteBuffer evioBuffer;
    private EvioCompactStructureHandler structure = null;
    private EvioDictionary dictionary = null;
    
    public EvioDataEvent(byte[] buffer, ByteOrder b_order){
        evioBuffer = ByteBuffer.wrap(buffer);
        evioBuffer.order(b_order);
        try {
            structure = new EvioCompactStructureHandler(evioBuffer,DataType.BANK);
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
   public EvioDataEvent(byte[] buffer, ByteOrder b_order, EvioDictionary dict){
        evioBuffer = ByteBuffer.wrap(buffer);
        evioBuffer.order(b_order);
        try {
            structure = new EvioCompactStructureHandler(evioBuffer,DataType.BANK);
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        dictionary = dict;
        this.setProperty("banks", "*");
        this.setProperty("variables", "*");
    }
    
   
    @Override
    public String[] getBankList() {
        // TODO Auto-generated method stub
        List<EvioNode> nodes   = structure.getNodes();
        ArrayList<String> list = new ArrayList<String>();
        String[] descList = dictionary.getDescriptorList();
        for(EvioNode item : nodes){            
            //System.out.println(" TAG-NUM = " +  item.getTag() + "   " + item.getNum()
            //+ "  " + item.getDataTypeObj());
            if(item.getDataTypeObj()==DataType.ALSOBANK&&item.getNum()==0){
                for(String di : descList){
                    if(dictionary.getDescriptor(di).getProperty("tag")==item.getTag()){
                        list.add(di);
                    }
                    //list.add("[" + item.getTag() + ":" + item.getNum() + "]");
                }
                   // ((EvioDataBank ) bank).addDoubleBuffer(tag, item.); 
            }
        }
        String[] banks = new String[list.size()];
        for(int loop = 0; loop < list.size();loop++) banks[loop] = list.get(loop);
        return banks;
    }
    
    public void initEvent(ByteBuffer buffer){
        evioBuffer = buffer;
    }
    
    @Override
    public String[] getColumnList(String bank_name) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public DataDictionary getDictionary() {
        return this.dictionary;
    }
    
    private int[] getTagNum(String path) {
        String[] split_path = path.split("[.]+");
        String bank = split_path[0];
        String col = split_path[1];
        
        DataDescriptor desc = this.dictionary.getDescriptor(bank);
        int tag = desc.getProperty("tag");
        int num = desc.getProperty("num",col);
        int[] ret = {tag,num};
        return ret;
    }
    
    @Override
    public float[] getFloat(String path) {
        int[] tagnum = this.getTagNum(path);
        return this.getFloat(tagnum[0],tagnum[1]);
    }
    
    private float[] getFloat(int tag, int num) {
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
        int[] tagnum = this.getTagNum(path);
        return this.getInt(tagnum[0],tagnum[1]);
    }
    
    private int[] getInt(int tag, int num) {
        EvioNode node = this.getNodeFromTree(tag, num, DataType.INT32);
        if(node!=null){
            ByteBuffer buffer = structure.getData(node);
            int[] nodedata = ByteDataTransformer.toIntArray(buffer);

            return nodedata;
        }
        return null;
    }
    
    @Override
    public void setInt(String path, int[] arr) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void appendInt(String path, int[] arr) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public short[] getShort(String path) {
        int[] tagnum = this.getTagNum(path);
        return this.getShort(tagnum[0],tagnum[1]);
    }
	
    private short[] getShort(int tag, int num) {
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
    
    @Override
    public boolean hasBank(String bank_name) {
        EvioDescriptor desc = (EvioDescriptor) this.dictionary.getDescriptor(bank_name);
        if(desc==null) return false;
        EvioNode banknode = this.getNodeFromTree(desc.tag, 0, DataType.ALSOBANK);
        if(banknode==null) return false;
        return true;
    }
    
    @Override
    public DataBank getBank(String bank_name) {
        EvioDescriptor desc = (EvioDescriptor) this.dictionary.getDescriptor(bank_name);
        if(desc==null) return null;
        EvioDataBank bank = new EvioDataBank(desc);
        String[] entries = desc.getEntryList();
        for(String item : entries){
            //if(item.getValue()<20){
            int type = desc.getProperty("type", item);
            if(type==1){
                int[] data = this.getInt(bank_name+"."+item);
                bank.setInt(item, data);
            }
            if(type==3){
                double[] data = this.getDouble(bank_name+"."+item);
                bank.setDouble(item, data);
            }
            //} else {
                
            //}
            //System.out.println(item.getKey() + " " + item.getValue() 
            //        + "  " + desc.types.get(item.getKey()));
        }
        
        return bank;
    }

    @Override
    public void getBank(String bank_name, DataBank bank) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void show(){
        
        //System.out.println("-----> event show");
        //dictionary.show();
        String[] bankList = this.getBankList();
        TablePrintout table = new TablePrintout("bank:nrows:ncols","24:12:12");
        
        if(bankList!=null){
            for(String bank : bankList){
                String[] tokens = new String[3];
                tokens[0] = bank;
                DataBank dbank = this.getBank(bank);
                Integer ncols  = dbank.getColumnList().length;
                Integer nrows  = dbank.rows();
                tokens[1] = nrows.toString(); 
                tokens[2] = ncols.toString();
                table.addData(tokens);
                //System.out.println("BANK [] ---> " + bank);
            }
        }
        table.show();
    }
    
    private EvioNode getNodeFromTree(int tag, int num, DataType type){
        List<EvioNode> nodes   = structure.getNodes();
        for(EvioNode item: nodes){
            if(item.getTag()==tag&&item.getNum()==num&&
                    item.getDataTypeObj()==type)
                return item;
        }
        return null;
    }
    @Override
    public double[] getDouble(String path) {
        int[] tagNum = this.getTagNum(path);
        
        EvioNode node = this.getNodeFromTree(tagNum[0],tagNum[1],DataType.DOUBLE64);
        if(node!=null){
            ByteBuffer buffer = structure.getData(node);
            double[] nodedata = ByteDataTransformer.toDoubleArray(buffer);
            return nodedata;
        }
        double[] ret = {0.0};
        return ret;             
    }

    @Override
    public void setDouble(String path, double[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendDouble(String path, double[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendBank(DataBank bank) {
        //System.err.println("---------> 1");
        int tag = bank.getDescriptor().getProperty("tag");
        //System.err.println("---------> 2");
        EvioEvent baseBank = new EvioEvent(tag, DataType.ALSOBANK, 0);
        //System.err.println("---------> 3");
        EvioBank intBank = new EvioBank(tag, DataType.ALSOBANK, 100);
        //System.err.println("---------> 4");
        EvioBank doubleBank = new EvioBank(tag, DataType.ALSOBANK, 200);
        EventBuilder builder = new EventBuilder(baseBank);
        ByteOrder byteOrder = structure.getByteBuffer().order();
        
        baseBank.setByteOrder(byteOrder);
        intBank.setByteOrder(byteOrder);
        doubleBank.setByteOrder(byteOrder);
        //System.err.println("------------ adding bank ");
        //System.err.println("------------ adding bank " + bank.getDescriptor().getName());
        try {
            String[] entries = bank.getColumnList();
            for(String entry : entries){
                //System.out.println("----> adding entry " + entry);
                int e_tag = bank.getDescriptor().getProperty("tag", entry);
                int e_num = bank.getDescriptor().getProperty("num", entry);
                int e_typ = bank.getDescriptor().getProperty("type", entry);
                if(e_typ==1){
                    EvioBank dataBank = new EvioBank(e_tag, DataType.INT32, e_num);
                    dataBank.setByteOrder(byteOrder);
                    dataBank.appendIntData(bank.getInt(entry));
                    builder.addChild(intBank, dataBank);
                }
                
                if(e_typ==3){
                    EvioBank dataBank = new EvioBank(e_tag, DataType.DOUBLE64, e_num);
                    dataBank.setByteOrder(byteOrder);
                    dataBank.appendDoubleData(bank.getDouble(entry));
                    builder.addChild(doubleBank, dataBank);
                }
            }
            
            builder.addChild(baseBank, intBank);
            builder.addChild(baseBank, doubleBank);
            
            int byteSize = baseBank.getTotalBytes();
            ByteBuffer bb = ByteBuffer.allocate(byteSize);
            //System.out.println("-------> adding bank " + bank.getDescriptor().getName()
            //+ "  size = " + byteSize);
            bb.order(byteOrder);
            baseBank.write(bb);
            bb.flip();
            //System.out.println("-----> prior size = " + structure.getByteBuffer().limit());
            ByteBuffer newBuffer = structure.addStructure(bb);
            //System.out.println("---> new byte buffer has size " + newBuffer.limit()            
            //        +  "   changed from " + structure.getByteBuffer().limit());
            //structure.
            EvioCompactStructureHandler handler = new EvioCompactStructureHandler(structure.getByteBuffer(),DataType.BANK);
            structure = handler;
            /*
            for (Map.Entry<String, int[]> bank : integerContainer.entrySet()) {
            EvioBank dataBank = new EvioBank(tag, DataType.INT32, bank.getKey());
            dataBank.setByteOrder(byteOrder);
            dataBank.appendIntData(bank.getValue());
            builder.addChild(intBank, dataBank);
            }
            
            for (Entry<Integer, double[]> bank : doubleBanks.entrySet()) {
            EvioBank dataBank = new EvioBank(tag, DataType.DOUBLE64, bank.getKey());
            dataBank.setByteOrder(byteOrder);
            dataBank.appendDoubleData(bank.getValue());
            builder.addChild(doubleBank, dataBank);
            }*/
        } catch (EvioException e) {
            e.printStackTrace();
        }
        
    }        

    @Override
    public ByteBuffer getEventBuffer() {
        return structure.getByteBuffer();
    }

    @Override
    public void setProperty(String property, String value) {
        if(eventProperties.containsKey(property)==true){
            eventProperties.remove(property);
        }         
        eventProperties.put(property, value);        
    }

    @Override
    public String getProperty(String property) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
