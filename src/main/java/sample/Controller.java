package sample;

import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.message.DeviceAddress;
import sample.message.MessageTransport;
import sample.message.MessageTransportFactory;
import sample.utils.Alerts;
import sample.utils.FileUtil;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


public class Controller implements Initializable{

    final String filePath = "info.data";
    private Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public TableView<CmdInfo> tb_cmdTable;
    public TextFlow tf_msg;
    public TextField tx_deviceAddress;
    public TextField tx_linkAddress;
    public TextField tx_size;
    public Button bt_send;
    public ComboBox<String> combo_type;
    public TextField tx_split;

    SimpleStringProperty nameProperty = new SimpleStringProperty();
    SimpleStringProperty cmdProperty = new SimpleStringProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combo_type.getItems().setAll(FXCollections.observableArrayList("TCP", "COM"));
        combo_type.setValue(combo_type.getItems().get(0));

        tx_linkAddress.setText("192.168.1.1");
        tx_deviceAddress.setText("1.1");
        tx_size.setText("1");
        tx_split.setText("1000");

        List<String> columnNames = Arrays.asList("index", "name", "cmd");
        for (int i = 0; i < columnNames.size(); i++) {
            tb_cmdTable.getColumns().get(i).setCellValueFactory(new PropertyValueFactory(columnNames.get(i)));
        }

        try {
            List<CmdInfo> o = (ArrayList<CmdInfo>)FileUtil.readObjectFromFile(filePath);
            tb_cmdTable.setItems(FXCollections.observableArrayList(o));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void bt_send_on_action(ActionEvent actionEvent) {
        DeviceAddress deviceAddress = new DeviceAddress(combo_type.getValue(), tx_linkAddress.getText().trim(), tx_deviceAddress.getText());
        MessageTransport messageTransport = MessageTransportFactory.create(deviceAddress);
        try {
            messageTransport.open();
            ObservableList<CmdInfo> items = tb_cmdTable.getItems();
            for (int i = 0; i < Integer.valueOf(tx_size.getText()); i++) {
                items.forEach(item -> {
                    try {
                        messageTransport.sendMessageNoReturn(hexStringToByteArray(item.getCmd().trim()));
                        TimeUnit.MILLISECONDS.sleep(Integer.valueOf(tx_split.getText().trim()));
                    } catch (Exception e) {
                        LOGGER.error("发送消息时发生错误",e);
                    }
                });
            }
        } catch (PortInUseException e) {
            Alerts.create(Alert.AlertType.ERROR).setTitle("错误").setHeaderText("端口被占用").showAndWait();
        } catch (UnsupportedCommOperationException e) {
            Alerts.create(Alert.AlertType.ERROR).setTitle("错误").setHeaderText("端口不可用").showAndWait();
        } catch (IOException e) {
            Alerts.create(Alert.AlertType.ERROR).setTitle("错误").setHeaderText("通讯失败").showAndWait();
        } finally {
            try {
                messageTransport.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        s = s.replaceAll(" ","");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public void tx_size_onKeyReleased(Event event) {
        TextField source = (TextField)event.getSource();
        try {
            String trim = source.getText().trim();
            Integer.parseInt(trim);
        }catch (Exception e){
            source.setText("1");
        }
    }

    public void addCmd_onAction(ActionEvent actionEvent) throws IOException {
        nameProperty.setValue("");
        cmdProperty.setValue("");

        VBox vBox = new VBox(10);

        HBox hBox1 = new HBox();
        Label label1 = new Label("名称：");
        TextField textField1 = new TextField();
        nameProperty.bindBidirectional(textField1.textProperty());
        hBox1.getChildren().addAll(label1,textField1);
        hBox1.setPadding(new Insets(10,10,0,10));

        HBox hBox2 = new HBox();
        Label label2 = new Label("命令：");
        TextArea textField2 = new TextArea();
        cmdProperty.bindBidirectional(textField2.textProperty());
        hBox2.getChildren().addAll(label2,textField2);
        hBox2.setPadding(new Insets(10, 10, 10, 10));

        vBox.getChildren().addAll(hBox1, hBox2);

        Optional<ButtonType> alert = Alerts.create(Alert.AlertType.CONFIRMATION).setTitle("添加命令").setHeaderContent(vBox).showAndWait();
        if(!alert.isPresent()){
            return;
        }
        if(alert.get() == ButtonType.OK){
            ArrayList<CmdInfo> cmdInfos = new ArrayList<>(tb_cmdTable.getItems());
            cmdInfos.add(new CmdInfo(cmdInfos.size()+1,nameProperty.get(),cmdProperty.get()));
            tb_cmdTable.setItems(FXCollections.observableArrayList(cmdInfos));
        }

        ArrayList<CmdInfo> cmdInfos = new ArrayList<>(tb_cmdTable.getItems());
        FileUtil.writeObjectToFile(cmdInfos, filePath);
    }

    public void deleteCmd_onAction(ActionEvent actionEvent) {
        CmdInfo selectedItem = tb_cmdTable.getSelectionModel().getSelectedItem();
        tb_cmdTable.getItems().remove(selectedItem);
        ArrayList<CmdInfo> cmdInfos = new ArrayList<>(tb_cmdTable.getItems());
        tb_cmdTable.setItems(FXCollections.observableArrayList(cmdInfos));
    }
}
