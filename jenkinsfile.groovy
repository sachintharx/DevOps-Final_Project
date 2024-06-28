pipeline {
    agent any

    triggers {
        githubPush()
    }

    environment {
        REPO_URL = 'https://github.com/sachintharx/DevOps-Final_Project'
        BRANCH = 'main'
        DOCKER_REGISTRY = 'irajapaksha'
        APP_NAME = 'RXAGRO'
        PORT = '3000'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: "${BRANCH}", url: "${REPO_URL}"
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    bat 'docker-compose build'
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    bat 'docker-compose push'
                }
            }
        }

        // stage('Free Port') {
        //     steps {
        //         script {
        //             // Check if the port is in use and free it if necessary
        //             def portCheck = bat(script: "netstat -ano | findstr \":${PORT}\"", returnStatus: true)
        //             if (portCheck == 0) {
        //                 def pid = bat(script: "for /f \"tokens=5\" %%a in ('netstat -ano ^| findstr \":${PORT}\"') do @echo %%a", returnStdout: true).trim()
        //                 echo "Killing process with PID ${pid} on port ${PORT}"
        //                 bat "taskkill /PID ${pid} /F"
        //             } else {
        //                 echo "Port ${PORT} is not in use."
        //             }
        //         }
        //     }
        // }

        stage('Deploy Application') {
            steps {
                script {
                    bat 'docker-compose down'
                    bat 'docker-compose up -d'
                }
            }
        }
    }
}
