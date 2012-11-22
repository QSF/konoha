package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe que le o prop. files de configuração do router.
 * */
public class RouterConfig {
	protected Properties prop;
    
    private String fileName;
    private int port;
    private int connections;
    private int ttl;
    
    
    public RouterConfig(){
        this.fileName = "RouterConfig";
        this.readConfig();
    }
    
    public RouterConfig(String fileName){
        this.fileName = fileName;
        this.readConfig();
    }    
    
    public void readConfig(){
        this.prop = new Properties();

        try{
            this.prop.load(new FileInputStream(this.fileName + ".properties"));

            this.setPort(Integer.parseInt(this.prop.getProperty("port")));
            this.setConnections(Integer.parseInt(
            		this.prop.getProperty("connections") ));
            this.setConnections(Integer.parseInt(
            		this.prop.getProperty("ttl") ));
        }catch (IOException ex) {
                ex.printStackTrace();
        }
    }
    

    public int getPort() {
            return port;
    }

    public void setPort(int port) {
            this.port = port;
    }

	public int getConnections() {
		return connections;
	}

	public void setConnections(int connections) {
		this.connections = connections;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
    
    

}
