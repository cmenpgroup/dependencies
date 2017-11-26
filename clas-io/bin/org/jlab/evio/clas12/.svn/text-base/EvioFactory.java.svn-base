/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.evio.clas12;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author gavalian
 */
public class EvioFactory {
    
    private final static EvioDictionary factoryDict = EvioFactory.readDefaultDictionary();
    
    public static EvioDictionary readDefaultDictionary(){
        String CLAS12DIR = System.getenv("CLAS12DIR");
        EvioDictionary dict = new EvioDictionary();
        if(CLAS12DIR==null){
            System.err.println("[EvioFactory] ---> Sevire ERROR : CLAS12DIR " + 
                    "environment is not defined.");
            return dict;
        }

        String ClasDictDir = CLAS12DIR + "/lib/bankdefs";
        try {
            dict.setDictionary(new File(ClasDictDir));
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(EvioFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(EvioFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EvioFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EvioFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(EvioFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.err.println("[EvioFactory] ---> CLAS12 Dictionary loaded succesfully.");
        return dict;
    }
    public static EvioDictionary getDictionary(){
        return factoryDict;
    }
    
    public static EvioDataBank createEvioBank(String name){
        EvioDescriptor desc = (EvioDescriptor) EvioFactory.getDictionary().getDescriptor(name);
        return new EvioDataBank(desc);
    }
    
    public static EvioDataBank createEvioBank(String name,int rows){
        EvioDescriptor desc = (EvioDescriptor) EvioFactory.getDictionary().getDescriptor(name);
        EvioDataBank bank = new EvioDataBank(desc);
        String[] entries = desc.getEntryList();
        for(String entry : entries){
            int e_type = desc.getProperty("type", entry);
            if(e_type==1){
                bank.setInt(entry, new int[rows]);
            }
            if(e_type==3){
                bank.setDouble(entry, new double[rows]);
            }
        }
        return bank;
    }
    
}
