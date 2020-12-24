package cl.fonasa.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

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
public class FTP
{
    @Value("${cl.fonasa.sftp.host}")
    private String host;
    @Value("${cl.fonasa.sftp.port}")
    private int port;
    @Value("${cl.fonasa.sftp.user}")
    private String user;
    @Value("${cl.fonasa.sftp.password}")
    private String password;
    private Session session;
    private JSch jsch;
    private Channel channel;
    private ChannelSftp channelSftp;
    private static final Log log;
    static  InputStream isFile;

    
    static {
        log = LogFactory.getLog((Class)FTP.class);
    }
    
    public FTP() {
    }
    
    public FTP(final String host, final int port, final String user, final String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.connectSession();
        this.connectChannel();
    }
    
    public void connectSession() {
        try {
            this.jsch = new JSch();
            FTP.log.info((Object)("SFTP to: " + this.host));
            (this.session = this.jsch.getSession(this.user, this.host, this.port)).setPassword(this.password);
            final Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            this.session.setConfig(config);
            this.session.setTimeout(5000);
            FTP.log.info((Object)"Intento de conexi\u00f3n");
            this.session.connect();
        }
        catch (JSchException je) {
            FTP.log.error((Object)je.getMessage());
            FTP.log.warn((Object)"reintento de conexi\u00f3n");
            this.connectSession();
        }
    }
    
    public void connect() {
        if (!this.session.isConnected()) {
            this.connectSession();
        }
        if (this.channel == null || !this.channel.isConnected() || !this.channelSftp.isConnected()) {
            this.connectChannel();
        }
    }
    
