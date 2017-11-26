/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.evio.clas12;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.coda.jevio.EventWriter;
import org.jlab.coda.jevio.EvioCompactEventWriter;
import org.jlab.coda.jevio.EvioException;
import org.jlab.data.io.DataEvent;
import org.jlab.data.io.DataSync;

/**
 *
 * @author gavalian
 */
public class EvioDataSync implements DataSync {
    private ByteOrder writerByteOrder = ByteOrder.LITTLE_ENDIAN;
    private EvioCompactEventWriter evioWriter    = null;
    
    @Override
    public void open(String filename) {
        File file = new File(filename);
        try {
            evioWriter = new EvioCompactEventWriter(filename, null,
                    0, 0, 
                    4*300, 1000, 2*1024*1024, writerByteOrder, null, true);
//new EventWriter(file, 1000000, 2,
            //ByteOrder.BIG_ENDIAN, null, null);
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void writeEvent(DataEvent event) {
        try {
            //System.err.println("[sync] ---> buffer size = " + event.getEventBuffer().limit());
            ByteBuffer original = event.getEventBuffer();
            ByteBuffer clone = ByteBuffer.allocate(original.capacity());
            clone.order(original.order());
            original.rewind();
            clone.put(original);
            original.rewind();
            clone.flip();
            evioWriter.writeEvent(clone);            
//            event.getEventBuffer().flip();
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataSync.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EvioDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() {
        evioWriter.close();
    }
    
}
