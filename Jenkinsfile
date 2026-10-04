pipeline {
    agent any

    parameters {
        booleanParam(name: 'RUN_SONAR', defaultValue: false, description: 'Stage 3 : SonarQube + Quality Gate')
        booleanParam(name: 'RUN_DEPLOY', defaultValue: false, description: 'Stage 6 : mvn deploy vers Nexus')
        booleanParam(name: 'RUN_DOCKER_PUSH', defaultValue: false, description: 'Stage 7 : push des images Docker Hub')
    }

    stages {
        stage('1 - Get code from Git') {
            steps {
                checkout scm
            }
        }

        stage('2 - Maven Compile') {
            steps {
                dir('backend') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('3 - SonarQube') {
            when { expression { params.RUN_SONAR } }
            steps {
                withSonarQubeEnv('SonarQube') {
                    dir('backend') {
                        sh 'mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.token=$SONAR_AUTH_TOKEN'
                    }
                }
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }

        stage('4 - Maven Test') {
            steps {
                dir('backend') {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('5 - Maven Package') {
            steps {
                dir('backend') {
                    sh 'mvn package -DskipTests'
                }
                archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
            }
        }

        stage('6 - Maven Deploy') {
            when { expression { params.RUN_DEPLOY } }
            steps {
                dir('backend') {
                    sh 'mvn deploy -DskipTests'
                }
            }
        }

        stage('7 - Docker Image and Push') {
            when { expression { params.RUN_DOCKER_PUSH } }
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
                    sh 'docker build -t $DH_USER/projets-backend:$BUILD_NUMBER -t $DH_USER/projets-backend:latest ./backend'
                    sh 'docker build -t $DH_USER/projets-frontend:$BUILD_NUMBER -t $DH_USER/projets-frontend:latest ./frontend'
                    sh 'echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin'
                    sh 'docker push $DH_USER/projets-backend:$BUILD_NUMBER'
                    sh 'docker push $DH_USER/projets-backend:latest'
                    sh 'docker push $DH_USER/projets-frontend:$BUILD_NUMBER'
                    sh 'docker push $DH_USER/projets-frontend:latest'
                }
            }
        }

        stage('8 - Docker Compose Up') {
            steps {
                sh 'docker compose up -d --build'
                sh 'docker compose ps'
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }
    }
}
