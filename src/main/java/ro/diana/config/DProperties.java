package ro.diana.config;

/**
 * Created by diana on 13.04.2015.
 */
public class DProperties {
    private String clientRootUrl;

    private static DProperties instance;

    private DProperties(){
    };

    public static DProperties getInstance(){
        if (instance == null){
            instance = new DProperties();
        }
        return instance;
    }

    public String getClientRootUrl() {
        return clientRootUrl;
    }

    public void setClientRootUrl(String clientRootUrl) {
        this.clientRootUrl = clientRootUrl;
    }
}
