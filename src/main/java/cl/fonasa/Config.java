package cl.fonasa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.fonasa.util.FTP;
@Configuration
public class Config {

    @Value("${cl.fonasa.sftp.host}")
    private String ftpHost;
    @Value("${cl.fonasa.sftp.port}")
    private int ftpPort;
    @Value("${cl.fonasa.sftp.user}")
    private String ftpUser;
    @Value("${cl.fonasa.sftp.password}")
    private String ftpPassword;
    @Bean
    public FTP ftp() {
        return new FTP(ftpHost, ftpPort, ftpUser, ftpPassword);
    }
}
