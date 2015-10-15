package sample.message;

/**
 * Created by xiaopan on 2015/10/14 0014.
 */
public class DeviceAddress {
    enum LinkType{
        TCP,COM
    }

    private LinkType linkType;
    private String linkAddress;
    private String deviceAddress;

    public DeviceAddress(String linkType, String linkAddress, String deviceAddress) {
        if(linkType.equalsIgnoreCase("com")){
            this.linkType = LinkType.COM;
        }else{
            this.linkType = LinkType.TCP;
        }
        this.linkAddress = linkAddress;
        this.deviceAddress = deviceAddress;
    }

    public DeviceAddress(LinkType linkType, String linkAddress, String deviceAddress) {
        this.linkType = linkType;
        this.linkAddress = linkAddress;
        this.deviceAddress = deviceAddress;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }
}
