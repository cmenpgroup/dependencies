/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.bos.clas6;

import java.util.HashMap;
import org.jlab.data.io.DataDescriptor;
import org.jlab.data.io.DataDictionary;

/**
 *
 * @author gavalian
 */
public class BosDataDictionary implements DataDictionary {
    private HashMap<String,BosDataDescriptor> descriptors =
            new HashMap<String,BosDataDescriptor>();
    
    public BosDataDictionary(){
        
    }
    
    @Override
    public void init(String format) {
        System.err.println("[Dictionary] ---> Loading xml dictionary...");
        BankDictionaryXML xmlDict = new BankDictionaryXML().loadResource("CLAS6_BOS_Dictionary.xml");
        System.err.println("[Dictionary] ---> XML dictionary size = " + xmlDict.getDescriptors().size());
        for(BankDescriptorXML desc : xmlDict.getDescriptors()){
            StringBuilder str = new StringBuilder();
            for(BankEntryXML entry : desc.getEntries()){
                str.append(entry.getEntryName());
                str.append("/");
                str.append(entry.getEntryTypeString());
                str.append(":");
            }
            str.deleteCharAt(str.length()-1);
            //System.out.println("DESC : [" + desc.getBankName() + "] --> "
            //+ str.capacity()  + "  " + str.length() + "  "
            //+ str.toString());
            BosDataDescriptor descBOS = new BosDataDescriptor(desc.getBankName());
            descBOS.init(str.toString());
            descriptors.put(desc.getBankName(), descBOS);
        }
        /*
        BosDataDescriptor descEVNT = new BosDataDescriptor("EVNT");
        
        StringBuilder str = new StringBuilder();
        str.append("pid/int32:pmom/float32:mass/float32:charge/int32:beta/float32");
        str.append(":cx/float32:cy/float32:cz/float32:x/float32:y/float32");
        str.append(":z/float32:dcstat/int32:ccstat/int32:scstat/int32");
        str.append(":ecstat/int32:lcstat/int32:ststat/int32:status/int32");        
        descEVNT.init(str.toString());
        descriptors.put("EVNT", descEVNT);
        
        BosDataDescriptor descSCPB = new BosDataDescriptor("SCPB");
        StringBuilder str_scpb = new StringBuilder();
        str_scpb.append("sc_pd_ht/int32:edep/float32:time/float32:path/float32:");
        str_scpb.append("chi2sc/float32:status/int32");
        
        descSCPB.init(str_scpb.toString());
        descriptors.put("SCPB", descSCPB);*/
        
    }

    @Override
    public String getXML() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getDescriptorList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataDescriptor getDescriptor(String desc_name) {
        return descriptors.get(desc_name);
    }
    
}
