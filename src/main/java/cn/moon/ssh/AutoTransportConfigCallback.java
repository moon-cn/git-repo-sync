package cn.moon.ssh;

import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.TransportHttp;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;

@Component
public class AutoTransportConfigCallback implements TransportConfigCallback {
    @Override
    public void configure(Transport transport) {
         if (transport instanceof TransportHttp) {
            // 账号密码登录
            TransportHttp ts = (TransportHttp) transport;
            ts.setCredentialsProvider(new UsernamePasswordCredentialsProvider("username", "password"));
            return;
        }

        if (transport instanceof SshTransport) {
            // SSH 登录
            SshTransport ssh = (SshTransport) transport;
          //  ssh.setSshSessionFactory();
        }
    }


}
