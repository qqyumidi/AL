package com.corn.githooks

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.process.ExecResult

import java.nio.file.Files

class GitHooksUtil {


    static File getGitHooksPath(Project project, GitHooksExtension config) {
        File configFile = new File(config.gitRootPath)
        if (configFile.exists()) {
            return new File(configFile.absolutePath + File.separator + ".git" + File.separator + "hooks")
        }
        else {
            return new File(project.rootProject.rootDir.absolutePath + File.separator + ".git" + File.separator + "hooks")
        }
    }

    static void saveHookFile(String gitRootPath, String fileName) {
        InputStream is = null
        FileOutputStream fos = null

        try {
            is = GitHooksUtil.class.getClassLoader().getResourceAsStream(fileName)
            File file = new File(gitRootPath + File.separator + fileName)
            file.setExecutable(true)

            fos = new FileOutputStream(file)
            Files.copy(is, fos)

            fos.flush()
        } catch (Exception e) {
            throw new GradleException("Save hook file failed, file: " + gitRootPath + " e:" + e, e)
        } finally {
            closeStream(is)
            closeStream(fos)
        }
    }

    static void closeStream(Closeable closeable) {
        if(closeable == null) {
            return
        }

        try {
            closeable.close()
        } catch (Exception e) {
            // ignore Exception
        }
    }


    static boolean checkInstalledPython(Project project) {
        ExecResult result
        try {
            result = project.exec {
                executable 'python'
                args '--version'
            }
        } catch (Exception e) {
            e.printStackTrace()
        }

        return result != null && result.exitValue == 0
    }

}