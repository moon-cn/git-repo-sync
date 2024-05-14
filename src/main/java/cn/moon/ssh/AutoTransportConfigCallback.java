package cn.moon.ssh;

import cn.moon.git.entity.Credential;
import cn.moon.git.service.CredentialService;
import com.jcraft.jsch.Identity;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;
import org.eclipse.jgit.util.FS;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AutoTransportConfigCallback implements TransportConfigCallback {

    private static final String PRIVATE_KEY = "C:\\Users\\moon\\.ssh\\id_ed25519";

    @Resource
    CredentialService credentialService;

    @Override
    public void configure(Transport transport) {
        Credential credential = credentialService.findByUrl(transport.getURI().getHost());
         if (transport instanceof TransportHttp) {
            // 账号密码登录
            TransportHttp ts = (TransportHttp) transport;
             ts.setCredentialsProvider(new UsernamePasswordCredentialsProvider(credential.getUsername(), credential.getPassword()));
            return;
        }

        if (transport instanceof SshTransport) {
            // SSH 登录
            SshTransport ssh = (SshTransport) transport;

            final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
                @Override
                protected void configure(OpenSshConfig.Host host, Session session) {
                    session.setConfig("StrictHostKeyChecking", "no");
                }

                @Override
                protected JSch createDefaultJSch(FS fs) throws JSchException {
                    JSch sch = super.createDefaultJSch(fs);
                    sch.addIdentity(credential.getHostname(), credential.getSshKey().getBytes(), null, null);
                    return sch;
                }
            };

            ssh.setSshSessionFactory(sshSessionFactory);
        }
    }


}
