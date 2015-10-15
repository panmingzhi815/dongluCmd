package sample.message;

import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

/**
 * Created by xiaopan on 2015/10/14 0014.
 */
public interface MessageTransport {

    void open() throws IOException, PortInUseException, UnsupportedCommOperationException;

    void close() throws IOException;

    void sendMessageNoReturn(byte[] data) throws IOException;

    byte[] sendMessage(byte[] data, int returnSize, int returnTimeOut) throws IOException;
}
