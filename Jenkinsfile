pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'feature/initial-app',
                    url: 'https://github.com/sunath2711/ci-cd-springboot'
            }
        }

        stage('Build & Test') {
            steps {
                dir('app') {
                    sh 'mvn clean test'
                }
            }
        }
    }
}
