package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Exceptions.NotEmptyException;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 4/03/13
 * Time: 16:23
 */
public class BusinessCollection<V extends BusinessObject> extends BusinessObject{


    protected HashMap<String, TreeMap<String,V>> dataTables;

    protected File htmlFolder;
    protected File xmlFolder;

    public BusinessCollection(){
        dataTables = new HashMap<String, TreeMap<String, V>>();
        addKey(NAME);
    }

    protected void addKey(String key){
        if(dataTables.containsKey(key)){
            System.err.println("This collection already contains this key");
        }
        TreeMap<String, V> newMap = new TreeMap<String, V>();
        dataTables.put(key, newMap);
    }

    public List<V> getBusinessObjects(){
        TreeMap<String,V> map = dataTables.get(NAME);
        return new ArrayList<V>(map.values());
    }

    // -------------------------------------------------------------------------------------

    // Folders

    public void setHtmlFolder(File parentFolder){
        setHtmlFile(new File(parentFolder, getType() + ".html"));
        htmlFolder = new File(parentFolder, getType());
        for(BusinessObject businessObject: getBusinessObjects()){
            businessObject.setHtmlFile(new File(htmlFolder, businessObject.getName() + ".html"));
        }
    }

    protected void setXmlFolder(File parentFolder) {
        setXmlFile(new File(parentFolder, getType() + ".xml"));
        xmlFolder = new File(parentFolder, getType());
        for(BusinessObject businessObject: getBusinessObjects()){
            businessObject.setXmlFile(new File(xmlFolder, businessObject.getName() + ".xml"));
        }
    }

//    protected File getXmlFolder(){
//        return xmlFolder;
//    }
//
    public File getHtmlFolder() {
        return htmlFolder;
    }

    protected void createXmlFolder(){
        if(xmlFolder.mkdirs()){
            System.out.println(xmlFolder + " has been created");
        }
    }

    protected void createHtmlFolder(){
        if(htmlFolder.mkdirs()){
            System.out.println(htmlFolder + " has been created");
        }
    }

    // -------------------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getType()).append(":\r\n");
        for(BusinessObject businessObject : getBusinessObjects()){
            builder.append(businessObject.toString());
        }
        return builder.toString();
    }

    // -------------------------------------------------------------------------------------

    // Get

    public V getBusinessObject(String name){
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<String, String>(NAME, name);
        return getBusinessObject(entry);
    }

    private V getBusinessObject(Map.Entry<String, String> entry){
        String type = entry.getKey();
        String key = entry.getValue();
        TreeMap<String, V> map = dataTables.get(type);
        return map.get(key);
    }


    // -------------------------------------------------------------------------------------

    // Add

    public V addBusinessObject(V value) throws EmptyNameException, DuplicateNameException{
        return addBusinessObject(value, value.getKeyMap());
    }

    protected V addBusinessObject(V value, Map<String,String> keyMap) throws EmptyNameException, DuplicateNameException {
        for(Map.Entry<String,String> entry:keyMap.entrySet()){
            String key = entry.getValue();
            if(key==null || "".equals(key.trim())){
                throw new EmptyNameException();
            }
            V found = getBusinessObject(entry);
            if(found!=null){
                throw new DuplicateNameException(key);
            }
        }
        for(Map.Entry<String,String> entry:keyMap.entrySet()){
            // This will not throw any exceptions: we already handled them above.
            addBusinessObject(value, entry);
        }
        return value;
    }

    /**For internal use:
     * modify and merge
     *
     */
    protected V addBusinessObject(V value, Map.Entry<String,String> mapEntry){
        String type = mapEntry.getKey();
        String key = mapEntry.getValue();
        TreeMap<String, V> map = dataTables.get(type);

        key = key.trim();

        if(type.equals(NAME)){
            value.setName(key);
        }

        value.setXmlFile(new File(xmlFolder, value.getName() + ".xml"));
        if(htmlFolder!=null){
            value.setHtmlFile(new File(htmlFolder, value.getName() + ".html"));
        }

        map.put(key, value);
        return value;
    }



    // -------------------------------------------------------------------------------------

    // Modify

    public V modify(Map.Entry<String,String> oldEntry, Map.Entry<String,String> newEntry) throws EmptyNameException, DuplicateNameException{
        if(!oldEntry.getKey().equals(oldEntry.getKey())){
            throw new RuntimeException("Inproper use: keys should have the same value (modify)");
        }
        String key = newEntry.getValue();
        if(key==null || "".equals(key.trim())){
            throw new EmptyNameException();
        }
        V value = getBusinessObject(oldEntry);
        removeBusinessObject(oldEntry);

        V found = getBusinessObject(newEntry);
        if(found!=null){
            addBusinessObject(value, oldEntry);
            throw new DuplicateNameException(key);
        }
        addBusinessObject(value, newEntry);
        return value;
    }



    // -------------------------------------------------------------------------------------

    // Remove

    /**Removal function for external use: performs a check if the value is deletable
     * @see be.dafke.Accounting.Objects.Accounting.BusinessObject#isDeletable()
     * @param value the value to delete
     * @throws NotEmptyException if the value is not deletable
     */
    public void removeBusinessObject(V value) throws NotEmptyException {
        if(value.isDeletable()){
            removeBusinessObject(value.getKeyMap());
        } else {
            throw new NotEmptyException();
        }
    }

    private void removeBusinessObject(Map<String,String> entryMap){
        for(Map.Entry<String,String> entry : entryMap.entrySet()){
            removeBusinessObject(entry);
        }
    }

    //
    /**Remove function for interal use: performs no check
     */
    protected void removeBusinessObject(Map.Entry<String,String> entry){
        String type = entry.getKey();
        String key = entry.getValue();
        dataTables.get(type).remove(key);
    }
}