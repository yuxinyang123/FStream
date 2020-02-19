package top.beliefyu.fstream.util;

import java.io.*;

/**
 * SerializableUtil
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-19 17:40
 */
public class SerializableUtil {

    /**
     * java对象序列化成字节数组
     *
     * @param object
     * @return byte[]
     */
    public static byte[] toBytes(Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    /**
     * 字节数组反序列化成java对象
     *
     * @param bytes
     * @return Object
     */
    public static <T> T toObject(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            Object object = ois.readObject();
            return (T) object;
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
