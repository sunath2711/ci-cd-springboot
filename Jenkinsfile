pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'feature/initial-app',
                    url: 'https://github.com/sunath2711/ci-cd-springboot'
            }
        }

        stage('Build & Package') {
            steps {
                dir('app') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t cicd-springboot:${BUILD_NUMBER} app'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'app/target/*.jar', fingerprint: true
            }
        }
    }
}
