package sample.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.utils.ByteUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by xiaopan on 2015/10/14 0014.
 */
public abstract class AbstractMessageTransport {

    public final Logger LOGGER = LoggerFactory.getLogger(AbstractMessageTransport.class);

    public abstract InputStream getInputStream() throws IOException;

    public abstract OutputStream getOutputStream() throws IOException;

    public void sendMessageNoReturn(byte[] data) throws IOException {
        try{
            LOGGER.info("发送消息：{}",ByteUtils.byteArrayToHexString(data));
            OutputStream outputStream = getOutputStream();
            outputStream.write(data);
            outputStream.flush();
        }catch (Exception e){
            throw new IOException("发送消息失败",e);
        }
    }

    public byte[] sendMessage(byte[] data, int returnSize, int returnTimeOut) throws IOException {
        long start = System.currentTimeMillis();
        sendMessageNoReturn(data);
        byte[] bytes = new byte[returnSize];
        try{
            InputStream inputStream = getInputStream();
            int hasRead = 0;
            while (true){
                if(System.currentTimeMillis() - start > returnTimeOut){
                    throw new TimeoutException("读消息超时:"+returnTimeOut);
                }
                int read = inputStream.read(bytes,hasRead,bytes.length-hasRead);
                if(read == -1){
                    TimeUnit.MILLISECONDS.sleep(10);
                    continue;
                }
                hasRead += read;
                LOGGER.info("己接收消息：{}",ByteUtils.byteArrayToHexString(bytes));
                if(hasRead == bytes.length){
                    return bytes;
                }
            }
        }catch (Exception e){
            throw new IOException("发送消息失败",e);
        }
    }

}
