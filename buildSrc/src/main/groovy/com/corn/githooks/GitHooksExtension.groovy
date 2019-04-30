package com.corn.githooks

import org.gradle.api.Project

class GitHooksExtension {

    public static final String NAME = "gitHooks"
    public static final String VERSION = "v1.0"

    private Project project

    String gitRootPath
    Commit commit


    GitHooksExtension(Project project) {
        this.project = project
    }

    def commit(Closure closure) {
        commit = new Commit()
        project.configure(commit, closure)
    }


    class Commit {
        // commit规范正则
        String regex = ''
        // commit规范文档url
        String docUrl = ''
        String emailSuffix = ''

        void regex(String regex) {
            this.regex = regex
        }

        void docUrl(String docUrl) {
            this.docUrl = docUrl
        }

        void emailSuffix(String emailSuffix){
            this.emailSuffix = emailSuffix
        }
    }


}