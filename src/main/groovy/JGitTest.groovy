import org.eclipse.jgit.api.AddCommand
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.CommitCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PullCommand
import org.eclipse.jgit.api.PushCommand
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.transport.CredentialsProvider
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

/**
 * Created by ohya on 3/13/17.
 *
 * http://dotnsf.blog.jp/archives/1060578613.html
 * https://gist.github.com/SaitoWu/2487157
 *
 */
class JGitTest {
    Git git
    CredentialsProvider cp
    String usr, psw, email

    /**
     * コンストラクタ
     * @param usr ：ユーザー名
     * @param psw ：パスワード
     * @param email ：メールアドレス
     */
    JGitTest(String usr, String psw, String email) {
        this.usr = usr
        this.psw = psw
        this.email = email
        cp = new UsernamePasswordCredentialsProvider(usr, psw)
    }

    /**
     * PULL or Cloneを行う
     * @param url
     * @param local
     * @param cp
     */
    JGitTest open(String url, String local) {
        File localRepoDir = new File(local, ".git")

        if (localRepoDir.exists() == false) {
            // Cloneする
            CloneCommand cc = new CloneCommand().with {
                it.setCredentialsProvider(cp)
                it.setDirectory(new File(local))
                it.setURI(url)
            }
            git = cc.call()
        } else {
            // pullする
            Repository repo = new FileRepository(localRepoDir)
            git = new Git(repo)
            // Pullする
            PullCommand pc = git.pull()
            pc.setCredentialsProvider(cp).call()
        }
        this
    }

    /**
     * ファイルを追加
     * @param path ：パス
     * @param content ：内容
     * @return
     */
    JGitTest addFile(String path, String content = null) {
        // ファイルを更新
        if (content != null) {
            new File(git.repository.directory.parent, path).text = content
        }

        AddCommand ac = git.add()
        ac.addFilepattern(path)
        ac.call()
        this
    }

    /**
     * コミットする
     * @param comment :コメント
     * @return
     */
    JGitTest commit(String comment) {
        CommitCommand commit = git.commit()
        commit.setCommitter(usr, email)
                .setMessage(comment)
        commit.call()
        this
    }

    /**
     * プッシュする
     * @return
     */
    JGitTest push() {
        PushCommand pc = git.push()
        pc.setCredentialsProvider(cp)
                .setForce(true)
                .setPushAll()
        for ( obj in pc.call().iterator()) {
            println(obj)
        }
        this
    }

    static void main(String[] args) {
        def url = Constants.remote
        def tmp = Constants.local

        new JGitTest(Constants.usr, Constants.psw, Constants.email).with { app ->
            app.open(url, tmp)
            app.addFile("sample.txt", new Date().toString())
            app.commit("commit by JGit")
            app.push()
        }
    }
}
