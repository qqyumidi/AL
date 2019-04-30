package com.corn.githooks

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class GitHooksPlugin implements Plugin<Project> {

        @Override
        void apply(Project project) {
            project.extensions.create(GitHooksExtension.NAME, GitHooksExtension, project)


            project.afterEvaluate {
                GitHooksExtension gitHooksExtension = project.extensions.getByName(GitHooksExtension.NAME)

                if (!GitHooksUtil.checkInstalledPython(project)) {
                    throw new GradleException("GitHook require python env, please install python first!", e)
                }

                File gitRootPathFile = GitHooksUtil.getGitHooksPath(project, gitHooksExtension)
                if (!gitRootPathFile.exists()) {
                    throw new GradleException("Can't found project git root file, please check your gitRootPath config value")
                }

                GitHooksUtil.saveHookFile(gitRootPathFile.absolutePath, "commit-msg")

                File saveConfigFile = new File(gitRootPathFile.absolutePath + File.separator + "git-hooks.conf")

                saveConfigFile.withWriter('utf-8') { writer ->
                    writer.writeLine '## 程序自动生成，请勿手动改动此文件!!! ##'
                    writer.writeLine '[version]'
                    writer.writeLine "v = ${GitHooksExtension.VERSION}"
                    writer.writeLine '\n'
                    if (gitHooksExtension.commit != null) {
                        writer.writeLine '[commit-msg]'
                        writer.writeLine "cm_regex=${gitHooksExtension.commit.regex}"
                        writer.writeLine "cm_doc_url=${gitHooksExtension.commit.docUrl}"
                        writer.writeLine "cm_email_suffix=${gitHooksExtension.commit.emailSuffix}"
                    }
                }
            }
    }
}