    public void connectChannel() {
        try {
            (this.channel = this.session.openChannel("sftp")).connect();
        }
        catch (JSchException e) {
            e.printStackTrace();
        }
        this.channelSftp = (ChannelSftp)this.channel;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        FTP f = new FTP("192.168.1.158", 22, "aespinoza", "platini1710");
        ChannelSftp c = f.getChannelSftp();
        // c.cd("/fonresuelve");
        // println c.pwd();
        // println c.lpwd();
        // println c.getHome();
        String testPath = "/fonresuelve/11/1";
        SftpATTRS attrs;
        try {
          attrs = c.stat(testPath);
          // println "Directorio existe!";

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
    }
    
    public ChannelSftp getChannelSftp() {
        return this.channelSftp;
    }
    
    public InputStream downloadFile(final String path, final String file) throws SftpException {
        this.connect();
        try {
            this.channelSftp.cd("/" + path);
        }
        catch (SftpException e) {
            e.printStackTrace();
        }
        return this.channelSftp.get(file);
    }
    
    public void upload(final InputStream isFile, String path, final String fileName) {
        this.connect();
        path = "/fonresuelve/" + path;
        try {
            this.channelSftp.stat(path);
        }
        catch (SftpException se) {
            FTP.log.warn((Object)se.getMessage());
            se.printStackTrace();
            try {
                this.channelSftp.mkdir(path);
            }
            catch (SftpException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e2) {
            FTP.log.warn((Object)("Error stat ->" + e2.getMessage()));
        }
        try {
            this.channelSftp.put(isFile, String.valueOf(path) + "/" + fileName);
        }
        catch (SftpException e3) {
            e3.printStackTrace();
        }
    }
    
    public boolean delete(final String path, final String filename) {
        this.connect();
        try {
            this.channelSftp.cd("/fonresuelve/" + path);
            this.channelSftp.rm(filename);
            return true;
        }
        catch (SftpException e) {
            e.printStackTrace();
            FTP.log.warn((Object)e.getMessage());
            return false;
        }
    }
    
    public String listFiles(final String dir, String path) throws SftpException {
        this.connect();
        final StringBuffer msg = new StringBuffer();
        path = "/" + dir + "/" + path;
        FTP.log.warn((Object)(" path to: " + path));
        try {
            this.channelSftp.cd(path);
            FTP.log.warn((Object)" path to: 1");
            final Vector<ChannelSftp.LsEntry> filelist = (Vector<ChannelSftp.LsEntry>)this.channelSftp.ls(path);
            FTP.log.debug((Object)" path to: 2");
            for (final ChannelSftp.LsEntry file : filelist) {
                msg.append(String.valueOf(file.getFilename()) + "\n");
            }
        }
        catch (SftpException e) {
            FTP.log.error((Object)("error  directorio no existe:" + e.getMessage()));
            msg.append(" directorio o path  no existe ");
        }
        catch (Exception e2) {
            FTP.log.error((Object)("error  ::" + e2.getMessage()));
            msg.append(" directorio o path  no existe ");
        }
        return msg.toString();
    }
    
    public String listFiles(String dir) throws SftpException {
        this.connect();
        final StringBuffer msg = new StringBuffer();
        dir = "/" + dir;
        FTP.log.debug((Object)(" dir to: " + dir));
        try {
            this.channelSftp.cd(dir);
            FTP.log.debug((Object)" path to: 1");
            final Vector<ChannelSftp.LsEntry> filelist = (Vector<ChannelSftp.LsEntry>)this.channelSftp.ls(dir);
            FTP.log.debug((Object)" path to: 2");
            for (final ChannelSftp.LsEntry file : filelist) {
                msg.append(String.valueOf(file.getFilename()) + "\n");
            }
        }
        catch (SftpException e) {
            FTP.log.error((Object)("error  directorio no existe:" + e.getMessage()));
            msg.append(" directorio o path  no existe ");
        }
        catch (Exception e2) {
            FTP.log.error((Object)("error  ::" + e2.getMessage()));
            msg.append(" directorio o path  no existe ");
        }
        return msg.toString();
    }
    
    public void upload(final InputStream isFile, String path, final String dir, final String fileName) {
        FTP.log.warn((Object)("path :::::::/" + dir + "/" + path));
        this.connect();
        path = "/" + dir + "/" + path;
        try {
            FTP.log.warn((Object)("stat :" + path));
            this.channelSftp.stat(path);
        }
        catch (SftpException se) {
            FTP.log.info((Object)("Error SftpException stat ->" + se.getMessage()));
            FTP.log.warn((Object)se.getMessage());
            se.printStackTrace();
            try {
                this.channelSftp.mkdir(path);
            }
            catch (SftpException e) {
                FTP.log.warn((Object)("Error SftpException stat ->" + e.getMessage()));
                e.printStackTrace();
            }
        }
        catch (Exception e2) {
            FTP.log.warn((Object)("Error stat ->" + e2.getMessage()));
        }
        try {
            this.channelSftp.put(isFile, String.valueOf(path) + "/" + fileName);
        }
        catch (SftpException e3) {
            FTP.log.warn((Object)("error SftpException" + e3.getMessage()));
            e3.printStackTrace();
        }
    }
    
   
    
    public void upload( long idCaso,  InputStream isFile,  String ruta, String path,  String fileName) {

        this.connect();
        path ="/solicitudesCiudadanas/" + String.valueOf(ruta) + path;
		if (log.isDebugEnabled()) {
			log.debug("path   ::" + path + "/" + fileName);
		}
        try { 
            this.channelSftp.stat(path);
        }
        catch (SftpException se) {

            FTP.log.warn((Object)se.getMessage());

            try {
                this.channelSftp.mkdir(path);
            }
            catch (SftpException e) {
                FTP.log.warn((Object)("SftpException->" + e.getMessage()));
            }
        }
        catch (Exception e2) {
            FTP.log.warn((Object)("Error stat ->" + e2.getMessage()));
        }
        try {
            FTP.log.info((Object)("filcopy file ::" + path + "/" + fileName));
            channelSftp.put(isFile, path + "/" + fileName);
        }
        catch (Exception e3) {
            e3.printStackTrace();
        }
    }
}