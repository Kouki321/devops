pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'my-app:latest'  // Valid Docker image name and tag
    }

    tools {
        maven "MAVEN_HOME"  // Ensure Maven is configured in Jenkins with this name
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Kouki321/devops.git'
            }
        }

        stage('Compile') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean install -DskipTests'
                    } else {
                        bat 'mvn clean install -DskipTests'
                    }
                }
            }
        }

        stage('JUnit Tests') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean test'
                    } else {
                        bat 'mvn clean test'
                    }
                }
            }
        }

        stage('Build JAR') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean package'
                    } else {
                        bat 'mvn clean package'
                    }
                }
            }
        }
stage('SonarQube Analysis') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn sonar:sonar -Dsonar.projectKey=my-app -Dsonar.host.url=http://your-sonarqube-server'
                    } else {
                        bat 'mvn sonar:sonar -Dsonar.projectKey=my-app -Dsonar.host.url=http://your-sonarqube-server'
                    }
                }
            }
        }

        stage('Dependency Check') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'dependency-check --project my-app --out . --scan ./target'
                    } else {
                        bat 'dependency-check --project my-app --out . --scan ./target'
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    if (isUnix()) {
                        sh "docker build -t ${DOCKER_IMAGE} ."
                    } else {
                        bat "docker build -t ${DOCKER_IMAGE} ."
                    }
                }
            }
        }

         stage('Scan with Trivy') {
            steps {
                script {
                    if (isUnix()) {
                        sh "trivy image ${DOCKER_IMAGE}"
                    } else {
                        bat "trivy image ${DOCKER_IMAGE}"
                    }
                }
            }
        }
    
        stage('Build and Deploy') {
            steps {
                script {
                    // Stop and remove any running containers
                    bat 'docker-compose down || exit 0'

                    // Build and start containers
                    bat 'docker-compose up --build -d'
                }
            }
        }
        
        
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline executed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
