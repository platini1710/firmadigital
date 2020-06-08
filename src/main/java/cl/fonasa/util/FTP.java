package cl.fonasa.util;



import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

@Component
public class FTP {

    @Value("${cl.fonasa.sftp.host}")
    private String host;
    @Value("${cl.fonasa.sftp.port}")
    private int port;
    @Value("${cl.fonasa.sftp.user}")
    private String user;
    @Value("${cl.fonasa.sftp.password}")
    private String password;
    // private String baseFolder = "/fonresuelve";
    private Session session; // JSch Session
    private JSch jsch;
    private Channel channel;
    private ChannelSftp channelSftp;
    private static final Log log = LogFactory.getLog(FTP.class);

    public FTP() {
        // context.getInitParameter('urlReal') // connect();
    }

    public FTP(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        // context.getInitParameter('urlReal')
        // channelSftp.cd("/fonresuelve"); // TODO: Refactorizar Carpeta defecto
        // connect(host, port, user, password);
    	int i=0;
        connectSession(i);
        connectChannel();
    }

    /**
     * Conecta Session de JSCH
     */
    public void connectSession(int i) {
        try {

            this.jsch = new JSch();
            this.session = jsch.getSession(this.user, this.host, this.port);
            this.session.setPassword(this.password);
            // config
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            this.session.setConfig(config);
            this.session.setTimeout(20000);

            this.session.connect();



            log.warn("conectado");
        } catch (JSchException je) {
            // je.printStackTrace();
            log.error(je.getMessage());
            log.warn("reintento de conexión");
            i=i+1;
            log.warn("i==" + i);
            if (i<12) {
            connectSession(i);
            }
        }
    }

    public void connect() {
        log.warn("connectSession" + session.isConnected());
        if (!session.isConnected()) {
            // log.warn("connectSession");
        	int i=0;
            connectSession(i);
        } else {
            // log.warn("connected");
        }
        if (channel == null || !channel.isConnected() || !this.channelSftp.isConnected()) {
            // log.warn("connectChannel");
            connectChannel();
        }
        // log.warn("connectChannel");
    }

    public void connectChannel() {
        try {
            this.channel = this.session.openChannel("sftp");
            this.channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        this.channelSftp = (ChannelSftp) this.channel;
    }

    public static void main(String[] args) {
        FTP f = new FTP("fosqaotdgen.fonasa.local", 7522, "fonresuelve", "Fonasa123.,");
        ChannelSftp c = f.getChannelSftp();
        // c.cd("/fonresuelve");
        // println c.pwd();
        // println c.lpwd();
        // println c.getHome();
        String testPath = "/fonresuelve/11/1";
        // SftpATTRS attrs;
        try {
        	SftpATTRS attrs = c.stat(testPath);

        } catch (SftpException se) {
            // println se.id
            if (se.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                try {
                    c.mkdir(testPath);
                } catch (SftpException e) {
                    e.printStackTrace();
                }
                // println("Directorio creado");
                // println c.pwd();
                // Crear archivo
            }
        }
        // println c.ls(c.pwd()).toString()
        // c.disconnect();
        return;
    }

    public ChannelSftp getChannelSftp() {
        return channelSftp;
    }



    public void upload2(InputStream isFile, String path, String fileName) {
        connect();
        path = "/fonresuelve/" + path;
        // println fileName;
        // println path;
        try {
            channelSftp.stat(path);
        } catch (SftpException se) {
            log.warn(se.getMessage());
            se.printStackTrace();
            try {
                channelSftp.mkdir(path);
            } catch (SftpException e) {
                log.warn("SftpException->" + e.getMessage());
            }
        } catch (Exception e) {
            log.warn("Error stat ->" + e.getMessage());
        }
        try {
            channelSftp.put(isFile, path + "/" + fileName);
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }
	
	public void upload(long idCaso,InputStream isFile, String ruta, String path,String fileName) {




		log.info("ruta::"  + ruta);
		log.info("fileName::"  + fileName);
		log.info("path::"  + path);
		log.info("idCaso::"  + idCaso);

		connect();
		 path = ruta  +path ;
			log.info("path::"  + path);
		// println path;
		try {
		    channelSftp.stat(path);
		} catch (SftpException se) {
		    log.warn(se.getMessage());

		    try {
		        channelSftp.mkdir(path);
		    } catch (SftpException e) {
		        log.warn("SftpException->" + e.getMessage());
		    }
		} catch (Exception e) {
		    log.warn("Error stat ->" + e.getMessage());
		}
		try {
			log.info("filcopy file ::"  + path + "/" + fileName);
		    channelSftp.put(isFile, path + "/" + fileName);
		} catch (SftpException e) {
		    e.printStackTrace();
		}
		
			}

}