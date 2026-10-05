pipeline {
    agent any

    environment {
        // ===== A ADAPTER (valeurs reconstruites, je n'ai pas vu ton Jenkinsfile) =====
        DOCKERHUB_USER = 'TON_USER_DOCKERHUB'
        DOCKERHUB_CREDS = 'dockerhub-credentials'   // ID du credential Docker Hub dans Jenkins
        SONAR_CREDS     = 'sonar-token'             // ID du credential (Secret text) du token Sonar
        IMAGE_TAG       = "${env.BUILD_NUMBER}"
    }

    stages {

        stage('1 - Get code from Git') {
            steps {
                git branch: 'main', url: 'https://github.com/Ons177/-DevOps-AppGestionDesProjets.git'
            }
        }

        stage('2 - Maven Compile') {
            steps {
                dir('backend') {
                    sh 'mvn -B compile'
                }
            }
        }

        // Les tests passent AVANT Sonar : jacoco.exec et jacoco.xml sont générés ici
        stage('3 - Maven Test') {
            steps {
                dir('backend') {
                    sh 'mvn -B test'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        // Sonar lit target/site/jacoco/jacoco.xml : pas de "clean" ici
        stage('4 - SonarQube') {
            steps {
                dir('backend') {
                    withCredentials([string(credentialsId: "${SONAR_CREDS}", variable: 'SONAR_TOKEN')]) {
                        sh 'mvn -B sonar:sonar -Dsonar.token=$SONAR_TOKEN'
                    }
                }
            }
        }

        stage('5 - Maven Package') {
            steps {
                dir('backend') {
                    sh 'mvn -B package -DskipTests'
                }
            }
        }

        stage('6 - Maven Deploy') {
            steps {
                dir('backend') {
                    // Nécessite les identifiants nexus-releases / nexus-snapshots dans ~/.m2/settings.xml
                    sh 'mvn -B deploy -DskipTests'
                }
            }
        }

        stage('7 - Docker Image and Push') {
            steps {
<<<<<<< HEAD
                withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDS}",
                                                  usernameVariable: 'DH_USER',
                                                  passwordVariable: 'DH_PASS')]) {
                    sh '''
                        echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin

                        docker build -t $DOCKERHUB_USER/projets-backend:$IMAGE_TAG -t $DOCKERHUB_USER/projets-backend:latest ./backend
                        docker push $DOCKERHUB_USER/projets-backend:$IMAGE_TAG
                        docker push $DOCKERHUB_USER/projets-backend:latest

                        docker build -t $DOCKERHUB_USER/projets-frontend:$IMAGE_TAG -t $DOCKERHUB_USER/projets-frontend:latest ./frontend
                        docker push $DOCKERHUB_USER/projets-frontend:$IMAGE_TAG
                        docker push $DOCKERHUB_USER/projets-frontend:latest

                        docker build -t $DOCKERHUB_USER/projets-mysql:$IMAGE_TAG -t $DOCKERHUB_USER/projets-mysql:latest ./mysql
                        docker push $DOCKERHUB_USER/projets-mysql:$IMAGE_TAG
                        docker push $DOCKERHUB_USER/projets-mysql:latest
                    '''
=======
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
                    sh 'docker build -t $DH_USER/projets-backend:$BUILD_NUMBER -t $DH_USER/projets-backend:latest ./backend'
                    sh 'docker build -t $DH_USER/projets-frontend:$BUILD_NUMBER -t $DH_USER/projets-frontend:latest ./frontend'
                    sh 'docker build -t $DH_USER/projets-mysql:$BUILD_NUMBER -t $DH_USER/projets-mysql:latest ./mysql'
                    retry(3) { sh 'echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin' }
                    retry(3) { sh 'docker push $DH_USER/projets-backend:$BUILD_NUMBER' }
                    retry(3) { sh 'docker push $DH_USER/projets-backend:latest' }
                    retry(3) { sh 'docker push $DH_USER/projets-frontend:$BUILD_NUMBER' }
                    retry(3) { sh 'docker push $DH_USER/projets-frontend:latest' }
                    retry(3) { sh 'docker push $DH_USER/projets-mysql:$BUILD_NUMBER' }
                    retry(3) { sh 'docker push $DH_USER/projets-mysql:latest' }
>>>>>>> d67ee15 (Retry sur login et push Docker Hub)
                }
            }
        }

        stage('8 - Docker Compose Up') {
            steps {
                sh 'docker compose up -d'
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }
    }
}
