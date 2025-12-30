pipeline {
    agent any

    triggers {
    githubPush()
    }
    options {
    disableConcurrentBuilds()
    }

    environment {
        IMAGE_NAME = "sunath27/cicd-springboot"
    }

    stages {

        stage('Build Info') {
            steps {
                sh '''
                echo "Job      : $JOB_NAME"
                echo "Build    : $BUILD_NUMBER"
                echo "Branch   : $BRANCH_NAME"
                echo "Commit   : $GIT_COMMIT"
                '''
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'feature/initial-app',
                    credentialsId: 'git_pat',
                    url: 'https://github.com/sunath2711/ci-cd-springboot'
            }
        }

        stage('Build & Package') {
             when {
                branch 'main'
            }
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

        stage('Image Security Scan (Trivy)') {
            steps {
                script {
                    def imageName = "sunath27/cicd-springboot:1.0.${BUILD_NUMBER}"

                    sh """
                    mkdir -p trivy-reports

                    echo "Running Trivy scan on ${imageName}"

                    trivy image \
                        --timeout 15m \
                        --severity CRITICAL \
                        --format json \
                        --output trivy-reports/trivy-report.json \
                        --no-progress \
                        ${imageName}

                    trivy image \
                        --timeout 15m \
                        --severity CRITICAL \
                        --exit-code 1 \
                        --no-progress \
                        ${imageName}
                    """
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'trivy-reports/*.json', fingerprint: true
                }
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
