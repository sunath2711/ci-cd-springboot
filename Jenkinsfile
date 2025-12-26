pipeline {
    agent any

    environment {
        IMAGE_NAME = "sunath2711/cicd-springboot"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'feature/initial-app',
                    credentialsId: 'git_pat',
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
                sh '''
                  docker build -t $IMAGE_NAME:${BUILD_NUMBER} app
                  docker tag $IMAGE_NAME:${BUILD_NUMBER} $IMAGE_NAME:latest
                '''
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dhub_cred',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                      echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                      docker push $IMAGE_NAME:${BUILD_NUMBER}
                      docker push $IMAGE_NAME:latest
                    '''
                }
            }
        }
    }
}
