/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.bos.clas6;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.data.io.DataEvent;
import org.jlab.data.io.DataEventList;
import org.jlab.data.io.DataSource;

/**
 *
 * @author gavalian
 */
public class BosDataSource implements DataSource {
    private String bosFileName = "undef";    
    private static final int  MAXIMUM_BYTE_READ = 70000;
    private int  currentBufferPosition = -1;
    private int  currentEventInBuffer = 0;
    private int  currentBufferEventPosition = -1;
    private int  nextBufferEventPosition = -1;
    private ByteBuffer  ioFileBuffer;
    private BufferedInputStream buffInputStream = null;
    private BosDataDictionary  dictionary = new BosDataDictionary();
    private ArrayList<Integer> eventIndex = new ArrayList<Integer>();
    private Boolean            isLastBufferRead = true;
            
    public BosDataSource(){
        dictionary.init("*");
        dictionary.getDescriptor("EVNT").show();
        ioFileBuffer = ByteBuffer.allocate(0);
    }
    
    @Override
    public void open(File file) {

    }

    @Override
    public void open(String filename) {
        try {
            bosFileName = filename;
            File inFile = new File(bosFileName);
            buffInputStream = new BufferedInputStream(new FileInputStream(inFile));
            currentEventInBuffer = -1;
            isLastBufferRead = false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BosDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void open(ByteBuffer buff) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataEventList getEventList(int start, int stop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataEventList getEventList(int nrecords) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private int findStructure(String struct, int startpos){
        byte[] bufferBytes = ioFileBuffer.array();
        byte[] nameBytes   = struct.getBytes();
        for(int loop = startpos; loop < bufferBytes.length; loop++){
            if(bufferBytes[loop]==nameBytes[0] && loop<bufferBytes.length-12){
                String header = new String(bufferBytes,loop,8);
                //System.out.println("[StrcutureFinder] ----> Found R at pos = "
                //+ loop + " header = " + header);
                if(header.compareTo(struct)==0){
                    //System.out.println("[StrcutureFinder] ----> Found events at pos = "
                    //+ loop);
                    return loop;
                }
            }
        }
        return -1;
    }
    private void printIndexArray(){
        System.err.print("--> EVENT INDEX : ");
        for(Integer i : eventIndex){
            System.out.print(" " + i);
        }
        System.err.println();
        System.err.print("--> EVENT SIZE : ");
        for(int loop = 0 ; loop < eventIndex.size()-1; loop++){
            System.out.print(" " + (eventIndex.get(loop+1)-eventIndex.get(loop)));
        }
        System.err.println();
    }
    
    private void readAppendEvent(int last_event_start){
        byte[] a = Arrays.copyOfRange(ioFileBuffer.array(),
                last_event_start,ioFileBuffer.array().length);
        byte[] b = new byte[MAXIMUM_BYTE_READ];        
        try {
            int bytesRead = buffInputStream.read(b);
            if(bytesRead<MAXIMUM_BYTE_READ){
                System.err.println("********** BOSIO FILE LAST BUFFER READ ********");
                isLastBufferRead = true;
            }
            byte[] c = new byte[a.length + b.length];
            System.arraycopy(a, 0, c, 0, a.length);
            System.arraycopy(b, 0, c, a.length, b.length);
            /*System.out.println("-----> array copy " 
                    + " a = " + a.length 
                    + " b = " + b.length 
                    + " c = " + c.length);
            */
            ioFileBuffer = ByteBuffer.wrap(c);
            //ioFileBuffer = ByteBuffer.wrap(b);
            //System.out.println("----> result buffer = " + ioFileBuffer.array().length);
            this.updateEventIndex();
            currentEventInBuffer = 0;
        } catch (IOException ex) {
            Logger.getLogger(BosDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void updateEventIndex(){
        eventIndex.clear();
        int start_position = 0;
        int nextPosition   = this.findStructure("RUNEVENT", start_position);
        //System.out.println("------> first position = " + nextPosition);
        eventIndex.add(nextPosition);
        while(nextPosition>=0){
            start_position = nextPosition + 8;
            nextPosition   = this.findStructure("RUNEVENT", start_position);
            //System.out.println("------> adding position " + nextPosition);
            if(nextPosition>0) 
                eventIndex.add(nextPosition);
        }
        //System.err.println("[BosDataSource]-----> read buffer contains " +
        //        eventIndex.size() + " events");
        //this.printIndexArray();
    }
    
    @Override
    public DataEvent getNextEvent() {
        
        if(this.hasEvent()==false){
            byte[] emptybuff = new byte[4];
            ByteBuffer iobcs = ByteBuffer.wrap(emptybuff);
            iobcs.order(ByteOrder.LITTLE_ENDIAN);
            return new BosDataEvent(iobcs,dictionary);
        }
        
        if(currentEventInBuffer<0){
            try {                
                byte[] result = new byte[MAXIMUM_BYTE_READ];
                int bytesRead = buffInputStream.read(result);
                System.err.println("[BosDataSource]----> Read buffer bytes read = "
                + bytesRead);
                ioFileBuffer = ByteBuffer.wrap(result);
                ioFileBuffer.order(ByteOrder.LITTLE_ENDIAN);
                currentEventInBuffer = 0;
                this.updateEventIndex();
            } catch (IOException ex) {
                System.err.println("[BosDataSource]----> ERROR while reading the file...");
            }            
        }
        
        if(currentEventInBuffer==eventIndex.size()-1){
            /*
            System.out.println("[BosDataSource]----> current size " 
                    + ioFileBuffer.array().length + "  " + eventIndex.get(eventIndex.size()-2));
            System.out.println("[BosDataSource]----> READING new BUFFER "
                    + " remaining size = " + (ioFileBuffer.array().length-
                    eventIndex.get(eventIndex.size()-2)));*/
            this.readAppendEvent(eventIndex.get(eventIndex.size()-2));            
        }
        
        //System.err.println(" current event = " + currentEventInBuffer + " index size = "
        //+ eventIndex.size());
        if(currentEventInBuffer>=0&&currentEventInBuffer<eventIndex.size()-1){
            //System.err.println("[BosDataSource]----> creating an event start pos = "
            //        + eventIndex.get(currentEventInBuffer) + "  end pos = "
            //        + eventIndex.get(currentEventInBuffer+1));
            byte[] evt = ioFileBuffer.array();
                byte[] bcs = Arrays.copyOfRange(evt, eventIndex.get(currentEventInBuffer),
                        eventIndex.get(currentEventInBuffer+1)+8);
                
                ByteBuffer bcsBOS = ByteBuffer.wrap(bcs);
                bcsBOS.order(ByteOrder.LITTLE_ENDIAN);
                currentEventInBuffer++;
                return new BosDataEvent(bcsBOS,dictionary);
        }                
        /*
        try {
            byte[] result = new byte[MAXIMUM_BYTE_READ];
        int bytesRead = buffInputStream.read(result);
            
            ByteBuffer bosBytes = ByteBuffer.wrap(result);
            bosBytes.order(ByteOrder.LITTLE_ENDIAN);
            return new BosDataEvent(bosBytes,dictionary);
        } catch (IOException ex) {
            Logger.getLogger(BosDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return null;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getCurrentIndex() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasEvent() {
        if(currentEventInBuffer>=eventIndex.size()-2 && isLastBufferRead==true){
            return false;
        }
        return true;
    }
    
}
