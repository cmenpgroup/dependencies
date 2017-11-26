package org.jlab.data.io;

/**
 * DataDescriptor is the dictionary of a single DataBank object.
 * It consists of a list of Entries by name and integer ID.
 */
public interface DataDescriptor {
	void init(String s);
	String[] getEntryList();
	String getName();
	int getProperty(String property_name, String entry_name);
	int getProperty(String property_name);
        void show();
}
