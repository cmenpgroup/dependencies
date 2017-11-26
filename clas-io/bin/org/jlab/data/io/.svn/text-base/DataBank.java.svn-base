package org.jlab.data.io;

public interface DataBank {
    /**
     * Get the names of the columns in the bank.
     * The name is used in the calls getInt() and getDouble()
     * and other calls for get the arrays.
     * @return array of strings with names.
     */
    String[] getColumnList();
    /**
     * Returns the descriptor of the bank, that contains
     * names of the variables and their types.
     * @return bank descriptor
     */
    DataDescriptor getDescriptor();
    
    /**
     * Returns a native array of doubles for given name.     
     * @param path - name of the column.
     * @return double[] array with values.
     */
    double[] getDouble(String path);
    /**
     * Adds array of doubles into the bank under the name.    
     * @param path - name of the array.
     * @param arr primitive type array of doubles.
     */
    void setDouble(String path, double[] arr);
    /**
     * Appends an array to existing array with the same name.
     * The resulting array will increase in size by arr.length.
     * @param path name of the variable
     * @param arr primitive type array of doubles.
     */
    void appendDouble(String path, double[] arr);
    
    float[] getFloat(String path);
    void setFloat(String path, float[] arr);
    void appendFloat(String path, float[] arr);
    
    int[] getInt(String path);
    void setInt(String path, int[] arr);
    void appendInt(String path, int[] arr);

    short[] getShort(String path);
    void setShort(String path, short[] arr);
    void appendShort(String path, short[] arr);
    int  columns();
    int  rows();
    /**
     * Prints the content of the bank on the screen.
     * The implementation of printout is left up to the
     * particular implementation of the class.
     */
    void show();
}
