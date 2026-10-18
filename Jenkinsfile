pipeline {
    agent any

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
                    sh 'mvn -B test jacoco:report'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        // Sonar lit target/site/jacoco/jacoco.xml : pas de "clean" ici
        // withSonarQubeEnv est obligatoire pour que waitForQualityGate fonctionne ensuite
        stage('4 - SonarQube') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQube') {   // <-- A REMPLACER par le nom exact du serveur (Administrer Jenkins > System > SonarQube servers)
                        sh 'mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:5.0.0.4389:sonar -Dsonar.token=$SONAR_AUTH_TOKEN -Dsonar.host.url=$SONAR_HOST_URL'
                    }
                }
            }
        }

        // Attend le résultat du Quality Gate (via le webhook Sonar -> Jenkins)
        stage('5 - Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('6 - Maven Package') {
            steps {
                dir('backend') {
                    sh 'mvn -B package -DskipTests'
                }
            }
        }

        stage('7 - Maven Deploy') {
            steps {
                dir('backend') {
                    // Nécessite les identifiants nexus-releases / nexus-snapshots dans ~/.m2/settings.xml
                    sh 'mvn -B deploy -DskipTests'
                }
            }
        }

        stage('8 - Docker Image and Push') {
            steps {
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
                }
            }
        }

        stage('9 - Docker Compose Up') {
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
