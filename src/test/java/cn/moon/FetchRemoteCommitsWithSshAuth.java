package cn.moon;

import com.jcraft.jsch.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;
import org.eclipse.jgit.util.FS;

import java.util.Map;

/**
 * Simple snippet which shows how to fetch commits from a remote Git repository
 * via ssh protocol authentication.
 *
 * You need to provide proper values for the URL of the Git repository and
 * the location of the private key file
 *
 * <p>
 * Created by zhengmaoshao on 2019/4/3 上午12:38
 */
public class FetchRemoteCommitsWithSshAuth {
    private static final String REMOTE_URL = "git@github.com:moon-cn/git-repo-sync.git";
    private static final String PRIVATE_KEY = "C:\\Users\\moon\\.ssh\\id_ed25519";

    public static void main(String[] args) throws GitAPIException {

        final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host host, Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
            }

            @Override
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                JSch sch = super.createDefaultJSch(fs);
                sch.addIdentity(PRIVATE_KEY);
                return sch;
            }
        };

        LsRemoteCommand lsRemoteCommand = Git.lsRemoteRepository();
        lsRemoteCommand.setRemote(REMOTE_URL);
        lsRemoteCommand.setTransportConfigCallback(transport -> {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        });

        final Map<String, Ref> map = lsRemoteCommand.setHeads(true)
                .setTags(true)
                .callAsMap();

        for (Map.Entry<String, Ref> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey()/*eg.refs/heads/develop*/ + ", Ref: " + entry.getValue().getObjectId().getName()/*eg.e16c937848d5c1ad50ef163003c7b076103f7e37*/);
        }
    }
}