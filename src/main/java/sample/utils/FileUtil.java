package sample.utils;

import java.io.*;

/**
 * Created by xiaopan on 2015/10/14 0014.
 */
public class FileUtil {

    public static void writeObjectToFile(Object obj,String filePath) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);){
            oos.writeObject(obj);
        }catch(Exception e){
            throw new IOException("保存数据对象到文件时发生错误:"+filePath,e);
        }
    }

    public static Object readObjectFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.exists()){
            throw new IOException("读取数据对象时发生错误:"+filePath+",文件不存在");
        }
        try(FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis)){
            return ois.readObject();
        }catch(Exception e){
            throw new IOException("读取数据对象时发生错误:"+filePath,e);
        }
    }

}
