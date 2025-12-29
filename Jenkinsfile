pipeline {
    agent any

    triggers {
    githubPush()
    }

    environment {
        IMAGE_NAME = "sunath27/cicd-springboot"
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
        stage('SonarQube Analysis') {
           steps {
                withSonarQubeEnv('sonarqube') {
                    dir('app') {
                        sh '''
                        mvn sonar:sonar \
                            -Dsonar.projectKey=release-info-service \
                            -Dsonar.projectName=release-info-service
                        '''
                    }
                }
            }
        }
        stage('Quality Gate') {
           steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                   }
                }
        }

        stage('Docker Build') {
            steps {
                sh '''
                  docker build \
                    --build-arg BUILD_VERSION=${BUILD_NUMBER} \
                    -t $IMAGE_NAME:1.0.${BUILD_NUMBER} \
                    -t $IMAGE_NAME:latest \
                    app
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
                      docker push $IMAGE_NAME:1.0.${BUILD_NUMBER}
                      docker push $IMAGE_NAME:latest
                    '''
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                  sed -i "s|IMAGE_PLACEHOLDER|$IMAGE_NAME:1.0.${BUILD_NUMBER}|g" k8s/deployment.yaml
                  kubectl apply -f k8s/deployment.yaml
                  kubectl apply -f k8s/service.yaml
                  kubectl rollout status deployment/cicd-springboot
            '''
            }
        }
        
    }
}
